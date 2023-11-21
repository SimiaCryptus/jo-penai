package com.simiacryptus.openai.exceptions

class ModelMaxException(
    val modelMax: Int,
    val request: Int,
    val messages: Int,
    private val completion: Int
) : AIServiceException("Model max exceeded: $modelMax, request: $request, messages: $messages, completion: $completion")

