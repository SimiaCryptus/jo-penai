@file:Suppress("unused")

package com.simiacryptus.jopenai.audio

import org.slf4j.LoggerFactory
import java.util.*
import javax.sound.sampled.AudioFormat
import kotlin.collections.ArrayList

abstract class WindowBuffer(
    private val inputBuffer: Queue<AudioPacket>,
    private val outputBuffer: Queue<AudioPacket>,
    var continueFn: () -> Boolean,
    private val audioFormat: AudioFormat,
    var memoryPackets: Int = 60,
    val onPacket: (AudioPacket) -> Unit
) {
    protected var outputPacketBuffer = ArrayList<AudioPacket>()
    protected var lastOutputBuffer : ArrayList<AudioPacket>? = null

    fun run() {
        log.info("Starting LoudnessWindowBuffer processing loop.")
        while (this.continueFn() || inputBuffer.isNotEmpty()) {
            val bytes = inputBuffer.poll()
            if (null == bytes) {
                Thread.sleep(1)
            } else {
                val packet = bytes // AudioPacket(AudioPacket.convertRaw(, audioFormat), audioFormat)
                synchronized(this.outputPacketBuffer) {
                    this.outputPacketBuffer.add(packet)
                    while (this.outputPacketBuffer.size > memoryPackets) this.outputPacketBuffer.removeAt(0)
                }
                onPacket(packet)
                if (shouldOutput()) {
                    val reduced = this.outputPacketBuffer.reduce { a, b -> a + b }
                    outputBuffer.add(reduced) // AudioPacket.convertRawToWav(AudioPacket.convertFloatsToRaw(reduced.samples), audioFormat)
                    lastOutputBuffer = this.outputPacketBuffer
                    this.outputPacketBuffer = ArrayList()
                    log.debug("Output packet size: ${reduced.samples.size}.")
                }
            }
        }
        log.info("LoudnessWindowBuffer processing loop ended.")
    }


    abstract fun shouldOutput(): Boolean

    companion object {
        private val log = LoggerFactory.getLogger(WindowBuffer::class.java)
    }
}