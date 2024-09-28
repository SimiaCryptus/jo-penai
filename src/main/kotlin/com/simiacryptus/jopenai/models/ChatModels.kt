package com.simiacryptus.jopenai.models

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.simiacryptus.jopenai.models.ApiModel.Usage
import com.simiacryptus.jopenai.models.ChatModels.Companion.values


@JsonDeserialize(using = ChatModelsDeserializer::class)
@JsonSerialize(using = ChatModelsSerializer::class)
open class ChatModels(
    val name: String,
    modelName: String,
    maxTotalTokens: Int,
    maxOutTokens: Int = maxTotalTokens,
    override val provider: APIProvider,
    val inputTokenPricePerK: Double,
    val outputTokenPricePerK: Double,
) : OpenAITextModel(
    modelName = modelName,
    maxTotalTokens = maxTotalTokens,
    maxOutTokens = maxOutTokens
) {
    override fun toString() = modelName

    override fun pricing(usage: Usage) =
        (usage.prompt_tokens * inputTokenPricePerK + usage.completion_tokens * outputTokenPricePerK) / 1000.0

    companion object {

        fun values() = mapOf(
            "GPT35Turbo" to OpenAIModels.GPT35Turbo,
            "GPT4Turbo" to OpenAIModels.GPT4Turbo,
            "GPT4o" to OpenAIModels.GPT4o,
            "GPT4oMini" to OpenAIModels.GPT4oMini,
            "O1Preview" to OpenAIModels.O1Preview,
            "O1Mini" to OpenAIModels.O1Mini,
            "SonarSmallChat128k" to PerplexityModels.SonarSmallChat128k,
            "SonarSmallOnline128k" to PerplexityModels.SonarSmallOnline128k,
            "SonarLargeChat128k" to PerplexityModels.SonarLargeChat128k,
            "SonarLargeOnline128k" to PerplexityModels.SonarLargeOnline128k,
            "Mistral7B" to MistralModels.Mistral7B,
            "Mixtral8x7B" to MistralModels.Mixtral8x7B,
            "Mixtral8x22B" to MistralModels.Mixtral8x22B,
            "MistralSmall" to MistralModels.MistralSmall,
            "MistralMedium" to MistralModels.MistralMedium,
            "MistralLarge" to MistralModels.MistralLarge,
            "MistralNemo" to MistralModels.MistralNemo,
            "Codestral" to MistralModels.Codestral,
            "CodestralMamba" to MistralModels.CodestralMamba,
            "LaMA38b" to GroqModels.LaMA38b,
            "LLaMA370b" to GroqModels.LLaMA370b,
            "Llama_31_405B" to GroqModels.Llama_31_405B,
            "Llama_31_70B" to GroqModels.Llama_31_70B,
            "Llama_31_8B" to GroqModels.Llama_31_8B,
            "LLaMA270bChat" to GroqModels.LLaMA270bChat,
            "LLaMA38b" to GroqModels.LLaMA38b,
            "Mixtral8x7bInstructV01" to GroqModels.Mixtral8x7bInstructV01,
            "Gemma7bIt" to GroqModels.Gemma7bIt,
            "Gemma2_9bIt" to GroqModels.Gemma2_9bIt,
            "Zephyr7bBeta" to ModelsLabModels.Zephyr7bBeta,
            "DialoGPTLarge" to ModelsLabModels.DialoGPTLarge,
            "YarnMistral7b128k" to ModelsLabModels.YarnMistral7b128k,
            "Pygmalion13b" to ModelsLabModels.Pygmalion13b,
            "Opt67b" to ModelsLabModels.Opt67b,
            "MistralLite" to ModelsLabModels.MistralLite,
            "Openchat35" to ModelsLabModels.Openchat35,
            "NeuralChat7bV3" to ModelsLabModels.NeuralChat7bV3,
            "OpenHermes25Mistral7B" to ModelsLabModels.OpenHermes25Mistral7B,
            "Dolphin221Mistral7b" to ModelsLabModels.Dolphin221Mistral7b,
            "Mistral7BOpenOrca" to ModelsLabModels.Mistral7BOpenOrca,
            "DeepseekCoder67bInstruct" to ModelsLabModels.DeepseekCoder67bInstruct,
            "Phi15" to ModelsLabModels.Phi15,
            "Zephyr7bAlpha" to ModelsLabModels.Zephyr7bAlpha,
            "AWSLLaMA31_405bChat" to AWSModels.AWSLLaMA31_405bChat,
            "AWSLLaMA31_70bChat" to AWSModels.AWSLLaMA31_70bChat,
            "AWSLLaMA31_8bChat" to AWSModels.AWSLLaMA31_8bChat,
            "AWSLLaMA270bChat" to AWSModels.AWSLLaMA270bChat,
            "AWSLLaMA213bChat" to AWSModels.AWSLLaMA213bChat,
            "Mistral7bInstructV02" to AWSModels.Mistral7bInstructV02,
            "Mixtral8x7bInstructV01AWS" to AWSModels.Mixtral8x7bInstructV01AWS,
            "MistralLarge2402" to AWSModels.MistralLarge2402,
            "MistralLarge2407" to AWSModels.MistralLarge2407,
            "AmazonTitanTextLiteV1" to AWSModels.AmazonTitanTextLiteV1,
            "AmazonTitanTextExpressV1" to AWSModels.AmazonTitanTextExpressV1,
            "Claude3OpusAWS" to AWSModels.Claude3OpusAWS,
            "CohereCommandTextV14" to AWSModels.CohereCommandTextV14,
            "AI21J2UltraV1" to AWSModels.AI21J2UltraV1,
            "AI21J2MidV1" to AWSModels.AI21J2MidV1,
            "Claude35Sonnet" to AWSModels.Claude35Sonnet,
            "Claude3Sonnet" to AWSModels.Claude3Sonnet,
            "Claude3Haiku" to AWSModels.Claude3Haiku,
            "ClaudeV2_1" to AWSModels.ClaudeV2_1,
            "ClaudeV2" to AWSModels.ClaudeV2,
            "ClaudeV2Instant" to AWSModels.ClaudeV2Instant,
            "LLaMA38bInstructAWS" to AWSModels.LLaMA38bInstructAWS,
            "LLaMA370bInstructAWS" to AWSModels.LLaMA370bInstructAWS,
            "AnthropicClaude3Opus" to AnthropicModels.Claude3Opus,
            "AnthropicClaude35Sonnet" to AnthropicModels.Claude35Sonnet,
            "AnthropicClaude3Sonnet" to AnthropicModels.Claude3Sonnet,
            "AnthropicClaude3Haiku" to AnthropicModels.Claude3Haiku,
            "Gemini15ProPreview" to GoogleModels.Gemini15ProPreview,
            "GeminiFlashPreview" to GoogleModels.GeminiFlashPreview,
            "GeminiPro" to GoogleModels.GeminiPro
        ).toMutableMap()
    }
}

fun String.chatModel(): ChatModels {
    return ChatModels.values().entries.firstOrNull {
        it.value.modelName == this || it.key == this
    }?.value ?: OpenAIModels.GPT4oMini
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