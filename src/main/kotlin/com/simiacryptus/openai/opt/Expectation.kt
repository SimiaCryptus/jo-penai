package com.simiacryptus.openai.opt

import com.simiacryptus.openai.OpenAIClient
import org.slf4j.LoggerFactory

abstract class Expectation {
    companion object {
        val log = LoggerFactory.getLogger(PromptOptimization::class.java)
    }

    open class VectorMatch(val example: String) : Expectation() {
        override fun matches(api: OpenAIClient, prompt: OpenAIClient.ChatResponse): Boolean {
            return true
        }

        override fun score(api: OpenAIClient, prompt: OpenAIClient.ChatResponse): Double {
            val promptStr = prompt.choices.first().message?.content ?: ""
            val contentEmbedding = createEmbedding(api, example)
            val promptEmbedding = createEmbedding(api, promptStr)
            return distance(contentEmbedding, promptEmbedding)
        }

        open  fun distance(
            contentEmbedding: Array<Double>,
            promptEmbedding: Array<Double>
        ) = 1.0 - contentEmbedding.zip(promptEmbedding).map { (a, b) -> Math.abs(a - b) }.average()

        protected fun createEmbedding(api: OpenAIClient, str: String) = api.createEmbedding(
            OpenAIClient.EmbeddingRequest(
                model = OpenAIClient.Models.AdaEmbedding.modelName, input = str
            )
        ).data.first().embedding!!
    }

    open class ContainsMatch(
        val pattern: Regex,
        val critical: Boolean = true
    ) : Expectation() {
        override fun matches(api: OpenAIClient, prompt: OpenAIClient.ChatResponse): Boolean {
            return !critical || pattern.containsMatchIn(prompt.choices.first().message?.content?:"")
        }

        override fun score(api: OpenAIClient, prompt: OpenAIClient.ChatResponse): Double {
            return if(!critical) {
                if(matches(api, prompt)) 1.0 else 0.0
            } else 1.0
        }

    }

    abstract fun matches(api: OpenAIClient, prompt: OpenAIClient.ChatResponse): Boolean

    abstract fun score(api: OpenAIClient, prompt: OpenAIClient.ChatResponse): Double


}