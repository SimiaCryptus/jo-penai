package com.simiacryptus.jopenai.models

@Suppress("unused")
enum class AudioModels(
    override val modelName: String,
) : OpenAIModel {
    Whisper("whisper-1"),
    TTS("tts-1"),
    TTS_HD("tts-1-hd");

    fun pricing(length: Int): Double = when (this) {
        Whisper -> 0.006 * length // seconds
        TTS -> 0.000015 * length // characters
        TTS_HD -> 0.00003 * length // characters
    }
}