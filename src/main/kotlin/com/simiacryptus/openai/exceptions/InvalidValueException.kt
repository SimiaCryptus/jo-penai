package com.simiacryptus.openai.exceptions

class InvalidValueException(field: String?, value: String?) : AIServiceException("Invalid value: $field = $value")