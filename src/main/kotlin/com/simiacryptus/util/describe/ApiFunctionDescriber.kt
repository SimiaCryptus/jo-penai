package com.simiacryptus.util.describe

import com.fasterxml.jackson.module.kotlin.isKotlinClass
import com.google.common.reflect.TypeToken
import com.simiacryptus.util.describe.DescriptorUtil.componentType
import com.simiacryptus.util.describe.DescriptorUtil.isArray
import com.simiacryptus.util.describe.TypeDescriber.Companion.primitives
import java.lang.reflect.Method
import java.lang.reflect.Modifier
import java.lang.reflect.Parameter
import java.lang.reflect.Type
import kotlin.reflect.full.memberProperties
import java.util.*
import kotlin.reflect.KClass
import kotlin.reflect.KVisibility
import kotlin.reflect.full.functions
import kotlin.reflect.jvm.javaType

open class ApiFunctionDescriber : TypeDescriber {

    open val includeMethods: Boolean = true
    private val truncation = "..."

    override fun describe(self: Method, clazz: Class<*>?, stackMax: Int): String {
        if (stackMax <= 0) return truncation
        val parameters = self.parameters.joinToString("\n") {
            "  ${describe(it, stackMax - 1).replace("\n", "\n  ")}"
        }
        if (parameters.isBlank()) return "${self.name}()"
        return "${self.name}(\n$parameters\n)"
    }

    override fun describe(rawType: Class<in Nothing>, stackMax: Int): String {
        if (isAbbreviated(rawType)) return rawType.simpleName
        if (stackMax <= 0) return truncation
        return if (!rawType.isKotlinClass()) {
            describeJavaClass(rawType, stackMax)
        } else {
            describeKotlinClass(rawType.kotlin, stackMax)
        }
    }

    fun describe(self: Parameter, stackMax: Int): String {
        if (stackMax <= 0) return truncation
        return "${self.name}: ${toApiFunctionFormat(self.parameterizedType, stackMax - 1)}"
    }

    private fun toApiFunctionFormat(self: Type, stackMax: Int = 10): String {
        if (stackMax <= 0) return truncation
        val typeName = self.typeName.substringAfterLast('.').replace('$', '.').lowercase(Locale.getDefault())
        return when {
            typeName in primitives -> typeName
            self.isArray -> "${toApiFunctionFormat(self.componentType!!, stackMax - 1)}[]"
            else -> describe(TypeToken.of(self).rawType, stackMax - 1)
        }
    }

    override val methodBlacklist = setOf("equals", "hashCode", "copy", "toString", "valueOf")

    private fun describeKotlinClass(kClass: KClass<out Any>, stackMax: Int): String {
        val properties = try {
            kClass.memberProperties.filter { it.visibility == KVisibility.PUBLIC }
                .joinToString("\n") {
                    "  val ${it.name}: ${
                        toApiFunctionFormat(it.returnType.javaType, stackMax - 1).replace(
                            "\n",
                            "\n  "
                        )
                    }"
                }
        } catch (e: Throwable) {
            ""
        }
        val methods = try {
            kClass.functions.filter {
                it.visibility == KVisibility.PUBLIC
                        && !methodBlacklist.contains(it.name)
                        && !it.isOperator && !it.isInfix && !it.isAbstract
            }.toList().sortedBy { it.toString() }.joinToString("\n") {
                val kParameters = it.parameters.filter { it.name != null }
                if (kParameters.isEmpty()) return@joinToString "  fun ${it.name}()"
                "  fun ${it.name}(\n${
                    kParameters.joinToString("\n") {
                        "    ${it.name}: ${
                            toApiFunctionFormat(
                                it.type.javaType,
                                stackMax - 1
                            ).replace("\n", "\n    ")
                        }"
                    }
                })"
            }
        } catch (e: Throwable) {
            ""
        }
        if (kClass.isData) {
            if (methods.isBlank()) return "data class ${kClass.simpleName} (\n  $properties\n)"
            return "data class ${kClass.simpleName} (\n$properties\n){\n$methods\n}"
        } else {
            if (methods.isBlank()) return "class ${kClass.simpleName} {\n  $properties\n}"
            return "class ${kClass.simpleName} {\n$properties\n$methods\n}"
        }
    }

    private fun describeJavaClass(rawType: Class<in Nothing>, stackMax: Int): String {
        val typeName = rawType.typeName.substringAfterLast('.').replace('$', '.').lowercase(Locale.getDefault())
        if (typeName in primitives) return typeName
        if (!includeMethods) return rawType.simpleName
        val methods =
            rawType.methods.toList()
                .filter {
                    Modifier.isPublic(it.modifiers) && !it.isSynthetic && !it.name.contains("$") && !methodBlacklist.contains(
                        it.name
                    )
                }
                .sortedBy { it.toString() }
                .joinToString("\n") { "  ${describe(it, rawType, stackMax - 1).replace("\n", "\n  ")}" }
        if (methods.isBlank()) return rawType.simpleName
        return "class ${rawType.simpleName} {\n$methods\n}"
    }

}
