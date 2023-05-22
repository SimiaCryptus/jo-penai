package com.simiacryptus.util

import com.fasterxml.jackson.module.kotlin.isKotlinClass
import com.google.common.reflect.TypeToken
import com.simiacryptus.openai.proxy.Description
import com.simiacryptus.util.DescriptorUtil.componentType
import com.simiacryptus.util.DescriptorUtil.isArray
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType

open class YamlDescriber() : TypeDescriber {

    override fun describe(
        rawType: Class<in Nothing>,
        stackMax: Int,
    ): String {
        if (isAbbreviated(rawType.name))
            return """
            |type: object
            |class: ${rawType.name}
            """.trimMargin()
        val propertiesYaml = if (rawType.isKotlinClass() && rawType.kotlin.isData) {
            rawType.kotlin.memberProperties.map {
                val description =
                    DescriptorUtil.getAllAnnotations(rawType, it).find { x -> x is Description } as? Description
                // Find annotation on the kotlin data class constructor parameter
                val yaml = if (description != null) {
                    """
                    |${it.name}:
                    |  description: ${description.value}
                    |  ${toYaml(it.returnType.javaType, stackMax - 1).replace("\n", "\n  ")}
                    """.trimMargin().trim()
                } else {
                    """
                    |${it.name}:
                    |  ${toYaml(it.returnType.javaType, stackMax - 1).replace("\n", "\n  ")}
                    """.trimMargin().trim()
                }
                yaml
            }.toTypedArray()
        } else {
            rawType.declaredFields.map {
                """
                |${it.name}:
                |  ${toYaml(it.genericType, stackMax - 1).replace("\n", "\n  ")}
                """.trimMargin().trim()
            }.toTypedArray()
        }
        var fieldsYaml = propertiesYaml.toList().joinToString("\n")
        if (fieldsYaml.isBlank()) fieldsYaml = "{}"
        return """
            |type: object
            |class: ${rawType.name}
            |properties:
            |  ${fieldsYaml.replace("\n", "\n  ")}
            """.trimMargin()
    }

    override fun describe(self: Method, stackMax: Int): String {
        if (stackMax <= 0) return "..."
        val parameterYaml = self.parameters.map { toYaml(it, stackMax - 1) }.toTypedArray().joinToString("\n").trim()
        val returnTypeYaml = toYaml(self.genericReturnType, stackMax - 1).trim()
        val description = self.annotations.find { x -> x is Description } as? Description
        val responseYaml = """
            |responses:
            |  application/json:
            |    schema:
            |      ${returnTypeYaml.replace("\n", "\n      ")}
            """.trimMargin().trim()
        return if (description != null) {
            """
            |operationId: ${self.name}
            |description: ${description.value}
            |parameters:
            |  ${parameterYaml.replace("\n", "\n  ")}
            |$responseYaml
            """.trimMargin()
        } else {
            """
            |operationId: ${self.name}
            |parameters:
            |  ${parameterYaml.replace("\n", "\n  ")}
            |$responseYaml
            """.trimMargin()
        }
    }

    protected open val primitives = setOf(
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

    protected open fun isAbbreviated(name: String) = false

    private fun toYaml(self: Parameter, stackMax: Int = 10): String {
        if (stackMax <= 0) return "..."
        val description = self.getAnnotation(Description::class.java)?.value
        return if (description != null) {
            """
            |- name: ${self.name}
            |  description: ${description.replace("\n", "\\n")}
            |  ${toYaml(self.parameterizedType, stackMax - 1).replace("\n", "\n  ")}
            |""".trimMargin().trim()
        } else {
            """
            |- name: ${self.name}
            |  ${toYaml(self.parameterizedType, stackMax - 1).replace("\n", "\n  ")}
            |""".trimMargin().trim()
        }
    }

    private fun toYaml(self: Type, stackMax: Int = 10): String {
        if (stackMax <= 0) return "..."
        val typeName = self.typeName.substringAfterLast('.').replace('$', '.').lowercase(Locale.getDefault())
        return if (typeName in primitives) {
            "type: $typeName"
        } else if (self is ParameterizedType && List::class.java.isAssignableFrom(self.rawType as Class<*>)) {
            """
            |type: array
            |items:
            |  ${toYaml(self.actualTypeArguments[0], stackMax - 1).replace("\n", "\n  ")}
            |""".trimMargin()
        } else if (self is ParameterizedType && Map::class.java.isAssignableFrom(self.rawType as Class<*>)) {
            """
            |type: map
            |keys:
            |  ${toYaml(self.actualTypeArguments[0], stackMax - 1).replace("\n", "\n  ")}
            |values:
            |  ${toYaml(self.actualTypeArguments[1], stackMax - 1).replace("\n", "\n  ")}
            |""".trimMargin()
        } else if (self.isArray) {
            """
            |type: array
            |items:
            |  ${toYaml(self.componentType!!, stackMax - 1).replace("\n", "\n  ")}
            |""".trimMargin()
        } else {
            describe(TypeToken.of(self).rawType, stackMax)
        }
    }


}