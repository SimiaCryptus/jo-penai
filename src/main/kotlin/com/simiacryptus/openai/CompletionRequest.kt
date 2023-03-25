package com.simiacryptus.openai

import java.util.*

open class CompletionRequest() {

    var prompt: String = ""
    var suffix: String? = null

    @Suppress("unused")
    var temperature = 0.0

    @Suppress("unused")
    var max_tokens = 0
    var stop: Array<CharSequence>? = null

    @Suppress("unused")
    var logprobs: Int? = null

    @Suppress("unused")
    var echo = false

    constructor(other: CompletionRequest) : this() {
        prompt = other.prompt
        temperature = other.temperature
        max_tokens = other.max_tokens
        stop = other.stop
        logprobs = other.logprobs
        echo = other.echo
    }

    fun appendPrompt(prompt: CharSequence): CompletionRequest {
        this.prompt = this.prompt + prompt
        return this
    }

    fun addStops(vararg newStops: CharSequence): CompletionRequest {
        val stops = ArrayList<CharSequence>()
        for (x in newStops) {
            if (x.isNotEmpty()) {
                stops.add(x)
            }
        }
        if (stops.isNotEmpty()) {
            if (null != stop) Arrays.stream(stop).forEach { e: CharSequence ->
                stops.add(
                    e
                )
            }
            stop = stops.stream().distinct().toArray { size: Int -> arrayOfNulls<CharSequence>(size) }
        }
        return this
    }

    fun setSuffix(suffix: CharSequence?): CompletionRequest {
        this.suffix = suffix?.toString()
        return this
    }

}