package com.simiacryptus.jopenai.models
import org.slf4j.LoggerFactory

@Suppress("unused")
enum class AudioModels(
    override val modelName: String,
) : OpenAIModel {
    Whisper("whisper-1"),
    TTS("tts-1"),
    TTS_HD("tts-1-hd");
    private val logger = LoggerFactory.getLogger(AudioModels::class.java)

    fun pricing(length: Int): Double = when (this) {
        Whisper -> 0.006 * length // seconds
        TTS -> 0.000015 * length // characters
        TTS_HD -> 0.00003 * length // characters
    }
    .also { logger.info("Calculated price: {}", it) }
}