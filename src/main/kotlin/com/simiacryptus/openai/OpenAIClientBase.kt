package com.simiacryptus.openai

import org.slf4j.event.Level
import java.io.BufferedOutputStream
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

@Suppress("unused")
open class OpenAIClientBase(
    key: String = keyTxt,
    private val apiBase: String = "https://api.openai.com/v1",
    logLevel: Level = Level.INFO,
    logStreams: MutableList<BufferedOutputStream> = mutableListOf()
) : APIClientBase(key, apiBase, logLevel, logStreams) {

    private val tokenCounter = AtomicInteger(0)

    open fun incrementTokens(model: Model?, tokens: Int) {
        tokenCounter.addAndGet(tokens)
    }

    open val metrics: Map<String, Any>
        get() = hashMapOf(
            "tokens" to tokenCounter.get(),
            "chats" to chatCounter.get(),
            "completions" to completionCounter.get(),
            "moderations" to moderationCounter.get(),
            "renders" to renderCounter.get(),
            "transcriptions" to transcriptionCounter.get(),
            "edits" to editCounter.get(),
        )
    protected val chatCounter = AtomicInteger(0)
    protected val completionCounter = AtomicInteger(0)
    protected val moderationCounter = AtomicInteger(0)
    protected val renderCounter = AtomicInteger(0)
    protected val transcriptionCounter = AtomicInteger(0)
    protected val editCounter = AtomicInteger(0)

    companion object {
        var auxillaryLog: File? = null
        val auxillaryLogOutputStream: BufferedOutputStream? by lazy { auxillaryLog?.outputStream()?.buffered() }

        private var _keyTxt: String? = null
        var keyTxt: String
            get() {
                if (null != _keyTxt) return _keyTxt!!
                val resourceAsStream = OpenAIClient::class.java.getResourceAsStream("/openai.key")
                if (null != resourceAsStream) return resourceAsStream.readAllBytes().toString(Charsets.UTF_8).trim()
                val keyFile = File(File(System.getProperty("user.home")), "openai.key")
                if (keyFile.exists()) return keyFile.readText().trim()
                if (System.getenv().containsKey("OPENAI_KEY")) return System.getenv("OPENAI_KEY").trim()
                return ""
            }
            set(value) {
                _keyTxt = value
            }
    }
}