package com.simiacryptus.openai.models

enum class EditModels(
    override val modelName: String,
    override val maxTokens: Int
) : OpenAITextModel {
    DaVinciEdit("text-davinci-edit-001", 2049),
}