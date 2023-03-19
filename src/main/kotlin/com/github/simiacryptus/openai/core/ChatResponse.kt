package com.github.simiacryptus.openai.core

import java.util.*

class ChatResponse {
    @Suppress("unused")
    var id: String? = null

    @Suppress("unused")
    var `object`: String? = null

    @Suppress("unused")
    var created: Long = 0

    @Suppress("unused")
    var model: String? = null
    var choices: Array<com.github.simiacryptus.openai.core.ChatChoice> = arrayOf()

    @Suppress("unused")
    var error: com.github.simiacryptus.openai.core.ApiError? = null

    @Suppress("unused")
    var usage: com.github.simiacryptus.openai.core.Usage? = null
    val response: Optional<CharSequence>
        get() = Optional.ofNullable(choices).flatMap(
            { choices: Array<com.github.simiacryptus.openai.core.ChatChoice>? ->
                Arrays.stream(
                    choices
                ).findFirst()
            }).map(
            { choice: com.github.simiacryptus.openai.core.ChatChoice -> choice.message!!.content!!.trim { it <= ' ' } })
}