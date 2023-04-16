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

    fun getAllAnnotations(
        rawType: Class<in Nothing>,
        property: KProperty1<out Any, *>,
    ) =
        property.annotations + (rawType.kotlin.constructors.first().parameters.find { x -> x.name == property.name }?.annotations
            ?: listOf())

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