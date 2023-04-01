package com.simiacryptus.openai

data class TranscriptionResult(
    val task: String? = "",
    val language: String? = "",
    val duration: Double = 0.0,
    val segments: Array<TranscriptionPacket> = arrayOf(),
    val text: String? = ""
)