package com.simiacryptus.jopenai.models

import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.simiacryptus.util.DynamicEnum
import com.simiacryptus.util.DynamicEnumDeserializer
import com.simiacryptus.util.DynamicEnumSerializer


@JsonDeserialize(using = APIProviderDeserializer::class)
@JsonSerialize(using = APIProviderSerializer::class)
class APIProvider private constructor(name: String, val base: String? = null) : DynamicEnum<APIProvider>(name) {
    companion object {
        val Google = APIProvider("Google", "https://generativelanguage.googleapis.com")
        val OpenAI = APIProvider("OpenAI", "https://api.openai.com/v1")
        val Anthropic = APIProvider("Anthropic", "https://api.anthropic.com/v1")
        val AWS = APIProvider("AWS", "https://api.openai.aws")
        val Groq = APIProvider("Groq", "https://api.groq.com/openai/v1")
        val Perplexity = APIProvider("Perplexity", "https://api.perplexity.ai")
        val ModelsLab = APIProvider("ModelsLab", "https://modelslab.com/api/v6")
        val Mistral = APIProvider("Mistral", "https://api.mistral.ai/v1")

        init {
            register(APIProvider::class.java, Google)
            register(APIProvider::class.java, OpenAI)
            register(APIProvider::class.java, Anthropic)
            register(APIProvider::class.java, AWS)
            register(APIProvider::class.java, Groq)
            register(APIProvider::class.java, Perplexity)
            register(APIProvider::class.java, ModelsLab)
            register(APIProvider::class.java, Mistral)
        }

        @JvmStatic
        fun valueOf(name: String): APIProvider = valueOf(APIProvider::class.java, name)

        @JvmStatic
        fun values(): Collection<APIProvider> = values(APIProvider::class.java)
    }
}

class APIProviderSerializer : DynamicEnumSerializer<APIProvider>(APIProvider::class.java)
class APIProviderDeserializer : DynamicEnumDeserializer<APIProvider>(APIProvider::class.java)