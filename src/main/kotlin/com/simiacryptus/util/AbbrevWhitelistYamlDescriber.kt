package com.simiacryptus.util

class AbbrevWhitelistYamlDescriber(vararg val abbreviated: String) : YamlDescriber() {
    override fun isAbbreviated(name: String) = abbreviated.find { name.startsWith(it) } == null
}