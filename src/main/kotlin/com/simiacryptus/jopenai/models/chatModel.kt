package com.simiacryptus.jopenai.models
import org.slf4j.LoggerFactory
private val logger = LoggerFactory.getLogger("ChatModelLogger")

fun String.chatModel(): ChatModel {
    logger.info("Attempting to find ChatModel for input: $this")
    val model = ChatModel.values().entries.find {
        it.key.equals(this, true) || it.value?.modelName.equals(this, true)
    }?.value
    return if (model == null) {
        logger.error("Unknown model: $this")
        throw IllegalArgumentException("Unknown model: $this")
    } else {
        logger.info("Found ChatModel: ${model.modelName}")
        model
    }
}