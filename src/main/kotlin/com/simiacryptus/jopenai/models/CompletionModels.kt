package com.simiacryptus.jopenai.models

import com.simiacryptus.jopenai.models.ApiModel.Usage

open class CompletionModels(
    modelName: String,
    maxTokens: Int,
    private val tokenPricePerK: Double,
) : TextModel(modelName, maxTokens) {
    override fun pricing(usage: Usage) = usage.prompt_tokens * tokenPricePerK / 1000.0

    companion object {
        fun values() = mapOf("DaVinci" to DaVinci)

        private val DaVinci = CompletionModels("text-davinci-003", 2049, 0.002)
    }
}