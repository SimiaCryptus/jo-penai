package com.simiacryptus.openai.models

import com.simiacryptus.openai.OpenAIClient

enum class EditModels(
    override val modelName: String,
    override val maxTokens: Int,
    private val tokenPricePerK: Double,
) : OpenAITextModel {
    DaVinciEdit("text-davinci-edit-001", 2049, 0.002);
    override fun pricing(usage: OpenAIClient.Usage) = usage.prompt_tokens * tokenPricePerK / 1000.0
}