package com.simiacryptus.jopenai.models

import com.simiacryptus.jopenai.ApiModel.Usage

enum class ChatModels(
    override val modelName: String,
    override val maxTokens: Int,
    private val inputTokenPricePerK: Double,
    private val outputTokenPricePerK: Double,
) : OpenAITextModel {
    GPT35Turbo("gpt-3.5-turbo-0125", 16384, 0.0005, 0.0015),
    @Deprecated("Use GPT4 Turbo") GPT4("gpt-4-32k", 32768, 0.06, 0.12),
    GPT4Turbo("gpt-4-turbo-preview", /* 128k */ 128000, 0.01, 0.03),
    GPT4Vision("gpt-4-vision-preview", 8192, 0.01, 0.03);
    override fun pricing(usage: Usage) =
        (usage.prompt_tokens * inputTokenPricePerK + usage.completion_tokens * outputTokenPricePerK) / 1000.0
}


