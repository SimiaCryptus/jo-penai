package com.simiacryptus.jopenai.models

object GoogleModels {

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


}