package com.simiacryptus.jopenai.proxy

import kotlin.reflect.full.memberProperties

interface ValidatedObject {
    fun validate(): String? = validateFields(this)

    companion object {
        fun validateFields(obj: Any): String? {
            obj.javaClass.declaredFields.forEach { field ->
                field.isAccessible = true
                val value = field.get(obj)
                if (value is ValidatedObject) {
                    val validate = value.validate()
                    if (null != validate) return validate
                }
                // If the property is a list, validate each element
                if (value is List<*>) {
                    value.forEach {
                        if (it is ValidatedObject) {
                            val validate = it.validate()
                            if (null != validate) return validate
                        }
                    }
                }
            }
            obj.javaClass.kotlin.memberProperties.forEach { property ->
                val value = property.getter.call(obj)
                if (value is ValidatedObject) {
                    val validate = value.validate()
                    if (null != validate) return validate
                }
                // If the property is a list, validate each element
                if (value is List<*>) {
                    value.forEach {
                        if (it is ValidatedObject) {
                            val validate = it.validate()
                            if (null != validate) return validate
                        }
                    }
                }
            }
            return null
        }
    }
}