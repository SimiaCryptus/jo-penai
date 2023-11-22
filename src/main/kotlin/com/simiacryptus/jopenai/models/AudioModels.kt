package com.simiacryptus.jopenai.models

@Suppress("unused")
enum class AudioModels(
    override val modelName: String,
) : OpenAIModel {
    Whisper("whisper-1"),
    TTS("tts-1"),
    TTS_HD("tts-1-hd");
}