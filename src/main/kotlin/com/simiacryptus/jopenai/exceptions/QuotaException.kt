package com.simiacryptus.jopenai.exceptions

class QuotaException : AIServiceException("Quota exceeded", isFatal = true)
