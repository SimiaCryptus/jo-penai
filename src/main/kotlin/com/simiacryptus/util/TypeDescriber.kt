package com.simiacryptus.util

import java.lang.reflect.Method

interface TypeDescriber {
    open fun describe(
        rawType: Class<in Nothing>,
        stackMax: Int = 10,
    ): String

    fun describe(self: Method, stackMax: Int = 10): String
}