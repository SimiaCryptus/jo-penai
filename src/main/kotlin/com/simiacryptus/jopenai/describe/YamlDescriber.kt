package com.simiacryptus.jopenai.describe

import com.fasterxml.jackson.module.kotlin.isKotlinClass
import com.google.common.reflect.TypeToken
import com.simiacryptus.jopenai.describe.DescriptorUtil.componentType
import com.simiacryptus.jopenai.describe.DescriptorUtil.isArray
import com.simiacryptus.jopenai.describe.DescriptorUtil.resolveGenericType
import com.simiacryptus.jopenai.describe.TypeDescriber.Companion.primitives
import java.lang.reflect.*
import java.util.*
import kotlin.reflect.*
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType

open class YamlDescriber : TypeDescriber {
  override val markupLanguage: String
    get() = "yaml"

  override fun describe(
    rawType: Class<in Nothing>,
    stackMax: Int,
  ): String {
    if (isAbbreviated(rawType) || stackMax <= 0) return """
            |type: object
            |class: ${rawType.name}
            """.trimMargin()
    val propertiesYaml = if (rawType.isKotlinClass()) {
      rawType.kotlin.memberProperties.filter { it.visibility == KVisibility.PUBLIC }.map {
        val description =
          DescriptorUtil.getAllAnnotations(rawType, it).find { x -> x is Description } as? Description
        if (description != null) {
          """
                    |${it.name}:
                    |  description: ${description.value.trim()}
                    |  ${toYaml(it.returnType.javaType, stackMax - 1).replace("\n", "\n  ")}
                    """.trimMargin().trim()
        } else {
          """
                    |${it.name}:
                    |  ${toYaml(it.returnType.javaType, stackMax - 1).replace("\n", "\n  ")}
                    """.trimMargin().trim()
        }
      }.toTypedArray()
    } else {
      rawType.declaredFields.filter { Modifier.isPublic(it.modifiers) }.map {
        val description =
          it.annotations.find { x -> x is Description } as? Description
        return@map if (description != null) """
                |${it.name}:
                |  description: ${description.value.trim()}
                |  ${toYaml(it.genericType, stackMax - 1).replace("\n", "\n  ")}
                """.trimIndent()
        else
          """
                |${it.name}:
                |  ${toYaml(it.genericType, stackMax - 1).replace("\n", "\n  ")}
                """.trimMargin().trim()
      }.toTypedArray()
    }
    val methodsYaml = if (rawType.isKotlinClass()) {
      rawType.kotlin.functions.filter {
        it.visibility == KVisibility.PUBLIC
            && !methodBlacklist.contains(it.name)
            && !it.isOperator && !it.isInfix && !it.isAbstract
      }.map {
        """
            |${it.name}:
            |  ${describe(it, rawType.kotlin, stackMax - 1, false).replace("\n", "\n  ")}
            """.trimMargin().trim()
      }.toTypedArray()
    } else {
      if (includeMethods) {
        rawType.methods
          .filter {
            Modifier.isPublic(it.modifiers) && !it.isSynthetic && !it.name.contains("$") && !methodBlacklist.contains(
              it.name
            )
          }
          .map {
            """
                |${it.name}:
                |  ${describe(it, rawType, stackMax - 1).replace("\n", "\n  ")}
                """.trimMargin().trim()
          }.toTypedArray()
      } else {
        arrayOf()
      }
    }
    if (propertiesYaml.isEmpty() && methodsYaml.isEmpty()) return """
            |type: object
            |class: ${rawType.name}
            """.trimMargin()
    if (propertiesYaml.isEmpty()) return """
            |type: object
            |class: ${rawType.name}
            |methods:
            |  ${methodsYaml.joinToString("\n").replace("\n", "\n  ")}
            """.trimMargin()
    if (methodsYaml.isEmpty()) return """
            |type: object
            |class: ${rawType.name}
            |properties:
            |  ${propertiesYaml.joinToString("\n").replace("\n", "\n  ")}
            """.trimMargin()
    return """
            |type: object
            |class: ${rawType.name}
            |properties:
            |  ${propertiesYaml.joinToString("\n").replace("\n", "\n  ")}
            |methods:
            |  ${methodsYaml.joinToString("\n").replace("\n", "\n  ")}
            """.trimMargin()
  }

  open val includeMethods: Boolean = true
  override val methodBlacklist = setOf(
    "equals",
    "hashCode",
    "copy",
    "toString",
    "valueOf",
    "wait",
    "notify",
    "notifyAll",
    "getClass",
    "invokeMethod"
  )

  override fun describe(self: Method, clazz: Class<*>?, stackMax: Int): String {
    if (stackMax <= 0) return "..."
    // If implClass is a Kotlin class, resolve the KFunction and call the other describe method
    if (clazz != null && clazz.isKotlinClass()) {
      val function = clazz.kotlin.functions.find { it.name == self.name }
      if (function != null) {
        return describe(function, clazz.kotlin, stackMax)
      }
    }
    val parameterYaml = self.parameters.map { toYaml(it, stackMax - 1) }.toTypedArray().joinToString("\n").trim()
    val returnTypeYaml = toYaml(self.genericReturnType, stackMax - 1).trim()
    val description = self.annotations.find { x -> x is Description } as? Description
    val responseYaml = """
        |responses:
        |  application/json:
        |    schema:
        |      ${returnTypeYaml.replace("\n", "\n      ")}
        """.trimMargin().trim().filterEmptyLines()
    val buffer = StringBuffer()
    buffer.append("operationId: ${self.name}\n")
    if (description != null) {
      buffer.append("description: ${description.value.trim()}\n")
    }
    if (parameterYaml.isNotBlank()) {
      buffer.append("parameters:\n  ${parameterYaml.replace("\n", "\n  ")}\n")
    }
    buffer.append("$responseYaml\n")
    return buffer.toString()
  }

  private fun toYaml(self: Parameter, stackMax: Int): String {
    if (stackMax <= 0) return "..."
    val description = self.getAnnotation(Description::class.java)?.value?.trim()
      ?.let { "description: " + it.replace("\n", "\\n") } ?: ""
    return """
        |- name: ${self.name}
        |  ${description}
        |  ${toYaml(self.parameterizedType, stackMax - 1).replace("\n", "\n  ")}
        |""".trimMargin().trim().filterEmptyLines()
  }

  private fun describe(self: KFunction<*>, concreteClass: KClass<*>, stackMax: Int, includeOperationID : Boolean = true): String {
    if (stackMax <= 0) return "..."
    val parameterYaml = self.parameters.filter { it.name != null }
      .map { toYaml(it, concreteClass, stackMax - 1) }.toTypedArray().joinToString("\n").trim()
    val returnTypeYaml = toYaml(self.returnType, stackMax - 1).trim()
    val description = (self.annotations.find { x -> x is Description } as? Description)
      ?.let { "description: ${it.value.trim().replace("\n", "\\n")}" } ?: ""
    val operationID = if (includeOperationID) "operationId: ${self.name}" else ""
    return """
      |${operationID}
      |${description}
      |parameters:
      |  ${parameterYaml.replace("\n", "\n  ")}
      |responses:
      |  application/json:
      |    schema:
      |      ${returnTypeYaml.replace("\n", "\n      ")}
      """.trimMargin().filterEmptyLines()
  }

  private fun toYaml(self: KParameter, concreteClass: KClass<*>, stackMax: Int): String {
    if (stackMax <= 0) return "..."
    val kType = resolveGenericType(concreteClass, self.type)
    val description = (self.annotations.find { it is Description } as? Description)?.value?.trim()
      ?.let { "description: " + it.replace("\n", "\\n") } ?: ""
    val defaultValueInfo = if (self.isOptional) "required: false" else "required: true"
    return """
      |- name: ${self.name}
      |  ${description}
      |  ${toYaml(kType, stackMax - 1).replace("\n", "\n  ")}
      |  ${defaultValueInfo}
      |""".trimMargin().trim().filterEmptyLines()
  }

  private fun toYaml(self: Type, stackMax: Int): String {
    if (isAbbreviated(self) || stackMax <= 0) return """
      |type: object
      |class: ${self.typeName}
      """.trimMargin().filterEmptyLines()
    val typeName = self.typeName.substringAfterLast('.').replace('$', '.').lowercase(Locale.getDefault())
    return if (typeName in primitives) {
      "type: $typeName"
    } else if (self is ParameterizedType && List::class.java.isAssignableFrom(self.rawType as Class<*>)) {
      """
      |type: array
      |items:
      |  ${toYaml(self.actualTypeArguments[0], stackMax - 1).replace("\n", "\n  ")}
      |""".trimMargin().filterEmptyLines()
    } else if (self is ParameterizedType && Map::class.java.isAssignableFrom(self.rawType as Class<*>)) {
      """
      |type: map
      |keys:
      |  ${toYaml(self.actualTypeArguments[0], stackMax - 1).replace("\n", "\n  ")}
      |values:
      |  ${toYaml(self.actualTypeArguments[1], stackMax - 1).replace("\n", "\n  ")}
      |""".trimMargin().filterEmptyLines()
    } else if (self.isArray) {
      """
      |type: array
      |items:
      |  ${toYaml(self.componentType!!, stackMax - 1).replace("\n", "\n  ")}
      |""".trimMargin().filterEmptyLines()
    } else {
      describe(TypeToken.of(self).rawType, stackMax)
    }
  }

  private fun toYaml(self: KType, stackMax: Int): String {
    if (isAbbreviated(self.javaType) || stackMax <= 0) return """
      |type: object
      |class: $self
      """.trimMargin().filterEmptyLines()
    val typeName = self.toString().substringAfterLast('.').replace('$', '.').lowercase(Locale.getDefault())
    return if (typeName in primitives) {
      "type: $typeName"
    } else if (self is ParameterizedType && List::class.java.isAssignableFrom(self.rawType as Class<*>)) {
      """
      |type: array
      |items:
      |  ${toYaml(self.actualTypeArguments[0], stackMax - 1).replace("\n", "\n  ")}
      |""".trimMargin().filterEmptyLines()
    } else if (self is ParameterizedType && Map::class.java.isAssignableFrom(self.rawType as Class<*>)) {
      """
      |type: map
      |keys:
      |  ${toYaml(self.actualTypeArguments[0], stackMax - 1).replace("\n", "\n  ")}
      |values:
      |  ${toYaml(self.actualTypeArguments[1], stackMax - 1).replace("\n", "\n  ")}
      |""".trimMargin().filterEmptyLines()
    } else if (self.javaType.isArray) {
      """
      |type: array
      |items:
      |  ${toYaml(self.javaType.componentType!!, stackMax - 1).replace("\n", "\n  ")}
      |""".trimMargin().filterEmptyLines()
    } else {
      describe(TypeToken.of(self.javaType).rawType, stackMax)
    }
  }

}

fun String.filterEmptyLines() = this.split("\n").filter { it.isNotBlank() }.joinToString("\n")
