package com.simiacryptus.openai.models

import com.simiacryptus.openai.OpenAIClient

interface OpenAITextModel : OpenAIModel {
    val maxTokens: Int
    fun pricing(usage: OpenAIClient.Usage): Double
}