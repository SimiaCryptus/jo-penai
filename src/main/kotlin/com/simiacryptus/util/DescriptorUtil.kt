@file:Suppress("unused")

package com.simiacryptus.util

import com.fasterxml.jackson.module.kotlin.isKotlinClass
import com.google.common.reflect.TypeToken
import com.simiacryptus.openai.proxy.Description
import java.lang.reflect.Method
import java.lang.reflect.Parameter
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KProperty1
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType

object DescriptorUtil {

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
            var fieldsYaml = propertiesYaml.toList().joinToString("\n")
            if (fieldsYaml.isBlank()) fieldsYaml = "{}"
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
        property: KProperty1<out Any, *>,
    ) =
        property.annotations + (rawType.kotlin.constructors.first().parameters.find { x -> x.name == property.name }?.annotations
            ?: listOf())

    fun Method.toYaml(newFormat: Boolean = false): String {
        val parameterYaml = parameters.map { it.toYaml() }.toTypedArray().joinToString("\n").trim()
        val returnTypeYaml = genericReturnType.toYaml().trim()
        val description = annotations.find { x -> x is Description } as? Description
        val responseYaml = if (newFormat) {
            """
            |responses:
            |  default:
            |    description: Successful operation
            |    schema:
            |      ${returnTypeYaml.replace("\n", "\n      ")}
            """.trimMargin().trim()
        } else {
            """
            |responses:
            |  application/json:
            |    schema:
            |      ${returnTypeYaml.replace("\n", "\n      ")}
            """.trimMargin().trim()
        }
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