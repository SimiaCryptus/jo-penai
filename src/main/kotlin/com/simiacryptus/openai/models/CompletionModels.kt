package com.simiacryptus.openai.models

import com.simiacryptus.openai.OpenAIClient

enum class CompletionModels(
    override val modelName: String,
    override val maxTokens: Int,
    private val tokenPricePerK: Double,
) : OpenAITextModel {
    DaVinci("text-davinci-003", 2049, 0.002);
    override fun pricing(usage: OpenAIClient.Usage) = usage.prompt_tokens * tokenPricePerK / 1000.0
}