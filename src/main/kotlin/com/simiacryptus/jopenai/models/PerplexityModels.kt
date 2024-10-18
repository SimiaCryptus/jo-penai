package com.simiacryptus.jopenai.models

object PerplexityModels {

    val SonarSmallChat128k = ChatModel(
        name = "SonarSmallChat128k",
        modelName = "llama-3.1-sonar-small-128k-chat",
        maxTotalTokens = 128 * 1024,
        provider = APIProvider.Perplexity,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )

    val SonarSmallOnline128k = ChatModel(
        name = "SonarSmallOnline128k",
        modelName = "llama-3.1-sonar-small-128k-online",
        maxTotalTokens = 128 * 1024,
        provider = APIProvider.Perplexity,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )

    val SonarLargeChat128k = ChatModel(
        name = "SonarLargeChat128k",
        modelName = "llama-3.1-sonar-large-128k-chat",
        maxTotalTokens = 128 * 1024,
        provider = APIProvider.Perplexity,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )

    val SonarLargeOnline128k = ChatModel(
        name = "SonarLargeOnline128k",
        modelName = "llama-3.1-sonar-large-128k-online",
        maxTotalTokens = 128 * 1024,
        provider = APIProvider.Perplexity,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015
    )

}