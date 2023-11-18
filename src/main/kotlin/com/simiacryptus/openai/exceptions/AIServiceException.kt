package com.simiacryptus.openai.exceptions

import java.io.IOException

open class AIServiceException : IOException {
    constructor(message: String?) : super(message)
}