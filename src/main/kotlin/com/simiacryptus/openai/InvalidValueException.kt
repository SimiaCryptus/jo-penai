package com.simiacryptus.openai

class InvalidValueException(field: String?, value: String?) : AIServiceException("Invalid value: $field = $value")