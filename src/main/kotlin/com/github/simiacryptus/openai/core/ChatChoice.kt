package com.github.simiacryptus.openai.core

class ChatChoice @Suppress("unused") constructor() {
    var message: com.github.simiacryptus.openai.core.ChatMessage? = null

    @Suppress("unused")
    var index = 0

    @Suppress("unused")
    var finish_reason: String? = null
}