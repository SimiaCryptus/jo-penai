package com.simiacryptus.jopenai.audio

import com.simiacryptus.jopenai.OpenAIClient
import org.slf4j.LoggerFactory
import java.util.*

open class TranscriptionProcessor(
    var client: OpenAIClient,
    private var audioBuffer: Queue<ByteArray>,
    var continueFn: () -> Boolean,
    var prompt: String = "",
    var onTranscriptionUpdate: (String) -> Unit,
) {
    private val logger = LoggerFactory.getLogger(TranscriptionProcessor::class.java)
    fun run() {
        logger.debug("TranscriptionProcessor started.")
        while (this.continueFn() || audioBuffer.isNotEmpty()) {
            val recordAudio = audioBuffer.poll()
            if (null == recordAudio) {
                Thread.sleep(1)
            } else {
                logger.debug("Processing audio buffer of size: ${recordAudio.size}.")
                val text = client.transcription(recordAudio, prompt)
                prompt = (prompt + text).split(" ").takeLast(32).joinToString(" ")
                onTranscriptionUpdate(text)
                logger.debug("Transcribed text: `$text` - New Prompt: `$prompt`")
            }
        }
        logger.debug("TranscriptionProcessor finished.")
    }
}