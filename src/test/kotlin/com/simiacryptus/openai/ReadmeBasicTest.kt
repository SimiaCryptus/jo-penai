package com.simiacryptus.openai

import com.simiacryptus.util.AudioRecorder
import com.simiacryptus.util.LoudnessWindowBuffer
import org.junit.jupiter.api.Test
import java.awt.Desktop
import java.io.File
import java.util.*
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicInteger
import javax.imageio.ImageIO

class ReadmeBasicTest {

    private val keyFile = File("C:\\Users\\andre\\code\\all-projects\\openai.key")
    private val apiKey = keyFile.readText().trim()

    @Test
    fun testCompletion() {
        if (!keyFile.exists()) return
        val client = OpenAIClient(apiKey)
        val request = CompletionRequest()
        request.prompt = "This is a test! This"
        val completion = client.complete(request, "text-davinci-003")
        println(completion.choices.first().text)
    }

    @Test
    fun testEdit() {
        // Doesn't seem to work right now... Wrong model error!?!?
        if (!keyFile.exists()) return
        val client = OpenAIClient(apiKey)
        val request = EditRequest()
        request.input = "This is a test!"
        request.instruction = "Rewrite as an epic novel"
        request.model = "text-davinci-edit-001"
        val completion = client.edit(request)
        println(completion.choices.first().text)
    }

    @Test
    fun testChat() {
        if (!keyFile.exists()) return
        val client = OpenAIClient(apiKey)
        val request = ChatRequest()
        request.model = "gpt-3.5-turbo"
        request.messages = arrayOf(
            ChatMessage(ChatMessage.Role.system, "You are a spiritual teacher"),
            ChatMessage(ChatMessage.Role.user, "What is the meaning of life?"),
        )
        val chatResponse = client.chat(request)
        println(chatResponse.choices.first().message?.content ?: "No response")
    }

    @Test
    fun testRender() {
        if (!keyFile.exists()) return
        val client = OpenAIClient(apiKey)
        val image = client.render("This is a test!").first()
        val tempFile = File.createTempFile("test", ".png")
        ImageIO.write(image, "png", tempFile)
        Desktop.getDesktop().browse(tempFile.toURI())
    }

    @Test
    fun testDictate() {
        if (!keyFile.exists()) return
        val client = OpenAIClient(apiKey)
        class DictationPump(
            private val audioBuffer: Deque<ByteArray>,
            val continueFn: () -> Boolean,
            var prompt: String = ""
        ) {
            fun run() {
                while (this.continueFn() || audioBuffer.isNotEmpty()) {
                    val recordAudio = audioBuffer.poll()
                    if (null == recordAudio) {
                        Thread.sleep(1)
                    } else {
                        var text = client.dictate(recordAudio, prompt)
                        if (prompt.isNotEmpty()) text = " $text"
                        val newPrompt = (prompt + text).split(" ").takeLast(32).joinToString(" ")
                        prompt = newPrompt
                        println(text)
                    }
                }
            }
        }
        val endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10)
        val continueFn : () -> Boolean = { System.currentTimeMillis() < endTime }
        val rawBuffer = ConcurrentLinkedDeque<ByteArray>()
        Thread({
            try {
                AudioRecorder(rawBuffer, 0.05, continueFn).run()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }, "dication-audio-recorder").start()
        val wavBuffer = ConcurrentLinkedDeque<ByteArray>()
        Thread({
            try {
                LoudnessWindowBuffer(rawBuffer, wavBuffer, continueFn).run()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }, "dictation-audio-processor").start()
        val dictationPump = DictationPump(wavBuffer, continueFn)
        val dictationThread = Thread({
            try {
                dictationPump.run()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }, "dictation-api-processor")
        dictationThread.start()
        dictationThread.join()
    }

}
