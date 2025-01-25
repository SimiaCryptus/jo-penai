package com.simiacryptus.jopenai.audio

import org.slf4j.LoggerFactory
import java.util.*
import javax.sound.sampled.AudioFormat
import kotlin.math.pow
import kotlin.math.sqrt

class LookbackLoudnessWindowBuffer(
    inputBuffer: Queue<ByteArray>,
    outputBuffer: Queue<ByteArray>,
    private val onRmsUpdate: (AudioPacket) -> Unit,
    private val onIec61672Update: (AudioPacket) -> Unit,
    continueFn: () -> Boolean,
    audioFormat: AudioFormat,
) : LoudnessWindowBuffer(inputBuffer, outputBuffer, continueFn, audioFormat) {

    private var minimumOutputTimeSeconds = 5.0
    var rmsPercentileThreshold = 0.5
    var iec61672PercentileThreshold = 0.25

    override fun shouldOutput(): Boolean {

        val thisPacket = outputPacketBuffer.takeLast(1).get(0)
        onRmsUpdate(thisPacket)
        onIec61672Update(thisPacket)

        val recentRMS = recentPacketBuffer.map { it.rms }.toDoubleArray().sortedArray()
        val percentileRMS = percentile(thisPacket.rms, recentRMS)

        val recentIEC61672 = recentPacketBuffer.map { it.iec61672 }.toDoubleArray().sortedArray()
        val percentileIEC61672 = percentile(thisPacket.iec61672, recentIEC61672)

        val outputTime = outputPacketBuffer.map { it.duration }.sum()
        val output = percentileRMS < rmsPercentileThreshold && percentileIEC61672 < iec61672PercentileThreshold && outputTime > minimumOutputTimeSeconds

        return output
    }

    private fun percentile(value: Double, values: DoubleArray): Double {
        var index = values.binarySearch(value)
        if (index < 0) index = -index - 1
        return index.toDouble() / values.size
    }

    private fun statistics(values: DoubleArray): List<Double> {
        val mean = values.average()
        val variance = values.map { (it - mean).pow(2) }.average()
        val stdDev = sqrt(variance)
        return listOf(mean, stdDev)
    }

    companion object {
        private val log = LoggerFactory.getLogger(LookbackLoudnessWindowBuffer::class.java)
    }

}