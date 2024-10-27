package com.simiacryptus.jopenai.exceptions
import org.slf4j.LoggerFactory

class InvalidModelException(model: String?) : AIServiceException("Invalid model: $model", isFatal = true)
{
    companion object {
        private val logger = LoggerFactory.getLogger("InvalidModelLogger")
    }
    init {
        if (model.isNullOrEmpty()) {
            logger.warn("InvalidModelException thrown with no model specified")
        } else {
            logger.error("InvalidModelException thrown for model: $model")
        }
    }
}