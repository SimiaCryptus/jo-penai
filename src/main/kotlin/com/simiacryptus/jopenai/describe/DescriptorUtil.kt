package com.simiacryptus.jopenai.describe

import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import kotlin.reflect.KClass
import kotlin.reflect.KProperty1
import kotlin.reflect.KType
import kotlin.reflect.KTypeParameter
import kotlin.reflect.full.memberFunctions

object DescriptorUtil {

    fun getAllAnnotations(
        rawType: Class<in Nothing>,
        property: KProperty1<out Any, *>,
    ): List<Annotation> =
        property.annotations + (rawType.kotlin.constructors.firstOrNull()?.parameters?.find { x -> x.name == property.name }?.annotations
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


    fun resolveMethodReturnType(concreteClass: KClass<*>, methodName: String): KType {
        // Get the method from the class by name
        val method = concreteClass.memberFunctions.firstOrNull { it.name == methodName }
            ?: throw IllegalArgumentException("Method $methodName not found in class $concreteClass")

        // Start with the return type of the method
        var returnType = method.returnType

        // If the return type is a type parameter, try to resolve it
        if (returnType.classifier is KTypeParameter) {
            returnType = resolveGenericType(concreteClass, returnType)
        }

        return returnType
    }

    fun resolveGenericType(concreteClass: KClass<*>, kType: KType): KType {
        val classifier = kType.classifier

        // Only proceed if the classifier is a type parameter
        if (classifier is KTypeParameter) {
            // Find the type parameter in the concrete class that matches by name
            val typeArgument = concreteClass.typeParameters
                .firstOrNull { it.name == classifier.name }
                ?.let { typeParameter ->
                    // Find the corresponding argument from the concrete class
                    concreteClass.supertypes.flatMap { it.arguments }.firstOrNull { argument ->
                        argument.type?.classifier == typeParameter
                    }?.type
                }
            // Return the found type argument, or the original type if not found
            return typeArgument ?: kType
        }

        return kType
    }
}