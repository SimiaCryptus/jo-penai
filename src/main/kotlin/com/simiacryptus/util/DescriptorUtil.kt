@file:Suppress("unused")

package com.simiacryptus.util

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KProperty1

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