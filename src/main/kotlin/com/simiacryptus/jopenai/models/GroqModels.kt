package com.simiacryptus.jopenai.models
import org.slf4j.LoggerFactory

object GroqModels {
    private val logger = LoggerFactory.getLogger(GroqModels::class.java)

    val LaMA38b = createChatModel(
        name = "LaMA38b",
        modelName = "llama3-8b-8192",
        maxTotalTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    val LLaMA370b = createChatModel(
        name = "LLaMA370b",
        modelName = "llama3-70b-8192",
        maxTotalTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )

    val Llama_31_405B = createChatModel(
        name = "Llama_31_405B",
        modelName = "llama-3.1-405b-reasoning",
        maxTotalTokens = 1288 * 1024 - 1,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )
    val Llama_31_70B = createChatModel(
        name = "Llama_31_70B",
        modelName = "llama-3.1-70b-versatile",
        maxTotalTokens = 1288 * 1024 - 1,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )
    val Llama_31_8B = createChatModel(
        name = "Llama_31_8B",
        modelName = "llama-3.1-8b-instant",
        maxTotalTokens = 1288 * 1024 - 1,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )
    val LLaMA270bChat = createChatModel(
        name = "LLaMA270bChat",
        modelName = "llama2-70b-4096",
        maxTotalTokens = 4096,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )
    val LLaMA38b = createChatModel(
        name = "LLaMA38b",
        modelName = "llama3-8b-8192",
        maxTotalTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0015  // Assuming pricing, adjust as necessary
    )
    val Mixtral8x7bInstructV01 = createChatModel(
        name = "Mixtral8x7bInstructV01",
        modelName = "mixtral-8x7b-32768",
        maxTotalTokens = 32768,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )

    val Gemma7bIt = createChatModel(
        name = "Gemma7bIt",
        modelName = "gemma-7b-it",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )

    val Gemma2_9bIt = createChatModel(
        name = "Gemma2_9bIt",
        modelName = "gemma2-9b-it",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )


    val Llama3Groq70b8192ToolUsePreview = ChatModel(
        name = "Llama3Groq70b8192ToolUsePreview",
        modelName = "llama3-groq-70b-8192-tool-use-preview",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0
    )
    val Llama3Groq8b8192ToolUsePreview = ChatModel(
        name = "Llama3Groq8b8192ToolUsePreview",
        modelName = "llama3-groq-8b-8192-tool-use-preview",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0
    )
    val Llama31_70bVersatile = ChatModel(
        name = "Llama31_70bVersatile",
        modelName = "llama-3.1-70b-versatile",
        maxTotalTokens = 131072,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0
    )
    val Llama31_8bInstant = ChatModel(
        name = "Llama31_8bInstant",
        modelName = "llama-3.1-8b-instant",
        maxTotalTokens = 131072,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0
    )
    val Llama32_1bPreview = ChatModel(
        name = "Llama32_1bPreview",
        modelName = "llama-3.2-1b-preview",
        maxTotalTokens = 131072,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0
    )
    val Llama32_3bPreview = ChatModel(
        name = "Llama32_3bPreview",
        modelName = "llama-3.2-3b-preview",
        maxTotalTokens = 131072,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0
    )
    val Llama32_11bVisionPreview = ChatModel(
        name = "Llama32_11bVisionPreview",
        modelName = "llama-3.2-11b-vision-preview",
        maxTotalTokens = 131072,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0
    )
    val Llama32_90bVisionPreview = ChatModel(
        name = "Llama32_90bVisionPreview",
        modelName = "llama-3.2-90b-vision-preview",
        maxTotalTokens = 131072,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0
    )
    val LlamaGuard38b = ChatModel(
        name = "LlamaGuard38b",
        modelName = "llama-guard-3-8b",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0
    )
    val Llama370b8192 = ChatModel(
        name = "Llama370b8192",
        modelName = "llama3-70b-8192",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0
    )
    val Llama38b8192 = ChatModel(
        name = "Llama38b8192",
        modelName = "llama3-8b-8192",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0
    )
    val Mixtral8x7b32768 = ChatModel(
        name = "Mixtral8x7b32768",
        modelName = "mixtral-8x7b-32768",
        maxTotalTokens = 32768,
        maxOutTokens = 32768,
        provider = APIProvider.Mistral,
        inputTokenPricePerK = 0.0, // Assuming pricing, adjust as necessary
        outputTokenPricePerK = 0.0
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

}