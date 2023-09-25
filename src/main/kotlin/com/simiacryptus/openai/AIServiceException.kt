package com.simiacryptus.openai

import java.io.IOException

open class AIServiceException : IOException {
    constructor(message: String?) : super(message)
    constructor(message: String?, cause: Throwable?) : super(message, cause)
}