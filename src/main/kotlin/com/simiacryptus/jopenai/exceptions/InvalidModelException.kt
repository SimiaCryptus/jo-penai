package com.simiacryptus.jopenai.exceptions

class InvalidModelException(model: String?) : AIServiceException("Invalid model: $model", isFatal = true)

