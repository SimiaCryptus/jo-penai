package com.simiacryptus.openai.opt

import com.simiacryptus.openai.Models
import com.simiacryptus.openai.OpenAIClient
import org.slf4j.LoggerFactory

abstract class Expectation {
    companion object {
        val log = LoggerFactory.getLogger(Expectation::class.java)
    }

    open class VectorMatch(val example: String, private val metric: DistanceType = DistanceType.Cosine) : Expectation() {
        override fun matches(api: OpenAIClient, response: OpenAIClient.ChatResponse): Boolean {
            return true
        }

        override fun score(api: OpenAIClient, response: OpenAIClient.ChatResponse): Double {
            val promptStr = response.choices.first().message?.content ?: ""
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
                model = Models.AdaEmbedding.modelName, input = str
            )
        ).data.first().embedding!!
    }

    open class ContainsMatch(
        val pattern: Regex,
        val critical: Boolean = true
    ) : Expectation() {
        override fun matches(api: OpenAIClient, response: OpenAIClient.ChatResponse): Boolean {
            if (!critical) return true
            return _matches(response)
        }
        override fun score(api: OpenAIClient, response: OpenAIClient.ChatResponse): Double {
            return if (_matches(response)) 1.0 else 0.0
        }

        private fun _matches(response: OpenAIClient.ChatResponse): Boolean {
            if (pattern.containsMatchIn(response.choices.first().message?.content ?: "")) return true
            log.info(
                """Failed to match ${
                    pattern.pattern.replace("\n", "\\n")
                } in ${
                    response.choices.first().message?.content?.replace("\n", "\\n") ?: ""
                }"""
            )
            return false
        }

    }

    abstract fun matches(api: OpenAIClient, response: OpenAIClient.ChatResponse): Boolean

    abstract fun score(api: OpenAIClient, response: OpenAIClient.ChatResponse): Double


}