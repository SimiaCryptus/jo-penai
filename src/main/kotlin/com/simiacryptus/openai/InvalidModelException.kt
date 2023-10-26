package com.simiacryptus.openai

class InvalidModelException(model: String?) : AIServiceException("Invalid model: $model")
