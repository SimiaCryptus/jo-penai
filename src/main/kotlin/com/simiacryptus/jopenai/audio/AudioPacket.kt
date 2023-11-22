package com.simiacryptus.jopenai.audio

import edu.emory.mathcs.jtransforms.fft.FloatFFT_1D
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.sound.sampled.AudioFileFormat
import javax.sound.sampled.AudioFormat
import javax.sound.sampled.AudioInputStream
import javax.sound.sampled.AudioSystem
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.sqrt

data class AudioPacket(
    val samples: FloatArray,
    val sampleRate: Int = AudioRecorder.audioFormat.frameRate.toInt(),
) {
    val duration: Double by lazy { samples.size.toDouble() / sampleRate }
    private val fft: FloatArray by lazy { fft(samples) }
    val rms: Double by lazy { rms(samples) }
    val size: Int by lazy { samples.size }
    val spectralEntropy: Double by lazy { spectralEntropy(fft) }
    @Suppress("unused")
    val zeroCrossings: Int by lazy {
        samples.toList().windowed(2).count { it[0] > 0 && it[1] < 0 || it[0] < 0 && it[1] > 0 }
    }
    val iec61672: Double by lazy {
        aWeightingFilter(
            fft, sampleRate, arrayOf(
                12200.0 * 12200.0, 20.6 * 20.6, 107.7 * 107.7, 737.9 * 737.9
            )
        ).map { it * it }.toFloatArray().average()
    }

    @Suppress("unused")
    fun spectrumWindowPower(minFrequency: Double, maxFrequency: Double): Double {
        val minIndex = (samples.size * minFrequency / sampleRate).toInt()
        val maxIndex = (samples.size * maxFrequency / sampleRate).toInt()
        return fft.sliceArray(minIndex until maxIndex).map { it * it }.average()
    }

    private fun aWeightingFilter(
        fft: FloatArray,
        sampleRate: Int,
        aWeightingConstants: Array<Double>,
    ): FloatArray {
        val aWeightingFilter = FloatArray(fft.size) { 0f }
        for (i in fft.indices) {
            val frequency = i * sampleRate.toFloat() / fft.size
            val numerator = aWeightingConstants[0] * frequency.toDouble().pow(4.0)
            val denominator = (frequency.toDouble().pow(2.0) + aWeightingConstants[1]) *
                    sqrt(
                        (frequency.toDouble().pow(2.0) + aWeightingConstants[2]) *
                                (frequency.toDouble().pow(2.0) + aWeightingConstants[3])
                    ) *
                    (frequency.toDouble().pow(2.0) + aWeightingConstants[0])
            val aWeight = numerator / denominator
            aWeightingFilter[i] = (fft[i] * aWeight.toFloat())
        }
        return aWeightingFilter
    }

    operator fun plus(packet: AudioPacket): AudioPacket {
        return AudioPacket(this.samples + packet.samples)
    }

    companion object {
        // Create a Logger instance for the AudioPump class
        private val log = LoggerFactory.getLogger(AudioPacket::class.java)

        fun spectralEntropy(floats: FloatArray): Double {
            val fft = fft(floats)
            val fftSize = fft.size / 2
            var sum = 0.0
            for (i in 0 until fftSize) {
                sum += fft[i].toDouble().pow(2.0)
            }
            var entropy = 0.0
            for (i in 0 until fftSize) {
                val p = fft[i].toDouble().pow(2.0) / sum
                entropy -= p * ln(p)
            }
            return entropy
        }

        // Function to convert raw audio data to a WAV file
        fun convertRawToWav(audio: ByteArray): ByteArray? {
            // Create an AudioInputStream from the raw audio data
            AudioInputStream(
                ByteArrayInputStream(audio),
                AudioRecorder.audioFormat,
                audio.size.toLong()
            ).use { audioInputStream ->
                // Create a ByteArrayOutputStream to store the WAV file
                val wavBuffer = ByteArrayOutputStream()
                // Write the AudioInputStream to the ByteArrayOutputStream
                AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, wavBuffer)
                // Return the WAV file as a ByteArray
                return wavBuffer.toByteArray()
            }
        }

        fun rms(samples: FloatArray) = sqrt(samples.sum() / (samples.size / 2.0))

        fun convertRaw(audio: ByteArray): FloatArray {
            // Create a ByteArrayInputStream from the raw audio data
            val byteArrayInputStream = ByteArrayInputStream(audio)
            // Create an AudioInputStream from the ByteArrayInputStream
            val audioInputStream =
                AudioInputStream(byteArrayInputStream, AudioRecorder.audioFormat, audio.size.toLong())
            // Create an AudioFloatInputStream from the AudioInputStream
            val audioFloatInputStream =
                AudioSystem.getAudioInputStream(AudioFormat.Encoding.PCM_SIGNED, audioInputStream)
            // Read all the bytes from the AudioFloatInputStream
            val samples = audioFloatInputStream.readAllBytes()
            // Calculate the sum of the squares of the samples
            val sum = (samples.indices step 2).map { i ->
                // Convert the bytes to a double
                val r = samples[i].toInt()
                val l = samples[i + 1].toInt()
                val sample = ((r and 0xff) or ((l and 0xff) shl 8)).toDouble() / 32768.0
                // Square the sample
                (sample * sample).toFloat()
            }.toTypedArray()
            return sum.toFloatArray()
        }

        fun convertFloatsToRaw(audio: FloatArray): ByteArray {
            val byteArray = ByteArray(audio.size * 2)
            // Iterate through the float samples
            for (i in audio.indices) {
                // Convert the float sample to an integer
                val sample = (audio[i] * 32768.0).toInt()
                // Convert the integer to bytes
                val r = (sample and 0xff).toByte()
                val l = ((sample shr 8) and 0xff).toByte()
                // Add the bytes to the ByteArray
                byteArray[i * 2] = r
                byteArray[i * 2 + 1] = l
            }
            // Return the ByteArray
            return byteArray
        }

        fun fft(input: FloatArray): FloatArray {
            val output = input.copyOf(input.size)
            val fft = FloatFFT_1D(output.size)
            fft.realForward(output)
            return output
        }
    }

}