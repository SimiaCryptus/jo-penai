package com.simiacryptus.jopenai.describe
import org.slf4j.LoggerFactory

@Retention(AnnotationRetention.RUNTIME)
annotation class Description(val value: String)
private val logger = LoggerFactory.getLogger("DescriptionLogger")
fun logDescription(description: Description) {
    if (logger.isDebugEnabled) {
        logger.debug("Description annotation value: ${description.value}")
    }
}