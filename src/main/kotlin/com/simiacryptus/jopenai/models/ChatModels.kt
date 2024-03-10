package com.simiacryptus.jopenai.models

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.simiacryptus.jopenai.ApiModel.Usage
import com.simiacryptus.jopenai.models.ChatModels.Companion.values

@JsonDeserialize(using = ChatModelsDeserializer::class)
@JsonSerialize(using = ChatModelsSerializer::class)
open class ChatModels(
  modelName: String,
  maxTokens: Int,
  private val inputTokenPricePerK: Double,
  private val outputTokenPricePerK: Double,
) : OpenAITextModel(modelName, maxTokens) {
  override fun pricing(usage: Usage) =
    (usage.prompt_tokens * inputTokenPricePerK + usage.completion_tokens * outputTokenPricePerK) / 1000.0

  companion object {
    fun values() = mapOf(
      "GPT35Turbo" to GPT35Turbo,
      "GPT4" to GPT4,
      "GPT4Turbo" to GPT4Turbo,
      "GPT4Vision" to GPT4Vision
    )

    val GPT35Turbo = ChatModels("gpt-3.5-turbo-0125", 16384, 0.0005, 0.0015)
    val GPT4 = ChatModels("gpt-4-32k", 32768, 0.06, 0.12)
    val GPT4Turbo = ChatModels("gpt-4-turbo-preview", 128000, 0.01, 0.03)
    val GPT4Vision = ChatModels("gpt-4-vision-preview", 8192, 0.01, 0.03)
  }
}

class ChatModelsSerializer : StdSerializer<ChatModels>(ChatModels::class.java) {
  override fun serialize(value: ChatModels, gen: JsonGenerator, provider: SerializerProvider) {
    values().entries.find { it.value == value }?.key?.let { gen.writeString(it) }
  }
}

class ChatModelsDeserializer : JsonDeserializer<ChatModels>() {
  override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ChatModels {
    val modelName = p.readValueAs(String::class.java)
    return ChatModels.values()[modelName]
      ?: throw IllegalArgumentException("Unknown model name: $modelName")
  }
}
