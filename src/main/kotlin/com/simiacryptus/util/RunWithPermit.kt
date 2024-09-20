package com.simiacryptus.util

import java.util.concurrent.Semaphore

fun Semaphore.runWithPermit(function: () -> String): String {
    this.acquire()
    try {
        return function()
    } finally {
        this.release()
    }
}