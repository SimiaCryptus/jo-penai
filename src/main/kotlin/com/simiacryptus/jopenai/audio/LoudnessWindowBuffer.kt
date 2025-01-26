package com.simiacryptus.jopenai.audio

import org.slf4j.LoggerFactory
import java.util.*
import javax.sound.sampled.AudioFormat

class LoudnessWindowBuffer(
    inputBuffer: Queue<ByteArray>,
    outputBuffer: Queue<ByteArray>,
    private val onPacket: (AudioPacket) -> Unit,
    continueFn: () -> Boolean,
    audioFormat: AudioFormat,
) : WindowBuffer(
    inputBuffer = inputBuffer,
    outputBuffer = outputBuffer,
    continueFn = continueFn,
    audioFormat = audioFormat
) {

    private var minimumOutputTimeSeconds = 5.0
    var rmsThreshold = 0.5
    var minRMS = 0.1
    var iec61672Threshold = 0.25
    var minIEC61672 = 0.1

    override fun shouldOutput(): Boolean {
        val thisPacket = this.outputPacketBuffer.takeLast(1).firstOrNull() ?: return false
        onPacket(thisPacket)
        val recent = outputPacketBuffer.takeLast(10)
        val recentRMS = recent.map { it.rms }.toDoubleArray().sortedArray()
        val percentileRMS = percentile(thisPacket.rms, recentRMS)
        val recentIEC61672 = recent.map { it.iec61672 }.toDoubleArray().sortedArray()
        val percentileIEC61672 = percentile(thisPacket.iec61672, recentIEC61672)
        val outputTime = recent.sumOf { it.duration }
        val output = percentileRMS < rmsThreshold && percentileIEC61672 < iec61672Threshold && outputTime > minimumOutputTimeSeconds
        log.debug(
            listOf(
                "RMS: ${thisPacket.rms.format("%.2f")} (${(percentileRMS*100.0).format("%.2f")}%) > ${rmsThreshold.format("%.2f")}",
                "IEC61672: ${thisPacket.iec61672.format("%.2f")} (${(percentileIEC61672*100.0).format("%.2f")}%) > ${iec61672Threshold.format("%.2f")}",
                "Output Time: $outputTime > $minimumOutputTimeSeconds",
                "Output: $output"
            ).joinToString(" | ")
        )
        return output
    }

    private fun percentile(value: Double, values: DoubleArray): Double {
        var index = values.binarySearch(value)
        if (index < 0) index = -index - 1
        return index.toDouble() / values.size
    }

    companion object {
        private val log = LoggerFactory.getLogger(LoudnessWindowBuffer::class.java)
    }
}

private fun Number.format(s: String): String {
    return String.format(s, this)
}
