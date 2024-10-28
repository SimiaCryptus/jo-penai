package com.simiacryptus.jopenai.describe
import org.slf4j.LoggerFactory

import com.simiacryptus.jopenai.describe.DescriptorUtil.componentType
import com.simiacryptus.jopenai.describe.DescriptorUtil.isArray
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.*

abstract class TypeDescriber {
    private val logger = LoggerFactory.getLogger(TypeDescriber::class.java)

    abstract val markupLanguage: String
    abstract val methodBlacklist: Set<String>
    var coverMethods = true
    abstract fun describe(
        rawType: Class<in Nothing>,
        stackMax: Int = 10,
        describedTypes: MutableSet<String> = mutableSetOf()
    ): String

    abstract fun describe(self: Method, clazz: Class<*>? = null, stackMax: Int = 5): String
    open fun isAbbreviated(self: Type): Boolean {
        logger.debug("Checking if type is abbreviated: {}", self.typeName)
        val name = self.typeName
        val typeName = self.typeName.substringAfterLast('.').replace('$', '.').lowercase(Locale.getDefault())
        if (typeName in primitives) {
            logger.debug("Type is a primitive: {}", typeName)
            return false
        } else if (self is ParameterizedType && List::class.java.isAssignableFrom(self.rawType as Class<*>)) {
            logger.debug("Type is a parameterized List: {}", self.typeName)
            return isAbbreviated(self.actualTypeArguments[0])
        } else if (self is ParameterizedType && Map::class.java.isAssignableFrom(self.rawType as Class<*>)) {
            logger.debug("Type is a parameterized Map: {}", self.typeName)
            return isAbbreviated(self.actualTypeArguments[0]) && isAbbreviated(self.actualTypeArguments[1])
        } else if (self.isArray) {
            logger.debug("Type is an array: {}", self.typeName)
            return isAbbreviated(self.componentType!!)
        }
        logger.debug("Type is not abbreviated: {}", self.typeName)
        if (name.startsWith("java.")) return true
        if (name.startsWith("kotlin.")) return true
        if (name.startsWith("sun.")) return true
        if (name.startsWith("apache.")) return true
        if (name.startsWith("org.slf4j.")) return true
        if (name.startsWith("com.fasterxml.")) return true
        return false
    }

    companion object {
        val primitives = setOf(
            "boolean",
            "int",
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
    }
}