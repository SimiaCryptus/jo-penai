package com.simiacryptus.openai.proxy

import com.simiacryptus.openai.CompletionRequest
import com.simiacryptus.openai.OpenAIClient
import com.simiacryptus.util.JsonUtil.toJson
import org.slf4j.event.Level

class CompletionProxy<T:Any>(
    clazz: Class<T>,
    apiKey: String,
    var model: String = "text-davinci-003",
    var maxTokens: Int = 4000,
    temperature: Double = 0.7,
    var verbose: Boolean = false,
    private val moderated: Boolean = true,
    base: String = "https://api.openai.com/v1",
    apiLog: String,
    val deserializerRetries: Int
) : GPTProxyBase<T>(clazz, apiLog, temperature, true, deserializerRetries) {
    val api: OpenAIClient

    init {
        api = OpenAIClient(apiKey, base, Level.DEBUG)
    }

    override fun complete(prompt: ProxyRequest, vararg examples: RequestResponse): String {
        if(verbose) log.info(prompt.toString())
        val request = CompletionRequest()
        request.prompt = """
        |Method: ${prompt.methodName}
        |Response Type: 
        |    ${prompt.apiYaml.replace("\n", "\n            ")}
        |Request: 
        |    {
        |        ${
            prompt.argList.entries.joinToString(",\n", transform = { (argName, argValue) ->
                """"$argName": $argValue"""
            }).replace("\n", "\n                ")
        }
        |    }
        |Response:
        |    {""".trim().trimIndent()
        request.max_tokens = maxTokens
        request.temperature = temperature
        if (moderated) api.moderate(toJson(request))
        val completion = api.complete(request, model).firstChoice.get().toString()
        if(verbose) log.info(completion)
        return "{$completion"
    }
}