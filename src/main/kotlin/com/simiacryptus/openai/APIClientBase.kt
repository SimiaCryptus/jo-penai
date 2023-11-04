package com.simiacryptus.openai

import com.google.gson.Gson
import com.google.gson.JsonObject
import org.apache.http.HttpResponse
import org.apache.http.client.methods.HttpGet
import org.apache.http.client.methods.HttpPost
import org.apache.http.client.methods.HttpRequestBase
import org.apache.http.entity.StringEntity
import org.apache.http.util.EntityUtils
import org.slf4j.LoggerFactory
import org.slf4j.event.Level
import java.io.BufferedOutputStream
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.*
import java.util.concurrent.atomic.AtomicInteger
import java.util.regex.Pattern

open class APIClientBase(
    protected var key: String,
    private val apiBase: String,
    logLevel: Level = Level.INFO,
    auxillaryLogOutputStream: BufferedOutputStream?
) : HttpClientManager(logLevel, auxillaryLogOutputStream) {

    companion object {
        val log = LoggerFactory.getLogger(APIClientBase::class.java)
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

            // Due to the invasion of Ukraine, Russia and allies are currently sanctioned.
            // Slava Ukraini!
            val locale = Locale.getDefault()
            // ISO 3166 - Russia
            if (locale.country.compareTo("RU", true) == 0) return true
            // ISO 3166 - Belarus
            if (locale.country.compareTo("BY", true) == 0) return true
            // ISO 3166 - Iran
            if (locale.country.compareTo("IR", true) == 0) return true
            // ISO 3166 - North Korea
            if (locale.country.compareTo("KP", true) == 0) return true
            // ISO 3166 - Syria
            if (locale.country.compareTo("SY", true) == 0) return true

            // Due to ongoing war crimes in Gaza, Israel is currently sanctioned.
            // ISO 3166 - Israel
            if (locale.country.compareTo("IL", true) == 0) return true

            return false
        }
    }

    init {
        if (isSanctioned()) {
            throw RuntimeException("You are not allowed to use this software.")
        }
    }

    open val metrics : Map<String, Any> get() = hashMapOf(
        "tokens" to tokens.get(),
    )

    private val tokens = AtomicInteger(0)


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
    protected open fun authorize(request: HttpRequestBase) {
        request.addHeader("Authorization", "Bearer $key")
    }

    /**
     * Gets the response from the given URL.
     *
     * @param url The URL to GET the response from.
     * @return The response from the given URL.
     * @throws IOException If an I/O error occurs.
     */
    @Throws(IOException::class)
    protected operator fun get(url: String?): String = withClient {
        val request = HttpGet(url)
        request.addHeader("Content-Type", "application/json")
        request.addHeader("Accept", "application/json")
        authorize(request)
        val response: HttpResponse = it.execute(request)
        val entity = response.entity
        EntityUtils.toString(entity)
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

    open fun incrementTokens(totalTokens: Int) {
        tokens.addAndGet(totalTokens)
    }

}