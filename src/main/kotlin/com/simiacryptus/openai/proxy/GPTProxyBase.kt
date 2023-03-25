package com.simiacryptus.openai.proxy

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.isKotlinClass
import com.google.common.reflect.TypeToken
import org.slf4j.Logger
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.lang.reflect.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.pow
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType


abstract class GPTProxyBase<T : Any>(
    val clazz: Class<T>,
    apiLogFile: String?,
    private val deserializerRetries: Int = 5,
    var temperature: Double = 0.7
) {

    open val metrics : Map<String, Any>
        get() = hashMapOf(
            "requests" to requestCounter.get(),
            "attempts" to attemptCounter.get(),
        ) + requestCounters.mapValues { it.value.get() }.mapKeys { "requests.${it.key}" }
    protected val requestCounter = AtomicInteger(0)
    protected val attemptCounter = AtomicInteger(0)
    private val requestCounters = HashMap<String, AtomicInteger>()


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
                for (retry in 0 until deserializerRetries) {
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
                        if (obj is ValidatedObject && !obj.validate()) {
                            log.warn("Invalid response: $result")
                            continue
                        }
                        return@newProxyInstance obj
                    } catch (e: Exception) {
                        log.warn("Failed to parse response: $result", e)
                        lastException = e
                        log.info("Retry $retry of $deserializerRetries")
                    }
                }
                throw RuntimeException("Failed to parse response", lastException)
            } finally {
                temperature = originalTemp
            }
        } as T
    }


    private val apiLog = apiLogFile?.let { openApiLog(it) }
    private val examples = HashMap<String, MutableList<RequestResponse>>()
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
            ) { proxy: Any, method: Method, args: Array<Any> ->
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

    open fun toJson(data: Any): String {
        return objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(data)
    }

    open fun <T> fromJson(data: String, type: Type): T {
        if (type is Class<*> && type.isAssignableFrom(String::class.java)) return data as T
        val value = objectMapper().readValue(data, objectMapper().typeFactory.constructType(type)) as T
        //log.debug("Deserialized $data to $value")
        return value
    }

    open fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .enable(JsonParser.Feature.ALLOW_COMMENTS)
            .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
            .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
//            .disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
//            .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
//            .disable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
            .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS.mappedFeature())
            .enable(JsonReadFeature.ALLOW_YAML_COMMENTS.mappedFeature())
            .enable(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature())
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature())
            .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature())
            .enable(JsonReadFeature.ALLOW_MISSING_VALUES.mappedFeature())
            .enable(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS.mappedFeature())
            .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature())
            .enable(JsonReadFeature.ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS.mappedFeature())
            .enable(JsonReadFeature.ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS.mappedFeature())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
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

        fun Parameter.toYaml(): String {
            val description = getAnnotation(Description::class.java)?.value
            val yaml = if (description != null) {
                val yamlEscapedDescription = description.replace("\n", """\n""")
                """
                |- name: ${this.name}
                |  description: $yamlEscapedDescription
                |  ${this.parameterizedType.toYaml().replace("\n", "\n  ")}
                |""".trimMargin().trim()
            } else {
                """
                |- name: ${this.name}
                |  ${this.parameterizedType.toYaml().replace("\n", "\n  ")}
                |""".trimMargin().trim()
            }
            return yaml
        }


        fun Type.toYaml(): String {
            val typeName = this.typeName.substringAfterLast('.').replace('$', '.').toLowerCase()
            val primitives = setOf(
                "boolean",
                "integer",
                "number",
                "string",
                "double",
                "float",
                "long",
                "short",
                "byte",
                "char",
                "object"
            )
            val yaml = if (typeName in primitives) {
                "type: $typeName"
            } else if (this is ParameterizedType && List::class.java.isAssignableFrom(this.rawType as Class<*>)) {
                """
                |type: array
                |items:
                |  ${this.actualTypeArguments[0].toYaml().replace("\n", "\n  ")}
                |""".trimMargin()
            } else if (this is ParameterizedType && Map::class.java.isAssignableFrom(this.rawType as Class<*>)) {
                """
                |type: map
                |keys:
                |  ${this.actualTypeArguments[0].toYaml().replace("\n", "\n  ")}
                |values:
                |  ${this.actualTypeArguments[1].toYaml().replace("\n", "\n  ")}
                |""".trimMargin()
            } else if (this.isArray) {
                """
                |type: array
                |items:
                |  ${this.componentType?.toYaml()?.replace("\n", "\n  ")}
                |""".trimMargin()
            } else {
                val rawType = TypeToken.of(this).rawType
                val propertiesYaml = if (rawType.isKotlinClass() && rawType.kotlin.isData) {
                    rawType.kotlin.memberProperties.map {
                        val description = getAllAnnotations(rawType, it).find { x -> x is Description } as? Description
                        // Find annotation on the kotlin data class constructor parameter
                        val yaml = if (description != null) {
                            """
                            |${it.name}:
                            |  description: ${description.value}
                            |  ${it.returnType.javaType.toYaml().replace("\n", "\n  ")}
                            """.trimMargin().trim()
                        } else {
                            """
                            |${it.name}:
                            |  ${it.returnType.javaType.toYaml().replace("\n", "\n  ")}
                            """.trimMargin().trim()
                        }
                        yaml
                    }.toTypedArray()
                } else {
                    rawType.declaredFields.map {
                        """
                        |${it.name}:
                        |  ${it.genericType.toYaml().replace("\n", "\n  ")}
                        """.trimMargin().trim()
                    }.toTypedArray()
                }
                val fieldsYaml = propertiesYaml.toList().joinToString("\n")
                """
                |type: object
                |properties:
                |  ${fieldsYaml.replace("\n", "\n  ")}
                """.trimMargin()
            }
            return yaml
        }

        private fun getAllAnnotations(
            rawType: Class<in Nothing>,
            property: KProperty1<out Any, *>
        ) =
            property.annotations + (rawType.kotlin.constructors.first().parameters.find { x -> x.name == property.name }?.annotations
                ?: listOf())

        fun Method.toYaml(): String {
            val parameterYaml = parameters.map { it.toYaml() }.toTypedArray().joinToString("\n").trim()
            val returnTypeYaml = genericReturnType.toYaml().trim()
            val description = annotations.find { x -> x is Description } as? Description
            val responseYaml = """
                                      |responses:
                                      |  application/json:
                                      |    schema:
                                      |      ${returnTypeYaml.replace("\n", "\n      ")}
                                      """.trimMargin().trim()
            val yaml = if (description != null) {
                """
                |operationId: $name
                |description: ${description.value}
                |parameters:
                |  ${parameterYaml.replace("\n", "\n  ")}
                |$responseYaml
                """.trimMargin()
            } else {
                """
                |operationId: $name
                |parameters:
                |  ${parameterYaml.replace("\n", "\n  ")}
                |$responseYaml
                """.trimMargin()
            }

            return yaml
        }

        val Type.isArray: Boolean
            get() {
                return this is Class<*> && this.isArray
            }

        val Type.componentType: Type?
            get() {
                return when (this) {
                    is Class<*> -> if (this.isArray) this.componentType else null
                    is ParameterizedType -> this.actualTypeArguments.firstOrNull()
                    else -> null
                }
            }
    }

}

