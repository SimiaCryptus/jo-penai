package com.simiacryptus.util

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import org.slf4j.LoggerFactory
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.fasterxml.jackson.databind.node.ArrayNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

@JsonDeserialize(using = ListWrapper.ListWrapperDeserializer::class)
@JsonSerialize(using = ListWrapper.ListWrapperSerializer::class)
open class ListWrapper<T : Any>(
    items: List<T> = emptyList()
) : List<T> by items {
    private val logger = LoggerFactory.getLogger(ListWrapper::class.java)
    open fun deepClone(): ListWrapper<T>? {
        logger.info("Cloning ListWrapper with items: {}", this)
        return ListWrapper(this.map { it })
    }

    override fun equals(other: Any?): Boolean {
        logger.info("Checking equality between {} and {}", this, other)
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as ListWrapper<*>
        if (this.size != other.size) return false
        return indices.all {
            this[it] == other[it]
        }
    }

    override fun hashCode(): Int {
        logger.info("Calculating hashCode for ListWrapper: {}", this)
        var result = 1
        forEach {
            result = 31 * result + it.hashCode()
        }
        return result
    }

    override fun toString(): String {
        return joinToString(", ", prefix = "[", postfix = "]")
    }

    class ListWrapperDeserializer<T : Any> : JsonDeserializer<ListWrapper<T>>() {
        private val logger = LoggerFactory.getLogger(ListWrapperDeserializer::class.java)
        override fun deserialize(p: JsonParser, ctxt: DeserializationContext): ListWrapper<T> {
            logger.info("Deserializing ListWrapper from JSON")
            val javaType = JsonUtil._initForReading.get()
            val node = p.codec.readTree<JsonNode>(p)
            if (null == node) {
                logger.error("Deserialized node is null, returning empty ListWrapper")
                return ListWrapper()
            } else if (node.isArray) {
                val contextualType = ctxt.contextualType
                val contentType = contextualType?.containedType(0) ?: javaType.let {
                    if (it?.isCollectionLikeType == true) javaType?.containedType(0)
                    else javaType
                }
                val objectMapper = JsonUtil.objectMapper()
                val items = (node as ArrayNode).toList().map { jsonElement ->
                    val jsonString = jsonElement.toString()
                    try {
                        logger.info("Deserializing item: {}", jsonString)
                        val readValue = objectMapper.readValue<T>(jsonString, contentType)
                        readValue
                    } catch (e: Throwable) {
                        logger.warn("Error deserializing item: {}", jsonString, e)
                        e.printStackTrace()
                        null
                    }
                }.filterNotNull()
                logger.info("Deserialized ListWrapper with items: {}", items)
                return ListWrapper(items)
            }
            // If the node is an object, we assume it's a wrapper object with a single field
            logger.info("Deserializing ListWrapper from object node")
            val items = jacksonObjectMapper().convertValue(node.fields().next().value, List::class.java)
            return ListWrapper(items as List<T>)
        }

    }

    class ListWrapperSerializer<T : Any> : JsonSerializer<ListWrapper<T>>() {
        private val logger = LoggerFactory.getLogger(ListWrapperSerializer::class.java)
        override fun serialize(value: ListWrapper<T>, gen: JsonGenerator, serializers: SerializerProvider) {
            logger.info("Serializing ListWrapper with items: {}", value)
            gen.writeStartArray()
            value.forEach {
                gen.writeObject(it)
            }
            gen.writeEndArray()
        }
    }
}

//    object : TypeReference<T>() { override fun getType() = kType.javaType }