package com.simiacryptus.jopenai.audio

import org.slf4j.LoggerFactory
import java.util.*
import javax.sound.sampled.AudioFormat

class LoudnessWindowBuffer(
    inputBuffer: Queue<AudioPacket>,
    outputBuffer: Queue<AudioPacket>,
    onPacket: (AudioPacket) -> Unit,
    continueFn: () -> Boolean,
    audioFormat: AudioFormat,
) : WindowBuffer(
    inputBuffer = inputBuffer,
    outputBuffer = outputBuffer,
    continueFn = continueFn,
    audioFormat = audioFormat,
    onPacket = onPacket
) {

    var minimumTalkSeconds = 2.0
    var minRMS = 0.1
    var minIEC61672 = 0.1
    var minSpectralEntropy = 0.5
    var lookbackPackets = 20
    var quietWindowPackets = 3
    val talkTime: Double get() = outputPacketBuffer.takeLast(lookbackPackets).filter{ isQuiet(it) }.sumOf { it.duration }

    override fun shouldOutput(): Boolean {
        val packets = this.outputPacketBuffer.takeLast(quietWindowPackets)
        val output = isQuiet(*packets.toTypedArray()) && talkTime > minimumTalkSeconds
        val thisPacket = packets.last()
        log.debug(
            listOf(
                "RMS: ${compare(thisPacket.rms, minRMS)}",
                "IEC61672: ${compare(thisPacket.iec61672, minIEC61672)}",
                //"Spectral Entropy: ${compare(thisPacket.spectralEntropy, minSpectralEntropy)}",
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
        listOf(
            packet.rms < minRMS,
            packet.iec61672 < minIEC61672,
            //packet.spectralEntropy < minSpectralEntropy
        ).all { it }
    }

    companion object {
        private val log = LoggerFactory.getLogger(LoudnessWindowBuffer::class.java)
    }
}

private fun Number.format(s: String): String {
    return String.format(s, this)
}