package com.simiacryptus.openai.exceptions

class InvalidModelException(model: String?) : AIServiceException("Invalid model: $model")

