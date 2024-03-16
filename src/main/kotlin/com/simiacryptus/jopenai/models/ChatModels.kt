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
  val name : String,
  modelName: String,
  maxTokens: Int,
  val provider: APIProvider,
  private val inputTokenPricePerK: Double,
  private val outputTokenPricePerK: Double,
) : OpenAITextModel(modelName, maxTokens) {
  override fun pricing(usage: Usage) =
    (usage.prompt_tokens * inputTokenPricePerK + usage.completion_tokens * outputTokenPricePerK) / 1000.0

  companion object {
    val GPT35Turbo = ChatModels(
      name = "GPT35Turbo",
      modelName = "gpt-3.5-turbo-0125",
      maxTokens = 16384,
      provider = APIProvider.OpenAI,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
/*
    val GPT4 = ChatModels(
      name = "GPT4",
      modelName = "gpt-4-32k",
      maxTokens = 32768,
      provider = APIProvider.OpenAI,
      inputTokenPricePerK = 0.06,
      outputTokenPricePerK = 0.12
    )
*/
    val GPT4Turbo = ChatModels(
      name = "GPT4Turbo",
      modelName = "gpt-4-turbo-preview",
      maxTokens = 128000,
      provider = APIProvider.OpenAI,
      inputTokenPricePerK = 0.01,
      outputTokenPricePerK = 0.03
    )
    val GPT4Vision = ChatModels(
      name = "GPT4Vision",
      modelName = "gpt-4-vision-preview",
      maxTokens = 8192,
      provider = APIProvider.OpenAI,
      inputTokenPricePerK = 0.01,
      outputTokenPricePerK = 0.03
    )

    val SonarSmallChat = ChatModels(
      name = "SonarSmallChat",
      modelName = "sonar-small-chat",
      maxTokens = 16384,
      provider = APIProvider.Perplexity,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val SonarSmallOnline = ChatModels(
      name = "SonarSmallOnline",
      modelName = "sonar-small-online",
      maxTokens = 12000,
      provider = APIProvider.Perplexity,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val SonarMediumChat = ChatModels(
      name = "SonarMediumChat",
      modelName = "sonar-medium-chat",
      maxTokens = 16384,
      provider = APIProvider.Perplexity,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val SonarMediumOnline = ChatModels(
      name = "SonarMediumOnline",
      modelName = "sonar-medium-online",
      maxTokens = 12000,
      provider = APIProvider.Perplexity,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val Codellama70bInstruct = ChatModels(
      name = "Codellama70bInstruct",
      modelName = "codellama-70b-instruct",
      maxTokens = 16384,
      provider = APIProvider.Perplexity,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val Mistral7bInstruct = ChatModels(
      name = "Mistral7bInstruct",
      modelName = "mistral-7b-instruct",
      maxTokens = 16384,
      provider = APIProvider.Perplexity,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val Mixtral8x7bInstruct = ChatModels(
      name = "Mixtral8x7bInstruct",
      modelName = "mixtral-8x7b-instruct",
      maxTokens = 16384,
      provider = APIProvider.Perplexity,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val LLaMA270bChat = ChatModels(
      name = "LLaMA270bChat",
      modelName = "llama2-70b-4096",
      maxTokens = 4096,
      provider = APIProvider.Groq,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val Mixtral8x7bInstructV01 = ChatModels(
      name = "Mixtral8x7bInstructV01",
      modelName = "mixtral-8x7b-32768",
      maxTokens = 32768,
      provider = APIProvider.Groq,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

/*
    val Gemma7bIt = ChatModels(
      name = "Gemma7bIt",
      modelName = "Gemma-7b-it",
      maxTokens = 8192,
      provider = APIProvider.Groq,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
*/

    val Zephyr7bBeta = ChatModels(
      name = "Zephyr7bBeta",
      modelName = "zephyr-7b-beta",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val DialoGPTLarge = ChatModels(
      name = "DialoGPTLarge",
      modelName = "DialoGPT-large",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val YarnMistral7b128k = ChatModels(
      name = "YarnMistral7b128k",
      modelName = "Yarn-Mistral-7b-128k",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
/*
    val PygmalionAI = ChatModels(
      name = "PygmalionAI",
      modelName = "PygmalionAI",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
*/
    val Pygmalion13b = ChatModels(
      name = "Pygmalion13b",
      modelName = "pygmalion-1.3b",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val Opt67b = ChatModels(
      name = "Opt67b",
      modelName = "opt-6.7b",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val MistralLite = ChatModels(
      name = "MistralLite",
      modelName = "MistralLite",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val Openchat35 = ChatModels(
      name = "Openchat35",
      modelName = "openchat_3.5",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val NeuralChat7bV3 = ChatModels(
      name = "NeuralChat7bV3",
      modelName = "neural-chat-7b-v3",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val OpenHermes25Mistral7B = ChatModels(
      name = "OpenHermes25Mistral7B",
      modelName = "OpenHermes-2.5-Mistral-7B",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val Dolphin221Mistral7b = ChatModels(
      name = "Dolphin221Mistral7b",
      modelName = "dolphin-2.2.1-mistral-7b",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val Mistral7BOpenOrca = ChatModels(
      name = "Mistral7BOpenOrca",
      modelName = "Mistral-7B-OpenOrca",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
//    val CodeLlama7bHf = ChatModels(
//      name = "CodeLlama7bHf",
//      modelName = "CodeLlama-7b-hf",
//      maxTokens = 16384,
//      provider = APIProvider.ModelsLab,
//      inputTokenPricePerK = 0.0005,
//      outputTokenPricePerK = 0.0015
//    )
    val DeepseekCoder67bInstruct = ChatModels(
      name = "DeepseekCoder67bInstruct",
      modelName = "deepseek-coder-6.7b-instruct",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val Phi15 = ChatModels(
      name = "Phi15",
      modelName = "phi-1_5",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val Zephyr7bAlpha = ChatModels(
      name = "Zephyr7bAlpha",
      modelName = "zephyr-7b-alpha",
      maxTokens = 16384,
      provider = APIProvider.ModelsLab,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    val AWSLLaMA270bChat = ChatModels(
      name = "AWSLLaMA270bChat",
      modelName = "meta.llama2-70b-chat-v1",
      maxTokens = 2048,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val AWSLLaMA213bChat = ChatModels(
      name = "AWSLLaMA213bChat",
      modelName = "meta.llama2-13b-chat-v1",
      maxTokens = 2048,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val Mistral7bInstructV02 = ChatModels(
      name = "Mistral7bInstructV02",
      modelName = "mistral.mistral-7b-instruct-v0:2",
      maxTokens = 2 * 1024,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val Mixtral8x7bInstructV01AWS = ChatModels(
      name = "Mixtral8x7bInstructV01AWS",
      modelName = "mistral.mixtral-8x7b-instruct-v0:1",
      maxTokens = 2 * 1024,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
//    val AmazonTitanEmbedTextV1 = ChatModels(
//      name = "AmazonTitanEmbedTextV1",
//      modelName = "amazon.titan-embed-text-v1",
//      maxTokens = 16384,
//      provider = APIProvider.AWS,
//      inputTokenPricePerK = 0.0005,
//      outputTokenPricePerK = 0.0015
//    )
    val AmazonTitanTextLiteV1 = ChatModels(
      name = "AmazonTitanTextLiteV1",
      modelName = "amazon.titan-text-lite-v1",
      maxTokens = 4096,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val AmazonTitanTextExpressV1 = ChatModels(
      name = "AmazonTitanTextExpressV1",
      modelName = "amazon.titan-text-express-v1",
      maxTokens = 8192,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val CohereCommandTextV14 = ChatModels(
      name = "CohereCommandTextV14",
      modelName = "cohere.command-text-v14",
      maxTokens = 4000,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val CohereCommandLightTextV14 = ChatModels(
      name = "CohereCommandLightTextV14",
      modelName = "cohere.command-light-text-v14",
      maxTokens = 4000,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val AI21J2UltraV1 = ChatModels(
      name = "AI21J2UltraV1",
      modelName = "ai21.j2-ultra-v1",
      maxTokens = 8191,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val AI21J2MidV1 = ChatModels(
      name = "AI21J2MidV1",
      modelName = "ai21.j2-mid-v1",
      maxTokens = 8191,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val Claude3Sonnet = ChatModels(
      name = "Claude3Sonnet",
      modelName = "anthropic.claude-3-sonnet-20240229-v1:0",
      maxTokens = 200000,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val Claude3Haiku = ChatModels(
      name = "Claude3Haiku",
      modelName = "anthropic.claude-3-haiku-20240307-v1:0",
      maxTokens = 200000,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val ClaudeV2_1 = ChatModels(
      name = "ClaudeV2",
      modelName = "anthropic.claude-v2:1",
      maxTokens = 100000,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val ClaudeV2 = ChatModels(
      name = "ClaudeV2",
      modelName = "anthropic.claude-v2",
      maxTokens = 100000,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )
    val ClaudeV2Instant = ChatModels(
      name = "ClaudeV2",
      modelName = "anthropic.claude-instant-v1",
      maxTokens = 100000,
      provider = APIProvider.AWS,
      inputTokenPricePerK = 0.0005,
      outputTokenPricePerK = 0.0015
    )

    fun values() = mapOf(
      "GPT35Turbo" to GPT35Turbo,
//      "GPT4" to GPT4,
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
//      "Gemma7bIt" to Gemma7bIt,
      "Zephyr7bBeta" to Zephyr7bBeta,
      "DialoGPTLarge" to DialoGPTLarge,
      "YarnMistral7b128k" to YarnMistral7b128k,
//      "PygmalionAI" to PygmalionAI,
      "Pygmalion13b" to Pygmalion13b,
      "Opt67b" to Opt67b,
      "MistralLite" to MistralLite,
      "Openchat35" to Openchat35,
      "NeuralChat7bV3" to NeuralChat7bV3,
      "OpenHermes25Mistral7B" to OpenHermes25Mistral7B,
      "Dolphin221Mistral7b" to Dolphin221Mistral7b,
      "Mistral7BOpenOrca" to Mistral7BOpenOrca,
//      "CodeLlama7bHf" to CodeLlama7bHf,
      "DeepseekCoder67bInstruct" to DeepseekCoder67bInstruct,
      "Phi15" to Phi15,
      "Zephyr7bAlpha" to Zephyr7bAlpha,
      "AWSLLaMA270bChat" to AWSLLaMA270bChat,
      "AWSLLaMA213bChat" to AWSLLaMA213bChat,
      "Mistral7bInstructV02" to Mistral7bInstructV02,
      "Mixtral8x7bInstructV01AWS" to Mixtral8x7bInstructV01AWS,
//      "AmazonTitanEmbedTextV1" to AmazonTitanEmbedTextV1,
      "AmazonTitanTextLiteV1" to AmazonTitanTextLiteV1,
      "AmazonTitanTextExpressV1" to AmazonTitanTextExpressV1,
      "CohereCommandTextV14" to CohereCommandTextV14,
      "AI21J2UltraV1" to AI21J2UltraV1,
      "AI21J2MidV1" to AI21J2MidV1,
      "Claude3Sonnet" to Claude3Sonnet,
      "Claude3Haiku" to Claude3Haiku,
      "ClaudeV2_1" to ClaudeV2_1,
      "ClaudeV2" to ClaudeV2,
      "ClaudeV2Instant" to ClaudeV2Instant,
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
