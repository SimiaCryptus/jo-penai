@file:Suppress("unused")

package com.simiacryptus.jopenai.audio

import org.slf4j.LoggerFactory
import java.util.*
import kotlin.collections.ArrayList

abstract class WindowBuffer(
    private val inputBuffer: Queue<AudioPacket>,
    private val outputBuffer: Queue<AudioPacket>,
    var continueFn: () -> Boolean,
    var memoryPackets: Int = 100,
    val onPacket: (AudioPacket) -> Unit
) {
    protected var outputPacketBuffer = ArrayList<AudioPacket>()
    protected var lastOutputBuffer : ArrayList<AudioPacket>? = null

    fun run() {
        log.info("Starting LoudnessWindowBuffer processing loop.")
        while (this.continueFn() || inputBuffer.isNotEmpty()) {
            val audioPacket = inputBuffer.poll()
            if (null == audioPacket) {
                Thread.sleep(1)
            } else {
                processPacket(audioPacket)
            }
        }
        log.info("LoudnessWindowBuffer processing loop ended.")
    }

    protected open fun processPacket(packet: AudioPacket) {
        synchronized(this.outputPacketBuffer) {
            this.outputPacketBuffer.add(packet)
            while (this.outputPacketBuffer.size > memoryPackets) this.outputPacketBuffer.removeAt(0)
        }
        onPacket(packet)
        if (shouldOutput()) {
            val reduced = flushOutput()
            outputBuffer.add(reduced)
            log.debug("Output packet size: ${reduced.samples.size}.")
        }
    }

    protected open fun flushOutput(): AudioPacket {
        val reduced = this.outputPacketBuffer.reduce { a, b -> a + b }
        lastOutputBuffer = this.outputPacketBuffer
        this.outputPacketBuffer = ArrayList()
        return reduced
    }


    abstract fun shouldOutput(): Boolean

    companion object {
        private val log = LoggerFactory.getLogger(WindowBuffer::class.java)
    }
}