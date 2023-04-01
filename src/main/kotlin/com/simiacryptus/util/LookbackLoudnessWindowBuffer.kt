package com.simiacryptus.util

import org.slf4j.LoggerFactory
import java.util.*
import kotlin.math.pow
import kotlin.math.sqrt

class LookbackLoudnessWindowBuffer(
    inputBuffer: Deque<ByteArray>,
    outputBuffer: Deque<ByteArray>,
    continueFn: () -> Boolean,
) : LoudnessWindowBuffer(inputBuffer, outputBuffer, continueFn) {

    var minimumOutputTimeSeconds = 5.0
    var rmsPercentileThreshold = 0.5
    var iec61672PercentileThreshold = 0.25

    override fun shouldOutput(): Boolean {


        val thisPacket = outputPacketBuffer.takeLast(1).get(0)

        val recentRMS = recentPacketBuffer.map { it.rms }.toDoubleArray().sortedArray()
        val rmsStats = statistics(recentRMS)
        val percentileRMS = percentile(thisPacket.rms, recentRMS)

        val recentIEC61672 = recentPacketBuffer.map { it.iec61672 }.toDoubleArray().sortedArray()
        val iec61672Stats = statistics(recentRMS)
        val percentileIEC61672 = percentile(thisPacket.iec61672, recentIEC61672)

        val outputTime = outputPacketBuffer.map { it.duration }.sum()

        val output =
            percentileRMS < rmsPercentileThreshold && percentileIEC61672 < iec61672PercentileThreshold && outputTime > minimumOutputTimeSeconds
        if (output) {
            log.info(
                """
                |Packet RMS: ${thisPacket.rms}
                |Recent RMS: ${rmsStats}
                |Percentile RMS: ${percentileRMS}
                |Recent IEC61672: ${iec61672Stats}
                |Packet IEC61672: ${thisPacket.iec61672}
                |Percentile IEC61672: ${percentileIEC61672}
                |Output Time: ${outputTime}
                """.trimMargin().trim()
            )
        } else {
            if (log.isDebugEnabled) log.debug(
                """
                |Packet RMS: ${thisPacket.rms}
                |Recent RMS: ${rmsStats}
                |Percentile RMS: ${percentileRMS}
                |Recent IEC61672: ${iec61672Stats}
                |Packet IEC61672: ${thisPacket.iec61672}
                |Percentile IEC61672: ${percentileIEC61672}
                |Output Time: ${outputTime}
                """.trimMargin().trim()
            )

        }
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
        val log = LoggerFactory.getLogger(LookbackLoudnessWindowBuffer::class.java)
    }

}
