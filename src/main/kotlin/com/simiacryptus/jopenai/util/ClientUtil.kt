package com.simiacryptus.jopenai.util

import com.google.gson.Gson
import com.google.gson.JsonObject
import com.simiacryptus.jopenai.models.ApiModel
import com.simiacryptus.jopenai.OpenAIClient
import com.simiacryptus.jopenai.exceptions.*
import com.simiacryptus.jopenai.models.APIProvider
import com.simiacryptus.util.JsonUtil.fromJson
import java.io.File
import java.io.IOException
import java.nio.charset.Charset
import java.util.regex.Pattern

object ClientUtil {

    fun checkError(result: String) {
        try {
            val jsonObject = Gson().fromJson(
                result,
                JsonObject::class.java
            )
            if (null == jsonObject) return
            if (jsonObject.has("error")) {
                val errorObject = jsonObject.getAsJsonObject("error")
                val errorMessage = errorObject["message"].asString
                if (errorMessage.startsWith("That model is currently overloaded with other requests.")) {
                    throw RequestOverloadException(errorMessage)
                }
                if (safetyErrorMessage.matcher(errorMessage).matches()) {
                    throw SafetyException()
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
                    if (it.find()) {
                        val org = it.group(3)
                        val limit = it.group(4).toInt()
                        val delay = it.group(5).toLong()
                        throw RateLimitException(org, limit, delay)
                    }
                }
                rateLimitErrorMessage2.matcher(errorMessage).let {
                    if (it.find()) {
                        val org = it.group(2)
                        val limit = it.group(3).toInt()
                        val used = it.group(4).toInt()
                        val requested = it.group(5).toInt()
                        val delay = it.group(6).toLong()
                        throw RateLimitException(org, limit, delay)
                    }
                }
                rateLimitErrorMessage3.matcher(errorMessage).let {
                    if (it.find()) {
                        val org = it.group(2)
                        val limit = it.group(3).toInt()
                        val divisor = it.group(4).toLong()
                        val used = it.group(5).toInt()
                        throw RateLimitException(org, limit, divisor * 60)
                    }
                }
                quotaErrorMessage.matcher(errorMessage).let {
                    if (it.find()) {
                        throw QuotaException()
                    }
                }
                invalidModelException.matcher(errorMessage).let {
                    if (it.find()) {
                        val model = it.group(1)
                        throw InvalidModelException(model)
                    }
                }
                invalidValueException.matcher(errorMessage).let {
                    if (it.find()) {
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
    val defaultApiProvider: APIProvider = APIProvider.OpenAI
    var keyTxt: String
        get() {
            if (null != _keyTxt) return _keyTxt!!
            val resourceAsStream = OpenAIClient::class.java.getResourceAsStream("/openai.key.json")
            if (null != resourceAsStream) return resourceAsStream.readAllBytes().toString(Charsets.UTF_8).trim()
            val keyFile = File(File(System.getProperty("user.home")), "openai.key.json")
            if (keyFile.exists()) return keyFile.readText().trim()
            //if (System.getenv().containsKey("OPENAI_KEY")) return System.getenv("OPENAI_KEY").trim()
            return ""
        }
        set(value) {
            _keyTxt = value
        }

    val keyMap: Map<String, String>
        get() = try {
            fromJson(keyTxt, Map::class.java)
        } catch (e: Exception) { emptyMap() }

    fun String.toContentList() = listOf(this).map { ApiModel.ContentPart(text = it, type = "text") }
    fun String.toChatMessage(role: ApiModel.Role = ApiModel.Role.user) =
        ApiModel.ChatMessage(role = role, content = toContentList())

    val allowedCharset: Charset = Charset.forName("ASCII")
    private val maxTokenErrorMessage = listOf(
        Pattern.compile(
            """This model's maximum context length is (\d+) tokens. However, you requested (\d+) tokens \((\d+) in the messages, (\d+) in the completion\).*"""
        ),
        // This model's maximum context length is 4097 tokens, however you requested 80052 tokens (52 in your prompt; 80000 for the completion). Please reduce your prompt; or completion length.
        Pattern.compile(
            """This model's maximum context length is (\d+) tokens, however you requested (\d+) tokens \((\d+) in your prompt; (\d+) for the completion\).*"""
        )
    )

    // Your request was rejected as a result of our safety system. Image descriptions generated from your prompt may contain text that is not allowed by our safety system. If you believe this was done in error, your request may succeed if retried, or by adjusting your prompt.
    private val safetyErrorMessage = Pattern.compile("""Your request was rejected as a result of our safety system.""")
    private val rateLimitErrorMessage = Pattern.compile(
        """Rate limit reached for (\d+)KTPM-(\d+)RPM in organization (\S+) on tokens per min. Limit: (\d+) / min. Please try again in (\d+)ms. Contact us through our help center at help.openai.com if you continue to have issues."""
    )
    private val rateLimitErrorMessage2 = Pattern.compile(
        """Rate limit reached for (\S+) in organization (\S+) on requests per min \(RPM\): Limit (\d+), Used (\d+), Requested (\d+). Please try again in (\d+)s."""
    )
    private val rateLimitErrorMessage3 = Pattern.compile(
        """Rate limit exceeded for (\S+) per minute in organization (\S+). Limit: (\d+)/(\d+)min. Current: (\d+)/(\d+)min."""
    )

    //
    private val quotaErrorMessage = Pattern.compile(
        """You exceeded your current quota, please check your plan and billing details."""
    )
    private val invalidModelException = Pattern.compile(
        """The model `(\S+)` does not exist or you do not have access to it."""
    )
    private val invalidValueException = Pattern.compile(
        """Invalid value for '(\S+)': (\S+)"""
    )

}