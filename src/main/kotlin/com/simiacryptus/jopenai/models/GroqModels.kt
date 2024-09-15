package com.simiacryptus.jopenai.models

object GroqModels {

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

    val Llama_31_405B = ChatModels(
        name = "Llama_31_405B",
        modelName = "llama-3.1-405b-reasoning",
        maxTotalTokens = 1288 * 1024 - 1,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )
    val Llama_31_70B = ChatModels(
        name = "Llama_31_70B",
        modelName = "llama-3.1-70b-versatile",
        maxTotalTokens = 1288 * 1024 - 1,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )
    val Llama_31_8B = ChatModels(
        name = "Llama_31_8B",
        modelName = "llama-3.1-8b-instant",
        maxTotalTokens = 1288 * 1024 - 1,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
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

    val Gemma2_9bIt = ChatModels(
        name = "Gemma2_9bIt",
        modelName = "gemma2-9b-it",
        maxTotalTokens = 8192,
        maxOutTokens = 8192,
        provider = APIProvider.Groq,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )

}