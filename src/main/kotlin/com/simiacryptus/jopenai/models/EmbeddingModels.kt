package com.simiacryptus.jopenai.models

import com.simiacryptus.jopenai.ApiModel.Usage

enum class EmbeddingModels(
    override val modelName: String,
    override val maxTokens: Int,
    private val tokenPricePerK: Double,
) : OpenAITextModel {
    AdaEmbedding("text-embedding-ada-002", 2049, 0.0001);
    override fun pricing(usage: Usage) = usage.prompt_tokens * tokenPricePerK / 1000.0
}