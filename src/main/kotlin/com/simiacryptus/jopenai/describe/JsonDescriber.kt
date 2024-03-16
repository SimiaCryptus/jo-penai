package com.simiacryptus.jopenai.describe

import com.fasterxml.jackson.module.kotlin.isKotlinClass
import org.slf4j.LoggerFactory
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import kotlin.reflect.KVisibility
import kotlin.reflect.full.functions
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaMethod

open class JsonDescriber(
  private val whitelist: MutableSet<String> = setOf(
    "com.simiacryptus",
    "com.github.simiacryptus"
  ).toMutableSet()
) : TypeDescriber() {
  companion object {
    val log = LoggerFactory.getLogger(JsonDescriber::class.java)
  }

  override val markupLanguage: String
    get() = "json"

  override fun describe(
    rawType: Class<in Nothing>,
    stackMax: Int,
    describedTypes: MutableSet<String>
  ): String {
    if (!whitelist.contains(rawType.name)) {
      return """{
                 "type": "object",
                 "class": "${rawType.name}",
                 "allowed": false
               }""".trimIndent()
    }
    if (!describedTypes.add(rawType.name) && rawType.name !in primitives) {
//      log.debug("Preventing recursion for type: ${rawType.name}")
      return "{...}"
    }
//    log.debug("Describing type: ${rawType.name} with stackMax: $stackMax")
    if (isAbbreviated(rawType) || stackMax <= 0) return """{
            {
              "type": "object",
              "class": "${rawType.name}"
            }
            """.trimIndent()
    val propertiesJson = if (rawType.isKotlinClass()) {
      rawType.kotlin.memberProperties.filter { it.visibility == KVisibility.PUBLIC }.joinToString(",\n") {
        val description =
          DescriptorUtil.getAllAnnotations(rawType, it).find { x -> x is Description } as? Description
        val propertyDescription = if (description != null) """
                    "${it.name}": {
                      "description": "${description.value.trim()}",
                      "type": "${it.returnType.classifier}"
                    }
                    """.trimIndent().trim() else """
                    "${it.name}": {
                      "type": "${it.returnType.classifier}"
                    }
                    """.trimIndent().trim()
        propertyDescription
      }
    } else {
      rawType.declaredFields.filter { Modifier.isPublic(it.modifiers) }.joinToString(",\n") {
        val description =
          it.annotations.find { x -> x is Description } as? Description
        val fieldDescription = if (description != null) """
                "${it.name}": {
                  "description": "${description.value.trim()}",
                 "type": "${it.type.simpleName.toLowerCase()}"
                }
                """.trimIndent() else """
                "${it.name}": {
                 "type": "${it.type.simpleName.toLowerCase()}"
                }
                """.trimIndent()
        fieldDescription
      }
    }
    val methodsJson = (if (rawType.isKotlinClass()) {
      rawType.kotlin.functions.filter {
        it.visibility == KVisibility.PUBLIC
            && !methodBlacklist.contains(it.name)
            && !it.isOperator && !it.isInfix && !it.isAbstract
      }.joinToString(",\n") {
        """
            "${it.name}": {
              ${describe(it.javaMethod!!, rawType.kotlin.java, stackMax - 1).replace("\n", "\n  ")}
            }
            """.trimIndent().trim()
      }
    } else {
      if (includeMethods) {
        rawType.methods
          .filter {
            Modifier.isPublic(it.modifiers) && !it.isSynthetic && !it.name.contains("$") && !methodBlacklist.contains(
              it.name
            )
          }
          .joinToString(",\n") {
            """
                "${it.name}": {
                  ${describe(it, rawType, stackMax - 1).replace("\n", "\n  ")}
                }
                """.trimIndent().trim()
          }
      } else {
        ""
      }
    }).ifEmpty { "" }
    val jsonBody = StringBuilder()
    jsonBody.append(
      """
            {
              "type": "object",
              "class": "${rawType.name}",
            """.trimIndent()
    )
    if (propertiesJson.isNotEmpty()) {
      jsonBody.append(
        """
              "properties": {
                $propertiesJson
              },
            """.trimIndent()
      )
    }
    if (methodsJson.isNotEmpty()) {
      jsonBody.append(
        """
              "methods": {
                $methodsJson
              }
            """.trimIndent()
      )
    }
    jsonBody.append("\n}")
    return jsonBody.toString()
  }

  override fun describe(self: Method, clazz: Class<*>?, stackMax: Int): String {
    val returnType = self.returnType
    clazz ?: return "..."
    val description = getAllAnnotations(clazz, self).find { x -> x is Description } as? Description
    val methodDescription = if (description != null) """
            "description": "${description.value.trim()}",
            ${describe(returnType, stackMax, mutableSetOf()).replace("\n", "\n  ")}
            """.trimIndent().trim() else """
            ${describe(returnType, stackMax, mutableSetOf()).replace("\n", "\n  ")}
            """.trimIndent()
    return """
            {
              "type": "method",
              "class": "${clazz?.name ?: "unknown"}",
              "name": "${self.name}",
              $methodDescription
            }
            """.trimIndent()
  }

  private fun getAllAnnotations(clazz: Class<*>, self: Method): List<Annotation> {
    return (self.annotations + (clazz.kotlin.constructors.firstOrNull()?.parameters?.find { x -> x.name == self.name }?.annotations
      ?: listOf())
        ).toList()
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

}

