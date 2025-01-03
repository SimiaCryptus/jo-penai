@file:Suppress("unused")

package com.simiacryptus.jopenai.audio

import org.slf4j.LoggerFactory
import java.util.*

class PercentileLoudnessWindowBuffer(
    inputBuffer: Deque<ByteArray>,
    outputBuffer: Deque<ByteArray>,
    continueFn: () -> Boolean,
) : LoudnessWindowBuffer(inputBuffer, outputBuffer, continueFn) {
    // Logger instance for PercentileLoudnessWindowBuffer class
    private val log = LoggerFactory.getLogger(PercentileLoudnessWindowBuffer::class.java)


    // Required number of quiet windows
    private var quietWindowMax = 3

    // Threshold for quiet windows
    private var quietThreshold = 0.25

    // Maximum number of seconds to flush the buffer
    private var flushSeconds = 60.0

    // Minimum number of seconds to flush the buffer
    private var minSeconds = 1.0

    // List of RMS values currently in the buffer
    private val rmsHeap = ArrayList<Double>()

    // List of consecutive quiet window percentiles
    private val quietWindow = ArrayList<Double>()

    init {
        log.info("PercentileLoudnessWindowBuffer initialized with quietWindowMax: {}, quietThreshold: {}, flushSeconds: {}, minSeconds: {}",
            quietWindowMax, quietThreshold, flushSeconds, minSeconds)
    }

    override fun shouldOutput(): Boolean {
        val quietPacket =
            synchronized(outputPacketBuffer) { outputPacketBuffer.takeLast(quietWindowMax).reduce { a, b -> a + b } }
        val loudness = quietPacket.spectralEntropy
        // Binary search the RMS value in the rmsHeap list
        var index = rmsHeap.binarySearch(loudness)
        // If the index is negative, set it to the negative index - 1
        if (index < 0) index = -index - 1
        // Calculate the percentile of the RMS value
        val percentile = index.toDouble() / rmsHeap.size
        // Calculate the minimum buffer size
        val minBufferSize = AudioRecorder.audioFormat.frameRate * AudioRecorder.audioFormat.frameSize * minSeconds
        // If the buffer size is less than the minimum buffer size, add the percentile to the quiet window and add the RMS value to the rmsHeap list
        val sum = synchronized(outputPacketBuffer) { outputPacketBuffer.map { it.size }.sum() }
        if (minBufferSize > sum) {
            // Add the percentile to the quiet window
            quietWindow.add(percentile)
            // Add the RMS value to the rmsHeap list
            rmsHeap.add(loudness)
            // Sort the rmsHeap list
            rmsHeap.sort()
            log.debug("Buffer size below minimum. Added percentile: {}, loudness: {}", percentile, loudness)
            // Continue to the next iteration of the loop
            return false
        }
        // While the quiet window size is greater than or equal to the quiet window max, remove the first element
        while (quietWindow.size >= quietWindowMax) quietWindow.removeAt(0)
        // While the quiet window is not empty and the maximum value is greater than the quiet threshold, remove the first element
        while (quietWindow.isNotEmpty() && quietWindow.maxOrNull()!! > quietThreshold) quietWindow.removeAt(0)
        // If the percentile is less than the quiet threshold, add the percentile to the quiet window
        if (percentile < quietThreshold) {
            quietWindow.add(percentile)
            log.debug("Percentile below threshold. Added to quiet window: {}", percentile)
            // Otherwise, clear the quiet window
        } else {
            quietWindow.clear()
            log.debug("Percentile above threshold. Cleared quiet window")
        }
        // Log the RMS value, percentile, and quiet windows
        log.debug("Loudness: {}, percentile: {}, quiet windows: [{}] ({} samples)",
                  loudness, percentile, quietWindow.joinToString(", "), quietPacket.samples.size)
        // Calculate the maximum buffer size
        val maxBufferSize = AudioRecorder.audioFormat.frameRate * AudioRecorder.audioFormat.frameSize * flushSeconds
        // If the buffer size is greater than the maximum buffer size or the quiet window size is greater than or equal to the quiet window max,
        // add the converted raw to wav byte array to the output buffer, reset the buffer, clear the rmsHeap list, and clear the quiet window
        return if (sum > maxBufferSize || quietWindow.size >= quietWindowMax) {
            log.info("Buffer size exceeded maximum or quiet window max reached. Flushing buffer")
            // Clear the rmsHeap list
            rmsHeap.clear()
            // Clear the quiet window
            quietWindow.clear()
            // Otherwise, add the RMS value to the rmsHeap list and sort it
            true
        } else {
            // Add the RMS value to the rmsHeap list
            rmsHeap.add(loudness)
            // Sort the rmsHeap list
            rmsHeap.sort()
            log.debug("Buffer size within limits. Added loudness: {}", loudness)
            false
        }
    }


}