@file:Suppress("unused")

package com.simiacryptus.jopenai.audio

import org.slf4j.LoggerFactory
import java.util.*

abstract class LoudnessWindowBuffer(
    private val inputBuffer: Deque<ByteArray>,
    private val outputBuffer: Deque<ByteArray>,
    var continueFn: () -> Boolean,
) {

    val outputPacketBuffer = ArrayList<AudioPacket>()
    val recentPacketBuffer = ArrayList<AudioPacket>()
    private val packetLookback = 100

    // Main function of the AudioPump class
    fun run() {
        // Loop until the continueFn returns false
        while (this.continueFn() || inputBuffer.isNotEmpty()) {
            // Poll the input buffer for a byte array
            val bytes = inputBuffer.poll()
            // If the byte array is null, sleep for 1 millisecond and continue
            if (null == bytes) {
                Thread.sleep(1)
            } else {
                val packet = AudioPacket(AudioPacket.convertRaw(bytes))
                synchronized(outputPacketBuffer) { outputPacketBuffer.add(packet) }
                synchronized(recentPacketBuffer) {
                    recentPacketBuffer.add(packet)
                    while(recentPacketBuffer.size > packetLookback) recentPacketBuffer.removeAt(0)
                }
                if (shouldOutput()) {
                    // Add the converted raw to wav byte array to the output buffer
                    val reduced = synchronized(outputPacketBuffer) { outputPacketBuffer.reduce { a, b -> a + b } }
                    outputBuffer.add(AudioPacket.convertRawToWav(AudioPacket.convertFloatsToRaw(reduced.samples)))
                    synchronized(outputPacketBuffer) { outputPacketBuffer.clear() }
                }
            }
        }
    }


    abstract fun shouldOutput(): Boolean

    companion object {
        // Create a Logger instance for the AudioPump class
        private val log = LoggerFactory.getLogger(LoudnessWindowBuffer::class.java)

    }

}
