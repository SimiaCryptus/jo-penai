@file:Suppress("MemberVisibilityCanBePrivate")

package com.simiacryptus.util

import org.apache.commons.io.input.buffer.CircularByteBuffer
import org.slf4j.LoggerFactory
import java.util.*
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.TargetDataLine

class AudioRecorder(
    val audioBuffer: Deque<ByteArray>,
    val secondsPerPacket: Double,
    val continueFn: () -> Boolean
) {
    val packetLength = (audioFormat.frameRate * audioFormat.frameSize * secondsPerPacket).toInt()

    fun run() {
        openMic().use { targetDataLine ->
            val buffer = ByteArray(packetLength)
            val circularBuffer = CircularByteBuffer(packetLength * 2)
            while (continueFn()) {
                var bytesRead = 0
                val endTime = (System.currentTimeMillis() + secondsPerPacket * 1000).toLong()
                while (bytesRead != -1 && System.currentTimeMillis() < endTime) {
                    bytesRead = targetDataLine.read(buffer, 0, buffer.size)
                    circularBuffer.add(buffer, 0, bytesRead)
                    while (circularBuffer.currentNumberOfBytes >= packetLength) {
                        val array = ByteArray(packetLength)
                        circularBuffer.read(array, 0, packetLength)
                        audioBuffer.add(array)
                    }
                }
            }
        }
    }

    companion object {
        val log = LoggerFactory.getLogger(AudioRecorder::class.java)

        val audioFormat = AudioFormat(16000f, 16, 1, true, false)

        fun openMic(): TargetDataLine {
            val targetDataLine = AudioSystem.getTargetDataLine(audioFormat)
            targetDataLine.open(audioFormat)
            targetDataLine.start()
            return targetDataLine
        }

    }

}