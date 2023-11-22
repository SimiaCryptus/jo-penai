package com.simiacryptus.jopenai

import com.simiacryptus.jopenai.describe.Description
import com.simiacryptus.jopenai.describe.TypeDescriber
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.reflect.jvm.javaMethod

abstract class TypeDescriberTestBase {

    data class DataClassExample(
        @Description("This is an integer")
        val a: Int,
        val b: String,
        val c: List<String>,
        val d: Map<String, Int>
    )

    @Description("This is a method")
    fun methodExample(@Description("This is a parameter") p1: Int, p2: String): String {
        return "$p1 - $p2"
    }

    abstract val typeDescriber: TypeDescriber

    @Test
    fun testDescribeType() {
        Assertions.assertEquals(classDescription, typeDescriber.describe(DataClassExample::class.java))
    }

    abstract val classDescription: String

    @Test
    fun testDescribeMethod() {
        Assertions.assertEquals(methodDescription, typeDescriber.describe(this::methodExample.javaMethod!!))
    }

    @Test
    fun testDescribeOpenAIClient() {
        println(typeDescriber.describe(OpenAIClient::class.java))
    }

    abstract val methodDescription: String
}