package com.simiacryptus.jopenai.models

object DeepSeekModels {
    val DeepSeekChat = ChatModel(
        name = "DeepSeekChat",
        modelName = "deepseek-chat",
        maxTotalTokens = 64000,
        maxOutTokens = 4096,
        provider = APIProvider.DeepSeek,
        inputTokenPricePerK = 0.014 / 1000.0,
        outputTokenPricePerK = 0.14 / 1000.0
    )
    val DeepSeekCoder = ChatModel(
        name = "DeepSeekCoder",
        modelName = "deepseek-coder",
        maxTotalTokens = 64000,
        maxOutTokens = 4096,
        provider = APIProvider.DeepSeek,
        inputTokenPricePerK = 0.014 / 1000.0,
        outputTokenPricePerK = 0.14 / 1000.0
    )
}