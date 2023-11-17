package com.simiacryptus.openai

import com.simiacryptus.openai.OpenAIClient.ChatMessage
import com.simiacryptus.openai.OpenAIClient.Role
import com.simiacryptus.openai.OpenAIClientBase.Companion.toContentList
import com.simiacryptus.util.JsonUtil
import com.simiacryptus.util.audio.AudioRecorder
import com.simiacryptus.util.audio.PercentileLoudnessWindowBuffer
import com.simiacryptus.util.audio.TranscriptionProcessor
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.awt.Desktop
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO



class OpenAIClientTest {

    companion object {
        val log = LoggerFactory.getLogger(OpenAIClientTest::class.java)
        fun convert(imageFile: File, colorModel: Int): File {
            val originalImage: BufferedImage = ImageIO.read(imageFile)
            val rgbaImage = BufferedImage(
                originalImage.width, originalImage.height, colorModel
            )
            val graphics = rgbaImage.createGraphics()
            graphics.drawImage(originalImage, 0, 0, null)
            graphics.dispose()
            val rgbaFile = File.createTempFile("temp_image", ".png")
            ImageIO.write(rgbaImage, "png", rgbaFile)
            return rgbaFile
        }

        const val imageSize = "512x512"
        const val imageModel = "dall-e-2"
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
                    ChatMessage(Role.system, "You are a spiritual teacher".toContentList()),
                    ChatMessage(Role.user, "What is the meaning of life?".toContentList()),
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
    fun testCreateImage() {
        if (OpenAIClientBase.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClientBase.keyTxt)
        val imageRequest = OpenAIClient.ImageGenerationRequest(
            prompt = "A cute baby sea otter",
            model = imageModel,
            n = 1,
            size = imageSize
        )
        val imageResponse = client.createImage(imageRequest)
        val imageUrl = imageResponse.data.first().url
        val image: BufferedImage = ImageIO.read(URL(imageUrl))
        val tempFile = File.createTempFile("test", ".png")
        ImageIO.write(image, "png", tempFile)
        Desktop.getDesktop().browse(tempFile.toURI())
    }

    @Test
    fun testGenerateAndEditImage() {
        if (OpenAIClientBase.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClientBase.keyTxt)

        val imageUrl = client.createImage(
            OpenAIClient.ImageGenerationRequest(
                prompt = "A picture of a helpful robot",
                model = imageModel,
                n = 1,
                size = imageSize
            )
        ).data.first().url
        val createdImage = File.createTempFile("test", ".png")
        ImageIO.write(ImageIO.read(URL(imageUrl)), "png", createdImage)
        Desktop.getDesktop().browse(createdImage.toURI())

        client.createImageEdit(
            OpenAIClient.ImageEditRequest(
                image = convert(createdImage, BufferedImage.TYPE_INT_ARGB),
                prompt = "Watercolor painting",
                mask = null as Nothing?, // Optional, can be null
                n = 2,
                size = imageSize
            )
        ).data.forEachIndexed { index, imageObject ->
            val imageUrl = imageObject.url
            val image: BufferedImage = ImageIO.read(URL(imageUrl))
            val tempFile = File.createTempFile("test_edit_$index", ".png")
            ImageIO.write(image, "png", tempFile)
            Desktop.getDesktop().browse(tempFile.toURI())
        }
    }

    @Test
    fun testGenerateAndVaryImage() {
        if (OpenAIClientBase.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClientBase.keyTxt)
        val imageGenerationRequest = OpenAIClient.ImageGenerationRequest(
            prompt = "A futuristic cityscape at night",
            model = imageModel,
            n = 1,
            size = imageSize
        )

        // Generate the image
        val imageGenerationResponse = client.createImage(imageGenerationRequest)
        val generatedImageUrl = imageGenerationResponse.data.first().url
        val generatedImageFile = File.createTempFile("generated_image", ".png")
        ImageIO.write(ImageIO.read(URL(generatedImageUrl)), "png", generatedImageFile)
        Desktop.getDesktop().browse(generatedImageFile.toURI())

        // Define the image edit request
        val imageEditRequest = OpenAIClient.ImageVariationRequest(
            image = convert(generatedImageFile, BufferedImage.TYPE_INT_ARGB),
            //model = "dall-e-3",
            n = 1,
            size = imageSize
        )

        val editedImageUrl = client.createImageVariation(imageEditRequest).data.first().url
        val editedImageFile = File.createTempFile("vary_image", ".png")
        ImageIO.write(ImageIO.read(URL(editedImageUrl)), "png", editedImageFile)
        Desktop.getDesktop().browse(editedImageFile.toURI())
    }

    @Test
    fun testDictate() {
        if (OpenAIClientBase.keyTxt.isBlank()) return
        val client = OpenAIClient(OpenAIClientBase.keyTxt)
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
