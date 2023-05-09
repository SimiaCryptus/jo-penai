package com.simiacryptus.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.core.json.JsonReadFeature
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import java.lang.reflect.Type

object JsonUtil {
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
            .enable(JsonParser.Feature.ALLOW_COMMENTS)
            .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
            .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_MISSING_CREATOR_PROPERTIES)
            .disable(DeserializationFeature.FAIL_ON_TRAILING_TOKENS)
            //            .disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE)
            //            .disable(DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES)
            //            .disable(DeserializationFeature.FAIL_ON_READING_DUP_TREE_KEY)
            .enable(JsonReadFeature.ALLOW_JAVA_COMMENTS.mappedFeature())
            .enable(JsonReadFeature.ALLOW_YAML_COMMENTS.mappedFeature())
            .enable(JsonReadFeature.ALLOW_TRAILING_COMMA.mappedFeature())
            .enable(JsonReadFeature.ALLOW_UNESCAPED_CONTROL_CHARS.mappedFeature())
            .enable(JsonReadFeature.ALLOW_BACKSLASH_ESCAPING_ANY_CHARACTER.mappedFeature())
            .enable(JsonReadFeature.ALLOW_MISSING_VALUES.mappedFeature())
            .enable(JsonReadFeature.ALLOW_LEADING_DECIMAL_POINT_FOR_NUMBERS.mappedFeature())
            .enable(JsonReadFeature.ALLOW_NON_NUMERIC_NUMBERS.mappedFeature())
            .enable(JsonReadFeature.ALLOW_LEADING_PLUS_SIGN_FOR_NUMBERS.mappedFeature())
            .enable(JsonReadFeature.ALLOW_TRAILING_DECIMAL_POINT_FOR_NUMBERS.mappedFeature())
            .setSerializationInclusion(JsonInclude.Include.NON_NULL)
            .registerModule(
                KotlinModule.Builder()
                .withReflectionCacheSize(512)
                .configure(KotlinFeature.NullToEmptyCollection, false)
                .configure(KotlinFeature.NullToEmptyMap, false)
                .configure(KotlinFeature.NullIsSameAsDefault, false)
                .configure(KotlinFeature.SingletonSupport, false)
                .configure(KotlinFeature.StrictNullChecks, false)
                .build())
    }

    fun toJson(data: Any): String {
        return objectMapper().writerWithDefaultPrettyPrinter().writeValueAsString(data)
    }

    fun <T> fromJson(data: String, type: Type): T {
        if (type is Class<*> && type.isAssignableFrom(String::class.java)) return data as T
        val objectMapper = objectMapper()
        val value = objectMapper.readValue(data, objectMapper.typeFactory.constructType(type)) as T
        //log.debug("Deserialized $data to $value")
        return value
    }
}