package com.simiacryptus.jopenai.describe

import com.fasterxml.jackson.module.kotlin.isKotlinClass
import com.google.common.reflect.TypeToken
import com.simiacryptus.jopenai.describe.DescriptorUtil.componentType
import com.simiacryptus.jopenai.describe.DescriptorUtil.getAllAnnotations
import com.simiacryptus.jopenai.describe.DescriptorUtil.isArray
import com.simiacryptus.jopenai.describe.DescriptorUtil.resolveGenericType
import com.simiacryptus.util.DynamicEnum
import org.slf4j.LoggerFactory
import java.lang.reflect.*
import java.util.*
import kotlin.reflect.*
import kotlin.reflect.full.functions
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.full.memberProperties
import kotlin.reflect.jvm.javaType

open class YamlDescriber : TypeDescriber() {
    companion object {
        val log = LoggerFactory.getLogger(YamlDescriber::class.java)
    }
    init {
        log.info("YamlDescriber initialized with markupLanguage: $markupLanguage")
    }


    override val markupLanguage: String
        get() = "yaml"

    override fun describe(
        rawType: Class<in Nothing>,
        stackMax: Int,
        describedTypes: MutableSet<String>
    ): String {
//        log.debug("Entering describe method with rawType: ${rawType.name}, stackMax: $stackMax")
//        log.info("Starting description for type: ${rawType.name} with stackMax: $stackMax")
        if (!describedTypes.add(rawType.name) && rawType.simpleName.lowercase() !in primitives) {
//            log.debug("Preventing recursion for type: ${rawType.name}")
            return "..."
        } else if (rawType.simpleName.lowercase() in primitives) {
            return "type: ${rawType.simpleName.lowercase()}"
        }
//        log.info("Describing type: ${rawType.name} with stackMax: $stackMax")
        if (isAbbreviated(rawType) || stackMax <= 0) return """
type: object
class: ${rawType.name}
            """.trim()
        if (rawType.isEnum || DynamicEnum::class.java.isAssignableFrom(rawType)) {
//            log.debug("Type is an enumeration: ${rawType.name}")
            return """
type: enumeration
values:
${getEnumValues(rawType).joinToString("\n") { "  - $it" }}
                """.trim()
        }
        val propertiesYaml = if (rawType.isKotlinClass()) {
            rawType.kotlin.memberProperties.filter { it.visibility == KVisibility.PUBLIC }.map {
                val description =
                    getAllAnnotations(rawType, it).filterIsInstance<Description>().firstOrNull()
                if (description != null) {
                    """
${it.name}:
  description: "${description.value.trim().replace("\"", "\\\"")}"
  ${toYaml(it.returnType.javaType, stackMax - 1, describedTypes).replace("\n", "\n  ")}
                    """.trim()
                } else {
                    """
${it.name}:
  ${toYaml(it.returnType.javaType, stackMax - 1, describedTypes).replace("\n", "\n  ")}
                    """.trim()
                }
            }.toTypedArray()
        } else {
            rawType.declaredFields.filter { Modifier.isPublic(it.modifiers) }.map {
                val description =
                    it.annotations.find { x -> x is Description } as? Description
                return@map if (description != null) """
${it.name}:
  description: ${description.value.trim()}
  ${toYaml(it.genericType, stackMax - 1, describedTypes).replace("\n", "\n  ")}
                    """.trim()
                else
                    """
${it.name}:
  ${toYaml(it.genericType, stackMax - 1, describedTypes).replace("\n", "\n  ")}
                    """.trim()
            }.toTypedArray()
        }
        val methodsYaml = (if (rawType.isKotlinClass()) {
            rawType.kotlin.functions.filter {
                it.visibility == KVisibility.PUBLIC
                        && !methodBlacklist.contains(it.name)
                        && !it.isOperator && !it.isInfix && !it.isAbstract
            }.map {
                """
${it.name}:
  ${describe(it, rawType.kotlin, stackMax - 1, false, describedTypes).replace("\n", "\n  ")}
                """.trim()
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
${it.name}:
  ${describe(it, rawType, stackMax - 1).replace("\n", "\n  ")}
                        """.trim()
                    }.toTypedArray()
            } else {
                arrayOf()
            }
        }).toMutableList()
        if (!coverMethods) methodsYaml.clear()
        if (propertiesYaml.isEmpty() && methodsYaml.isEmpty()) return """
type: object
class: "${rawType.name}"
            """.trim()
        if (propertiesYaml.isEmpty()) return """
type: object
class: ${rawType.name}
methods:
  ${methodsYaml.joinToString("\n").replace("\n", "\n  ")}
            """.trim()
        if (methodsYaml.isEmpty()) return """
type: object
class: ${rawType.name}
properties:
  ${propertiesYaml.joinToString("\n").replace("\n", "\n  ")}
            """.trim()
        return """
type: object
class: ${rawType.name}
properties:
  ${propertiesYaml.joinToString("\n").replace("\n", "\n  ")}
methods:
  ${methodsYaml.joinToString("\n").replace("\n", "\n  ")}
            """.trim()
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
//        log.debug("Describing method: ${self.name} in class: ${clazz?.name}")
//        log.info("Describing method: ${self.name} in class: ${clazz?.name} with stackMax: $stackMax")
        if (stackMax <= 0) return "..."
        if (!coverMethods) return ""
        // If implClass is a Kotlin class, resolve the KFunction and call the other describe method
        if (clazz != null && clazz.isKotlinClass()) {
            val function = clazz.kotlin.functions.find { it.name == self.name }
            if (function != null) {
                return describe(function, clazz.kotlin, stackMax, true, mutableSetOf())
            }
        }
        val parameterYaml = self.parameters.map { toYaml(it, stackMax - 1) }.toTypedArray().joinToString("\n").trim()
        val returnTypeYaml = toYaml(self.genericReturnType, stackMax - 1, mutableSetOf()).trim()
        val description = self.getAnnotation(Description::class.java)?.value?.trim()?.replace("\"", "\\\"")
        val responseYaml = "responses:\n  application/json:\n    schema:\n      ${returnTypeYaml.replace("\n", "\n      ")}".trim().filterEmptyLines()
      val buffer = StringBuffer()
      buffer.append("operationId: ${self.name}\n")
      if (description != null) {
        buffer.append("description: ${description.trim()}\n")
      }
      if (parameterYaml.isNotBlank()) {
        buffer.append("parameters:\n  ${parameterYaml.replace("\n", "\n  ")}\n")
      }
      buffer.append("$responseYaml\n")
      return buffer.toString()
    }

    private fun toYaml(self: Parameter, stackMax: Int): String {
//        log.debug("Converting parameter to YAML: ${self.name}")
        if (stackMax <= 0) return "..."
        val description = self.getAnnotation(Description::class.java)?.value?.trim()
            ?.let { "description: " + it.replace("\n", "\\n") } ?: ""
        return """
- name: ${self.name}
  ${description}
  ${toYaml(self.parameterizedType, stackMax - 1, mutableSetOf()).replace("\n", "\n  ")}
""".trim().filterEmptyLines()
    }

    private fun describe(
        self: KFunction<*>,
        concreteClass: KClass<*>,
        stackMax: Int,
        includeOperationID: Boolean = true,
        describedTypes: MutableSet<String>
    ): String {
//        log.debug("Describing Kotlin function: ${self.name} in class: ${concreteClass.qualifiedName}")
//        log.info("Describing Kotlin function: ${self.name} in class: ${concreteClass.qualifiedName} with stackMax: $stackMax")
        val functionTypeRepresentation = "${concreteClass.qualifiedName}::${self.name}"
        if (describedTypes.contains(functionTypeRepresentation) && functionTypeRepresentation !in primitives) return "..."
        describedTypes.add(functionTypeRepresentation)
        if (stackMax <= 0) return "..."
        if (!coverMethods) return ""
        val parameterYaml = self.parameters.filter { it.name != null }
            .map { toYaml(it, concreteClass, stackMax - 1, describedTypes) }.toTypedArray().joinToString("\n").trim()
        val returnTypeYaml = toYaml(self.returnType, stackMax - 1).trim()
        val description = (self.annotations.find { x -> x is Description } as? Description)
            ?.let { "description: ${it.value.trim().replace("\n", "\\n")}" } ?: ""
        val operationID = if (includeOperationID) "operationId: ${self.name}" else ""
        return """
${operationID}
${description}
parameters:
  ${parameterYaml.replace("\n", "\n  ")}
responses:
  application/json:
    schema:
      ${returnTypeYaml.replace("\n", "\n      ")}
          """.filterEmptyLines().trim()
    }

    private fun toYaml(
        self: KParameter,
        concreteClass: KClass<*>,
        stackMax: Int,
        describedTypes: MutableSet<String>
    ): String {
//        log.debug("Converting KParameter to YAML: ${self.name}")
        val parameterTypeRepresentation = "${concreteClass.qualifiedName}::${self.name}/${self.type}"
        if (describedTypes.contains(parameterTypeRepresentation) && parameterTypeRepresentation !in primitives) return "..."
        describedTypes.add(parameterTypeRepresentation)
        if (stackMax <= 0) return "..."
        val kType = resolveGenericType(concreteClass, self.type)
        val description = (self.annotations.find { it is Description } as? Description)?.value?.trim()
            ?.let { "description: " + it.replace("\n", "\\n") } ?: ""
        val defaultValueInfo = if (self.isOptional) "required: false" else "required: true"
        return """
- name: ${self.name}
  ${description}
  ${toYaml(kType, stackMax - 1).replace("\n", "\n  ")}
  ${defaultValueInfo}
""".trim().filterEmptyLines()
    }


    private fun toYaml(self: Type, stackMax: Int, describedTypes: MutableSet<String>): String {
//        log.debug("Converting Type to YAML: ${self.typeName}")
        if (describedTypes.contains(self.toString())) return "..."
        describedTypes.add(self.toString())
        val typeName = self.typeName.substringAfterLast('.').replace('$', '.')
        return if ((isAbbreviated(self) || stackMax <= 0) && typeName !in primitives) """
type: object
class: ${self.typeName}
          """.filterEmptyLines().trim()
        else if (self is Class<*> && (self.isEnum || DynamicEnum::class.java.isAssignableFrom(self))) {
            val enumConstants = getEnumValues(self).joinToString("\n") { "  - $it" }
            """
type: enum
values:
$enumConstants
            """.filterEmptyLines().trim()
        } else if (typeName in primitives) {
            "type: $typeName"
        } else if (self is Class<*> && (self.isEnum || DynamicEnum::class.java.isAssignableFrom(self))) {
            val enumConstants = getEnumValues(self).joinToString("\n") { "  - $it" }
            """
type: enum
values:
$enumConstants
            """.filterEmptyLines().trim()
        } else if (self is ParameterizedType && List::class.java.isAssignableFrom(self.rawType as Class<*>)) {
            """
type: array
items:
  ${toYaml(self.actualTypeArguments[0], stackMax - 1, describedTypes).replace("\n", "\n  ")}
""".filterEmptyLines().trim()
        } else if (self is ParameterizedType && Map::class.java.isAssignableFrom(self.rawType as Class<*>)) {
            """
type: map
keys:
  ${toYaml(self.actualTypeArguments[0], stackMax - 1, describedTypes).replace("\n", "\n  ")}
values:
  ${toYaml(self.actualTypeArguments[1], stackMax - 1, describedTypes).replace("\n", "\n  ")}
""".filterEmptyLines().trim()
        } else if (self.isArray) {
            """
type: array
items:
  ${toYaml(self.componentType!!, stackMax - 1, describedTypes).replace("\n", "\n  ")}
 """.filterEmptyLines().trim()
        } else {
            describe(TypeToken.of(self).rawType, stackMax, describedTypes)
        }
    }

    private fun toYaml(self: KType, stackMax: Int): String {
        log.debug("Converting KType to YAML: ${self}")
        if (isAbbreviated(self.javaType) || stackMax <= 0) return """
type: object
class: "$self"
            """.filterEmptyLines().trim()
        val typeName = self.toString().substringAfterLast('.').replace('$', '.').lowercase(Locale.getDefault())
        return if (typeName in primitives) {
            "type: $typeName"
        } else if (self is ParameterizedType && List::class.java.isAssignableFrom(self.rawType as Class<*>)) {
            """
type: array
items:
  ${toYaml(self.actualTypeArguments[0], stackMax - 1, mutableSetOf()).replace("\n", "\n  ")}
""".filterEmptyLines().trim()
        } else if (self is ParameterizedType && Map::class.java.isAssignableFrom(self.rawType as Class<*>)) {
            """
type: map
keys:
  ${toYaml(self.actualTypeArguments[0], stackMax - 1, mutableSetOf()).replace("\n", "\n  ")}
values:  
  ${toYaml(self.actualTypeArguments[1], stackMax - 1, mutableSetOf()).replace("\n", "\n  ")}
""".filterEmptyLines().trim()
        } else if (self.classifier is KClass<*> && ((self.classifier as KClass<*>).isSubclassOf(Enum::class) || (self.classifier as KClass<*>).isSubclassOf(
                DynamicEnum::class
            ))
        ) {
            val enumConstants = getEnumValues((self.classifier as KClass<*>).java).joinToString("\n") { "  - $it" }
            """
type: enum
values:
$enumConstants
            """.filterEmptyLines()
        } else if (self.javaType.isArray) {
            """
type: array
items:
  ${toYaml(self.javaType.componentType!!, stackMax - 1, mutableSetOf()).replace("\n", "\n  ")}
            """.filterEmptyLines()
        } else {
            describe(TypeToken.of(self.javaType).rawType, stackMax)
        }
    }

    open fun getEnumValues(clazz: Class<*>): List<String> {
//        log.debug("Getting enum values for class: ${clazz.name}")
        return when {
            clazz.isEnum -> clazz.enumConstants.map { it.toString() }
            DynamicEnum::class.java.isAssignableFrom(clazz) -> {
                DynamicEnum.values(clazz as Class<out DynamicEnum<*>>).map { it.name }
            }

            else -> emptyList()
        }
    }

    private fun String.filterEmptyLines() = this.split("\n").filter { it.isNotBlank() }.joinToString("\n").trim()
}