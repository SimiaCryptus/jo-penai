package com.simiacryptus.jopenai.models

import com.simiacryptus.jopenai.ApiModel.Usage

interface OpenAITextModel : OpenAIModel {
    val maxTokens: Int
    fun pricing(usage: Usage): Double
}