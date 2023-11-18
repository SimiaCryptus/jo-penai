package com.simiacryptus.openai.models

import com.simiacryptus.openai.OpenAIClient.Usage

enum class ChatModels(
    override val modelName: String,
    override val maxTokens: Int,
    val inputTokenPricePerK: Double,
    val outputTokenPricePerK: Double,
) : OpenAITextModel {
    GPT35Turbo("gpt-3.5-turbo-16k", 16384, 0.001, 0.002),
    GPT4("gpt-4", 8192, 0.03, 0.06),
    GPT4Turbo("gpt-4-1106-preview", /* 128k */ 131072, 0.01, 0.03),
    GPT4Vision("gpt-4-vision-preview", 8192, 0.01, 0.03);
    override fun pricing(usage: Usage) =
        (usage.prompt_tokens * inputTokenPricePerK + usage.completion_tokens * outputTokenPricePerK) / 1000.0
}


