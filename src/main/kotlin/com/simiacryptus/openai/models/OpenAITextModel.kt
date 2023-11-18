package com.simiacryptus.openai.models

interface OpenAITextModel : OpenAIModel {
    val maxTokens: Int
}