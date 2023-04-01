package com.simiacryptus.openai

data class TranscriptionPacket(
    val id: Int? = 0,
    val seek: Int? = 0,
    val start: Double? = 0.0,
    val end: Double? = 0.0,
    val text: String? = "",
    val tokens: Array<Int>? = arrayOf(),
    val temperature: Double? = 0.0,
    val avg_logprob: Double? = 0.0,
    val compression_ratio: Double? = 0.0,
    val no_speech_prob: Double? = 0.0,
    val `transient`: Boolean? = false
)