package com.simiacryptus.jopenai.models

import com.simiacryptus.jopenai.ApiModel.Usage

open class EditModels(
    modelName: String,
    maxTokens: Int,
    private val tokenPricePerK: Double,
) : OpenAITextModel(modelName, maxTokens) {
    override fun pricing(usage: Usage) = usage.prompt_tokens * tokenPricePerK / 1000.0

    companion object {
        fun values() = mapOf("DaVinciEdit" to DaVinciEdit)

        private val DaVinciEdit = EditModels("text-davinci-edit-001", 2049, 0.002)
    }
}