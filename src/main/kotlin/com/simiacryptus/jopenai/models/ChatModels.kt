package com.simiacryptus.jopenai.models

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.simiacryptus.jopenai.ApiModel.Usage
import com.simiacryptus.jopenai.models.ChatModels.Companion.values

@JsonDeserialize(using = ChatModelsDeserializer::class)
@JsonSerialize(using = ChatModelsSerializer::class)
open class ChatModels(
  modelName: String,
  maxTokens: Int,
  val providers: List<APIProvider>,
  private val inputTokenPricePerK: Double,
  private val outputTokenPricePerK: Double,
) : OpenAITextModel(modelName, maxTokens) {
  override fun pricing(usage: Usage) =
    (usage.prompt_tokens * inputTokenPricePerK + usage.completion_tokens * outputTokenPricePerK) / 1000.0

  companion object {
    val GPT35Turbo = ChatModels(
      modelName = "gpt-3.5-turbo-0125",
      maxTokens = 16384,
      providers = listOf(APIProvider.OpenAI),
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val GPT4 = ChatModels(
      modelName = "gpt-4-32k",
      maxTokens = 32768,
      providers = listOf(APIProvider.OpenAI),
      inputTokenPricePerK = 0.06,
      outputTokenPricePerK = 0.12
    )
    val GPT4Turbo = ChatModels(
      modelName = "gpt-4-turbo-preview",
      maxTokens = 128000,
      providers = listOf(APIProvider.OpenAI),
      inputTokenPricePerK = 0.01,
      outputTokenPricePerK = 0.03
    )
    val GPT4Vision = ChatModels(
      modelName = "gpt-4-vision-preview",
      maxTokens = 8192,
      providers = listOf(APIProvider.OpenAI),
      inputTokenPricePerK = 0.01,
      outputTokenPricePerK = 0.03
    )

    val SonarSmallChat = ChatModels(
      modelName = "sonar-small-chat",
      maxTokens = 16384,
      providers = listOf(APIProvider.Perplexity),
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val SonarSmallOnline = ChatModels(
      modelName = "sonar-small-online",
      maxTokens = 12000,
      providers = listOf(APIProvider.Perplexity),
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val SonarMediumChat = ChatModels(
      modelName = "sonar-medium-chat",
      maxTokens = 16384,
      providers = listOf(APIProvider.Perplexity),
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val SonarMediumOnline = ChatModels(
      modelName = "sonar-medium-online",
      maxTokens = 12000,
      providers = listOf(APIProvider.Perplexity),
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val Codellama70bInstruct = ChatModels(
      modelName = "codellama-70b-instruct",
      maxTokens = 16384,
      providers = listOf(APIProvider.Perplexity),
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val Mistral7bInstruct = ChatModels(
      modelName = "mistral-7b-instruct",
      maxTokens = 16384,
      providers = listOf(APIProvider.Perplexity),
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val Mixtral8x7bInstruct = ChatModels(
      modelName = "mixtral-8x7b-instruct",
      maxTokens = 16384,
      providers = listOf(APIProvider.Perplexity),
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val LLaMA270bChat = ChatModels(
      modelName = "llama2-70b-4096",
      maxTokens = 4096,
      providers = listOf(APIProvider.Groq),
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val Mixtral8x7bInstructV01 = ChatModels(
      modelName = "mixtral-8x7b-32768",
      maxTokens = 32768,
      providers = listOf(APIProvider.Groq),
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val Gemma7bIt = ChatModels(
      modelName = "Gemma-7b-it",
      maxTokens = 8192,
      providers = listOf(APIProvider.Groq),
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    fun values() = mapOf(
      "GPT35Turbo" to GPT35Turbo,
      "GPT4" to GPT4,
      "GPT4Turbo" to GPT4Turbo,
      "GPT4Vision" to GPT4Vision,
      "SonarSmallChat" to SonarSmallChat,
      "SonarSmallOnline" to SonarSmallOnline,
      "SonarMediumChat" to SonarMediumChat,
      "SonarMediumOnline" to SonarMediumOnline,
      "Codellama70bInstruct" to Codellama70bInstruct,
      "Mistral7bInstruct" to Mistral7bInstruct,
      "Mixtral8x7bInstruct" to Mixtral8x7bInstruct,
      "LLaMA270bChat" to LLaMA270bChat,
      "Mixtral8x7bInstructV01" to Mixtral8x7bInstructV01,
      "Gemma7bIt" to Gemma7bIt,
    )
  }
}

class ChatModelsSerializer : StdSerializer<ChatModels>(ChatModels::class.java) {
  override fun serialize(value: ChatModels, gen: JsonGenerator, provider: SerializerProvider) {
    values().entries.find { it.value == value }?.key?.let { gen.writeString(it) }
  }
}

class ChatModelsDeserializer : JsonDeserializer<ChatModels>() {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ChatModels {
    val modelName = p.readValueAs(String::class.java)
    return ChatModels.values()[modelName] ?: throw IllegalArgumentException("Unknown model name: $modelName")
  }
}
