package com.simiacryptus.jopenai.models

import com.simiacryptus.jopenai.ApiModel.Usage

open class CompletionModels(
    modelName: String,
    maxTokens: Int,
    private val tokenPricePerK: Double,
) : OpenAITextModel(modelName, maxTokens) {
    override fun pricing(usage: Usage) = usage.prompt_tokens * tokenPricePerK / 1000.0

    companion object {
        fun values() = mapOf("DaVinci" to DaVinci)

        val DaVinci = CompletionModels("text-davinci-003", 2049, 0.002)
    }
}