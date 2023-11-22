package com.simiacryptus.jopenai.models

import com.simiacryptus.jopenai.ApiModel.Usage

enum class CompletionModels(
    override val modelName: String,
    override val maxTokens: Int,
    private val tokenPricePerK: Double,
) : OpenAITextModel {
    DaVinci("text-davinci-003", 2049, 0.002);
    override fun pricing(usage: Usage) = usage.prompt_tokens * tokenPricePerK / 1000.0
}