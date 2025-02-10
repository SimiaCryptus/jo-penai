package com.simiacryptus.jopenai.models

object GoogleModels {

    val GeminiPro_15 = ChatModel(
        name = "GeminiPro_15",
        modelName = "models/gemini-1.5-pro",
        maxTotalTokens = 2097152,
        maxOutTokens = 8192,
        provider = APIProvider.Google,
        inputTokenPricePerK = 0.00025, // Pricing not provided, using a placeholder
        outputTokenPricePerK = 0.0005  // Pricing not provided, using a placeholder
    )
    val GeminiPro_10 = ChatModel(
        name = "GeminiPro_10",
        modelName = "models/gemini-1.0-pro",
        maxTotalTokens = 2097152,
        maxOutTokens = 8192,
        provider = APIProvider.Google,
        inputTokenPricePerK = 0.00025, // Pricing not provided, using a placeholder
        outputTokenPricePerK = 0.0005  // Pricing not provided, using a placeholder
    )
    val GeminiFlash_15 = ChatModel(
        name = "GeminiFlash_15",
        modelName = "models/gemini-1.5-flash",
        maxTotalTokens = 1048576,
        maxOutTokens = 8192,
        provider = APIProvider.Google,
        inputTokenPricePerK = 0.0001, // Pricing not provided, using a placeholder
        outputTokenPricePerK = 0.0002 // Pricing not provided, using a placeholder
    )
    val GeminiFlash_15_8B = ChatModel(
        name = "GeminiFlash_15_8B",
        modelName = "models/gemini-1.5-flash-8b",
        maxTotalTokens = 1048576,
        maxOutTokens = 8192,
        provider = APIProvider.Google,
        inputTokenPricePerK = 0.00005, // Pricing not provided, using a placeholder
        outputTokenPricePerK = 0.0001  // Pricing not provided, using a placeholder
    )
    val GeminiFlash_20 = ChatModel(
        name = "GeminiFlash_20",
        modelName = "models/gemini-2.0-flash",
        maxTotalTokens = 1048576,
        maxOutTokens = 8192,
        provider = APIProvider.Google,
        inputTokenPricePerK = 0.0001, // Pricing not provided, using a placeholder
        outputTokenPricePerK = 0.0002 // Pricing not provided, using a placeholder
    )
    val GeminiPro_20 = ChatModel(
        name = "GeminiPro_20",
        modelName = "models/gemini-2.0-pro",
        maxTotalTokens = 2097152,
        maxOutTokens = 8192,
        provider = APIProvider.Google,
        inputTokenPricePerK = 0.00025, // Pricing not provided, using a placeholder
        outputTokenPricePerK = 0.0005  // Pricing not provided, using a placeholder
    )
    val GeminiFlash_20_Thinking_Experimental_01_21 = ChatModel(
        name = "GeminiFlash_20_Thinking_Experimental_01_21",
        modelName = "models/gemini-2.0-flash-thinking-exp-01-21",
        maxTotalTokens = 1048576,
        maxOutTokens = 8192,
        provider = APIProvider.Google,
        inputTokenPricePerK = 0.0001, // Pricing not provided, using a placeholder
        outputTokenPricePerK = 0.0002 // Pricing not provided, using a placeholder
    )
    val values = mapOf(
        "GeminiPro_15" to GeminiPro_15,
        "GeminiFlash_15" to GeminiFlash_15,
        "GeminiFlash_15_8B" to GeminiFlash_15_8B,
        "GeminiPro" to GeminiPro_10,
        "GeminiFlash_20" to GeminiFlash_20,
        "GeminiPro_20" to GeminiPro_20,
        "GeminiFlash_20_Thinking_Experimental_01_21" to GeminiFlash_20_Thinking_Experimental_01_21,
    )
}