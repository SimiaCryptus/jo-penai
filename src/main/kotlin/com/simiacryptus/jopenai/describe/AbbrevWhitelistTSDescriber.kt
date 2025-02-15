package com.simiacryptus.jopenai.describe
import org.slf4j.LoggerFactory

import com.simiacryptus.jopenai.describe.DescriptorUtil.componentType
import com.simiacryptus.jopenai.describe.DescriptorUtil.isArray
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

open class AbbrevWhitelistTSDescriber(private vararg val abbreviated: String) : TypeScriptDescriber() {
    private val log = LoggerFactory.getLogger(AbbrevWhitelistTSDescriber::class.java)

    override fun isAbbreviated(self: Type): Boolean {
//        logger.info("Checking if type is abbreviated: {}", self.typeName)
        if (self.typeName in primitives) {
//            logger.info("Type is a primitive: {}", self.typeName)
            return false
        } else if (self is ParameterizedType && List::class.java.isAssignableFrom(self.rawType as Class<*>)) {
//            logger.info("Type is a parameterized List: {}", self.typeName)
            return isAbbreviated(self.actualTypeArguments[0])
        } else if (self is ParameterizedType && Map::class.java.isAssignableFrom(self.rawType as Class<*>)) {
//            logger.info("Type is a parameterized Map: {}", self.typeName)
            return isAbbreviated(self.actualTypeArguments[0]) && isAbbreviated(self.actualTypeArguments[1])
        } else if (self.isArray) {
//            logger.info("Type is an array: {}", self.typeName)
            return isAbbreviated(self.componentType!!)
        }
        val isAbbreviated = (abbreviated.find { self.typeName.startsWith(it) } == null) || super.isAbbreviated(self)
//        logger.info("Type {} is abbreviated: {}", self.typeName, isAbbreviated)
        return isAbbreviated
    }
}