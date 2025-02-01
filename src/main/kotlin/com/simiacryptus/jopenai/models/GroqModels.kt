package com.simiacryptus.jopenai.models
import org.slf4j.LoggerFactory

object GroqModels {
    private val logger = LoggerFactory.getLogger(GroqModels::class.java)


    val Llama33_70bVersatile = createChatModel(
        name = "Llama33_70bVersatile",
        modelName = "llama-3.3-70b-versatile",
        maxTotalTokens = 128000,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.59,
        outputTokenPricePerK = 0.79
    )
    val Mixtral8x7bInstructV01 = createChatModel(
        name = "Mixtral8x7bInstructV01",
        modelName = "mixtral-8x7b-32768",
        maxTotalTokens = 32768,
        maxOutTokens = 32768,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.24,
        outputTokenPricePerK = 0.24
    )

    val Gemma2_9b = createChatModel(
        name = "Gemma2_9b",
        modelName = "gemma-2-9b",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,

        inputTokenPricePerK = 0.20,
        outputTokenPricePerK = 0.20
    )


    val Llama33_70bSpecDec = createChatModel(
        name = "Llama33_70bSpecDec",
        modelName = "llama-3.3-70b-specdec",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.59,
        outputTokenPricePerK = 0.99
    )
    val Llama31_8bInstant = ChatModel(
        name = "Llama31_8bInstant",
        modelName = "llama-3.1-8b-instant",
        maxTotalTokens = 128000,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.05,
        outputTokenPricePerK = 0.08
    )
    val Llama32_1bPreview = ChatModel(
        name = "Llama32_1bPreview",
        modelName = "llama-3.2-1b-preview",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.04,
        outputTokenPricePerK = 0.04
    )
    val Llama32_3bPreview = ChatModel(
        name = "Llama32_3bPreview",
        modelName = "llama-3.2-3b-preview",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.06,
        outputTokenPricePerK = 0.06
    )
    val LlamaGuard38b = ChatModel(
        name = "LlamaGuard38b",
        modelName = "llama-guard-3-8b",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.20,
        outputTokenPricePerK = 0.20
    )
    val Llama370b8192 = ChatModel(
        name = "Llama370b8192",
        modelName = "llama3-70b-8192",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.59,
        outputTokenPricePerK = 0.79
    )
    val Llama38b8192 = ChatModel(
        name = "Llama38b8192",
        modelName = "llama3-8b-8192",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.05,
        outputTokenPricePerK = 0.08
    )
    val Mixtral8x7b32768 = ChatModel(
        name = "Mixtral8x7b32768",
        modelName = "mixtral-8x7b-32768",
        maxTotalTokens = 32768,
        maxOutTokens = 32768,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.24,
        outputTokenPricePerK = 0.24
    )

    private fun createChatModel(
        name: String,
        modelName: String,
        maxTotalTokens: Int,
        maxOutTokens: Int = maxTotalTokens,
        provider: APIProvider,
        inputTokenPricePerK: Double,
        outputTokenPricePerK: Double
    ): ChatModel {
        val model = ChatModel(name, modelName, maxTotalTokens, maxOutTokens, provider, inputTokenPricePerK, outputTokenPricePerK)
        logger.info("Initialized model: ${model.name} with max tokens: ${model.maxTotalTokens}")
        return model
    }
    val values = mapOf(
        "Llama33_70bVersatile" to Llama33_70bVersatile,
        "Gemma2_9b" to Gemma2_9b,
        "Mixtral8x7bInstructV01" to Mixtral8x7bInstructV01,
        "Llama31_8bInstant" to Llama31_8bInstant,
        "Llama32_1bPreview" to Llama32_1bPreview,
        "Llama32_3bPreview" to Llama32_3bPreview,
        "Llama33_70bSpecDec" to Llama33_70bSpecDec,
        "LlamaGuard38b" to LlamaGuard38b,
        "Llama370b8192" to Llama370b8192,
        "Llama38b8192" to Llama38b8192,
        "Mixtral8x7b32768" to Mixtral8x7b32768,
    )

}