package com.simiacryptus.jopenai.models

import com.simiacryptus.jopenai.ApiModel.Usage

enum class EditModels(
    override val modelName: String,
    override val maxTokens: Int,
    private val tokenPricePerK: Double,
) : OpenAITextModel {
    DaVinciEdit("text-davinci-edit-001", 2049, 0.002);
    override fun pricing(usage: Usage) = usage.prompt_tokens * tokenPricePerK / 1000.0
}