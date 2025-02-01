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
        outputTokenPricePerK = 0.0015,
        hasTemperature = false,
        hasReasoningEffort = true,
    )

    val O1 = ChatModel(
        name = "O1",
        modelName = "o1",
        maxTotalTokens = 200 * 1024,
        provider = APIProvider.OpenAI,
        inputTokenPricePerK = 0.0005, // TODO: Fix when they release pricing
        outputTokenPricePerK = 0.0015,
        hasTemperature = false,
        hasReasoningEffort = true,
    )

    val O1Mini = ChatModel(
        name = "O1Mini",
        modelName = "o1-mini",
        maxTotalTokens = 128 * 1024,
        provider = APIProvider.OpenAI,
        inputTokenPricePerK = 0.0005, // TODO: Fix when they release pricing
        outputTokenPricePerK = 0.0015,
        hasTemperature = false,
        hasReasoningEffort = true,
    )

    val O3Mini = ChatModel(
        name = "O3Mini",
        modelName = "o3-mini",
        maxTotalTokens = 128 * 1024,
        provider = APIProvider.OpenAI,
        inputTokenPricePerK = 0.0005, // TODO: Fix when they release pricing
        outputTokenPricePerK = 0.0015,
        hasTemperature = false,
        hasReasoningEffort = true,
    )
    val values = mapOf(
        "GPT35Turbo" to GPT35Turbo,
        "GPT4Turbo" to GPT4Turbo,
        "GPT4o" to GPT4o,
        "GPT4oMini" to GPT4oMini,
        "O1Preview" to O1Preview,
        "O1Mini" to O1Mini,
        "O3Mini" to O3Mini,
        "O1" to O1,
    )

}