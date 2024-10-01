package com.simiacryptus.jopenai.models

fun String.chatModel() : ChatModels = ChatModels.values().entries.find {
    it.key.equals(this,true) || it.value.modelName.equals(this,true)
}?.let { return it.value } ?: throw IllegalArgumentException("Unknown model: $this")