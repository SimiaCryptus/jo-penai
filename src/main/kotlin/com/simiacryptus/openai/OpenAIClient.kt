package com.simiacryptus.openai

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.simiacryptus.util.JsonUtil
import com.simiacryptus.util.StringUtil
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.ContentType
import org.apache.http.entity.StringEntity
import org.apache.http.entity.mime.HttpMultipartMode
import org.apache.http.entity.mime.MultipartEntityBuilder
import org.slf4j.event.Level
import java.awt.image.BufferedImage
import java.io.BufferedOutputStream
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO

@Suppress("unused")
open class OpenAIClient(
    key: String = keyTxt,
    private val apiBase: String = "https://api.openai.com/v1",
    logLevel: Level = Level.INFO,
    logStreams: MutableList<BufferedOutputStream> = mutableListOf()
) : APIClientBase(key, apiBase, logLevel, logStreams) {

    interface Model {
        val modelName: String
        val maxTokens: Int
    }

    enum class Models(
        override val modelName: String,
        override val maxTokens: Int
    ) : Model {
        AdaEmbedding("text-embedding-ada-002", 2049),
        DaVinci("text-davinci-003", 2049),
        DaVinciEdit("text-davinci-edit-001", 2049),
        GPT35Turbo("gpt-3.5-turbo-16k", 16384),
        GPT4("gpt-4", 8192),
        GPT4Turbo("gpt-4-1106-preview", /* 128k */ 131072),
        GPT4Vision("gpt-4-vision-preview", 8192),
    }

    private val tokenCounter = AtomicInteger(0)

    open fun incrementTokens(model: Model?, tokens: Int) {
        tokenCounter.addAndGet(tokens)
    }

    open val metrics: Map<String, Any>
        get() = hashMapOf(
            "tokens" to tokenCounter.get(),
            "chats" to chatCounter.get(),
            "completions" to completionCounter.get(),
            "moderations" to moderationCounter.get(),
            "renders" to renderCounter.get(),
            "transcriptions" to transcriptionCounter.get(),
            "edits" to editCounter.get(),
        )
    private val chatCounter = AtomicInteger(0)
    private val completionCounter = AtomicInteger(0)
    private val moderationCounter = AtomicInteger(0)
    private val renderCounter = AtomicInteger(0)
    private val transcriptionCounter = AtomicInteger(0)
    private val editCounter = AtomicInteger(0)

    @Suppress("unused")
    data class ApiError(
        var message: String? = null,
        var type: String? = null,
        var param: String? = null,
        var code: Double? = null,
    )

    @Suppress("unused")
    data class LogProbs(
        var tokens: Array<CharSequence> = arrayOf(),
        var token_logprobs: DoubleArray = DoubleArray(0),
        var top_logprobs: Array<ObjectNode> = arrayOf(),
        var text_offset: IntArray = IntArray(0),
    )

    @Suppress("unused")
    data class Usage(
        var prompt_tokens: Int = 0,
        var completion_tokens: Int = 0,
        var total_tokens: Int = 0
    )

    @Suppress("unused")
    data class Engine(
        var id: String? = null,
        var ready: Boolean = false,
        var owner: String? = null,
        var `object`: String? = null,
        var created: Int? = null,
        var permissions: String? = null,
        var replicas: Int? = null,
        var max_replicas: Int? = null,
    )

    fun listEngines(): List<Engine> = JsonUtil.objectMapper().readValue(
        JsonUtil.objectMapper().readValue(
            get(apiBase + "/engines"),
            ObjectNode::class.java
        )["data"]!!.toString(),
        JsonUtil.objectMapper().typeFactory.constructCollectionType(
            List::class.java,
            Engine::class.java
        )
    )

    fun getEngineIds(): Array<CharSequence?> = listEngines().map { it.id }.sortedBy { it }.toTypedArray()

    @Suppress("unused")
    data class CompletionRequest(
        var prompt: String = "",
        var suffix: String? = null,
        var temperature: Double = 0.0,
        var max_tokens: Int = 1000,
        var stop: Array<CharSequence>? = null,
        var logprobs: Int? = null,
        var echo: Boolean = false,
    ) {

        fun appendPrompt(prompt: CharSequence): CompletionRequest {
            this.prompt = this.prompt + prompt
            return this
        }

        fun addStops(vararg newStops: CharSequence): CompletionRequest {
            val stops = ArrayList<CharSequence>()
            for (x in newStops) {
                if (x.isNotEmpty()) {
                    stops.add(x)
                }
            }
            if (stops.isNotEmpty()) {
                if (null != stop) Arrays.stream(stop).forEach { e: CharSequence ->
                    stops.add(
                        e
                    )
                }
                stop = stops.stream().distinct().toArray { size: Int -> arrayOfNulls<CharSequence>(size) }
            }
            return this
        }

        fun setSuffix(suffix: CharSequence?): CompletionRequest {
            this.suffix = suffix?.toString()
            return this
        }

    }

    @Suppress("unused")
    data class CompletionResponse(
        var id: String? = null,
        var `object`: String? = null,
        var created: Int = 0,
        var model: String? = null,
        var choices: Array<CompletionChoice> = arrayOf(),
        var error: ApiError? = null,
        var usage: Usage? = null,
    ) {
        val firstChoice: Optional<CharSequence>
            get() = choices.first().text?.trim()?.let { Optional.of(it) } ?: Optional.empty()
    }

    data class CompletionChoice(
        var text: String? = null,
        var index: Int = 0,
        var logprobs: LogProbs? = null,
        var finish_reason: String? = null
    )

    private class TruncatedModel(
        override val modelName: String,
        override val maxTokens: Int
    ) : Model

    private val codex = GPT4Tokenizer(false)

    open fun complete(
        request: CompletionRequest,
        model: Model
    ): CompletionResponse {
        request.max_tokens = model.maxTokens - codex.estimateTokenCount(request.prompt)
        try {
            return withReliability {
                withPerformanceLogging {
                    completionCounter.incrementAndGet()
                    if (request.suffix == null) {
                        log(
                            msg = String.format(
                                "Text Completion Request\nPrefix:\n\t%s\n",
                                request.prompt.replace("\n", "\n\t")
                            )
                        )
                    } else {
                        log(
                            msg = String.format(
                                "Text Completion Request\nPrefix:\n\t%s\nSuffix:\n\t%s\n",
                                request.prompt.replace("\n", "\n\t"),
                                request.suffix!!.replace("\n", "\n\t")
                            )
                        )
                    }
                    val result = post(
                        "$apiBase/engines/${model.modelName}/completions",
                        StringUtil.restrictCharacterSet(
                            JsonUtil.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request),
                            allowedCharset
                        )
                    )
                    checkError(result)
                    val response = JsonUtil.objectMapper().readValue(
                        result,
                        CompletionResponse::class.java
                    )
                    if (response.usage != null) {
                        incrementTokens(model, response.usage!!.total_tokens)
                    }
                    val completionResult = StringUtil.stripPrefix(
                        response.firstChoice.orElse("").toString().trim { it <= ' ' },
                        request.prompt.trim { it <= ' ' })
                    log(
                        msg = String.format(
                            "Chat Completion:\n\t%s",
                            completionResult.toString().replace("\n", "\n\t")
                        )
                    )
                    response
                }
            }
        } catch (e: ModelMaxException) {
            return complete(request, TruncatedModel(model.modelName, (e.modelMax - e.messages) - 1))
        }
    }

    data class TranscriptionPacket(
        val id: Int? = 0,
        val seek: Int? = 0,
        val start: Double? = 0.0,
        val end: Double? = 0.0,
        val text: String? = "",
        val tokens: Array<Int>? = arrayOf(),
        val temperature: Double? = 0.0,
        val avg_logprob: Double? = 0.0,
        val compression_ratio: Double? = 0.0,
        val no_speech_prob: Double? = 0.0,
        val `transient`: Boolean? = false
    )

    data class TranscriptionResult(
        val task: String? = "",
        val language: String? = "",
        val duration: Double = 0.0,
        val segments: Array<TranscriptionPacket> = arrayOf(),
        val text: String? = ""
    )

    open fun transcription(wavAudio: ByteArray, prompt: String = ""): String = withReliability {
        withPerformanceLogging {
            transcriptionCounter.incrementAndGet()
            val url = "$apiBase/audio/transcriptions"
            val request = HttpPost(url)
            request.addHeader("Accept", "application/json")
            authorize(request)
            val entity = MultipartEntityBuilder.create()
            entity.setMode(HttpMultipartMode.RFC6532)
            entity.addBinaryBody("file", wavAudio, ContentType.create("audio/x-wav"), "audio.wav")
            entity.addTextBody("model", "whisper-1")
            entity.addTextBody("response_format", "verbose_json")
            if (prompt.isNotEmpty()) entity.addTextBody("prompt", prompt)
            request.entity = entity.build()
            val response = post(request)
            val jsonObject = Gson().fromJson(response, JsonObject::class.java)
            if (jsonObject.has("error")) {
                val errorObject = jsonObject.getAsJsonObject("error")
                throw RuntimeException(IOException(errorObject["message"].asString))
            }
            try {
                val result = JsonUtil.objectMapper().readValue(response, TranscriptionResult::class.java)
                result.text!!
            } catch (e: Exception) {
                jsonObject.get("text").asString!!
            }
        }
    }

    open fun render(prompt: String = "", resolution: Int = 1024, count: Int = 1): List<BufferedImage> =
        withReliability {
            withPerformanceLogging {
                renderCounter.incrementAndGet()
                val url = "$apiBase/images/generations"
                val request = HttpPost(url)
                request.addHeader("Accept", "application/json")
                request.addHeader("Content-Type", "application/json")
                authorize(request)
                val jsonObject = JsonObject()
                jsonObject.addProperty("prompt", prompt)
                jsonObject.addProperty("n", count)
                jsonObject.addProperty("size", "${resolution}x$resolution")
                request.entity = StringEntity(jsonObject.toString())
                val response = post(request)
                val jsonObject2 = Gson().fromJson(response, JsonObject::class.java)
                if (jsonObject2.has("error")) {
                    val errorObject = jsonObject2.getAsJsonObject("error")
                    throw RuntimeException(IOException(errorObject["message"].asString))
                }
                val dataArray = jsonObject2.getAsJsonArray("data")
                val images = ArrayList<BufferedImage>()
                for (i in 0 until dataArray.size()) {
                    images.add(ImageIO.read(URL(dataArray[i].asJsonObject.get("url").asString)))
                }
                images
            }
        }

    data class ChatRequest(
        var messages: Array<ChatMessage> = arrayOf(),
        var model: String? = null,
        var temperature: Double = 0.0,
        var max_tokens: Int? = null,
        var stop: Array<CharSequence>? = null,
        val function_call: String? = null,
        val n: Int? = null,
        val functions: Array<RequestFunction>? = null,
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as ChatRequest
            if (!messages.contentEquals(other.messages)) return false
            if (model != other.model) return false
            if (temperature != other.temperature) return false
            if (max_tokens != other.max_tokens) return false
            if (stop != null) {
                if (other.stop == null) return false
                if (!stop.contentEquals(other.stop)) return false
            } else if (other.stop != null) return false
            if (function_call != other.function_call) return false
            if (n != other.n) return false
            if (!functions.contentEquals(other.functions)) return false
            return true
        }

        override fun hashCode(): Int {
            var result = messages.contentHashCode()
            result = 31 * result + (model?.hashCode() ?: 0)
            result = 31 * result + temperature.hashCode()
            result = 31 * result + (max_tokens ?: -1)
            result = 31 * result + (stop?.contentHashCode() ?: 0)
            result = 31 * result + function_call.hashCode()
            result = 31 * result + (n ?: 0)
            result = 31 * result + functions.contentHashCode()
            return result
        }
    }

    data class RequestFunction(
        val name: String = "",
        val description: String = "",
        val parameters: Map<String, String> = mapOf(),
    )

    data class ChatResponse(
        val id: String? = null,
        val `object`: String? = null,
        val created: Long = 0,
        val model: String? = null,
        val choices: Array<ChatChoice> = arrayOf(),
        val error: ApiError? = null,
        val usage: Usage? = null,
    )

    @Suppress("unused")
    data class ChatChoice(
        var message: ChatMessage? = null,
        var index: Int = 0,
        var finish_reason: String? = null,
    )

    data class ChatMessage(
        var role: Role? = null,
        var content: String? = null,
        var function_call: FunctionCall? = null,
    ) {
        enum class Role {
            assistant, user, system
        }
    }

    data class FunctionCall(
        var name: String? = null,
        var arguments: String? = null,
    )

    open fun chat(
        completionRequest: ChatRequest,
        model: Model
    ): ChatResponse {
        try {
            return withReliability {
                withPerformanceLogging {
                    chatCounter.incrementAndGet()
                    val reqJson =
                        JsonUtil.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(completionRequest)
                    log(
                        msg = String.format(
                            "Chat Request\nPrefix:\n\t%s\n",
                            reqJson.replace("\n", "\n\t")
                        )
                    )
                    fun json() = StringUtil.restrictCharacterSet(
                        JsonUtil.objectMapper().writeValueAsString(completionRequest),
                        allowedCharset
                    )

                    val result = post("$apiBase/chat/completions", json())
                    checkError(result)
                    val response = JsonUtil.objectMapper().readValue(
                        result,
                        ChatResponse::class.java
                    )
                    if (response.usage != null) {
                        incrementTokens(model, response.usage.total_tokens)
                    }
                    log(
                        msg = String.format(
                            "Chat Completion:\n\t%s",
                            response.choices.firstOrNull()
                                ?.message?.content?.trim { it <= ' ' }
                                ?.replace("\n", "\n\t") ?: JsonUtil.toJson(response)
                        )
                    )
                    response
                }
            }
        } catch (e: ModelMaxException) {
            completionRequest.max_tokens = (e.modelMax - e.messages) - 1
            return chat(completionRequest, TruncatedModel(model.modelName, (e.modelMax - e.messages) - 1))
        }
    }

    open fun moderate(text: String) = withReliability {
        withPerformanceLogging {
            moderationCounter.incrementAndGet()
            val body: String = try {
                JsonUtil.objectMapper().writeValueAsString(
                    mapOf(
                        "input" to StringUtil.restrictCharacterSet(text, allowedCharset)
                    )
                )
            } catch (e: JsonProcessingException) {
                throw RuntimeException(e)
            }
            val result: String = try {
                this.post("$apiBase/moderations", body)
            } catch (e: IOException) {
                throw RuntimeException(e)
            } catch (e: InterruptedException) {
                throw RuntimeException(e)
            }
            val jsonObject =
                Gson().fromJson(
                    result,
                    JsonObject::class.java
                ) ?: return@withPerformanceLogging
            if (jsonObject.has("error")) {
                val errorObject = jsonObject.getAsJsonObject("error")
                throw RuntimeException(IOException(errorObject["message"].asString))
            }
            val moderationResult =
                jsonObject.getAsJsonArray("results")[0].asJsonObject
            if(false) log(
                Level.DEBUG,
                String.format(
                    "Moderation Request\nText:\n%s\n\nResult:\n%s",
                    text.replace("\n", "\n\t"),
                    result
                )
            )
            if (moderationResult["flagged"].asBoolean) {
                val categoriesObj =
                    moderationResult["categories"].asJsonObject
                throw RuntimeException(
                    ModerationException(
                        "Moderation flagged this request due to " + categoriesObj.keySet()
                            .stream().filter { c: String? ->
                                categoriesObj[c].asBoolean
                            }.reduce { a: String, b: String -> "$a, $b" }
                            .orElse("???")
                    )
                )
            }
        }
    }

    data class EditRequest(
        var model: String = "",
        var input: String? = null,
        var instruction: String = "",
        var temperature: Double? = 0.0,
        var n: Int? = null,
        var top_p: Double? = null
    )

    open fun edit(
        editRequest: EditRequest
    ): CompletionResponse = withReliability {

        withPerformanceLogging {
            editCounter.incrementAndGet()
            if (editRequest.input == null) {
                log(
                    msg = String.format(
                        "Text Edit Request\nInstruction:\n\t%s\n",
                        editRequest.instruction.replace("\n", "\n\t")
                    )
                )
            } else {
                log(
                    msg = String.format(
                        "Text Edit Request\nInstruction:\n\t%s\nInput:\n\t%s\n",
                        editRequest.instruction.replace("\n", "\n\t"),
                        editRequest.input!!.replace("\n", "\n\t")
                    )
                )
            }
            val request: String =
                StringUtil.restrictCharacterSet(
                    JsonUtil.objectMapper().writeValueAsString(editRequest),
                    allowedCharset
                )
            val result = post("$apiBase/edits", request)
            checkError(result)
            val response = JsonUtil.objectMapper().readValue(
                result,
                CompletionResponse::class.java
            )
            if (response.usage != null) {
                incrementTokens(
                    Models.values().find { it.name.equals(editRequest.model, true) },
                    response.usage!!.total_tokens
                )
            }
            log(
                msg = String.format(
                    "Chat Completion:\n\t%s",
                    response.firstChoice.orElse("").toString().trim { it <= ' ' }
                        .toString().replace("\n", "\n\t")
                )
            )
            response
        }
    }

    data class ModelListResponse(
        val data: List<ModelData>? = listOf(),
        val `object`: String? = null
    )

    data class ModelData(
        val id: String? = null,
        val `object`: String? = null,
        val owned_by: String? = null,
        val root: String? = null,
        val parent: String? = null,
        val created: Long? = null,
        val permission: List<Map<String, Object>>? = listOf(),
    )

    open fun listModels(): ModelListResponse {
        val result = get("$apiBase/models")
        checkError(result)
        return JsonUtil.objectMapper().readValue(result, ModelListResponse::class.java)
    }

    @Suppress("unused")
    class EmbeddingResponse {
        var `object`: String? = null
        var data: Array<EmbeddingData> = arrayOf()
        var model: String? = null
        var usage: Usage? = null
    }

    data class EmbeddingData(
        val `object`: String? = null,
        val embedding: Array<Double>? = arrayOf(),
        val index: Int? = null
    )

    data class EmbeddingRequest(
        val model: String? = null,
        val input: String? = null,
        val user: String? = null
    )

    open fun createEmbedding(
        request: EmbeddingRequest
    ): EmbeddingResponse {
        return withReliability {
            withPerformanceLogging {
                if (request.input is String) {
                    log(
                        msg = String.format(
                            "Embedding Creation Request\nModel:\n\t%s\nInput:\n\t%s\n",
                            request.model,
                            request.input.replace("\n", "\n\t")
                        )
                    )
                }
                val result = post(
                    "$apiBase/embeddings",
                    StringUtil.restrictCharacterSet(
                        JsonUtil.objectMapper().writeValueAsString(request),
                        allowedCharset
                    )
                )
                checkError(result)
                val response = JsonUtil.objectMapper().readValue(
                    result,
                    EmbeddingResponse::class.java
                )
                if (response.usage != null) {
                    incrementTokens(
                        Models.values().find { it.name.equals(request.model, true) },
                        response.usage!!.total_tokens
                    )
                }
                response
            }
        }
    }

    companion object {
        var auxillaryLog: File? = null
        val auxillaryLogOutputStream: BufferedOutputStream? by lazy { auxillaryLog?.outputStream()?.buffered() }

        private var _keyTxt: String? = null
        var keyTxt: String
            get() {
                if (null != _keyTxt) return _keyTxt!!
                val resourceAsStream = OpenAIClient::class.java.getResourceAsStream("/openai.key")
                if (null != resourceAsStream) return resourceAsStream.readAllBytes().toString(Charsets.UTF_8).trim()
                val keyFile = File(File(System.getProperty("user.home")), "openai.key")
                if (keyFile.exists()) return keyFile.readText().trim()
                if (System.getenv().containsKey("OPENAI_KEY")) return System.getenv("OPENAI_KEY").trim()
                return ""
            }
            set(value) {
                _keyTxt = value
            }
    }
}