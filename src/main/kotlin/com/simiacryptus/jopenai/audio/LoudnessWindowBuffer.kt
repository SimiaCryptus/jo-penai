@file:Suppress("unused")

package com.simiacryptus.jopenai.audio

import org.slf4j.LoggerFactory
import java.util.*
import javax.sound.sampled.AudioFormat

abstract class LoudnessWindowBuffer(
    private val inputBuffer: Deque<ByteArray>,
    private val outputBuffer: Deque<ByteArray>,
    var continueFn: () -> Boolean,
    val audioFormat: AudioFormat,
) {

    val outputPacketBuffer = ArrayList<AudioPacket>()
    val recentPacketBuffer = ArrayList<AudioPacket>()
    private val packetLookback = 100

    // Main function of the AudioPump class
    fun run() {
        log.info("Starting LoudnessWindowBuffer processing loop.")
        // Loop until the continueFn returns false
        while (this.continueFn() || inputBuffer.isNotEmpty()) {
            // Poll the input buffer for a byte array
            val bytes = inputBuffer.poll()
            // If the byte array is null, sleep for 1 millisecond and continue
            if (null == bytes) {
                //log.debug("Input buffer is empty, sleeping for 1 millisecond.")
                Thread.sleep(1)
            } else {
                log.debug("Processing byte array of size: ${bytes.size}.")
                val packet = AudioPacket(AudioPacket.convertRaw(bytes, audioFormat), audioFormat)
                synchronized(outputPacketBuffer) { outputPacketBuffer.add(packet) }
                synchronized(recentPacketBuffer) {
                    recentPacketBuffer.add(packet)
                    while (recentPacketBuffer.size > packetLookback) recentPacketBuffer.removeAt(0)
                }
                if (shouldOutput()) {
                    log.info("Output condition met, preparing to output.")
                    // Add the converted raw to wav byte array to the output buffer
                    val reduced = synchronized(outputPacketBuffer) { outputPacketBuffer.reduce { a, b -> a + b } }
                    log.debug("Reduced packet size: ${reduced.samples.size}.")
                    outputBuffer.add(AudioPacket.convertRawToWav(AudioPacket.convertFloatsToRaw(reduced.samples), audioFormat))
                    synchronized(outputPacketBuffer) { outputPacketBuffer.clear() }
                    log.info("Output buffer updated and outputPacketBuffer cleared.")
                }
            }
        }
        log.info("LoudnessWindowBuffer processing loop ended.")
    }


    abstract fun shouldOutput(): Boolean

    companion object {
        // Create a Logger instance for the AudioPump class
        private val log = LoggerFactory.getLogger(LoudnessWindowBuffer::class.java)

    }

}