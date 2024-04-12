package com.simiacryptus.jopenai

import com.simiacryptus.jopenai.ApiModel.*
import com.simiacryptus.jopenai.audio.AudioRecorder
import com.simiacryptus.jopenai.audio.PercentileLoudnessWindowBuffer
import com.simiacryptus.jopenai.audio.TranscriptionProcessor
import com.simiacryptus.jopenai.models.APIProvider
import com.simiacryptus.jopenai.models.ChatModels
import com.simiacryptus.jopenai.models.EmbeddingModels.Companion.AdaEmbedding
import com.simiacryptus.jopenai.models.ImageModels
import com.simiacryptus.jopenai.util.ClientUtil
import com.simiacryptus.jopenai.util.ClientUtil.defaultApiProvider
import com.simiacryptus.jopenai.util.ClientUtil.toContentList
import com.simiacryptus.jopenai.util.JsonUtil
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import java.awt.Desktop
import java.awt.image.BufferedImage
import java.io.File
import java.net.URL
import java.nio.file.Files
import java.nio.file.Paths
import java.util.concurrent.ConcurrentLinkedDeque
import java.util.concurrent.TimeUnit
import javax.imageio.ImageIO

class OpenAIClientTest {

  companion object {
    private val log = LoggerFactory.getLogger(OpenAIClientTest::class.java)
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

    // Modified testChat method to accept a ChatModels parameter

    const val imageSize = "512x512"
    val imageModel = ImageModels.DallE2.modelName
  }

  @Test
  fun testChat() {
    val prov = ClientUtil.keyMap[defaultApiProvider.name] ?: return
    if (prov?.isBlank() == true) return
    val client = OpenAIClient(
      mapOf(
        defaultApiProvider to prov
      )
    )
    val model = ChatModels.GPT35Turbo
    val request = ChatRequest(
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
  fun testJsonChat() {
    val prov = ClientUtil.keyMap[defaultApiProvider.name] ?: return
    if (prov?.isBlank() == true) return
    val client = OpenAIClient(
      mapOf(
        defaultApiProvider to prov
      )
    )
    val model = ChatModels.GPT4Turbo
    val request = ChatRequest(
      model = model.modelName,
      messages = ArrayList(
        listOf(
          ChatMessage(
            Role.system,
            "You are a spiritual teacher that responds to all questions in JSON format".toContentList()
          ),
          ChatMessage(Role.user, "What is the meaning of life?".toContentList()),
        )
      ),
      response_format = mapOf("type" to "json_object")
    )
    val chatResponse = client.chat(request, model)
    println(chatResponse.choices.first().message?.content ?: "No response")
  }

  @Test
  fun testImageChat() {
    val prov = ClientUtil.keyMap[defaultApiProvider.name] ?: return
    if (prov?.isBlank() == true) return
    val client = OpenAIClient(
      mapOf(
        defaultApiProvider to prov
      )
    )
    val imageUrl = client.createImage(
      ImageGenerationRequest(
        prompt = "A cute baby sea otter",
        model = imageModel,
        n = 1,
        size = imageSize
      )
    ).data.first().url
    val model = ChatModels.GPT4Turbo
    val request = ChatRequest(
      model = model.modelName,
      messages = ArrayList(
        listOf(
          ChatMessage(
            Role.system,
            "You are an image description service".toContentList()
          ),
          ChatMessage(
            Role.user,
            listOf(
              ContentPart(text = "Please describe this image", type = "text"),
              ContentPart(image_url = imageUrl, type = "image_url")
            )
          ),
        )
      ),
      stop = listOf("###")
    )
    val chatResponse = client.chat(request, model)
    println(chatResponse.choices.first().message?.content ?: "No response")
  }

  @Test
  fun testRender() {
    val prov = ClientUtil.keyMap[defaultApiProvider.name] ?: return
    if (prov?.isBlank() == true) return
    val client = OpenAIClient(
      mapOf(
        defaultApiProvider to prov
      )
    )
    val image = client.render("This is a test!").first()
    val tempFile = File.createTempFile("test", ".png")
    ImageIO.write(image, "png", tempFile)
    Desktop.getDesktop().browse(tempFile.toURI())
  }

  @Test
  fun testCreateImage() {
    val prov = ClientUtil.keyMap[defaultApiProvider.name] ?: return
    if (prov?.isBlank() == true) return
    val client = OpenAIClient(ClientUtil.keyMap.mapKeys { APIProvider.valueOf(it.key) })
    val imageUrl = client.createImage(
      ImageGenerationRequest(
        prompt = "A cute baby sea otter",
        model = imageModel,
        n = 1,
        size = imageSize
      )
    ).data.first().url
    val image: BufferedImage = ImageIO.read(URL(imageUrl))
    val tempFile = File.createTempFile("test", ".png")
    ImageIO.write(image, "png", tempFile)
    Desktop.getDesktop().browse(tempFile.toURI())
  }

  @Test
  fun testGenerateAndEditImage() {
    val prov = ClientUtil.keyMap[defaultApiProvider.name] ?: return
    if (prov?.isBlank() == true) return
    val client = OpenAIClient(
      mapOf(
        defaultApiProvider to prov
      )
    )

    val imageUrl = client.createImage(
      ImageGenerationRequest(
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
      ImageEditRequest(
        image = convert(createdImage, BufferedImage.TYPE_INT_ARGB),
        prompt = "Watercolor painting",
        mask = null,
        n = 2,
        size = imageSize
      )
    ).data.forEachIndexed { index, imageObject ->
      val image: BufferedImage = ImageIO.read(URL(imageObject.url))
      val tempFile = File.createTempFile("test_edit_$index", ".png")
      ImageIO.write(image, "png", tempFile)
      Desktop.getDesktop().browse(tempFile.toURI())
    }
  }

  @Test
  fun testGenerateAndVaryImage() {
    val prov = ClientUtil.keyMap[defaultApiProvider.name] ?: return
    if (prov?.isBlank() == true) return
    val client = OpenAIClient(
      mapOf(
        defaultApiProvider to prov
      )
    )
    val imageGenerationRequest = ImageGenerationRequest(
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
    val imageEditRequest = ImageVariationRequest(
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
    val prov = ClientUtil.keyMap[defaultApiProvider.name] ?: return
    if (prov?.isBlank() == true) return
    val client = OpenAIClient(
      mapOf(
        defaultApiProvider to prov
      )
    )
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
    val prov = ClientUtil.keyMap[defaultApiProvider.name] ?: return
    if (prov?.isBlank() == true) return
    val client = OpenAIClient(
      mapOf(
        defaultApiProvider to prov
      )
    )
    val models = client.listModels()
    log.info("Models: ${JsonUtil.toJson(models)}")
  }

  @Test
  fun testListEngines() {
    val prov = ClientUtil.keyMap[defaultApiProvider.name] ?: return
    if (prov?.isBlank() == true) return
    val client = OpenAIClient(
      mapOf(
        defaultApiProvider to prov
      )
    )
    val models = client.listEngines()
    log.info("Models: ${JsonUtil.toJson(models)}")
  }

  @Test
  fun testEmbedding() {
    val prov = ClientUtil.keyMap[defaultApiProvider.name] ?: return
    if (prov?.isBlank() == true) return
    val client = OpenAIClient(
      mapOf(
        defaultApiProvider to prov
      )
    )
    val request = EmbeddingRequest(model = AdaEmbedding.modelName, input = "This is a test!")
    val embedding = client.createEmbedding(request)
    log.info("Embedding: ${JsonUtil.toJson(embedding)}")
  }

  @Test
  fun testCreateSpeech() {
    val prov = ClientUtil.keyMap[defaultApiProvider.name] ?: return
    if (prov?.isBlank() == true) return
    val client = OpenAIClient(
      mapOf(
        defaultApiProvider to prov
      )
    )
    val speechRequest = SpeechRequest("The quick brown fox jumped over the lazy dog.")
    val bytes = client.createSpeech(speechRequest)
    assertTrue((bytes?.size ?: 0) > 0)
    val tempFile = File.createTempFile("test", ".mp3")
    Files.write(Paths.get(tempFile.toURI()), bytes!!)
    try {
      Desktop.getDesktop().browse(tempFile.toURI())
    } catch (e: Throwable) {/*ignore*/
    }
  }

}
