package com.simiacryptus.jopenai.audio

import com.simiacryptus.jopenai.OpenAIClient
import org.slf4j.LoggerFactory
import java.util.*

open class TranscriptionProcessor(
    var client: OpenAIClient,
    private var audioBuffer: Queue<AudioPacket>,
    var continueFn: () -> Boolean,
    var prompt: String = "",
    var onTranscriptionUpdate: (TranscriptionResult) -> Unit,
) {
    data class TranscriptionResult(
        val text: String,
        val prompt: String,
        val packet: AudioPacket,
        val processingTime: Long
    ) {
        override fun toString(): String {
            return "TranscriptionResult(text='$text', prompt='$prompt', processingTime=$processingTime)"
        }
    }
    private val logger = LoggerFactory.getLogger(TranscriptionProcessor::class.java)
    fun run() {
        logger.debug("TranscriptionProcessor started.")
        while (this.continueFn() || audioBuffer.isNotEmpty()) {
            val recordAudio = audioBuffer.poll()
            if (null == recordAudio) {
                Thread.sleep(1)
            } else {
                logger.debug("Processing audio buffer of size: ${recordAudio.size}.")
                val startTime = System.currentTimeMillis()
                val text = client.transcription(AudioPacket.convertRawToWav(AudioPacket.convertFloatsToRaw(recordAudio.samples), recordAudio.audioFormat)!!, prompt)
                val processingTime = System.currentTimeMillis() - startTime
                prompt = (prompt + text).split(" ").takeLast(32).joinToString(" ")
                val transcriptionResult = TranscriptionResult(text, prompt, recordAudio, processingTime)
                onTranscriptionUpdate(transcriptionResult)
                logger.debug("Transcribed text: ${transcriptionResult}")
            }
        }
        logger.debug("TranscriptionProcessor finished.")
    }
}