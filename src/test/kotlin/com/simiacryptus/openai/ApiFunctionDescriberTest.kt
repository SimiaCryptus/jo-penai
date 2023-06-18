package com.simiacryptus.openai

import com.simiacryptus.TypeDescriberTestBase
import com.simiacryptus.util.describe.ApiFunctionDescriber
import com.simiacryptus.util.describe.TypeDescriber

class ApiFunctionDescriberTest : TypeDescriberTestBase() {
    override val typeDescriber: TypeDescriber get() = ApiFunctionDescriber()
    override val classDescription: String get() = """
        |data class DataClassExample (
        |    val a: int
        |  val b: string
        |  val c: List
        |  val d: Map
        |)
    """.trimMargin()
    override val methodDescription get() = """
        |methodExample(
        |  p1: int
        |  p2: string
        |)
    """.trimMargin()

}