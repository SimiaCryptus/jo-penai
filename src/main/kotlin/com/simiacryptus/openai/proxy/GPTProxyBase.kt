@file:Suppress("MemberVisibilityCanBePrivate")

package com.simiacryptus.openai.proxy

import com.simiacryptus.util.YamlDescriber.Companion.toYaml
import com.simiacryptus.util.JsonUtil.fromJson
import com.simiacryptus.util.JsonUtil.toJson
import org.slf4j.Logger
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.lang.reflect.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.pow


abstract class GPTProxyBase<T : Any>(
    val clazz: Class<T>,
    apiLogFile: String?,
    var temperature: Double = 0.7,
    var validation: Boolean = true,
    var maxRetries: Int = 5,
) {

    open val metrics : Map<String, Any>
        get() = hashMapOf(
            "requests" to requestCounter.get(),
            "attempts" to attemptCounter.get(),
        ) + requestCounters.mapValues { it.value.get() }.mapKeys { "requests.${it.key}" }
    protected val requestCounter = AtomicInteger(0)
    protected val attemptCounter = AtomicInteger(0)
    val requestCounters = HashMap<String, AtomicInteger>()


    abstract fun complete(prompt: ProxyRequest, vararg examples: RequestResponse): String

    fun create(): T {
        return Proxy.newProxyInstance(clazz.classLoader, arrayOf(clazz)) { proxy, method, args ->
            if (method.name == "toString") return@newProxyInstance clazz.simpleName
            requestCounters.computeIfAbsent(method.name) { AtomicInteger(0) }.incrementAndGet()
            val type = method.genericReturnType
            val typeString = method.toYaml().trimIndent()
            val prompt = ProxyRequest(
                method.name,
                typeString,
                (args ?: arrayOf()).zip(method.parameters)
                    .filter<Pair<Any?, Parameter>> { (arg: Any?, _) -> arg != null }.associate { (arg, param) ->
                        param.name to toJson(arg!!)
                    }
            )

            var lastException: Exception? = null
            val originalTemp = temperature
            try {
                requestCounter.incrementAndGet()
                for (retry in 0 until maxRetries) {
                    attemptCounter.incrementAndGet()
                    if (retry > 0) {
                        // Increase temperature on retry; this encourages the model to return a different result
                        temperature =
                            if (temperature <= 0.0) 0.0 else temperature.coerceAtLeast(0.1).pow(1.0 / (retry + 1))
                    }
                    var result = complete(prompt, *examples[method.name]?.toTypedArray() ?: arrayOf())
                    // If the requested `type` is a list, check that result is a list
                    if (type is ParameterizedType && List::class.java.isAssignableFrom(type.rawType as Class<*>) && !result.startsWith(
                            "["
                        )
                    ) {
                        result = "[$result]"
                    }
                    writeToJsonLog(ProxyRecord(method.name, prompt.argList, result))
                    try {
                        val obj = fromJson<Any>(result, type)
                        if (validation && obj is ValidatedObject && !obj.validate()) {
                            log.warn("Invalid response: $result")
                            continue
                        }
                        return@newProxyInstance obj
                    } catch (e: Exception) {
                        log.warn("Failed to parse response: $result", e)
                        lastException = e
                        log.info("Retry $retry of $maxRetries")
                    }
                }
                throw RuntimeException("Failed to parse response", lastException)
            } finally {
                temperature = originalTemp
            }
        } as T
    }


    private val apiLog = apiLogFile?.let { openApiLog(it) }
    val examples = HashMap<String, MutableList<RequestResponse>>()
    private fun loadExamples(file: File = File("api.examples.json")): List<ProxyRecord> {
        if (!file.exists()) return listOf()
        val json = file.readText()
        return fromJson(json, object : ArrayList<ProxyRecord>() {}.javaClass)
    }

    fun addExamples(file: File) {
        examples.putAll(loadExamples(file).groupBy { it.methodName }
            .mapValues { it.value.map { RequestResponse(it.argList, it.response) }.toMutableList() })
    }

    @Suppress("unused")
    fun <R : Any> addExample(returnValue: R, functionCall: (T) -> Unit) {
        functionCall(
            Proxy.newProxyInstance(
                clazz.classLoader,
                arrayOf(clazz)
            ) { _: Any, method: Method, args: Array<Any> ->
                if (method.name == "toString") return@newProxyInstance clazz.simpleName
                val argList = args.zip(method.parameters)
                    .filter<Pair<Any?, Parameter>> { (arg: Any?, _) -> arg != null }
                    .associate { (arg, param) ->
                        param.name to toJson(arg!!)
                    }
                val result = toJson(returnValue)
                examples.getOrPut(method.name) { ArrayList() }.add(RequestResponse(argList, result))
                return@newProxyInstance returnValue
            } as T)
    }


    private fun openApiLog(file: String): BufferedWriter {
        val writer = BufferedWriter(FileWriter(File(file)))
        writer.write("[")
        writer.newLine()
        writer.flush()
        return writer
    }

    private fun writeToJsonLog(record: ProxyRecord) {
        if (apiLog != null) {
            apiLog.write(toJson(record))
            apiLog.write(",")
            apiLog.newLine()
            apiLog.flush()
        }
    }

    data class ProxyRequest(
        val methodName: String = "",
        val apiYaml: String = "",
        val argList: Map<String, String> = mapOf()
    )

    data class ProxyRecord(
        val methodName: String = "",
        val argList: Map<String, String> = mapOf(),
        val response: String = ""
    )

    data class RequestResponse(
        val argList: Map<String, String> = mapOf(),
        val response: String
    )

    companion object {
        val log: Logger = org.slf4j.LoggerFactory.getLogger(GPTProxyBase::class.java)
    }

}

