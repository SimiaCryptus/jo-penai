package com.simiacryptus.util

import org.slf4j.LoggerFactory
import java.util.concurrent.Semaphore
private val logger = LoggerFactory.getLogger("RunWithPermitLogger")

fun Semaphore.runWithPermit(function: () -> String): String {
    logger.info("Attempting to acquire permit...")
    this.acquire()
    logger.info("Permit acquired.")
    try {
        return function()
    } finally {
        logger.info("Releasing permit.")
        this.release()
        logger.info("Permit released.")
    }
}