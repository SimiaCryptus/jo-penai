package com.simiacryptus.jopenai.models

import com.simiacryptus.jopenai.models.ApiModel.Usage

open class EmbeddingModels(
    modelName: String,
    maxTokens: Int,
    private val tokenPricePerK: Double,
) : TextModel(modelName, maxTokens) {
    override fun pricing(usage: Usage) = usage.prompt_tokens * tokenPricePerK / 1000.0

    companion object {
        fun values() = mapOf(
            "AdaEmbedding" to AdaEmbedding,
            "Small" to Small,
            "Large" to Large
        )

        val AdaEmbedding = EmbeddingModels("text-embedding-ada-002", 2049, 0.0001)
        val Small = EmbeddingModels("text-embedding-3-small", 2049, 0.00002)
        val Large = EmbeddingModels("text-embedding-3-large", 2049, 0.00013)
    }
}