package com.simiacryptus.jopenai.audio

import com.simiacryptus.jopenai.OpenAIClient
import java.util.*

open class TranscriptionProcessor(
    var client: OpenAIClient,
    private var audioBuffer: Deque<ByteArray>,
    var continueFn: () -> Boolean,
    var prompt: String = "",
    var onText: (String) -> Unit,
) {
    fun run() {
        while (this.continueFn() || audioBuffer.isNotEmpty()) {
            val recordAudio = audioBuffer.poll()
            if (null == recordAudio) {
                Thread.sleep(1)
            } else {
                var text = client.transcription(recordAudio, prompt)
                if (prompt.isNotEmpty()) text = " $text"
                val newPrompt = (prompt + text).split(" ").takeLast(32).joinToString(" ")
                prompt = newPrompt
                onText(text)
            }
        }
    }
}