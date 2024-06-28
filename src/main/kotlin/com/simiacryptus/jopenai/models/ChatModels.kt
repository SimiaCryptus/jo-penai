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
    val name: String,
    modelName: String,
    maxTotalTokens: Int,
    maxOutTokens: Int = maxTotalTokens,
    val provider: APIProvider,
    val inputTokenPricePerK: Double,
    val outputTokenPricePerK: Double,
) : OpenAITextModel(
    modelName = modelName,
    maxTotalTokens = maxTotalTokens,
    maxOutTokens = maxOutTokens
) {
    override fun pricing(usage: Usage) =
        (usage.prompt_tokens * inputTokenPricePerK + usage.completion_tokens * outputTokenPricePerK) / 1000.0

    companion object {
        val GPT35Turbo = ChatModels(
            name = "GPT35Turbo",
            modelName = "gpt-3.5-turbo",
            maxTotalTokens = 16384,
            provider = APIProvider.OpenAI,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )

        val GPT4Turbo = ChatModels(
            name = "GPT4Turbo",
            modelName = "gpt-4-turbo",
            maxTotalTokens = 128000,
            provider = APIProvider.OpenAI,
            inputTokenPricePerK = 0.01,
            outputTokenPricePerK = 0.03
        )

        val GPT4o = ChatModels(
            name = "GPT4o",
            modelName = "gpt-4o",
            maxTotalTokens = 128000,
            provider = APIProvider.OpenAI,
            inputTokenPricePerK = 0.005,
            outputTokenPricePerK = 0.015
        )

        val SonarSmallChat = ChatModels(
            name = "SonarSmallChat",
            modelName = "sonar-small-chat",
            maxTotalTokens = 16384,
            provider = APIProvider.Perplexity,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )

        val SonarSmallOnline = ChatModels(
            name = "SonarSmallOnline",
            modelName = "sonar-small-online",
            maxTotalTokens = 12000,
            provider = APIProvider.Perplexity,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )

        val SonarMediumChat = ChatModels(
            name = "SonarMediumChat",
            modelName = "sonar-medium-chat",
            maxTotalTokens = 16384,
            provider = APIProvider.Perplexity,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )

        val SonarMediumOnline = ChatModels(
            name = "SonarMediumOnline",
            modelName = "sonar-medium-online",
            maxTotalTokens = 12000,
            provider = APIProvider.Perplexity,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )

        val Codellama70bInstruct = ChatModels(
            name = "Codellama70bInstruct",
            modelName = "codellama-70b-instruct",
            maxTotalTokens = 16384,
            provider = APIProvider.Perplexity,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )

        val Mistral7bInstruct = ChatModels(
            name = "Mistral7bInstruct",
            modelName = "mistral-7b-instruct",
            maxTotalTokens = 16384,
             provider = APIProvider.Perplexity,
             inputTokenPricePerK = 0.0005,
             outputTokenPricePerK = 0.0015
        )

        val Mixtral8x7bInstruct = ChatModels(
            name = "Mixtral8x7bInstruct",
            modelName = "mixtral-8x7b-instruct",
            maxTotalTokens = 16384,
             provider = APIProvider.Perplexity,
             inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
             outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
         )
        val Mistral7B = ChatModels(
            name = "Mistral7B",
            modelName = "open-mistral-7b",
            maxTotalTokens = 32768,
            provider = APIProvider.Mistral,
            inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
        )
        val Mixtral8x7B = ChatModels(
            name = "Mixtral8x7B",
            modelName = "open-mixtral-8x7b",
            maxTotalTokens = 32768,
            provider = APIProvider.Mistral,
            inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
        )
        val Mixtral8x22B = ChatModels(
            name = "Mixtral8x22B",
            modelName = "open-mixtral-8x22b",
            maxTotalTokens = 65536,
            provider = APIProvider.Mistral,
            inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
        )
        val MistralSmall = ChatModels(
            name = "MistralSmall",
            modelName = "mistral-small-latest",
            maxTotalTokens = 32768,
            provider = APIProvider.Mistral,
            inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
        )
        val MistralMedium = ChatModels(
            name = "MistralMedium",
            modelName = "mistral-medium-latest",
            maxTotalTokens = 32768,
            provider = APIProvider.Mistral,
            inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
        )
        val MistralLarge = ChatModels(
            name = "MistralLarge",
            modelName = "mistral-large-latest",
            maxTotalTokens = 32768,
            provider = APIProvider.Mistral,
            inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
        )
        val Codestral = ChatModels(
            name = "Codestral",
            modelName = "codestral-latest",
            maxTotalTokens = 32768,
            provider = APIProvider.Mistral,
            inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
        )

        val LLaMA270bChat = ChatModels(
            name = "LLaMA270bChat",
            modelName = "llama2-70b-4096",
            maxTotalTokens = 4096,
            provider = APIProvider.Groq,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        val LLaMA38b = ChatModels(
            name = "LLaMA38b",
            modelName = "llama3-8b-8192",
            maxTotalTokens = 8192,
             provider = APIProvider.Groq,
            inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
        )
        val Mixtral8x7bInstructV01 = ChatModels(
            name = "Mixtral8x7bInstructV01",
            modelName = "mixtral-8x7b-32768",
            maxTotalTokens = 32768,
            provider = APIProvider.Groq,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )

        val Gemma7bIt = ChatModels(
            name = "Gemma7bIt",
            modelName = "gemma-7b-it",
            maxTotalTokens = 8192,
            maxOutTokens = 8192,
            provider = APIProvider.Groq,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        /*
        */

        val Zephyr7bBeta = ChatModels(
            name = "Zephyr7bBeta",
            modelName = "zephyr-7b-beta",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        val DialoGPTLarge = ChatModels(
            name = "DialoGPTLarge",
            modelName = "DialoGPT-large",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        val YarnMistral7b128k = ChatModels(
            name = "YarnMistral7b128k",
            modelName = "Yarn-Mistral-7b-128k",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        val Pygmalion13b = ChatModels(
            name = "Pygmalion13b",
            modelName = "pygmalion-1.3b",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        val Opt67b = ChatModels(
            name = "Opt67b",
            modelName = "opt-6.7b",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        val MistralLite = ChatModels(
            name = "MistralLite",
            modelName = "MistralLite",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        val Openchat35 = ChatModels(
            name = "Openchat35",
            modelName = "openchat_3.5",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        val NeuralChat7bV3 = ChatModels(
            name = "NeuralChat7bV3",
            modelName = "neural-chat-7b-v3",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        val OpenHermes25Mistral7B = ChatModels(
            name = "OpenHermes25Mistral7B",
            modelName = "OpenHermes-2.5-Mistral-7B",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        val Dolphin221Mistral7b = ChatModels(
            name = "Dolphin221Mistral7b",
            modelName = "dolphin-2.2.1-mistral-7b",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        val Mistral7BOpenOrca = ChatModels(
            name = "Mistral7BOpenOrca",
            modelName = "Mistral-7B-OpenOrca",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )

        val DeepseekCoder67bInstruct = ChatModels(
            name = "DeepseekCoder67bInstruct",
            modelName = "deepseek-coder-6.7b-instruct",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        val Phi15 = ChatModels(
            name = "Phi15",
            modelName = "phi-1_5",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
        val Zephyr7bAlpha = ChatModels(
            name = "Zephyr7bAlpha",
            modelName = "zephyr-7b-alpha",
            maxTotalTokens = 16384,
            provider = APIProvider.ModelsLab,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )

        val AWSLLaMA270bChat = ChatModels(
            name = "AWSLLaMA270bChat",
            modelName = "meta.llama2-70b-chat-v1",
            maxTotalTokens = 2048,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.00195,
            outputTokenPricePerK = 0.00256
        )
        val AWSLLaMA213bChat = ChatModels(
            name = "AWSLLaMA213bChat",
            modelName = "meta.llama2-13b-chat-v1",
            maxTotalTokens = 2048,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.00075,
            outputTokenPricePerK = 0.001
        )
        val Mistral7bInstructV02 = ChatModels(
            name = "Mistral7bInstructV02",
            modelName = "mistral.mistral-7b-instruct-v0:2",
            maxTotalTokens = 32 * 1024,
            maxOutTokens = 2 * 1024,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.00015,
            outputTokenPricePerK = 0.0002
        )
        val Mixtral8x7bInstructV01AWS = ChatModels(
            name = "Mixtral8x7bInstructV01AWS",
            modelName = "mistral.mixtral-8x7b-instruct-v0:1",
            maxTotalTokens = 32 * 1024,
            maxOutTokens = 2 * 1024,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.00045,
            outputTokenPricePerK = 0.0007
        )
        val MistralLarge2402 = ChatModels(
            name = "MistralLarge2402",
            modelName = "mistral.mistral-large-2402-v1:0",
            maxTotalTokens = 32 * 1024,
            maxOutTokens = 4000,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.008,
            outputTokenPricePerK = 0.024
        )

        val AmazonTitanTextLiteV1 = ChatModels(
            name = "AmazonTitanTextLiteV1",
            modelName = "amazon.titan-text-lite-v1",
            maxTotalTokens = 4096,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.0003,
            outputTokenPricePerK = 0.0004
        )
        val AmazonTitanTextExpressV1 = ChatModels(
            name = "AmazonTitanTextExpressV1",
            modelName = "amazon.titan-text-express-v1",
            maxTotalTokens = 8192,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.0008,
            outputTokenPricePerK = 0.0016
        )
        val Claude3OpusAWS = ChatModels(
            name = "Claude3OpusAWS",
            modelName = "anthropic.claude-3-opus-20240229-v1:0",
            maxTotalTokens = 200000,
            maxOutTokens = 4096,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 15.0 / 1000.0,
            outputTokenPricePerK = 75.0 / 1000.0
        )
        val CohereCommandTextV14 = ChatModels(
            name = "CohereCommandTextV14",
            modelName = "cohere.command-text-v14",
            maxTotalTokens = 4000,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.0015,
            outputTokenPricePerK = 0.002
        )
        val AI21J2UltraV1 = ChatModels(
            name = "AI21J2UltraV1",
            modelName = "ai21.j2-ultra-v1",
            maxTotalTokens = 8191,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.0125,
            outputTokenPricePerK = 0.0125
        )
        val AI21J2MidV1 = ChatModels(
            name = "AI21J2MidV1",
            modelName = "ai21.j2-mid-v1",
            maxTotalTokens = 8191,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.0188,
            outputTokenPricePerK = 0.0188
        )
        val Claude35Sonnet = ChatModels(
            name = "Claude3Sonnet",
            modelName = "anthropic.claude-3-5-sonnet-20240620-v1:0",
            maxTotalTokens = 200000,
            maxOutTokens = 4096,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.003,
            outputTokenPricePerK = 0.015
        )
        val Claude3Sonnet = ChatModels(
            name = "Claude3Sonnet",
            modelName = "anthropic.claude-3-sonnet-20240229-v1:0",
            maxTotalTokens = 200000,
            maxOutTokens = 4096,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.003,
            outputTokenPricePerK = 0.015
        )
        val Claude3Haiku = ChatModels(
            name = "Claude3Haiku",
            modelName = "anthropic.claude-3-haiku-20240307-v1:0",
            maxTotalTokens = 200000,
            maxOutTokens = 4096,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.00025,
            outputTokenPricePerK = 0.000125
        )
        val ClaudeV2_1 = ChatModels(
            name = "ClaudeV2",
            modelName = "anthropic.claude-v2:1",
            maxTotalTokens = 100000,
            maxOutTokens = 4096,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.008,
            outputTokenPricePerK = 0.024
        )
        val ClaudeV2 = ChatModels(
            name = "ClaudeV2",
            modelName = "anthropic.claude-v2",
            maxTotalTokens = 100000,
            maxOutTokens = 4096,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.008,
            outputTokenPricePerK = 0.024
        )
        val ClaudeV2Instant = ChatModels(
            name = "ClaudeV2",
            modelName = "anthropic.claude-instant-v1",
            maxTotalTokens = 100000,
            maxOutTokens = 4096,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.0008,
            outputTokenPricePerK = 0.0024
        )
        val LLaMA38bInstructAWS = ChatModels(
            name = "LLaMA38bInstructAWS",
            modelName = "meta.llama3-8b-instruct-v1:0",
            maxTotalTokens = 8192,
            maxOutTokens = 2048,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
        )
        val LLaMA370bInstructAWS = ChatModels(
            name = "LLaMA370bInstructAWS",
            modelName = "meta.llama3-70b-instruct-v1:0",
            maxTotalTokens = 8192,
            maxOutTokens = 2048,
            provider = APIProvider.AWS,
            inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
        )
        val LaMA38b = ChatModels(
            name = "LaMA38b",
            modelName = "llama3-8b-8192",
            maxTotalTokens = 8192,
            provider = APIProvider.Groq,
            inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
        )
        val LLaMA370b = ChatModels(
            name = "LLaMA370b",
            modelName = "llama3-70b-8192",
            maxTotalTokens = 8192,
            provider = APIProvider.Groq,
            inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
        )
        val AnthropicClaude3Opus = ChatModels(
            name = "Claude3Opus",
            modelName = "claude-3-opus-20240229",
            maxTotalTokens = 200000,
            maxOutTokens = 4096,
            provider = APIProvider.Anthropic,
            inputTokenPricePerK = 15.0 / 1000.0,
            outputTokenPricePerK = 75.0 / 1000.0
        )
        val AnthropicClaude35Sonnet = ChatModels(
            name = "Claude3Sonnet",
            modelName = "claude-3-5-sonnet-20240620",
            maxTotalTokens = 200000,
            maxOutTokens = 4096,
            provider = APIProvider.Anthropic,
            inputTokenPricePerK = 3.0 / 1000.0,
            outputTokenPricePerK = 15.0 / 1000.0
        )
        val AnthropicClaude3Sonnet = ChatModels(
            name = "Claude3Sonnet",
            modelName = "claude-3-sonnet-20240229",
            maxTotalTokens = 200000,
            maxOutTokens = 4096,
            provider = APIProvider.Anthropic,
            inputTokenPricePerK = 3.0 / 1000.0,
            outputTokenPricePerK = 15.0 / 1000.0
        )
        val AnthropicClaude3Haiku = ChatModels(
            name = "Claude3Haiku",
            modelName = "claude-3-haiku-20240307",
            maxTotalTokens = 200000,
            maxOutTokens = 4096,
            provider = APIProvider.Anthropic,
            inputTokenPricePerK = 0.25 / 1000.0,
            outputTokenPricePerK = 1.25 / 1000.0
        )

        val Gemini15ProPreview = ChatModels(
            name = "Gemini15ProPreview",
            modelName = "models/gemini-1.5-pro-latest",
            maxTotalTokens = 1048576,
            maxOutTokens = 8192,
            provider = APIProvider.Google,
            inputTokenPricePerK = 0.007, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.021
        )
        val GeminiFlashPreview = ChatModels(
            name = "GeminiFlashPreview",
            modelName = "gemini-1.5-flash-latest",
            maxTotalTokens = 1048576,
            maxOutTokens = 8192,
            provider = APIProvider.Google,
            inputTokenPricePerK = 0.007, // Assuming pricing, adjust as necessary
            outputTokenPricePerK = 0.021
        )
        val GeminiPro = ChatModels(
            name = "GeminiPro",
            modelName = "models/gemini-pro",
            maxTotalTokens = 30720,
            maxOutTokens = 2048,
            provider = APIProvider.Google,
            inputTokenPricePerK = 0.0005,
            outputTokenPricePerK = 0.0015
        )
//    val Gemini10ProVision = ChatModels(
//      name = "Gemini10ProVision",
//      modelName = "models/gemini-pro-vision",
//      maxTotalTokens = 12288,
//      maxOutTokens = 4096,
//      provider = APIProvider.Google,
//      inputTokenPricePerK = 0.002, // Assuming pricing, adjust as necessary
//      outputTokenPricePerK = 0.004 // Assuming pricing, adjust as necessary
//    )


        fun values() = mapOf(
            "GPT35Turbo" to GPT35Turbo,
            "GPT4Turbo" to GPT4Turbo,
            "GPT4o" to GPT4o,
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
            "Zephyr7bBeta" to Zephyr7bBeta,
            "DialoGPTLarge" to DialoGPTLarge,
            "YarnMistral7b128k" to YarnMistral7b128k,
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
            "MistralLarge2402" to MistralLarge2402,
            "AWSLLaMA270bChat" to AWSLLaMA270bChat,
            "AWSLLaMA213bChat" to AWSLLaMA213bChat,
            "Mistral7bInstructV02" to Mistral7bInstructV02,
            "Mixtral8x7bInstructV01AWS" to Mixtral8x7bInstructV01AWS,
            "AmazonTitanTextLiteV1" to AmazonTitanTextLiteV1,
            "AmazonTitanTextExpressV1" to AmazonTitanTextExpressV1,
            "Claude3OpusAWS" to Claude3OpusAWS,
            "LLaMA38bInstructAWS" to LLaMA38bInstructAWS,
            "LLaMA370bInstructAWS" to LLaMA370bInstructAWS,
            "CohereCommandTextV14" to CohereCommandTextV14,
            "AI21J2UltraV1" to AI21J2UltraV1,
            "AI21J2MidV1" to AI21J2MidV1,
            "Claude35Sonnet" to Claude35Sonnet,
            "Claude3Sonnet" to Claude3Sonnet,
            "Claude3Haiku" to Claude3Haiku,
            "ClaudeV2_1" to ClaudeV2_1,
            "ClaudeV2" to ClaudeV2,
            "ClaudeV2Instant" to ClaudeV2Instant,
            "AnthropicClaude3Opus" to AnthropicClaude3Opus,
            "AnthropicClaude35Sonnet" to AnthropicClaude35Sonnet,
            "AnthropicClaude3Sonnet" to AnthropicClaude3Sonnet,
            "LLaMA38b" to LLaMA38b,
            "LLaMA370b" to LLaMA370b,
            "LaMA38b" to LaMA38b,
            "LLaMA370b" to LLaMA370b,
            "AnthropicClaude3Haiku" to AnthropicClaude3Haiku,

            "Mistral7B" to Mistral7B,
            "Mixtral8x7B" to Mixtral8x7B,
            "Mixtral8x22B" to Mixtral8x22B,
            "MistralSmall" to MistralSmall,
            "MistralMedium" to MistralMedium,
            "MistralLarge" to MistralLarge,
            "Codestral" to Codestral,

            "GeminiFlashPreview" to GeminiFlashPreview,
            "Gemini15ProPreview" to Gemini15ProPreview,
            "GeminiPro" to GeminiPro,
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
        return values()[modelName] ?: throw IllegalArgumentException("Unknown model name: $modelName")
    }
}