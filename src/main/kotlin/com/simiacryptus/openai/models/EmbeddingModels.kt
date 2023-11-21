package com.simiacryptus.openai.models

import com.simiacryptus.openai.OpenAIClient

enum class EmbeddingModels(
    override val modelName: String,
    override val maxTokens: Int,
    private val tokenPricePerK: Double,
) : OpenAITextModel {
    AdaEmbedding("text-embedding-ada-002", 2049, 0.0001);
    override fun pricing(usage: OpenAIClient.Usage) = usage.prompt_tokens * tokenPricePerK / 1000.0
}