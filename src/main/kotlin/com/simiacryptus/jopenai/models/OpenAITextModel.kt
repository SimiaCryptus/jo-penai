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

@JsonDeserialize(using = OpenAITextModelDeserializer::class)
@JsonSerialize(using = OpenAITextModelSerializer::class)
open class OpenAITextModel(
    override val modelName: String = "",
    val maxTotalTokens: Int = -1,
    val maxOutTokens: Int = maxTotalTokens,
) : OpenAIModel {
    open val provider: APIProvider = APIProvider.OpenAI

    open fun pricing(usage: Usage): Double = 0.0
}

class OpenAITextModelSerializer : StdSerializer<OpenAITextModel>(OpenAITextModel::class.java) {
    override fun serialize(value: OpenAITextModel, gen: JsonGenerator, provider: SerializerProvider) {
        ((listOf(
            ChatModels.values(),
            CompletionModels.values(),
            EmbeddingModels.values(),
            EditModels.values(),
        ).flatMap { it.entries }.find { it.value == value }?.key) ?: value.modelName)
            .let { gen.writeString(it) }
    }
}

class OpenAITextModelDeserializer : JsonDeserializer<OpenAITextModel>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): OpenAITextModel {
        val modelName = p.readValueAs(String::class.java)
        listOf(
            ChatModels.values(),
            CompletionModels.values(),
            EmbeddingModels.values(),
            EditModels.values(),
        ).flatMap { it.entries }.find { it.key == modelName }?.value?.let { return it }
        return OpenAITextModel(modelName)
    }
}
