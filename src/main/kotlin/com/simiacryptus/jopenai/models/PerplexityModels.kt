package com.simiacryptus.jopenai.models
import org.slf4j.LoggerFactory

object PerplexityModels {
    private val logger = LoggerFactory.getLogger(PerplexityModels::class.java)


    val SonarSmallChat128k = createChatModel(
        name = "SonarSmallChat128k",
        modelName = "llama-3.1-sonar-small-128k-chat",
        maxTotalTokens = 128 * 1024,
        provider = APIProvider.Perplexity,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015,
    )

    val SonarSmallOnline128k = createChatModel(
        name = "SonarSmallOnline128k",
        modelName = "llama-3.1-sonar-small-128k-online",
        maxTotalTokens = 128 * 1024,
        provider = APIProvider.Perplexity,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015,
    )

    val SonarLargeChat128k = createChatModel(
        name = "SonarLargeChat128k",
        modelName = "llama-3.1-sonar-large-128k-chat",
        maxTotalTokens = 128 * 1024,
        provider = APIProvider.Perplexity,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015,
    )

    val SonarLargeOnline128k = createChatModel(
        name = "SonarLargeOnline128k",
        modelName = "llama-3.1-sonar-large-128k-online",
        maxTotalTokens = 128 * 1024,
        provider = APIProvider.Perplexity,
        inputTokenPricePerK = 0.0005,
        outputTokenPricePerK = 0.0015,
    )
    private fun createChatModel(
        name: String,
        modelName: String,
        maxTotalTokens: Int,
        provider: APIProvider,
        inputTokenPricePerK: Double,
        outputTokenPricePerK: Double
    ): ChatModel {
        val model = ChatModel(name, modelName, maxTotalTokens, maxTotalTokens, provider, inputTokenPricePerK, outputTokenPricePerK)
        logger.info("Initialized model: $name with max tokens: $maxTotalTokens")
        return model
    }
    val values = mapOf(
        "SonarSmallChat128k" to PerplexityModels.SonarSmallChat128k,
        "SonarSmallOnline128k" to PerplexityModels.SonarSmallOnline128k,
        "SonarLargeChat128k" to PerplexityModels.SonarLargeChat128k,
        "SonarLargeOnline128k" to PerplexityModels.SonarLargeOnline128k,
    )

}