package com.simiacryptus.jopenai.models

import com.simiacryptus.jopenai.ApiModel.Usage

enum class EmbeddingModels(
    override val modelName: String,
    override val maxTokens: Int,
    private val tokenPricePerK: Double,
) : OpenAITextModel {
    AdaEmbedding("text-embedding-ada-002", 2049, 0.0001),
    Small("text-embedding-3-small", 2049, 0.00002),
    Large("text-embedding-3-large", 2049, 0.00013);
    override fun pricing(usage: Usage) = usage.prompt_tokens * tokenPricePerK / 1000.0
}