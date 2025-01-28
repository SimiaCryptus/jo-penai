package com.simiacryptus.jopenai.audio

import org.slf4j.LoggerFactory
import java.util.*

open class LoudnessWindowBuffer(
    inputBuffer: Queue<AudioPacket>,
    outputBuffer: Queue<AudioPacket>,
    onPacket: (AudioPacket) -> Unit,
    continueFn: () -> Boolean,
    var isVerbose: Boolean = false
) : WindowBuffer(
    inputBuffer = inputBuffer,
    outputBuffer = outputBuffer,
    continueFn = continueFn,
    onPacket = onPacket
) {

    var minimumTalkSeconds = 2.0
    var minRMSPercentile = 0.1
    var minIEC61672Percentile = 0.1
    var minSpectralEntropyPercentile = 0.5
    var lookbackPackets = 50
    var quietWindowPackets = 3

    protected val rmsPercentileTool by lazy { newPercentile() }
    protected val iec61672PercentileTool by lazy { newPercentile() }
    protected val spectralEntropyPercentileTool by lazy { newPercentile() }
    protected open fun newPercentile() = PercentileTool(10000)

    val talkTime: Double
        get() = outputPacketBuffer.takeLast(lookbackPackets).filter { !isQuiet(it) }.sumOf { it.duration }

    override fun processPacket(packet: AudioPacket) {
        rmsPercentileTool.add(packet.rms)
        iec61672PercentileTool.add(packet.aWeighting)
        spectralEntropyPercentileTool.add(packet.spectralEntropy)
        super.processPacket(packet)
    }

    override fun shouldOutput(): Boolean {
        val packets = this.outputPacketBuffer.takeLast(quietWindowPackets)
        val talkTime = talkTime
        val output = isQuiet(*packets.toTypedArray()) && talkTime > minimumTalkSeconds
        val thisPacket = packets.last()

//        val currentRMSThreshold = rmsPercentileTool.getPercentile(minRMSPercentile)
//        val currentIEC61672Threshold = iec61672PercentileTool.getPercentile(minIEC61672Percentile)
//        val currentSpectralEntropyThreshold = spectralEntropyPercentileTool.getPercentile(minSpectralEntropyPercentile)
        val currentRMSThreshold = rmsPercentileTool.findEntropyThreshold()
        val currentIEC61672Threshold = iec61672PercentileTool.findEntropyThreshold()
        val currentSpectralEntropyThreshold = spectralEntropyPercentileTool.findEntropyThreshold()

        if(isVerbose) log.debug(
            listOf(
                "RMS: ${compare(thisPacket.rms, currentRMSThreshold)}",
                "IEC61672: ${compare(thisPacket.aWeighting, currentIEC61672Threshold)}",
                "SpctEnt: ${compare(thisPacket.spectralEntropy, currentSpectralEntropyThreshold)}",
                "Talk Time: ${compare(talkTime, minimumTalkSeconds)}",
                "Output: $output"
            ).joinToString(" | ")
        )
        return output
    }

    private fun compare(a: Double, b: Double) = when {
        a > b -> "${a.format("%.2f")} > ${b.format("%.2f")}"
        a < b -> "${a.format("%.2f")} < ${b.format("%.2f")}"
        else -> "${a.format("%.2f")} = ${b.format("%.2f")}"
    }

    fun isQuiet(vararg packets: AudioPacket) = packets.all { packet ->
//        val currentRMSThreshold = rmsPercentileTool.getPercentile(minRMSPercentile)
//        val currentIEC61672Threshold = iec61672PercentileTool.getPercentile(minIEC61672Percentile)
        val currentRMSThreshold = rmsPercentileTool.findEntropyThreshold()
        val currentIEC61672Threshold = iec61672PercentileTool.findEntropyThreshold()

        listOf(
            packet.rms < currentRMSThreshold,
            packet.aWeighting < currentIEC61672Threshold,
            //packet.spectralEntropy < currentSpectralEntropyThreshold
        ).all { it }
    }

    override fun flushOutput(): AudioPacket {
        val reduced = super.outputPacketBuffer.dropWhile {
            isQuiet(it)
        }.reduce { a, b -> a + b }
        super.lastOutputBuffer = super.outputPacketBuffer
        super.outputPacketBuffer = ArrayList()
        return reduced
    }

    companion object {
        private val log = LoggerFactory.getLogger(LoudnessWindowBuffer::class.java)
    }
}

private fun Number.format(s: String): String {
    return String.format(s, this)
}

