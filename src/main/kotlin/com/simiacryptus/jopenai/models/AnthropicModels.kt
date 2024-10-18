package com.simiacryptus.jopenai.models

object AnthropicModels {

    val Claude3Opus = ChatModel(
        name = "Claude3Opus",
        modelName = "claude-3-opus-20240229",
        maxTotalTokens = 200000,
        maxOutTokens = 4096,
        provider = APIProvider.Anthropic,
        inputTokenPricePerK = 15.0 / 1000.0,
        outputTokenPricePerK = 75.0 / 1000.0
    )
    val Claude35Sonnet = ChatModel(
        name = "Claude35Sonnet",
        modelName = "claude-3-5-sonnet-20240620",
        maxTotalTokens = 200000,
        maxOutTokens = 4096,
        provider = APIProvider.Anthropic,
        inputTokenPricePerK = 3.0 / 1000.0,
        outputTokenPricePerK = 15.0 / 1000.0
    )
    val Claude3Sonnet = ChatModel(
        name = "Claude3Sonnet",
        modelName = "claude-3-sonnet-20240229",
        maxTotalTokens = 200000,
        maxOutTokens = 4096,
        provider = APIProvider.Anthropic,
        inputTokenPricePerK = 3.0 / 1000.0,
        outputTokenPricePerK = 15.0 / 1000.0
    )
    val Claude3Haiku = ChatModel(
        name = "Claude3Haiku",
        modelName = "claude-3-haiku-20240307",
        maxTotalTokens = 200000,
        maxOutTokens = 4096,
        provider = APIProvider.Anthropic,
        inputTokenPricePerK = 0.25 / 1000.0,
        outputTokenPricePerK = 1.25 / 1000.0
    )

}