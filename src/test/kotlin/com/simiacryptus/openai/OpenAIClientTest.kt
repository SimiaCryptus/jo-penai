package com.simiacryptus.openai

import com.simiacryptus.openai.OpenAIClient.ChatMessage
import com.simiacryptus.util.audio.AudioRecorder
import com.simiacryptus.util.JsonUtil
import com.simiacryptus.util.audio.TranscriptionProcessor
import com.simiacryptus.util.audio.PercentileLoudnessWindowBuffer
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.awt.Desktop
import java.io.File
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO

class OpenAIClientTest {

    companion object {
        val logger = LoggerFactory.getLogger(OpenAIClientTest::class.java)
    }

    @Test
    fun testCompletion() {
        if (OpenAIClient.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClient.keyTxt)
        val request = OpenAIClient.CompletionRequest()
        request.prompt = "This is a test! This"
        val completion = client.complete(request, OpenAIClient.Models.DaVinci)
        println(completion.choices.first().text)
    }

    @Test
    fun testEdit() {
        // Doesn't seem to work right now... Wrong model error!?!?
        if (OpenAIClient.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClient.keyTxt)
        val request = OpenAIClient.EditRequest()
        request.input = "This is a test!"
        request.instruction = "Rewrite as an epic novel"
        request.model = OpenAIClient.Models.DaVinciEdit.modelName
        val completion = client.edit(request)
        println(completion.choices.first().text)
    }

    @Test
    fun testChat() {
        if (OpenAIClient.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClient.keyTxt)
        val request = OpenAIClient.ChatRequest()
        val model = OpenAIClient.Models.GPT35Turbo
        request.model = model.modelName
        request.max_tokens = model.maxTokens
        request.messages = arrayOf(
            ChatMessage(ChatMessage.Role.system, "You are a spiritual teacher"),
            ChatMessage(ChatMessage.Role.user, "What is the meaning of life?"),
        )
        val chatResponse = client.chat(request)
        println(chatResponse.choices.first().message?.content ?: "No response")
    }

    @Test
    fun testRender() {
        if (OpenAIClient.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClient.keyTxt)
        val image = client.render("This is a test!").first()
        val tempFile = File.createTempFile("test", ".png")
        ImageIO.write(image, "png", tempFile)
        Desktop.getDesktop().browse(tempFile.toURI())
    }

    @Test
    fun testDictate() {
        if (OpenAIClient.keyTxt.isBlank()) return
        val client: OpenAIClient = OpenAIClient(OpenAIClient.keyTxt)
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

    @Test
    fun testListModels() {
        if (OpenAIClient.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClient.keyTxt)
        val models = client.listModels()
        logger.info("Models: ${JsonUtil.toJson(models)}")
    }


    @Test
    fun testListEngines() {
        if (OpenAIClient.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClient.keyTxt)
        val models = client.listEngines()
        logger.info("Models: ${JsonUtil.toJson(models)}")
    }

    @Test
    fun testEmbedding() {
        if (OpenAIClient.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClient.keyTxt)
        val request = OpenAIClient.EmbeddingRequest(model = OpenAIClient.Models.AdaEmbedding.modelName, input = "This is a test!")
        val embedding = client.createEmbedding(request)
        logger.info("Embedding: ${JsonUtil.toJson(embedding)}")
    }

}
