package com.simiacryptus.openai

enum class Models(
    override val modelName: String,
    override val maxTokens: Int
) : Model {
    AdaEmbedding("text-embedding-ada-002", 2049),
    DaVinci("text-davinci-003", 2049),
    DaVinciEdit("text-davinci-edit-001", 2049),
    GPT35Turbo("gpt-3.5-turbo-16k", 16384),
    GPT4("gpt-4", 8192),
    GPT4Turbo("gpt-4-1106-preview", /* 128k */ 131072),
    GPT4Vision("gpt-4-vision-preview", 8192),
}


interface Model {
    val modelName: String
    val maxTokens: Int
}
