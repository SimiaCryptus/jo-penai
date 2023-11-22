package com.simiacryptus.jopenai.opt

import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sqrt

enum class DistanceType {
    Euclidean {
        override fun distance(contentEmbedding: DoubleArray, promptEmbedding: DoubleArray): Double {
            return sqrt(
                contentEmbedding.zip(promptEmbedding).map { (a, b) ->
                    (a - b).pow(2)
                }.sum()
            )
        }
    },
    Manhattan {
        override fun distance(contentEmbedding: DoubleArray, promptEmbedding: DoubleArray): Double {
            return contentEmbedding.zip(promptEmbedding).map { (a, b) -> abs(a - b) }.sum()
        }
    },
    Cosine {
        override fun distance(contentEmbedding: DoubleArray, promptEmbedding: DoubleArray): Double {
            val dotProduct = contentEmbedding.zip(promptEmbedding).map { (a, b) -> a * b }.sum()
            val contentMagnitude = sqrt(contentEmbedding.map { it.pow(2) }.sum())
            val promptMagnitude = sqrt(promptEmbedding.map { it.pow(2) }.sum())
            return 1 - dotProduct / (contentMagnitude * promptMagnitude)
        }
    };
    abstract fun distance(
        contentEmbedding: DoubleArray,
        promptEmbedding: DoubleArray
    ): Double
}