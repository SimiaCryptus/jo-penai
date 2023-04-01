package com.simiacryptus.openai

import com.simiacryptus.util.AudioRecorder
import com.simiacryptus.util.TranscriptionProcessor
import com.simiacryptus.util.PercentileLoudnessWindowBuffer
import org.junit.jupiter.api.Test
import java.awt.Desktop
import java.io.File
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.TimeUnit
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
        val client: OpenAIClient = OpenAIClient(apiKey)
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
                PercentileLoudnessWindowBuffer(rawBuffer, wavBuffer, continueFn).run()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }, "transcription-audio-processor").start()
        val transcriptionProcessor = TranscriptionProcessor(client, wavBuffer, continueFn){ println(it) }
        val transcriptionThread = Thread({
            try {
                transcriptionProcessor.run()
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }, "transcription-api-processor")
        transcriptionThread.start()
        transcriptionThread.join()
    }

}
