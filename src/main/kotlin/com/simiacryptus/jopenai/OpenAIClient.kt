package com.simiacryptus.jopenai

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.node.ObjectNode
import com.google.common.util.concurrent.ListeningScheduledExecutorService
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.simiacryptus.jopenai.ApiModel.*
import com.simiacryptus.jopenai.exceptions.ModerationException
import com.simiacryptus.jopenai.models.*
import com.simiacryptus.jopenai.util.ClientUtil.allowedCharset
import com.simiacryptus.jopenai.util.ClientUtil.checkError
import com.simiacryptus.jopenai.util.ClientUtil.defaultApiProvider
import com.simiacryptus.jopenai.util.ClientUtil.keyMap
import com.simiacryptus.jopenai.util.JsonUtil
import com.simiacryptus.jopenai.util.StringUtil
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.entity.mime.FileBody
import org.apache.hc.client5.http.entity.mime.HttpMultipartMode
import org.apache.hc.client5.http.entity.mime.MultipartEntityBuilder
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.core5.http.ContentType
import org.apache.hc.core5.http.HttpRequest
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import org.slf4j.event.Level
import software.amazon.awssdk.auth.credentials.ProfileCredentialsProvider
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.bedrockruntime.BedrockRuntimeClient
import software.amazon.awssdk.services.bedrockruntime.model.InvokeModelRequest
import java.awt.image.BufferedImage
import java.io.BufferedOutputStream
import java.io.IOException
import java.net.URL
import java.util.*
import java.util.concurrent.Semaphore
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO

open class OpenAIClient(
  protected var key: Map<APIProvider, String> = keyMap.mapKeys { APIProvider.valueOf(it.key) },
  protected val apiBase: Map<APIProvider, String> = APIProvider.values().associate { it to (it.base ?: "") },
  logLevel: Level = Level.INFO,
  logStreams: MutableList<BufferedOutputStream> = mutableListOf(),
  scheduledPool: ListeningScheduledExecutorService = HttpClientManager.scheduledPool,
  workPool: ThreadPoolExecutor = HttpClientManager.workPool,
  client: CloseableHttpClient = HttpClientManager.client
) : HttpClientManager(
  logLevel = logLevel,
  logStreams = logStreams,
  scheduledPool = scheduledPool,
  workPool = workPool,
  client = client
) {

  private val tokenCounter = AtomicInteger(0)

  open fun onUsage(model: OpenAIModel?, tokens: Usage) {
    tokenCounter.addAndGet(tokens.total_tokens)
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

  @Throws(IOException::class, InterruptedException::class)
  protected fun post(url: String, json: String, apiProvider: APIProvider): String {
    val request = HttpPost(url)
    request.addHeader("Content-Type", "application/json")
    request.addHeader("Accept", "application/json")
    authorize(request, apiProvider)
    request.entity = StringEntity(json, Charsets.UTF_8, false)
    return post(request)
  }

  protected fun post(request: HttpPost): String = withClient { EntityUtils.toString(it.execute(request).entity) }

  @Throws(IOException::class)
  protected open fun authorize(request: HttpRequest, apiProvider: APIProvider) {
    request.addHeader("Authorization", "Bearer ${key.get(apiProvider)}")
  }

  @Throws(IOException::class)
  protected operator fun get(url: String?, apiProvider: APIProvider): String = withClient {
    val request = HttpGet(url)
    request.addHeader("Content-Type", "application/json")
    request.addHeader("Accept", "application/json")
    authorize(request, apiProvider)
    EntityUtils.toString(it.execute(request).entity)
  }

  fun listEngines(): List<Engine> = JsonUtil.objectMapper().readValue(
    JsonUtil.objectMapper().readValue(
      get("${apiBase[defaultApiProvider]}/engines", defaultApiProvider), ObjectNode::class.java
    )["data"]?.toString() ?: "{}", JsonUtil.objectMapper().typeFactory.constructCollectionType(
      List::class.java, Engine::class.java
    )
  )

  fun getEngineIds(): Array<CharSequence?> = listEngines().map { it.id }.sortedBy { it }.toTypedArray()

  open fun complete(
    request: CompletionRequest, model: OpenAITextModel
  ): CompletionResponse = withReliability {
    withPerformanceLogging {
      completionCounter.incrementAndGet()
      if (request.suffix == null) {
        log(
          msg = String.format(
            "Text Completion Request\nPrefix:\n\t%s\n", request.prompt.replace("\n", "\n\t")
          )
        )
      } else {
        log(
          msg = String.format(
            "Text Completion Request\nPrefix:\n\t%s\nSuffix:\n\t%s\n",
            request.prompt.replace("\n", "\n\t"),
            request.suffix.replace("\n", "\n\t")
          )
        )
      }
      val result = post(
        "${apiBase[defaultApiProvider]}/engines/${model.modelName}/completions", StringUtil.restrictCharacterSet(
          JsonUtil.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(request),
          allowedCharset
        ), defaultApiProvider
      )
      checkError(result)
      val response = JsonUtil.objectMapper().readValue(
        result, CompletionResponse::class.java
      )
      if (response.usage != null) {
        onUsage(model, response.usage.copy(cost = model.pricing(response.usage)))
      }
      val completionResult =
        StringUtil.stripPrefix(response.firstChoice.orElse("").toString().trim { it <= ' ' },
          request.prompt.trim { it <= ' ' })
      log(
        msg = String.format(
          "Chat Completion:\n\t%s", completionResult.toString().replace("\n", "\n\t")
        )
      )
      response
    }
  }

  open fun transcription(wavAudio: ByteArray, prompt: String = ""): String = withReliability {
    withPerformanceLogging {
      transcriptionCounter.incrementAndGet()
      val url = "$apiBase/audio/transcriptions"
      val request = HttpPost(url)
      request.addHeader("Accept", "application/json")
      authorize(request, defaultApiProvider)
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

  open fun createSpeech(request: SpeechRequest): ByteArray? = withReliability {
    withPerformanceLogging {
      val httpRequest = HttpPost("${apiBase[defaultApiProvider]}/audio/speech")
      authorize(httpRequest, defaultApiProvider)
      httpRequest.addHeader("Accept", "application/json")
      httpRequest.addHeader("Content-Type", "application/json")
      httpRequest.entity = StringEntity(JsonUtil.objectMapper().writeValueAsString(request), Charsets.UTF_8, false)
      val response = withClient { it.execute(httpRequest).entity }
      val contentType = response.contentType
      val bytes = response.content.readAllBytes()
      if (contentType != null && contentType.startsWith("text") || contentType.startsWith("application/json")) {
        checkError(bytes.toString(Charsets.UTF_8))
        null
      } else {
        val model = AudioModels.values().find { it.modelName.equals(request.model, true) }
        onUsage(
          model, Usage(
            prompt_tokens = request.input.length,
            cost = model?.pricing(request.input.length)
          )
        )
        bytes
      }
    }
  }


  open fun render(prompt: String = "", resolution: Int = 1024, count: Int = 1): List<BufferedImage> =
    withReliability {
      withPerformanceLogging {
        renderCounter.incrementAndGet()
        val url = "${apiBase[defaultApiProvider]}/images/generations"
        val request = HttpPost(url)
        request.addHeader("Accept", "application/json")
        request.addHeader("Content-Type", "application/json")
        authorize(request, defaultApiProvider)
        val jsonObject = JsonObject()
        jsonObject.addProperty("prompt", prompt)
        jsonObject.addProperty("n", count)
        jsonObject.addProperty("size", "${resolution}x$resolution")
        request.entity = StringEntity(jsonObject.toString(), Charsets.UTF_8, false)
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

  open fun chat(
    chatRequest: ChatRequest, model: ChatModels
  ): ChatResponse {
    //log.info("Chat request: $chatRequest", RuntimeException())
    return withReliability {
      withPerformanceLogging {
        chatCounter.incrementAndGet()

        val apiProvider = model.provider
        val result = when {
          apiProvider == APIProvider.Perplexity -> {
            val json =
              JsonUtil.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(chatRequest.copy(stop = null))
            log(msg = String.format("Chat Request\nPrefix:\n\t%s\n", json.replace("\n", "\n\t")))
            post("${apiBase[apiProvider]}/chat/completions", json, apiProvider)
          }

          apiProvider == APIProvider.Groq -> {
            val json = JsonUtil.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(toGroq(chatRequest))
            log(msg = String.format("Chat Request\nPrefix:\n\t%s\n", json.replace("\n", "\n\t")))
            post("${apiBase[apiProvider]}/chat/completions", json, apiProvider)
          }

          apiProvider == APIProvider.ModelsLab -> {
            modelsLabThrottle.runWithPermit {
              val json =
                JsonUtil.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(toModelsLab(chatRequest))
              log(msg = String.format("Chat Request\nPrefix:\n\t%s\n", json.replace("\n", "\n\t")))
              fromModelsLab(post("${apiBase[apiProvider]}/llm/chat", json, apiProvider))
            }
          }

          apiProvider == APIProvider.AWS -> {
            val awsAuth = JsonUtil.fromJson<AWSAuth>(key[apiProvider]!!, AWSAuth::class.java)
            val invokeModelRequest = toAWS(model, chatRequest)
            val bedrockRuntimeClient = BedrockRuntimeClient.builder()
              .credentialsProvider(ProfileCredentialsProvider.builder().profileName(awsAuth.profile).build())
              .region(Region.of(awsAuth.region))
              .build()
            val invokeModelResponse = bedrockRuntimeClient
              .invokeModel(invokeModelRequest)
            val responseBody = invokeModelResponse.body().asString(Charsets.UTF_8)
            fromAWS(responseBody, model.modelName)
          }

          else -> {
            val json = JsonUtil.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(chatRequest)
            log(msg = String.format("Chat Request\nPrefix:\n\t%s\n", json.replace("\n", "\n\t")))
            post("${apiBase[apiProvider]}/chat/completions", json, apiProvider)
          }
        }
        checkError(result)
        val response = JsonUtil.objectMapper().readValue(result, ChatResponse::class.java)
        if (response.usage != null) {
          onUsage(model, response.usage.copy(cost = model.pricing(response.usage)))
        }
        log(
          msg = String.format(
            "Chat Completion:\n\t%s",
            response.choices.firstOrNull()?.message?.content?.trim { it <= ' ' }?.replace("\n", "\n\t")
              ?: JsonUtil.toJson(response)
          )
        )
        response
      }
    }
  }

  data class AWSAuth(
    val profile: String = "default",
    val region: String = Region.US_WEST_2.id(),
  )

  open fun toAWS(model: ChatModels, chatRequest: ChatRequest) = InvokeModelRequest.builder()
    .modelId(model.modelName)
    .accept("application/json")
    .contentType("application/json")
    .body(SdkBytes.fromString(JsonUtil.toJson(awsBody(model, chatRequest)), Charsets.UTF_8))
    .build()

  open fun awsBody(
    model: ChatModels,
    chatRequest: ChatRequest
  ) = when {
    model.modelName.contains("llama2") -> {
      mapOf(
        "prompt" to toSimplePrompt(chatRequest),
        "max_gen_len" to model.maxTokens,
        "temperature" to chatRequest.temperature,
//        "top_p" to 0.9,
      )
    }
    //mistral
    model.modelName.contains("mistral") -> {
      mapOf(
        "prompt" to toSimplePrompt(chatRequest),
        "max_tokens" to model.maxTokens,
        "temperature" to chatRequest.temperature,
//        "top_p" to 0.9,
//        "top_k" to 50,
      )
    }

    model.modelName.contains("titan") -> {
      mapOf(
        "inputText" to toSimplePrompt(chatRequest),
        "textGenerationConfig" to mapOf(
          "maxTokenCount" to model.maxTokens,
          "stopSequences" to emptyList<String>(),
          "temperature" to chatRequest.temperature,
//          "topP" to 0.9,
        )
      )
    }

    model.modelName.contains("cohere") -> {
      mapOf(
        "prompt" to toSimplePrompt(chatRequest),
        "max_tokens" to model.maxTokens,
        "temperature" to chatRequest.temperature,
//        "p" to 1,
//        "k" to 0,
      )
    }

    model.modelName.contains("ai21") -> {
      mapOf(
        "prompt" to toSimplePrompt(chatRequest),
        "maxTokens" to model.maxTokens,
        "temperature" to chatRequest.temperature,
//        "topP" to 0.9,
        "stopSequences" to emptyList<String>(),
        "countPenalty" to mapOf("scale" to 0),
        "presencePenalty" to mapOf("scale" to 0),
        "frequencyPenalty" to mapOf("scale" to 0),
      )
    }

    model.modelName.contains("anthropic") -> {
      val alternatingMessages = alternateMessagesRoles(chatRequest.messages)
      mapOf(
        "anthropic_version" to anthropic_version(model),
        "max_tokens" to model.maxTokens,
        "temperature" to chatRequest.temperature,
        "messages" to alternatingMessages.filter {
          when (it.role) {
            Role.system -> false
            else -> true
          }
        }.map {
          mapOf(
            "role" to it.role.toString(),
            "content" to it.content?.map {
              mapOf(
                "type" to "text",
                "text" to it.text
              )
            }
          )
        },
        "system" to toSimplePrompt(chatRequest) { it.role == Role.system },
      ).filterValues { it != null }
    }


    else -> throw RuntimeException("Unsupported model: $model")
  }

  open fun anthropic_version(model: ChatModels) = when {
    else -> "bedrock-2023-05-31"
//    else -> null
  }

  private fun alternateMessagesRoles(messages: List<ChatMessage>): List<ChatMessage> {
    val alternatingMessages = mutableListOf<ChatMessage>()
    val messagesCopy = messages.toMutableList()
    while (messagesCopy.isNotEmpty()) {
      val thisRole = messagesCopy.firstOrNull()?.role
      val toConsolidate = messagesCopy.takeWhile { it.role == thisRole }.toTypedArray()
      messagesCopy.removeAll(toConsolidate)
      val consolidatedMessage = toConsolidate.reduce { acc, chatMessage ->
        ChatMessage(
          role = acc.role,
          content = listOf(
            ContentPart(
              type = "text",
              text = (acc.content?.plus(chatMessage.content ?: emptyList())
                ?: chatMessage.content)?.joinToString("\n") { it.text ?: "" }
            )
          )
        )
      }
      alternatingMessages.add(consolidatedMessage)
    }
    return alternatingMessages
  }

  open fun toSimplePrompt(
    chatRequest: ChatRequest,
    filterFn: (ChatMessage) -> Boolean = { true }
  ) = if (chatRequest.messages.filter(filterFn).map { it.role }.distinct().size <= 1) {
    chatRequest.messages.filter(filterFn).joinToString("\n\n") {
      it.content?.joinToString("\n") { it.text ?: "" } ?: ""
    }
  } else {
    chatRequest.messages.filter(filterFn).joinToString("\n\n") {
      "${it.role}: \n" + it.content?.joinToString("\n") { "\t" + (it.text ?: "") }
    }
  }

  private fun fromAWS(responseBody: String, model: String): String {
    return when {
      model.contains("llama2") -> {
        val fromJson = JsonUtil.fromJson<AwsResponseLlama2>(responseBody, AwsResponseLlama2::class.java)
        JsonUtil.toJson(
          ChatResponse(
            choices = listOf(
              ChatChoice(
                message = ChatMessageResponse(
                  content = fromJson.generation ?: ""
                ),
                index = 0
              )
            ),
            usage = Usage(
              prompt_tokens = fromJson.prompt_token_count ?: 0,
              completion_tokens = fromJson.generation_token_count ?: 0,
              total_tokens = (fromJson.prompt_token_count ?: 0) + (fromJson.generation_token_count ?: 0)
            )
          )
        )
      }

      model.contains("mistral") -> {
        val fromJson = JsonUtil.fromJson<AwsResponseMistral>(responseBody, AwsResponseMistral::class.java)
        JsonUtil.toJson(
          ChatResponse(
            choices = listOf(
              ChatChoice(
                message = ChatMessageResponse(
                  content = fromJson.outputs.first().text ?: ""
                ),
                index = 0
              )
            )
          )
        )
      }

      model.contains("titan") -> {
        val fromJson = JsonUtil.fromJson<AwsResponseTitan>(responseBody, AwsResponseTitan::class.java)
        JsonUtil.toJson(
          ChatResponse(
            choices = listOf(
              ChatChoice(
                message = ChatMessageResponse(
                  content = fromJson.results.first().outputText ?: ""
                ),
                index = 0
              )
            )
          )
        )
      }

      model.contains("cohere") -> {
        val fromJson = JsonUtil.fromJson<AwsResponseCohere>(responseBody, AwsResponseCohere::class.java)
        JsonUtil.toJson(
          ChatResponse(
            choices = listOf(
              ChatChoice(
                message = ChatMessageResponse(
                  content = fromJson.generations.first().text ?: ""
                ),
                index = 0
              )
            )
          )
        )
      }

      model.contains("ai21") -> {
        val fromJson = JsonUtil.objectMapper().readValue(responseBody, Ai21ChatResponse::class.java)
        return JsonUtil.toJson(
          ChatResponse(
            choices = fromJson.completions?.mapIndexed { index, completion ->
              ChatChoice(
                message = ChatMessageResponse(
                  content = completion.data?.text ?: ""
                ),
                index = index
              )
            } ?: emptyList(),
          )
        )
      }

      model.contains("anthropic") -> {
        val fromJson = JsonUtil.fromJson<AwsResponseAnthropic>(responseBody, AwsResponseAnthropic::class.java)
        JsonUtil.toJson(
          ChatResponse(
            choices = listOf(
              ChatChoice(
                message = ChatMessageResponse(
                  content = fromJson.content?.first()?.text ?: ""
                ),
                index = 0
              )
            ),
            usage = Usage(
              prompt_tokens = fromJson.usage?.input_tokens ?: 0,
              completion_tokens = fromJson.usage?.output_tokens ?: 0,
              total_tokens = (fromJson.usage?.input_tokens ?: 0) + (fromJson.usage?.output_tokens ?: 0)
            )
          )
        )
      }

      else -> throw RuntimeException("Unsupported model: $model")
    }
  }

  data class AwsResponseAnthropic(
    val id: String? = null,
    val type: String? = null,
    val role: String? = null,
    val content: List<AwsResponseAnthropicContent>? = null,
    val model: String? = null,
    val stop_reason: String? = null,
    val stop_sequence: String? = null,
    val usage: AwsResponseAnthropicUsage? = null
  )

  data class AwsResponseAnthropicContent(
    val type: String? = null,
    val text: String? = null
  )

  data class AwsResponseAnthropicUsage(
    val input_tokens: Int? = null,
    val output_tokens: Int? = null
  )

  data class Ai21ChatResponse(
    val id: Int? = null,
    val prompt: Ai21Prompt? = null,
    val completions: List<Ai21Completion>? = null
  )

  data class Ai21Completion(
    val data: Ai21Data? = null,
    val finishReason: Ai21FinishReason? = null
  )

  data class Ai21FinishReason(
    val reason: String? = null
  )

  data class Ai21Data(
    val text: String? = null,
    val tokens: List<Ai21Token>? = null
  )

  data class Ai21Prompt(
    val text: String? = null,
    val tokens: List<Ai21Token>? = null
  )

  data class Ai21Token(
    val generatedToken: Ai21GeneratedToken? = null,
    val topTokens: List<Ai21TopToken>? = null,
    val textRange: Ai21TextRange? = null
  )

  data class Ai21GeneratedToken(
    val token: String? = null,
    val logprob: Double? = null,
    val raw_logprob: Double? = null
  )

  data class Ai21TopToken(
    val token: String? = null,
    val logprob: Double? = null,
    val raw_logprob: Double? = null
  )

  data class Ai21TextRange(
    val start: Int? = null,
    val end: Int? = null
  )

  data class AwsResponseCohere(
    val generations: List<AwsResponseCohereGeneration>
  )

  data class AwsResponseCohereGeneration(
    val text: String? = null
  )

  data class AwsResponseMistral(
    val outputs: List<AwsResponseMistralOutput>
  )

  data class AwsResponseMistralOutput(
    val text: String? = null,
    val stop_reason: String? = null
  )

  // AWS Titan
  // {"inputTextTokenCount":31,"results":[{"tokenCount":201,"outputText":"Bot: A coconut (Cocos nucifera) is a drupe, not a nut, classified as a member of the palm family (Arecaceae). Coconuts are ubiquitous in tropical regions, with significant cultural and economic importance. They are known for their unique shape, elongated form, and hard, fibrous shell. Inside the shell, there is a white flesh containing a liquid, known as coconut water, and a large, edible seed, known as a coconut kernel. Coconuts have various uses, including food, beverages, cosmetics, and traditional medicine. They are a rich source of nutrients, including dietary fiber, vitamins, and minerals. Coconut water is renowned for its hydrating properties and is often consumed as a natural electrolyte replacement. Coconut kernels can be processed into coconut oil, which is widely used in cooking, baking, and skincare. Additionally, coconut shells can be used for various purposes, such as construction, handicrafts, and as a source of charcoal.","completionReason":"FINISH"}]}
  data class AwsResponseTitan(
    val inputTextTokenCount: Int? = null,
    val results: List<AwsResponseTitanResult>
  )

  data class AwsResponseTitanResult(
    val tokenCount: Int? = null,
    val outputText: String? = null,
    val completionReason: String? = null
  )

  data class AwsResponseLlama2(
    val generation: String? = null,
    val prompt_token_count: Int? = null,
    val generation_token_count: Int? = null,
    val stop_reason: String? = null
  )

  open fun fromModelsLab(rawResponse: String): String {
    val response = JsonUtil.objectMapper().readValue(rawResponse, ModelsLabDataModel.ChatResponse::class.java)
    return when (response.status) {
      "success" -> {
        JsonUtil.toJson(ChatResponse(
          id = response.chat_id,
          choices = listOf(
            ChatChoice(
              message = ChatMessageResponse(content = response.message),
              index = 0
            )
          ),
          usage = response.meta?.let {
            Usage(
              prompt_tokens = it.max_new_tokens ?: 0,
              completion_tokens = 0, // Assuming no direct mapping; adjust as needed.
              total_tokens = it.max_new_tokens ?: 0
            )
          }
        ))
      }

      "processing" -> {
        val seconds = response?.eta ?: 1
        log.info("Chat response is still processing; waiting ${seconds}s and trying again.")
        Thread.sleep(seconds * 1000L)
        val postCheck = JsonUtil.objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(
          mapOf(
            "chat_id" to (response.meta?.chat_id ?: response.chat_id),
            "key" to key[APIProvider.ModelsLab]
          )
        )
        fromModelsLab(post("${apiBase[defaultApiProvider]}/llm/get_queued_response", postCheck, defaultApiProvider))
      }

      "error" -> {
        throw RuntimeException("Error in chat request: ${response.message}\n$rawResponse")
      }

      "failed" -> {
        throw RuntimeException("Chat request failed: ${response.message}\n$rawResponse")
      }

      else -> throw RuntimeException("Unknown status: ${response.status}\n${response.message}\n$rawResponse")
    }
  }

  open fun toModelsLab(chatRequest: ApiModel.ChatRequest) =
    modelslab_chatRequest_prototype.copy(
      key = key[APIProvider.ModelsLab],
      model_id = chatRequest.model,
      system_prompt = chatRequest.messages.filter { it.role == Role.system }.joinToString("\n") {
        it.content?.joinToString("\n") { it.text ?: "" } ?: ""
      },
      prompt = chatRequest.messages.filter { it.role != Role.system }.joinToString("\n") {
        it.content?.joinToString("\n") { it.text ?: "" } ?: ""
      },
      temperature = chatRequest.temperature,
    )

  open fun toGroq(chatRequest: ChatRequest): GroqChatRequest = GroqChatRequest(
    messages = chatRequest.messages.map { message ->
      GroqChatMessage(
        role = message.role,
        content = message.content?.joinToString("\n") { it.text ?: "" } ?: "",
      )
    },
    model = chatRequest.model,
    max_tokens = chatRequest.max_tokens,
    temperature = chatRequest.temperature,
  )

  open fun moderate(text: String) = withReliability {
    when {
      defaultApiProvider == APIProvider.Groq -> return@withReliability
      defaultApiProvider == APIProvider.ModelsLab -> return@withReliability
    }
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
        this.post("${apiBase[defaultApiProvider]}/moderations", body, defaultApiProvider)
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
            }.reduce { a: String, b: String -> "$a, $b" }.orElse("???")
          )
        )
      }
    }
  }

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
      val result = post("${apiBase[defaultApiProvider]}/edits", request, defaultApiProvider)
      checkError(result)
      val response = JsonUtil.objectMapper().readValue(
        result, CompletionResponse::class.java
      )
      if (response.usage != null) {
        val model = EditModels.values().values.find { it.modelName.equals(editRequest.model, true) }
        onUsage(
          model, response.usage.copy(cost = model?.pricing(response.usage))
        )
      }
      log(
        msg = String.format(
          "Chat Completion:\n\t%s",
          response.firstChoice.orElse("").toString().trim { it <= ' ' }.toString().replace("\n", "\n\t")
        )
      )
      response
    }
  }

  open fun listModels(): ModelListResponse {
    val result = get("${apiBase[defaultApiProvider]}/models", defaultApiProvider)
    checkError(result)
    return JsonUtil.objectMapper().readValue(result, ModelListResponse::class.java)
  }

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
          "${apiBase[defaultApiProvider]}/embeddings", StringUtil.restrictCharacterSet(
            JsonUtil.objectMapper().writeValueAsString(request), allowedCharset
          ), defaultApiProvider
        )
        checkError(result)
        val response = JsonUtil.objectMapper().readValue(
          result, EmbeddingResponse::class.java
        )
        if (response.usage != null) {
          val model = EmbeddingModels.values().values.find { it.modelName.equals(request.model, true) }
          onUsage(
            model,
            response.usage.copy(cost = model?.pricing(response.usage))
          )
        }
        response
      }
    }
  }

  open fun createImage(request: ImageGenerationRequest): ImageGenerationResponse = withReliability {
    withPerformanceLogging {
      val url = "${apiBase[defaultApiProvider]}/images/generations"
      val httpRequest = HttpPost(url)
      httpRequest.addHeader("Accept", "application/json")
      httpRequest.addHeader("Content-Type", "application/json")
      authorize(httpRequest, defaultApiProvider)

      val requestBody = Gson().toJson(request)
      httpRequest.entity = StringEntity(requestBody, Charsets.UTF_8, false)

      val response = post(httpRequest)
      checkError(response)
      val model = ImageModels.values().find { it.modelName.equals(request.model, true) }
      val dims = request.size?.split("x")
      onUsage(
        model, Usage(
          completion_tokens = 1, cost = model?.pricing(
            width = dims?.get(0)?.toInt() ?: 0,
            height = dims?.get(1)?.toInt() ?: 0
          )
        )
      )

      JsonUtil.objectMapper().readValue(response, ImageGenerationResponse::class.java)
    }
  }

  open fun createImageEdit(request: ImageEditRequest): ImageEditResponse = withReliability {
    withPerformanceLogging {
      val url = "${apiBase[defaultApiProvider]}/images/edits"
      val httpRequest = HttpPost(url)
      httpRequest.addHeader("Accept", "application/json")
      authorize(httpRequest, defaultApiProvider)

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

  open fun createImageVariation(request: ImageVariationRequest): ImageVariationResponse = withReliability {
    withPerformanceLogging {
      val url = "${apiBase[defaultApiProvider]}/images/variations"
      val httpRequest = HttpPost(url)
      httpRequest.addHeader("Accept", "application/json")
      authorize(httpRequest, defaultApiProvider)

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

  companion object {
    private val log = org.slf4j.LoggerFactory.getLogger(OpenAIClient::class.java)
    var modelsLabThrottle = Semaphore(1)
    var modelslab_chatRequest_prototype = ModelsLabDataModel.ChatRequest(
      max_new_tokens = 1000,
      no_repeat_ngram_size = 5,
    )
  }

}

fun Semaphore.runWithPermit(function: () -> String): String {
  this.acquire()
  try {
    return function()
  } finally {
    this.release()
  }
}
