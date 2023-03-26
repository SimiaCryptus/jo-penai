package com.simiacryptus.openai.proxy

import com.simiacryptus.openai.ChatMessage
import com.simiacryptus.openai.ChatRequest
import com.simiacryptus.openai.OpenAIClient
import org.slf4j.event.Level
import java.util.concurrent.atomic.AtomicInteger

@Suppress("MemberVisibilityCanBePrivate")
class ChatProxy<T : Any>(
    clazz: Class<T>,
    apiKey: String,
    var model: String = "gpt-3.5-turbo",
    var maxTokens: Int = 3500,
    temperature: Double = 0.7,
    var verbose: Boolean = false,
    private val moderated: Boolean = true,
    base: String = "https://api.openai.com/v1",
    apiLog: String? = null,
    logLevel: Level = Level.INFO,
    val deserializerRetries: Int = 5
) : GPTProxyBase<T>(clazz, apiLog, deserializerRetries, temperature) {
    override val metrics : Map<String, Any>
        get() = hashMapOf(
            "totalInputLength" to totalInputLength.get(),
            "totalOutputLength" to totalOutputLength.get(),
            "totalNonJsonPrefixLength" to totalNonJsonPrefixLength.get(),
            "totalNonJsonSuffixLength" to totalNonJsonSuffixLength.get(),
            "totalYamlLength" to totalYamlLength.get(),
            "totalExamplesLength" to totalExamplesLength.get(),
        ) + super.metrics + api.metrics
    protected val totalNonJsonPrefixLength = AtomicInteger(0)
    protected val totalNonJsonSuffixLength = AtomicInteger(0)
    protected val totalInputLength = AtomicInteger(0)
    protected val totalYamlLength = AtomicInteger(0)
    protected val totalExamplesLength = AtomicInteger(0)
    protected val totalOutputLength = AtomicInteger(0)

    val api: OpenAIClient

    init {
        api = OpenAIClient(base, apiKey, logLevel)
    }

    override fun complete(prompt: ProxyRequest, vararg examples: RequestResponse): String {
        if (verbose) println(prompt)
        val request = ChatRequest()
        totalYamlLength.addAndGet(prompt.apiYaml.length)
        val exampleMessages = examples.flatMap {
            listOf(
                ChatMessage(
                    ChatMessage.Role.user,
                    argsToString(it.argList)
                ),
                ChatMessage(
                    ChatMessage.Role.assistant,
                    it.response
                )
            )
        }
        totalExamplesLength.addAndGet(toJson(exampleMessages).length)
        request.messages = (
                listOf(
                    ChatMessage(
                        ChatMessage.Role.system, """
                |You are a JSON-RPC Service
                |Responses are in JSON format
                |Do not include explaining text outside the JSON
                |All input arguments are optional
                |Outputs are based on inputs, with any missing information filled randomly
                |You will respond to the following method
                |
                |${prompt.apiYaml}
                |""".trimMargin().trim()
                    )
                ) +
                        exampleMessages +
                        listOf(
                            ChatMessage(
                                ChatMessage.Role.user,
                                argsToString(prompt.argList)
                            )
                        )
                ).toTypedArray()
        request.model = model
        request.max_tokens = maxTokens
        request.temperature = temperature
        val json = toJson(request)
        if (moderated) api.moderate(json)
        totalInputLength.addAndGet(json.length)

        val completion = api.chat(request).response.get().toString()
        if (verbose) println(completion)
        totalOutputLength.addAndGet(completion.length)
        val trimPrefix = trimPrefix(completion)
        val trimSuffix = trimSuffix(trimPrefix.first)
        totalNonJsonPrefixLength.addAndGet(trimPrefix.second.length)
        totalNonJsonSuffixLength.addAndGet(trimSuffix.second.length)
        return trimSuffix.first
    }

    companion object {
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