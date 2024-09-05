package com.simiacryptus.jopenai.proxy

import com.simiacryptus.jopenai.ApiModel
import com.simiacryptus.jopenai.ApiModel.ChatMessage
import com.simiacryptus.jopenai.ApiModel.ChatRequest
import com.simiacryptus.jopenai.ChatClient
import com.simiacryptus.jopenai.OpenAIClient
import com.simiacryptus.jopenai.models.ChatModels
import com.simiacryptus.jopenai.util.ClientUtil.toContentList
import com.simiacryptus.jopenai.util.JsonUtil.toJson
import java.util.concurrent.atomic.AtomicInteger

open class ChatProxy<T : Any>(
    clazz: Class<out T>,
    val api: ChatClient,
    var model: ChatModels,
    temperature: Double = 0.5,
    private var verbose: Boolean = false,
    private val moderated: Boolean = true,
    val deserializerRetries: Int = 2,
    validation: Boolean = true
) : GPTProxyBase<T>(clazz, temperature, validation, deserializerRetries) {

    constructor(params: LinkedHashMap<String, Any?>) : this(
        clazz = params["clazz"] as Class<T>,
        api = params["api"] as ChatClient? ?: ChatClient(),
        model = (params["model"] as ChatModels?)!!,
        temperature = params["temperature"] as Double? ?: 0.7,
        verbose = params["verbose"] as Boolean? ?: false,
        moderated = params["moderated"] as Boolean? ?: true,
        deserializerRetries = params["deserializerRetries"] as Int? ?: 5,
        validation = params["validation"] as Boolean? ?: true,
    )

    override fun complete(prompt: ProxyRequest, vararg examples: RequestResponse): String {
        if (verbose) log.info(prompt.toString())
        var request = ChatRequest()
        val exampleMessages = examples.flatMap {
            listOf(
                ChatMessage(
                    ApiModel.Role.user,
                    argsToString(it.argList).toContentList()
                ),
                ChatMessage(
                    ApiModel.Role.assistant,
                    it.response.toContentList()
                )
            )
        }
        request = request.copy(
            messages = ArrayList(
                listOf(
                    ChatMessage(
                        ApiModel.Role.system, """
                |You are a JSON-RPC Service
                |Responses are in JSON format
                |Do not include explaining text outside the JSON
                |All input arguments are optional
                |Outputs are based on inputs, with any missing information filled randomly
                |You will respond to the following method
                |
                |${prompt.apiYaml}
                |""".trimMargin().trim().toContentList()
                    )
                ) +
                        exampleMessages +
                        listOf(
                            ChatMessage(
                                ApiModel.Role.user,
                                argsToString(prompt.argList).toContentList()
                            )
                        )
            )
        )
        request = request.copy(model = model.modelName)
        request = request.copy(temperature = temperature)
        val json = toJson(request)
        if (moderated) api.moderate(json)

        val completion = api.chat(request, model).choices.first().message?.content.orEmpty()
        if (verbose) log.info(completion)
        val trimPrefix = trimPrefix(completion)
        val trimSuffix = trimSuffix(trimPrefix.first)
        return trimSuffix.first
    }

    companion object {

        private val log = org.slf4j.LoggerFactory.getLogger(ChatProxy::class.java)
        private fun trimPrefix(completion: String): Pair<String, String> {
            val start = completion.indexOf('{').coerceAtMost(completion.indexOf('['))
            return if (start < 0) {
                completion to ""
            } else {
                val substring = completion.substring(start)
                substring to completion.substring(0, start)
            }
        }

        private fun trimSuffix(completion: String): Pair<String, String> {
            val end = completion.lastIndexOf('}').coerceAtLeast(completion.lastIndexOf(']'))
            return if (end < 0) {
                completion to ""
            } else {
                val substring = completion.substring(0, end + 1)
                substring to completion.substring(end + 1)
            }
        }

        private fun argsToString(argList: Map<String, String>) =
            "{" + argList.entries.joinToString(",\n", transform = { (argName, argValue) ->
                """"$argName": $argValue"""
            }) + "}"
    }
}