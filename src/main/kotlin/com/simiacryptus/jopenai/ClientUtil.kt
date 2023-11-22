package com.simiacryptus.jopenai

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.simiacryptus.jopenai.exceptions.*
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.regex.Pattern

object ClientUtil {

    var auxiliaryLog: File? = null

    fun checkError(result: String) {
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
    fun String.toContentList() = listOf(this).map { ApiModel.ContentPart(text = it, type = "text") }

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

}