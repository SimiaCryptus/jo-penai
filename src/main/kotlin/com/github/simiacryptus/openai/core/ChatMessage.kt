package com.github.simiacryptus.openai.core

class ChatMessage {
    enum class Role {
        assistant, user, system
    }

    var role: com.github.simiacryptus.openai.core.ChatMessage.Role? = null
    var content: String? = null

    @Suppress("unused")
    constructor()
    constructor(role: com.github.simiacryptus.openai.core.ChatMessage.Role?, content: String?) {
        this.role = role
        this.content = content
    }
}