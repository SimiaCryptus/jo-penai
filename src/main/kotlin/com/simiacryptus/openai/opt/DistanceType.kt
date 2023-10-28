package com.simiacryptus.openai.opt

import kotlin.math.pow

enum class DistanceType {
    Euclidean {
        override fun distance(contentEmbedding: Array<Double>, promptEmbedding: Array<Double>): Double {
            return Math.sqrt(
                contentEmbedding.zip(promptEmbedding).map { (a, b) ->
                    (a - b).pow(2)
                }.sum()
            )
        }
    },
    Manhattan {
        override fun distance(contentEmbedding: Array<Double>, promptEmbedding: Array<Double>): Double {
            return contentEmbedding.zip(promptEmbedding).map { (a, b) -> Math.abs(a - b) }.sum()
        }
    },
    Cosine {
        override fun distance(contentEmbedding: Array<Double>, promptEmbedding: Array<Double>): Double {
            val dotProduct = contentEmbedding.zip(promptEmbedding).map { (a, b) -> a * b }.sum()
            val contentMagnitude = Math.sqrt(contentEmbedding.map { it.pow(2) }.sum())
            val promptMagnitude = Math.sqrt(promptEmbedding.map { it.pow(2) }.sum())
            return 1 - dotProduct / (contentMagnitude * promptMagnitude)
        }
    };
    abstract fun distance(
        contentEmbedding: Array<Double>,
        promptEmbedding: Array<Double>
    ): Double
}