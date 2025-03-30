package com.simiacryptus.jopenai.models
import org.slf4j.LoggerFactory

@Suppress("unused")
enum class AudioModels(
    override val modelName: String,
) : OpenAIModel {
    GPT4oTranscribe("gpt-4o-transcribe"),
    GPT4oMiniTranscribe("gpt-4o-mini-transcribe"),
    Whisper("whisper-1"),
    TTS("tts-1"),
    TTS_HD("tts-1-hd"),
    GPT4oMiniTTS("gpt-4o-mini-tts");
    private val logger = LoggerFactory.getLogger(AudioModels::class.java)

    fun pricing(length: Int): Double = when (this) {
        Whisper -> 0.006 * length // seconds
        TTS -> (15.0 / 1000000) * length // characters ($15 per 1M characters)
        TTS_HD -> (30.0 / 1000000) * length // characters ($30 per 1M characters)
        GPT4oTranscribe -> 0.006 * length // minutes ($0.006 per minute)
        GPT4oMiniTranscribe -> 0.003 * length // minutes ($0.003 per minute)
        GPT4oMiniTTS -> (0.60 / 1000000) * length // characters
    }
    .also { logger.info("Calculated price: {}", it) }
}