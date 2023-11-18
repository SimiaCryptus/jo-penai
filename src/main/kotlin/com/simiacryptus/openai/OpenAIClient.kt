package com.simiacryptus.openai

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.simiacryptus.openai.exceptions.ModelMaxException
import com.simiacryptus.openai.exceptions.ModerationException
import com.simiacryptus.openai.models.*
import com.simiacryptus.util.JsonUtil
import com.simiacryptus.util.StringUtil
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.entity.mime.FileBody
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.io.entity.StringEntity
import org.slf4j.event.Level
import java.awt.image.BufferedImage
import java.io.BufferedOutputStream
import java.io.File
import java.io.IOException
import java.net.URL
import java.util.*
import javax.imageio.ImageIO


@Suppress("PropertyName", "SpellCheckingInspection", "unused")
open class OpenAIClient(
    key: String = keyTxt,
    private val apiBase: String = "https://api.openai.com/v1",
    logLevel: Level = Level.INFO,
    logStreams: MutableList<BufferedOutputStream> = mutableListOf()
) : OpenAIClientBase(key, apiBase, logLevel, logStreams) {


    data class ApiError(
        val message: String? = null,
        val type: String? = null,
        val param: String? = null,
        val code: Double? = null,
    )


    data class LogProbs(
        val tokens: List<CharSequence> = ArrayList(),
        val token_logprobs: DoubleArray = DoubleArray(0),
        val top_logprobs: List<ObjectNode> = ArrayList(),
        val text_offset: IntArray = IntArray(0),
    ) {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            other as LogProbs
            if (tokens != other.tokens) return false
            if (!token_logprobs.contentEquals(other.token_logprobs)) return false
            if (top_logprobs != other.top_logprobs) return false
            if (!text_offset.contentEquals(other.text_offset)) return false
            return true
        }

        override fun hashCode(): Int {
            var result = tokens.hashCode()
            result = 31 * result + token_logprobs.contentHashCode()
            result = 31 * result + top_logprobs.hashCode()
            result = 31 * result + text_offset.contentHashCode()
            return result
        }
    }


    data class Usage(
        val prompt_tokens: Int = 0,
        val completion_tokens: Int = 0,
        val total_tokens: Int = 0
    )


    data class Engine(
        val id: String? = null,
        val ready: Boolean = false,
        val owner: String? = null,
        val `object`: String? = null,
        val created: Int? = null,
        val permissions: String? = null,
        val replicas: Int? = null,
        val max_replicas: Int? = null,
    )

    fun listEngines(): List<Engine> = JsonUtil.objectMapper().readValue(
        JsonUtil.objectMapper().readValue(
            get("$apiBase/engines"), ObjectNode::class.java
        )["data"]?.toString() ?: "{}", JsonUtil.objectMapper().typeFactory.constructCollectionType(
            List::class.java, Engine::class.java
        )
    )

    fun getEngineIds(): Array<CharSequence?> = listEngines().map { it.id }.sortedBy { it }.toTypedArray()


    data class CompletionRequest(
        val prompt: String = "",
        val suffix: String? = null,
        val temperature: Double = 0.0,
        val max_tokens: Int = 1000,
        val stop: List<CharSequence>? = null,
        val logprobs: Int? = null,
        val echo: Boolean = false,
    )


    data class CompletionResponse(
        val id: String? = null,
        val `object`: String? = null,
        val created: Int = 0,
        val model: String? = null,
        val choices: List<CompletionChoice> = ArrayList(),
        val error: ApiError? = null,
        val usage: Usage? = null,
    ) {
        val firstChoice: Optional<CharSequence>
            get() = choices.first().text?.trim()?.let { Optional.of(it) } ?: Optional.empty()
    }

    data class CompletionChoice(
        val text: String? = null, val index: Int = 0, val logprobs: LogProbs? = null, val finish_reason: String? = null
    )

    private class TruncatedModel(
        override val modelName: String, override val maxTokens: Int
    ) : OpenAITextModel

    private val codex = GPT4Tokenizer(false)

    open fun complete(
        request: CompletionRequest, model: OpenAITextModel
    ): CompletionResponse {
        val request2 = request.copy(max_tokens = model.maxTokens - codex.estimateTokenCount(request.prompt))
        try {
            return withReliability {
                withPerformanceLogging {
                    completionCounter.incrementAndGet()
                    if (request2.suffix == null) {
                        log(
                            msg = String.format(
                                "Text Completion Request\nPrefix:\n\t%s\n", request2.prompt.replace("\n", "\n\t")
                            )
                        )
                    } else {
                        log(
                            msg = String.format(
                                "Text Completion Request\nPrefix:\n\t%s\nSuffix:\n\t%s\n",
                                request2.prompt.replace("\n", "\n\t"),
                                request2.suffix.replace("\n", "\n\t")
                            )
                        )
                    }
                    val result = post(
                        "$apiBase/engines/${model.modelName}/completions", StringUtil.restrictCharacterSet(
                            JsonUtil.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request2),
                            allowedCharset
                        )
                    )
                    checkError(result)
                    val response = JsonUtil.objectMapper().readValue(
                        result, CompletionResponse::class.java
                    )
                    if (response.usage != null) {
                        incrementTokens(model, response.usage)
                    }
                    val completionResult =
                        StringUtil.stripPrefix(response.firstChoice.orElse("").toString().trim { it <= ' ' },
                            request2.prompt.trim { it <= ' ' })
                    log(
                        msg = String.format(
                            "Chat Completion:\n\t%s", completionResult.toString().replace("\n", "\n\t")
                        )
                    )
                    response
                }
            }
        } catch (e: ModelMaxException) {
            return complete(request2, TruncatedModel(model.modelName, (e.modelMax - e.messages) - 1))
        }
    }

    data class TranscriptionPacket(
        val id: Int? = 0,
        val seek: Int? = 0,
        val start: Double? = 0.0,
        val end: Double? = 0.0,
        val text: String? = "",
        val tokens: IntArray? = null,
        val temperature: Double? = 0.0,
        val avg_logprob: Double? = 0.0,
        val compression_ratio: Double? = 0.0,
        val no_speech_prob: Double? = 0.0,
        val transient: Boolean? = false
    ) {
        override fun equals(other: Any?) = when {
            this === other -> true
            javaClass != other?.javaClass -> false
            else -> {
                other as TranscriptionPacket
                when {
                    id != other.id -> false
                    seek != other.seek -> false
                    start != other.start -> false
                    end != other.end -> false
                    text != other.text -> false
                    tokens != null -> when {
                        other.tokens == null -> false
                        !tokens.contentEquals(other.tokens) -> false
                        else -> true
                    }
                    other.tokens != null -> false
                    temperature != other.temperature -> false
                    avg_logprob != other.avg_logprob -> false
                    compression_ratio != other.compression_ratio -> false
                    no_speech_prob != other.no_speech_prob -> false
                    transient != other.transient -> false
                    else -> true
                }
            }
        }

        override fun hashCode(): Int {
            var result = id ?: 0
            result = 31 * result + (seek ?: 0)
            result = 31 * result + (start?.hashCode() ?: 0)
            result = 31 * result + (end?.hashCode() ?: 0)
            result = 31 * result + (text?.hashCode() ?: 0)
            result = 31 * result + (tokens?.contentHashCode() ?: 0)
            result = 31 * result + (temperature?.hashCode() ?: 0)
            result = 31 * result + (avg_logprob?.hashCode() ?: 0)
            result = 31 * result + (compression_ratio?.hashCode() ?: 0)
            result = 31 * result + (no_speech_prob?.hashCode() ?: 0)
            result = 31 * result + (transient?.hashCode() ?: 0)
            return result
        }
    }

    data class TranscriptionResult(
        val task: String? = "",
        val language: String? = "",
        val duration: Double = 0.0,
        val segments: List<TranscriptionPacket> = listOf(),
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
            entity.setMode(HttpMultipartMode.EXTENDED)
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
                result.text ?: ""
            } catch (e: Exception) {
                jsonObject.get("text").asString ?: ""
            }
        }
    }

    data class SpeechRequest(
        val input: String,
        val model: String = "tts-1",
        val voice: String = "alloy",
        val response_format: String? = "mp3",
        val speed: Double? = 1.0
    )

    open fun createSpeech(request: SpeechRequest) : ByteArray? = withReliability {
        withPerformanceLogging {
            val httpRequest = HttpPost("$apiBase/audio/speech")
            authorize(httpRequest)
            httpRequest.addHeader("Accept", "application/json")
            httpRequest.addHeader("Content-Type", "application/json")
            httpRequest.entity = StringEntity(JsonUtil.objectMapper().writeValueAsString(request))
            val response = withClient { it.execute(httpRequest).entity }
            val contentType = response.contentType
            if(contentType != null && contentType.startsWith("text") || contentType.startsWith("application/json")) {
                checkError(response.content.readAllBytes().toString(Charsets.UTF_8))
                null
            } else {
                response.content.readAllBytes()
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
        val messages: List<ChatMessage> = listOf(),
        val model: String? = null,
        val temperature: Double = 0.0,
        val max_tokens: Int? = null,
        val stop: List<CharSequence>? = listOf(),
        val function_call: String? = null,
        val response_format: Map<String, Any>? = null,
        val n: Int? = null,
        val functions: List<RequestFunction>? = null,
    )

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
        val choices: List<ChatChoice> = listOf(),
        val error: ApiError? = null,
        val usage: Usage? = null,
    )


    data class ChatChoice(
        val message: ChatMessageResponse? = null,
        val index: Int = 0,
        val finish_reason: String? = null,
    )

    data class ContentPart(
        val type: String,
        val text: String? = null,
        val image_url: String? = null
    )

    data class ChatMessage(
        val role: Role? = null,
        val content: List<ContentPart>? = null,
        val function_call: FunctionCall? = null,
    )
    data class ChatMessageResponse(
        val role: Role? = null,
        val content: String? = null,
        val function_call: FunctionCall? = null,
    )
    enum class Role {
        assistant, user, system
    }
    data class FunctionCall(
        val name: String? = null,
        val arguments: String? = null,
    )

    open fun chat(
        chatRequest: ChatRequest, model: OpenAIModel
    ): ChatResponse {
        try {
            return withReliability {
                withPerformanceLogging {
                    chatCounter.incrementAndGet()
                    val reqJson =
                        JsonUtil.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(chatRequest)
                    log(
                        msg = String.format(
                            "Chat Request\nPrefix:\n\t%s\n", reqJson.replace("\n", "\n\t")
                        )
                    )

                    val jsonRequest = JsonUtil.objectMapper().writeValueAsString(chatRequest)
                    val result = post("$apiBase/chat/completions", jsonRequest)
                    checkError(result)
                    val response = JsonUtil.objectMapper().readValue(result, ChatResponse::class.java)
                    if (response.usage != null) {
                        incrementTokens(model, response.usage)
                    }
                    log(msg = String.format("Chat Completion:\n\t%s",
                        response.choices.firstOrNull()?.message?.content?.trim { it <= ' ' }?.replace("\n", "\n\t")
                            ?: JsonUtil.toJson(response)))
                    response
                }
            }
        } catch (e: ModelMaxException) {
            return chat(
                chatRequest.copy(max_tokens = (e.modelMax - e.messages) - 1),
                TruncatedModel(model.modelName, (e.modelMax - e.messages) - 1)
            )
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
            val jsonObject = Gson().fromJson(
                result, JsonObject::class.java
            ) ?: return@withPerformanceLogging
            if (jsonObject.has("error")) {
                val errorObject = jsonObject.getAsJsonObject("error")
                throw RuntimeException(IOException(errorObject["message"].asString))
            }
            val moderationResult = jsonObject.getAsJsonArray("results")[0].asJsonObject
            if (moderationResult["flagged"].asBoolean) {
                val categoriesObj = moderationResult["categories"].asJsonObject
                throw RuntimeException(
                    ModerationException("Moderation flagged this request due to " + categoriesObj.keySet()
                    .stream().filter { c: String? ->
                        categoriesObj[c].asBoolean
                    }.reduce { a: String, b: String -> "$a, $b" }.orElse("???"))
                )
            }
        }
    }

    data class EditRequest(
        val model: String = "",
        val input: String? = null,
        val instruction: String = "",
        val temperature: Double? = 0.0,
        val n: Int? = null,
        val top_p: Double? = null
    )

    open fun edit(
        editRequest: EditRequest
    ): CompletionResponse = withReliability {

        withPerformanceLogging {
            editCounter.incrementAndGet()
            if (editRequest.input == null) {
                log(
                    msg = String.format(
                        "Text Edit Request\nInstruction:\n\t%s\n", editRequest.instruction.replace("\n", "\n\t")
                    )
                )
            } else {
                log(
                    msg = String.format(
                        "Text Edit Request\nInstruction:\n\t%s\nInput:\n\t%s\n",
                        editRequest.instruction.replace("\n", "\n\t"),
                        editRequest.input.replace("\n", "\n\t")
                    )
                )
            }
            val request: String = StringUtil.restrictCharacterSet(
                JsonUtil.objectMapper().writeValueAsString(editRequest), allowedCharset
            )
            val result = post("$apiBase/edits", request)
            checkError(result)
            val response = JsonUtil.objectMapper().readValue(
                result, CompletionResponse::class.java
            )
            if (response.usage != null) {
                incrementTokens(
                    EditModels.values().find { it.modelName.equals(editRequest.model, true) }, response.usage
                )
            }
            log(msg = String.format("Chat Completion:\n\t%s",
                response.firstChoice.orElse("").toString().trim { it <= ' ' }.toString().replace("\n", "\n\t")))
            response
        }
    }

    data class ModelListResponse(
        val data: List<ModelData>? = listOf(), val `object`: String? = null
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

    data class EmbeddingResponse(
        val `object`: String? = null,
        val data: List<EmbeddingData> = listOf(),
        val model: String? = null,
        val usage: Usage? = null,
    )

    data class EmbeddingData(
        val `object`: String? = null,
        val embedding: DoubleArray? = null,
        val index: Int? = null
    ) {
        override fun equals(other: Any?): Boolean {
            when {
                this === other -> return true
                javaClass != other?.javaClass -> return false
                else -> {
                    other as EmbeddingData
                    when {
                        `object` != other.`object` -> return false
                        embedding != null -> {
                            when {
                                other.embedding == null -> return false
                                !embedding.contentEquals(other.embedding) -> return false
                            }
                        }
                        other.embedding != null -> return false
                        index != other.index -> return false
                    }
                    return true
                }
            }
        }

        override fun hashCode(): Int {
            var result = `object`?.hashCode() ?: 0
            result = 31 * result + (embedding?.contentHashCode() ?: 0)
            result = 31 * result + (index ?: 0)
            return result
        }
    }

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
                    "$apiBase/embeddings", StringUtil.restrictCharacterSet(
                        JsonUtil.objectMapper().writeValueAsString(request), allowedCharset
                    )
                )
                checkError(result)
                val response = JsonUtil.objectMapper().readValue(
                    result, EmbeddingResponse::class.java
                )
                if (response.usage != null) {
                    incrementTokens(
                        EmbeddingModels.values().find { it.modelName.equals(request.model, true) }, response.usage
                    )
                }
                response
            }
        }
    }

    data class ImageGenerationRequest(
        val prompt: String,
        val model: String? = null,
        val n: Int? = null,
        val quality: String? = null,
        val response_format: String? = null,
        val size: String? = null,
        val style: String? = null,
        val user: String? = null
    )

    data class ImageObject(
        val url: String
    )

    data class ImageGenerationResponse(
        val created: Long,
        val data: List<ImageObject>
    )

    open fun createImage(request: ImageGenerationRequest): ImageGenerationResponse = withReliability {
        withPerformanceLogging {
            val url = "$apiBase/images/generations"
            val httpRequest = HttpPost(url)
            httpRequest.addHeader("Accept", "application/json")
            httpRequest.addHeader("Content-Type", "application/json")
            authorize(httpRequest)

            val requestBody = Gson().toJson(request)
            httpRequest.entity = StringEntity(requestBody)

            val response = post(httpRequest)
            checkError(response)

            JsonUtil.objectMapper().readValue(response, ImageGenerationResponse::class.java)
        }
    }

    data class ImageEditRequest(
        val image: File,
        val prompt: String,
        val mask: File? = null,
        val model: String? = null,
        val n: Int? = null,
        val size: String? = null,
        val responseFormat: String? = null,
        val user: String? = null
    )

    data class ImageEditResponse(
        val created: Long,
        val data: List<ImageObject>
    )

    open fun createImageEdit(request: ImageEditRequest): ImageEditResponse = withReliability {
        withPerformanceLogging {
            val url = "$apiBase/images/edits"
            val httpRequest = HttpPost(url)
            httpRequest.addHeader("Accept", "application/json")
            authorize(httpRequest)

            val entityBuilder = MultipartEntityBuilder.create()
            entityBuilder.addPart("image", FileBody(request.image))
            entityBuilder.addTextBody("prompt", request.prompt)
            request.mask?.let { entityBuilder.addPart("mask", FileBody(it)) }
            request.model?.let { entityBuilder.addTextBody("model", it) }
            request.n?.let { entityBuilder.addTextBody("n", it.toString()) }
            request.size?.let { entityBuilder.addTextBody("size", it) }
            request.responseFormat?.let { entityBuilder.addTextBody("response_format", it) }
            request.user?.let { entityBuilder.addTextBody("user", it) }

            httpRequest.entity = entityBuilder.build()
            val response = post(httpRequest)
            checkError(response)

            JsonUtil.objectMapper().readValue(response, ImageEditResponse::class.java)
        }
    }

    data class ImageVariationRequest(
        val image: File,
        //val model: String? = null,
        val n: Int? = null,
        val responseFormat: String? = null,
        val size: String? = null,
        val user: String? = null
    )

    data class ImageVariationResponse(
        val created: Long,
        val data: List<ImageObject>
    )

    open fun createImageVariation(request: ImageVariationRequest): ImageVariationResponse = withReliability {
        withPerformanceLogging {
            val url = "$apiBase/images/variations"
            val httpRequest = HttpPost(url)
            httpRequest.addHeader("Accept", "application/json")
            authorize(httpRequest)

            val entityBuilder = MultipartEntityBuilder.create()
            entityBuilder.addPart("image", FileBody(request.image))
            //request.model?.let { entityBuilder.addTextBody("model", it) }
            request.n?.let { entityBuilder.addTextBody("n", it.toString()) }
            request.responseFormat?.let { entityBuilder.addTextBody("response_format", it) }
            request.size?.let { entityBuilder.addTextBody("size", it) }
            request.user?.let { entityBuilder.addTextBody("user", it) }

            httpRequest.entity = entityBuilder.build()
            val response = post(httpRequest)
            checkError(response)

            JsonUtil.objectMapper().readValue(response, ImageVariationResponse::class.java)
        }
    }



}
