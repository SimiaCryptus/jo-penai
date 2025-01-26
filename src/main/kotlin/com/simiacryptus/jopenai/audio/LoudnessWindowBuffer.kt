package com.simiacryptus.jopenai.audio

import org.slf4j.LoggerFactory
import java.util.*
import javax.sound.sampled.AudioFormat

class LoudnessWindowBuffer(
    inputBuffer: Queue<ByteArray>,
    outputBuffer: Queue<ByteArray>,
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
    var lookbackPackets = 20
    val talkTime: Double get() = outputPacketBuffer.takeLast(lookbackPackets).filter(::isQuiet).sumOf { it.duration }

    override fun shouldOutput(): Boolean {
        val thisPacket = this.outputPacketBuffer.takeLast(1).firstOrNull() ?: return false
        val output = isQuiet(thisPacket) && talkTime > minimumTalkSeconds
        log.debug(
            listOf(
                "RMS: ${compare(thisPacket.rms, minRMS)}",
                "IEC61672: ${compare(thisPacket.iec61672, minIEC61672)}",
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

    fun isQuiet(it: AudioPacket) = it.rms > minRMS && it.iec61672 > minIEC61672

    companion object {
        private val log = LoggerFactory.getLogger(LoudnessWindowBuffer::class.java)
    }
}

private fun Number.format(s: String): String {
    return String.format(s, this)
}
