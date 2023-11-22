package com.simiacryptus.jopenai.models

@Suppress("unused")
enum class ImageModels(
    override val modelName: String,
) : OpenAIModel {
    DallE2("dall-e-2"),
    DallE3("dall-e-3");
}
