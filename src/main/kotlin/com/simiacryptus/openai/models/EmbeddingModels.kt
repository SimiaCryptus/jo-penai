package com.simiacryptus.openai.models

enum class EmbeddingModels(
    override val modelName: String,
    override val maxTokens: Int
) : OpenAITextModel {
    AdaEmbedding("text-embedding-ada-002", 2049),
}