package com.simiacryptus.jopenai.audio

import org.apache.commons.io.input.buffer.CircularByteBuffer
import org.slf4j.LoggerFactory
import java.util.*
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioSystem
import javax.sound.sampled.TargetDataLine

class AudioRecorder(
    private val audioBuffer: Deque<ByteArray>,
    private val secondsPerPacket: Double,
    val continueFn: () -> Boolean,
) {
    private val packetLength = (audioFormat.frameRate * audioFormat.frameSize * secondsPerPacket).toInt()

    fun run() {
        val targetDataLine = openMic()
        try {
            log.info("Audio recording started with packet length: {} bytes", packetLength)
            val buffer = ByteArray(packetLength)
            val circularBuffer = CircularByteBuffer(packetLength * 2)
            while (continueFn()) {
                try {
                    var bytesRead = 0
                    val endTime = (System.currentTimeMillis() + secondsPerPacket * 1000).toLong()
                    while (bytesRead != -1 && System.currentTimeMillis() < endTime) {
                        bytesRead = targetDataLine.read(buffer, 0, buffer.size)
                        log.debug("Read {} bytes from microphone", bytesRead)
                        circularBuffer.add(buffer, 0, bytesRead)
                        while (circularBuffer.currentNumberOfBytes >= packetLength) {
                            val array = ByteArray(packetLength)
                            circularBuffer.read(array, 0, packetLength)
                            audioBuffer.add(array)
                            log.debug("Added packet to audio buffer, buffer size: {}", audioBuffer.size)
                        }
                    }
                } catch (e: Exception) {
                    log.error("Error during audio recording", e)
                }
            }
            log.info("Audio recording stopped, final buffer size: {}", audioBuffer.size)
        } finally {
            targetDataLine.close()
            log.info("Microphone line closed")
        }
    }

    companion object {
        private val log = LoggerFactory.getLogger(AudioRecorder::class.java)

        val audioFormat = AudioFormat(16000f, 16, 1, true, false)

        fun openMic(): TargetDataLine {
            val targetDataLine = AudioSystem.getTargetDataLine(audioFormat)
            targetDataLine.open(audioFormat)
            targetDataLine.start()
            log.info("Microphone line opened with format: {}", audioFormat)
            return targetDataLine
        }

    }

}