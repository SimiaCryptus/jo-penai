package com.simiacryptus.jopenai.exceptions

import java.io.IOException

open class AIServiceException : IOException {
    constructor(message: String?) : super(message)
}