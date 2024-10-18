package com.simiacryptus.jopenai.models

fun String.chatModel(): ChatModel = ChatModel.values().entries.find {
    it.key.equals(this, true) || it.value?.modelName.equals(this, true)
}?.value ?: throw IllegalArgumentException("Unknown model: $this")