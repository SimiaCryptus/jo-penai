package com.simiacryptus.jopenai.models
import org.slf4j.LoggerFactory

interface OpenAIModel {

    val modelName: String
    fun logModelName() {
        val logger = LoggerFactory.getLogger(OpenAIModel::class.java)
        logger.debug("Model name is: $modelName")
    }
}