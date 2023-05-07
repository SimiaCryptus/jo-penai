package com.simiacryptus.openai

class ChatRequest() {
    constructor(request: ChatRequest) : this() {
        model = (request.model)
        temperature = (request.temperature)
        max_tokens = (request.max_tokens)
        stop = (request.stop)
        messages = (request.messages)
    }

    var messages = arrayOf<ChatMessage>()
    var model: String? = null
    var temperature = 0.0
    var max_tokens = 1000
    var stop: Array<CharSequence>? = null
}