package com.simiacryptus.jopenai.models

object OpenAIModels {
    val GPT35Turbo = ChatModel(
        name = "GPT35Turbo",
        modelName = "gpt-3.5-turbo",
        maxTotalTokens = 16384,
        provider = APIProvider.OpenAI,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )

    val GPT4Turbo = ChatModel(
        name = "GPT4Turbo",
        modelName = "gpt-4-turbo",
        maxTotalTokens = 128000,
        provider = APIProvider.OpenAI,
        inputTokenPricePerK = 0.01,
        outputTokenPricePerK = 0.03
    )

    val GPT4o = ChatModel(
        name = "GPT4o",
        modelName = "gpt-4o",
        maxTotalTokens = 128000,
        provider = APIProvider.OpenAI,
        inputTokenPricePerK = 0.005,
        outputTokenPricePerK = 0.015
    )

    val GPT4oMini = ChatModel(
        name = "GPT4oMini",
        modelName = "gpt-4o-mini",
        maxTotalTokens = 128000,
        provider = APIProvider.OpenAI,
        inputTokenPricePerK = 0.005,
        outputTokenPricePerK = 0.015
    )

    val O1Preview = ChatModel(
        name = "O1Preview",
        modelName = "o1-preview",
        maxTotalTokens = 128 * 1024,
        provider = APIProvider.OpenAI,
        inputTokenPricePerK = 0.0005, // TODO: Fix when they release pricing
        outputTokenPricePerK = 0.0015
    )

    val O1Mini = ChatModel(
        name = "O1Mini",
        modelName = "o1-mini",
        maxTotalTokens = 128 * 1024,
        provider = APIProvider.OpenAI,
        inputTokenPricePerK = 0.0005, // TODO: Fix when they release pricing
        outputTokenPricePerK = 0.0015
    )
}