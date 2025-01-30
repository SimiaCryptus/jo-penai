package com.simiacryptus.jopenai.audio

import org.slf4j.LoggerFactory
import java.util.*
import kotlin.math.ln

open class TrainedSilenceDiscriminator(
    inputBuffer: Queue<AudioPacket>,
    outputBuffer: Queue<AudioPacket>,
    onPacket: (AudioPacket) -> Unit,
    continueFn: () -> Boolean,
    var isVerbose: Boolean = false,
) : SilenceDiscriminator(
    inputBuffer = inputBuffer,
    outputBuffer = outputBuffer,
    onPacket = onPacket,
    continueFn = continueFn
) {
    protected open fun newPercentile() = PercentileTool(10000)
    protected open fun PercentileTool.getCurrentRMSThreshold() = findEntropyThreshold5(0.0)
    inner class Statistics {
        val rmsPercentileTool by lazy { newPercentile() }
        val iec61672PercentileTool by lazy { newPercentile() }
        val spectralEntropyPercentileTool by lazy { newPercentile() }
        val spectralCentroidPercentileTool by lazy { newPercentile() }
        val spectralFlatnessPercentileTool by lazy { newPercentile() }
        fun isQuiet(vararg packets: AudioPacket): Double {
            return packets.map { packet ->
                listOf(
                    packet.rms < rmsPercentileTool.getCurrentRMSThreshold(),
                    packet.aWeighting < iec61672PercentileTool.getCurrentRMSThreshold(),
                    packet.spectralEntropy < spectralEntropyPercentileTool.getCurrentRMSThreshold(),
                    packet.spectralCentroid > spectralCentroidPercentileTool.getCurrentRMSThreshold(),
                    packet.spectralFlatness > spectralFlatnessPercentileTool.getCurrentRMSThreshold()
                ).map { if (it) 1.0 else 0.0 }.average()
            }.average()
        }

        fun isEmpty(): Boolean {
            return rmsPercentileTool.isEmpty() || iec61672PercentileTool.isEmpty() || spectralEntropyPercentileTool.isEmpty() || spectralCentroidPercentileTool.isEmpty() || spectralFlatnessPercentileTool.isEmpty()
        }
    }

    private val silence = Statistics()
    private val speech = Statistics()
    private val unknown = Statistics()
    var trainingState: Boolean? = false
    var bias: Double = -0.1

    override fun processPacket(packet: AudioPacket) {
        when (trainingState) {
            true -> speech
            false -> silence
            else -> unknown
        }.apply {
            rmsPercentileTool.add(packet.rms)
            iec61672PercentileTool.add(packet.aWeighting)
            spectralEntropyPercentileTool.add(packet.spectralEntropy)
            spectralCentroidPercentileTool.add(packet.spectralCentroid)
            spectralFlatnessPercentileTool.add(packet.spectralFlatness)
        }
        logPacket(packet)
        super.processPacket(packet)
    }

    override fun isQuiet(vararg packets: AudioPacket): Boolean {
        if (silence.isEmpty() || speech.isEmpty()) return unknown.isQuiet(*packets) < 0.5
        if (trainingState != null) return !trainingState!!
        return packets.map { computeQuietMetrics(it).average() > bias }.all { it }
    }

    protected open fun compare(v: Double, a: PercentileTool, b: PercentileTool): Double {
        val vB = b.getDensityOfValue(v)
        val vA = a.getDensityOfValue(v)
        return when {
            vB == 0.0 || vA == 0.0 -> {
                val pA = a.getPercentileOfValue(v)
                val pB = b.getPercentileOfValue(v)
                when {
                    pA == 1.0 && pB == 0.0 -> {
                        val dA = a.getDistanceFromBounds(v)
                        val dB = b.getDistanceFromBounds(v)
                        if (dA < dB) 1.0 else -1.0
                    }

                    pA == 1.0 -> -1.0
                    pB == 0.0 -> 1.0
                    else -> (pA - pB)
                }
            }

            else -> ln(vA / vB)
        }
    }

    /**
     * Computes comparison scores for various audio metrics between
     * the known silence and speech distributions. A positive average suggests "quiet".
     */
    private fun computeQuietMetrics(packet: AudioPacket) = listOf(
        compare(packet.rms, silence.rmsPercentileTool, speech.rmsPercentileTool),
        compare(packet.aWeighting, silence.iec61672PercentileTool, speech.iec61672PercentileTool),
        compare(packet.spectralEntropy, silence.spectralEntropyPercentileTool, speech.spectralEntropyPercentileTool),
        // Intentionally inverted - these metrics trend lower for active speech
        compare(packet.spectralCentroid, speech.spectralCentroidPercentileTool, silence.spectralCentroidPercentileTool),
        compare(packet.spectralFlatness, speech.spectralFlatnessPercentileTool, silence.spectralFlatnessPercentileTool)
    )

    private fun logPacket(packet: AudioPacket) {
        if (isVerbose) log.debug(
            "Audio metrics comparison: ${
                listOf(
                    "RMS: ${
                        compare(
                            silence.rmsPercentileTool.getPercentileOfValue(packet.rms),
                            speech.rmsPercentileTool.getPercentileOfValue(packet.rms)
                        )
                    }",
                    "IEC61672: ${
                        compare(
                            silence.iec61672PercentileTool.getPercentileOfValue(packet.aWeighting),
                            speech.iec61672PercentileTool.getPercentileOfValue(packet.aWeighting)
                        )
                    }",
                    "Spectral Entropy: ${
                        compare(
                            silence.spectralEntropyPercentileTool.getPercentileOfValue(packet.spectralEntropy),
                            speech.spectralEntropyPercentileTool.getPercentileOfValue(packet.spectralEntropy)
                        )
                    }",
                    "Spectral Centroid: ${
                        compare(
                            speech.spectralCentroidPercentileTool.getPercentileOfValue(packet.spectralCentroid),
                            silence.spectralCentroidPercentileTool.getPercentileOfValue(packet.spectralCentroid)
                        )
                    }",
                    "Spectral Flatness: ${
                        compare(
                            speech.spectralFlatnessPercentileTool.getPercentileOfValue(packet.spectralFlatness),
                            silence.spectralFlatnessPercentileTool.getPercentileOfValue(packet.spectralFlatness)
                        )
                    }",
                    "isTalk: ${trainingState}",
                ).joinToString(" ")
            }"
        )
    }

    fun reset(): Unit {
        silence.rmsPercentileTool.clear()
        silence.iec61672PercentileTool.clear()
        silence.spectralEntropyPercentileTool.clear()
        silence.spectralCentroidPercentileTool.clear()
        silence.spectralFlatnessPercentileTool.clear()
        speech.rmsPercentileTool.clear()
        speech.iec61672PercentileTool.clear()
        speech.spectralEntropyPercentileTool.clear()
        speech.spectralCentroidPercentileTool.clear()
        speech.spectralFlatnessPercentileTool.clear()
    }

    companion object {
        private val log = LoggerFactory.getLogger(TrainedSilenceDiscriminator::class.java)
        fun compare(a: Double, b: Double) = when {
            a > b -> "${a.format("%.2f")} > ${b.format("%.2f")}"
            a < b -> "${a.format("%.2f")} < ${b.format("%.2f")}"
            else -> "${a.format("%.2f")} = ${b.format("%.2f")}"
        }
    }
}

private fun Number.format(s: String) = String.format(s, this)
