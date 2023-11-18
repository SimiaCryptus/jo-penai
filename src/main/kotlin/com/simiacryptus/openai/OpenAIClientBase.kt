package com.simiacryptus.openai

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.simiacryptus.openai.exceptions.*
import com.simiacryptus.openai.models.OpenAIModel
import com.simiacryptus.util.HttpClientManager
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.core5.http.HttpRequest
import org.apache.hc.core5.http.io.entity.EntityUtils
import org.apache.hc.core5.http.io.entity.StringEntity
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.io.BufferedOutputStream
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Pattern

open class OpenAIClientBase(
    var key: String = keyTxt,
    private val apiBase: String = "https://api.openai.com/v1",
    logLevel: Level = Level.INFO,
    logStreams: MutableList<BufferedOutputStream> = mutableListOf(),
) : HttpClientManager(logLevel, logStreams) {

    private val tokenCounter = AtomicInteger(0)

    open fun incrementTokens(model: OpenAIModel?, tokens: Int) {
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
        var auxiliaryLog: File? = null

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
        fun String.toContentList() = this.split("\n").map { OpenAIClient.ContentPart(text = it,type = "text") }

        val log = LoggerFactory.getLogger(OpenAIClientBase::class.java)
        val allowedCharset: Charset = Charset.forName("ASCII")
        val maxTokenErrorMessage = listOf(
            Pattern.compile(
                """This model's maximum context length is (\d+) tokens. However, you requested (\d+) tokens \((\d+) in the messages, (\d+) in the completion\).*"""
            ),
            // This model's maximum context length is 4097 tokens, however you requested 80052 tokens (52 in your prompt; 80000 for the completion). Please reduce your prompt; or completion length.
            Pattern.compile(
                """This model's maximum context length is (\d+) tokens, however you requested (\d+) tokens \((\d+) in your prompt; (\d+) for the completion\).*"""
            )
        )
        val rateLimitErrorMessage = Pattern.compile(
            """Rate limit reached for (\d+)KTPM-(\d+)RPM in organization (\S+) on tokens per min. Limit: (\d+) / min. Please try again in (\d+)ms. Contact us through our help center at help.openai.com if you continue to have issues."""
        )
        val quotaErrorMessage = Pattern.compile(
            """You exceeded your current quota, please check your plan and billing details."""
        )
        val invalidModelException = Pattern.compile(
            """The model `(\S+)` does not exist or you do not have access to it."""
        )
        val invalidValueException = Pattern.compile(
            """Invalid value for '(\S+)': (\S+)"""
        )

        fun isSanctioned(): Boolean {
            return false
        }
    }

    init {
        if (isSanctioned()) {
            throw RuntimeException("You are not allowed to use this software.")
        }
    }

    @Throws(IOException::class, InterruptedException::class)
    protected fun post(url: String, json: String): String {
        val request = HttpPost(url)
        request.addHeader("Content-Type", "application/json")
        request.addHeader("Accept", "application/json")
        authorize(request)
        request.entity = StringEntity(json)
        return post(request)
    }

    protected fun post(request: HttpPost): String = withClient { EntityUtils.toString(it.execute(request).entity) }

    @Throws(IOException::class)
    protected open fun authorize(request: HttpRequest) {
        request.addHeader("Authorization", "Bearer $key")
    }

    @Throws(IOException::class)
    protected operator fun get(url: String?): String = withClient {
        val request = HttpGet(url)
        request.addHeader("Content-Type", "application/json")
        request.addHeader("Accept", "application/json")
        authorize(request)
        EntityUtils.toString(it.execute(request).entity)
    }

    protected fun checkError(result: String) {
        try {
            val jsonObject = Gson().fromJson(
                result,
                JsonObject::class.java
            )
            if(null == jsonObject) return
            if (jsonObject.has("error")) {
                val errorObject = jsonObject.getAsJsonObject("error")
                val errorMessage = errorObject["message"].asString
                if (errorMessage.startsWith("That model is currently overloaded with other requests.")) {
                    throw RequestOverloadException(errorMessage)
                }
                maxTokenErrorMessage.find { it.matcher(errorMessage).matches() }?.let {
                    val matcher = it.matcher(errorMessage)
                    if (matcher.find()) {
                        val modelMax = matcher.group(1).toInt()
                        val request = matcher.group(2).toInt()
                        val messages = matcher.group(3).toInt()
                        val completion = matcher.group(4).toInt()
                        throw ModelMaxException(modelMax, request, messages, completion)
                    }
                }
                rateLimitErrorMessage.matcher(errorMessage).let {
                    if (it.matches()) {
                        val org = it.group(3)
                        val limit = it.group(4).toInt()
                        val delay = it.group(5).toLong()
                        throw RateLimitException(org, limit, delay)
                    }
                }
                quotaErrorMessage.matcher(errorMessage).let {
                    if (it.matches()) {
                        throw QuotaException()
                    }
                }
                invalidModelException.matcher(errorMessage).let {
                    if (it.matches()) {
                        val model = it.group(1)
                        throw InvalidModelException(model)
                    }
                }
                invalidValueException.matcher(errorMessage).let {
                    if (it.matches()) {
                        val field = it.group(1)
                        val value = it.group(2)
                        throw InvalidValueException(field, value)
                    }
                }
                throw IOException(errorMessage)
            }
        } catch (e: com.google.gson.JsonSyntaxException) {
            throw IOException("Invalid JSON response: $result")
        }
    }

    class RequestOverloadException(message: String = "That model is currently overloaded with other requests.") :
        IOException(message)

}