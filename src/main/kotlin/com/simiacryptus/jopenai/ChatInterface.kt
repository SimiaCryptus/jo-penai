package com.simiacryptus.jopenai

import com.simiacryptus.jopenai.models.OpenAITextModel
import org.slf4j.event.Level
import java.io.BufferedOutputStream

interface ChatInterface {
    val logStreams: MutableList<BufferedOutputStream>
    fun moderate(text: String)
    fun log(level: Level, msg: String)
    fun chat(chatRequest: ApiModel.ChatRequest, model: OpenAITextModel): ApiModel.ChatResponse
}