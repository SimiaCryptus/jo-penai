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

        fun isSanctioned(): Boolean {
            // Due to the invasion of Ukraine, Russia and allied groups are currently sanctioned.
            // Slava Ukraini!
            val locale = Locale.getDefault()
            // ISO 3166 - Russia
            if (locale.country.compareTo("RU", true) == 0) return true
            // ISO 3166 - Belarus
            if (locale.country.compareTo("BY", true) == 0) return true
            // ISO 639 - Russian
            if (locale.language.compareTo("ru", true) == 0) {
                // ISO 3166 - Ukraine
                if (locale.country.compareTo("UA", true) == 0) return false
                // ISO 3166 - United States
                if (locale.country.compareTo("US", true) == 0) return false
                // ISO 3166 - Britian
                if (locale.country.compareTo("GB", true) == 0) return false
                // ISO 3166 - United Kingdom
                if (locale.country.compareTo("UK", true) == 0) return false
                // ISO 3166 - Georgia
                if (locale.country.compareTo("GE", true) == 0) return false
                // ISO 3166 - Kazakhstan
                if (locale.country.compareTo("KZ", true) == 0) return false
                // ISO 3166 - Germany
                if (locale.country.compareTo("DE", true) == 0) return false
                // ISO 3166 - Poland
                if (locale.country.compareTo("PL", true) == 0) return false
                // ISO 3166 - Latvia
                if (locale.country.compareTo("LV", true) == 0) return false
                // ISO 3166 - Lithuania
                if (locale.country.compareTo("LT", true) == 0) return false
                // ISO 3166 - Estonia
                if (locale.country.compareTo("EE", true) == 0) return false
                // ISO 3166 - Moldova
                if (locale.country.compareTo("MD", true) == 0) return false
                // ISO 3166 - Armenia
                if (locale.country.compareTo("AM", true) == 0) return false
                // ISO 3166 - Azerbaijan
                if (locale.country.compareTo("AZ", true) == 0) return false
                // ISO 3166 - Kyrgyzstan
                if (locale.country.compareTo("KG", true) == 0) return false
                // ISO 3166 - Tajikistan
                if (locale.country.compareTo("TJ", true) == 0) return false
                // ISO 3166 - Turkmenistan
                if (locale.country.compareTo("TM", true) == 0) return false
                // ISO 3166 - Uzbekistan
                if (locale.country.compareTo("UZ", true) == 0) return false
                // ISO 3166 - Mongolia
                return locale.country.compareTo("MN", true) != 0
            }
            return false
        }
    }

    init {
        if (isSanctioned()) {
            throw RuntimeException("You are not allowed to use this software. Slava Ukraini!")
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