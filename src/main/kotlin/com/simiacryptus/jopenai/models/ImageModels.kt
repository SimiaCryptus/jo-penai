package com.simiacryptus.jopenai.models

@Suppress("unused")
enum class ImageModels(
    override val modelName: String,
    val maxPrompt: Int
) : OpenAIModel {

    DallE2("dall-e-2", 1000) {
        override fun pricing(width: Int, height: Int): Double {
            return when {
                width == 1024 && height == 1024 -> 0.02
                width == 512 && height == 512 -> 0.018
                width == 256 && height == 256 -> 0.016
                else -> throw IllegalArgumentException("Unsupported image size: $width x $height")
            }
        }
    },
    DallE3("dall-e-3", 1000) {
        override fun pricing(width: Int, height: Int): Double {
            return when {
                width == 1024 && height == 1024 -> 0.04
                width == 1024 && height == 1792 -> 0.08
                width == 1792 && height == 1024 -> 0.08
                else -> throw IllegalArgumentException("Unsupported image size: $width x $height")
            }
        }
    },
    DallE3_HD("dall-e-3", 1000) {
        override val quality: String = "hd"
        override fun pricing(width: Int, height: Int): Double {
            return when {
                width == 1024 && height == 1024 -> 0.08
                width == 1024 && height == 1792 -> 0.12
                width == 1792 && height == 1024 -> 0.12
                else -> throw IllegalArgumentException("Unsupported image size: $width x $height")
            }
        }
    };

    open val quality: String = "standard"
    abstract fun pricing(width: Int, height: Int): Double
}
