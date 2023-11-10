package com.simiacryptus.openai.opt

import com.simiacryptus.openai.OpenAIClient
import org.slf4j.LoggerFactory

abstract class Expectation {
    companion object {
        val log = LoggerFactory.getLogger(PromptOptimization::class.java)
    }

    open class VectorMatch(val example: String, private val metric: DistanceType = DistanceType.Cosine) : Expectation() {
        override fun matches(api: OpenAIClient, prompt: OpenAIClient.ChatResponse): Boolean {
            return true
        }

        override fun score(api: OpenAIClient, prompt: OpenAIClient.ChatResponse): Double {
            val promptStr = prompt.choices.first().message?.content ?: ""
            val contentEmbedding = createEmbedding(api, example)
            val promptEmbedding = createEmbedding(api, promptStr)
            val distance = metric.distance(contentEmbedding, promptEmbedding)
            log.info(
                """Distance = $distance
                |  from "${example.replace("\n", "\\n")}" 
                |  to "${promptStr.replace("\n", "\\n")}"
                """.trimMargin().trim()
            )
            return -distance
        }

        private fun createEmbedding(api: OpenAIClient, str: String) = api.createEmbedding(
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
            if (!critical) return true
            return _matches(prompt)
        }
        override fun score(api: OpenAIClient, prompt: OpenAIClient.ChatResponse): Double {
            return if (_matches(prompt)) 1.0 else 0.0
        }

        private fun _matches(prompt: OpenAIClient.ChatResponse): Boolean {
            if (pattern.containsMatchIn(prompt.choices.first().message?.content ?: "")) return true
            log.info(
                """Failed to match ${
                    pattern.pattern.replace("\n", "\\n")
                } in ${
                    prompt.choices.first().message?.content?.replace("\n", "\\n") ?: ""
                }"""
            )
            return false
        }

    }

    abstract fun matches(api: OpenAIClient, prompt: OpenAIClient.ChatResponse): Boolean

    abstract fun score(api: OpenAIClient, prompt: OpenAIClient.ChatResponse): Double


}