package com.simiacryptus.openai

import com.simiacryptus.openai.OpenAIClient.ChatMessage
import com.simiacryptus.util.JsonUtil
import com.simiacryptus.util.audio.AudioRecorder
import com.simiacryptus.util.audio.PercentileLoudnessWindowBuffer
import com.simiacryptus.util.audio.TranscriptionProcessor
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.awt.Desktop
import java.io.File
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO

class OpenAIClientTest {

    companion object {
        val log = LoggerFactory.getLogger(OpenAIClientTest::class.java)
    }

    @Test
    fun testCompletion() {
        if (OpenAIClientBase.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClientBase.keyTxt)
        val request = OpenAIClient.CompletionRequest(prompt = "This is a test! This")
        val completion = client.complete(request, Models.DaVinci)
        println(completion.choices.first().text)
    }

    @Test
    fun testEdit() {
        // Doesn't seem to work right now... Wrong model error!?!?
        if (OpenAIClientBase.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClientBase.keyTxt)
        val request = OpenAIClient.EditRequest(
            input = "This is a test!",
            instruction = "Rewrite as an epic novel",
            model = Models.DaVinciEdit.modelName
        )
        val completion = client.edit(request)
        println(completion.choices.first().text)
    }

    @Test
    fun testChat() {
        if (OpenAIClientBase.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClientBase.keyTxt)
        val model = Models.GPT35Turbo
        val request = OpenAIClient.ChatRequest(
            model = model.modelName,
            messages = ArrayList(
                listOf(
                    ChatMessage(ChatMessage.Role.system, "You are a spiritual teacher"),
                    ChatMessage(ChatMessage.Role.user, "What is the meaning of life?"),
                )
            )
        )
        val chatResponse = client.chat(request, model)
        println(chatResponse.choices.first().message?.content ?: "No response")
    }

    @Test
    fun testRender() {
        if (OpenAIClientBase.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClientBase.keyTxt)
        val image = client.render("This is a test!").first()
        val tempFile = File.createTempFile("test", ".png")
        ImageIO.write(image, "png", tempFile)
        Desktop.getDesktop().browse(tempFile.toURI())
    }

    @Test
    fun testDictate() {
        if (OpenAIClientBase.keyTxt.isBlank()) return
        val client: OpenAIClient = OpenAIClient(OpenAIClientBase.keyTxt)
        val endTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(10)
        val continueFn: () -> Boolean = { System.currentTimeMillis() < endTime }
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
        val transcriptionProcessor = TranscriptionProcessor(client, wavBuffer, continueFn) { println(it) }
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

    @Test
    fun testListModels() {
        if (OpenAIClientBase.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClientBase.keyTxt)
        val models = client.listModels()
        log.info("Models: ${JsonUtil.toJson(models)}")
    }


    @Test
    fun testListEngines() {
        if (OpenAIClientBase.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClientBase.keyTxt)
        val models = client.listEngines()
        log.info("Models: ${JsonUtil.toJson(models)}")
    }

    @Test
    fun testEmbedding() {
        if (OpenAIClientBase.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClientBase.keyTxt)
        val request = OpenAIClient.EmbeddingRequest(model = Models.AdaEmbedding.modelName, input = "This is a test!")
        val embedding = client.createEmbedding(request)
        log.info("Embedding: ${JsonUtil.toJson(embedding)}")
    }

}
