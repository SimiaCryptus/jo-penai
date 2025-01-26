@file:Suppress("unused")

package com.simiacryptus.jopenai.audio

import org.slf4j.LoggerFactory
import java.util.*
import javax.sound.sampled.AudioFormat
import kotlin.collections.ArrayList

abstract class WindowBuffer(
    private val inputBuffer: Queue<ByteArray>,
    private val outputBuffer: Queue<ByteArray>,
    var continueFn: () -> Boolean,
    private val audioFormat: AudioFormat,
    var size: Int = 100,
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
                val packet = AudioPacket(AudioPacket.convertRaw(bytes, audioFormat), audioFormat)
                synchronized(this.outputPacketBuffer) {
                    this.outputPacketBuffer.add(packet)
                    while (this.outputPacketBuffer.size > size) this.outputPacketBuffer.removeAt(0)
                }
                if (shouldOutput()) {
                    val reduced = this.outputPacketBuffer.reduce { a, b -> a + b }
                    outputBuffer.add(AudioPacket.convertRawToWav(AudioPacket.convertFloatsToRaw(reduced.samples), audioFormat))
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