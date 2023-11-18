package com.simiacryptus.openai.models

enum class ChatModels(
    override val modelName: String,
    override val maxTokens: Int
) : OpenAITextModel {
    GPT35Turbo("gpt-3.5-turbo-16k", 16384),
    GPT4("gpt-4", 8192),
    GPT4Turbo("gpt-4-1106-preview", /* 128k */ 131072),
    GPT4Vision("gpt-4-vision-preview", 8192),
}


