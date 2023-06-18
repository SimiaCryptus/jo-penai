package com.simiacryptus.util.describe

import java.lang.reflect.Method

interface TypeDescriber {
    open val methodBlacklist: Set<String>
    open fun describe(
        rawType: Class<in Nothing>,
        stackMax: Int = 10,
    ): String

    fun describe(self: Method, stackMax: Int = 10): String
    fun isAbbreviated(name: String): Boolean {
        if (name.startsWith("java.")) return true
        if (name.startsWith("kotlin.")) return true
        if (name.startsWith("sun.")) return true
        if (name.startsWith("apache.")) return true
        if (name.startsWith("org.slf4j.")) return true
        if (name.startsWith("com.fasterxml.")) return true
        return false
    }

    companion object {


        open val primitives = setOf(
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
    }
}