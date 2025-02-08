package com.simiacryptus.util
import org.slf4j.LoggerFactory

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.node.ObjectNode
import com.fasterxml.jackson.databind.node.TextNode
import com.fasterxml.jackson.databind.ser.std.StdSerializer

open class DynamicEnum<T : DynamicEnum<T>>(val name: String) {
    companion object {
        private val logger = LoggerFactory.getLogger(DynamicEnum::class.java)
        private val registries = mutableMapOf<Class<*>, MutableList<Pair<String, DynamicEnum<*>>>>()

        internal fun <T> getRegistry(clazz: Class<T>): MutableList<Pair<String, T>> {
//            logger.debug("Fetching registry for class: {}", clazz.name)
            @Suppress("UNCHECKED_CAST")
            return registries.getOrPut(clazz) { mutableListOf() } as MutableList<Pair<String, T>>
        }

        fun <T> valueOf(clazz: Class<T>, name: String): T {
//            logger.debug("Looking up value for class: {}, name: {}", clazz.name, name)
            return getRegistry(clazz).toMap().get(name)
                ?: throw IllegalArgumentException("Unknown enum constant: $name")
        }

        fun <T : DynamicEnum<T>> values(clazz: Class<T>): List<T> {
//            logger.debug("Fetching all values for class: {}", clazz.name)
            return getRegistry(clazz).map { it.second }
        }

        @JvmStatic
        fun <T : DynamicEnum<T>> register(clazz: Class<T>, enumConstant: T) {
//            logger.info("Registering enum constant: {} for class: {}", enumConstant.name, clazz.name)
            getRegistry(clazz).add(enumConstant.name to enumConstant)
        }
    }

    override fun toString() = name
    override fun hashCode() = name.hashCode()
    override fun equals(other: Any?): Boolean {
        return this === other || other is DynamicEnum<*> && name == other.name
    }
}

abstract class DynamicEnumSerializer<T : DynamicEnum<T>>(
    private val clazz: Class<T>
) : StdSerializer<T>(clazz) {
    private val logger = LoggerFactory.getLogger(DynamicEnumSerializer::class.java)
    override fun serialize(value: T, gen: JsonGenerator, provider: SerializerProvider) {
        logger.debug("Serializing value: {} for class: {}", value.name, clazz.name)
        DynamicEnum.getRegistry(clazz).find { it.second == value }?.first?.let { gen.writeString(it) }
    }
}

abstract class DynamicEnumDeserializer<T : DynamicEnum<T>>(
    private val clazz: Class<T>
) : JsonDeserializer<T>() {
    private val logger = LoggerFactory.getLogger(DynamicEnumDeserializer::class.java)
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): T {
        logger.debug("Deserializing JSON for class: {}", clazz.name)
        return when (val node = p.codec.readTree<JsonNode>(p)) {
            is TextNode -> DynamicEnum.getRegistry(clazz).toMap()[node.asText()]
                ?: run {
                    logger.error("Unknown enum constant: {}", node.asText())
                    throw JsonMappingException(p, "Unknown enum constant: " + node.asText())
                }
            is ObjectNode -> DynamicEnum.getRegistry(clazz).toMap()[node.get("name")?.asText()]
                ?: run {
                    logger.error("Unknown enum constant: {}", node.toPrettyString())
                    throw JsonMappingException(p, "Unknown enum constant: " + node.toPrettyString())
                }
            else -> throw JsonMappingException(p, "Unexpected JSON value type: ${node.nodeType}")
        } as T
    }
}