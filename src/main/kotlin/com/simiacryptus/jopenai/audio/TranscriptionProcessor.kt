package com.simiacryptus.jopenai.audio

import com.simiacryptus.jopenai.OpenAIClient
import org.slf4j.LoggerFactory
import java.util.*

open class TranscriptionProcessor(
    var client: OpenAIClient,
    private var audioBuffer: Deque<ByteArray>,
    var continueFn: () -> Boolean,
    var prompt: String = "",
    var onText: (String) -> Unit,
) {
    private val logger = LoggerFactory.getLogger(TranscriptionProcessor::class.java)
    fun run() {
        logger.debug("TranscriptionProcessor started.")
        while (this.continueFn() || audioBuffer.isNotEmpty()) {
            val recordAudio = audioBuffer.poll()
            if (null == recordAudio) {
                logger.trace("Audio buffer is empty, sleeping for 1ms.")
                Thread.sleep(1)
            } else {
                logger.debug("Processing audio buffer of size: ${recordAudio.size}.")
                var text = client.transcription(recordAudio, prompt)
                if (prompt.isNotEmpty()) text = "$text"
                val newPrompt = (prompt + text).split(" ").takeLast(32).joinToString(" ")
                prompt = newPrompt
                logger.debug("Updated prompt: $prompt")
                onText(text)
                logger.debug("Transcribed text: $text")
            }
        }
        logger.debug("TranscriptionProcessor finished.")
    }
}