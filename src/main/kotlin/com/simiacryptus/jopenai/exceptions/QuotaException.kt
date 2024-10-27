package com.simiacryptus.jopenai.exceptions
import org.slf4j.LoggerFactory

class QuotaException : AIServiceException("Quota exceeded", isFatal = true)
{
    companion object {
        private val logger = LoggerFactory.getLogger("QuotaLogger")
    }
    init {
        logger.warn("QuotaException initialized: Quota exceeded")
    }
}