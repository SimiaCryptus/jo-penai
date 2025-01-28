package com.simiacryptus.jopenai.audio

import kotlin.math.log

class PercentileTool(
    val memorySize: Int = 10000
) {
    companion object {
        private val log = org.slf4j.LoggerFactory.getLogger(PercentileTool::class.java)
    }

    internal var memory = ArrayList<Double>() // Maintains sorted order
        private set

    fun add(value: Double) {
        // Insert value in sorted order using binary search
        val index = memory.binarySearch(value).let { if (it < 0) -it - 1 else it }
        memory.add(index, value)
        // If memory exceeds memorySize, rebuild the list to include every other element
        if (memory.size > memorySize) {
            val newSize = memorySize / 2
            val newMemory = ArrayList<Double>(newSize)
            for (i in 0 until memory.size step 2) {
                newMemory.add(memory[i])
            }
            memory = newMemory
        }
    }

    fun getPercentile(percentile: Double): Double {
        if (memory.isEmpty()) return 0.0
        val sorted = memory.sorted()
        val index = (percentile * sorted.size).toInt().coerceIn(0, sorted.size - 1)
        return sorted[index]
    }

    /**
     * Finds the entropy-based threshold to differentiate between two modes in a bimodal distribution.
     * @param percentileBias Optional bias towards a specific percentile. Default is 0.5.
     * @return The threshold value that maximizes the combined entropy of the two partitions.
     */
    fun findEntropyThreshold(percentileBias: Double = 0.05): Double {
        if (memory.size < 2) return 0.0
        if (memory.first() == memory.last()) {
            return memory.first()
        }
        var bestThreshold = memory[0]
        var bestIndex = -1
        var maxEntropy = Double.NEGATIVE_INFINITY
        val min = memory.first()
        val max = memory.last()
        for (i in 1 until memory.size) {
            if (memory[i] == memory[i - 1]) continue
            val threshold = (memory[i - 1] + memory[i]) / 2
            if (threshold <= min || threshold >= max) continue
            val fractionOfRange = (threshold - min) / (max - min)
            val fractionOfValues = i.toDouble() / memory.size
            if (fractionOfValues == 0.0 || fractionOfRange <= 0.0) continue
            if (fractionOfValues >= 1.0 || fractionOfRange >= 1.0) break
            val entropy = -(fractionOfValues * fractionOfRange * (log(fractionOfRange, 2.0)) +
                    (1.0 - fractionOfValues) * (1.0 - fractionOfRange) * (log(1.0 - fractionOfRange, 2.0)))
            if (entropy > maxEntropy) {
                maxEntropy = entropy
                bestThreshold = threshold
                bestIndex = i
            }
        }
        if (percentileBias != 0.0) {
            val percentileIndex = (bestIndex + (percentileBias * memory.size)).toInt().coerceIn(0, memory.size - 1)
            if (percentileIndex != bestIndex) {
                bestThreshold = memory[percentileIndex]
            }
        }
        return bestThreshold
    }
}