package com.simiacryptus.util.describe

class AbbrevWhitelistYamlDescriber(vararg val abbreviated: String) : YamlDescriber() {
    override fun isAbbreviated(name: String) = (abbreviated.find { name.startsWith(it) } == null) || super.isAbbreviated(name)
}