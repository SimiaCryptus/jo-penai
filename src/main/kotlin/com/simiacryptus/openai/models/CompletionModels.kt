package com.simiacryptus.openai.models

enum class CompletionModels(
    override val modelName: String,
    override val maxTokens: Int
) : OpenAITextModel {
    DaVinci("text-davinci-003", 2049),
}