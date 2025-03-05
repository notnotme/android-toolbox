package com.notnotme.openmpt

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import java.io.InputStream
import java.util.Arrays
import java.util.Optional
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.concurrent.thread

class Player {
    private var executor    : Optional<Thread>  = Optional.empty()
    private val isRunning   : AtomicBoolean     = AtomicBoolean(false)
    private val isPaused    : AtomicBoolean     = AtomicBoolean(false)

    fun load(stream: InputStream): Boolean {
        return OpenMPT.load(stream.readBytes())
    }

    fun close() {
        OpenMPT.close()
    }

    fun play(sampleRate: Int) {
        executor.ifPresent {
            throw IllegalStateException("Player is already running")
        }

        executor = Optional.of(thread(name = "PlayerThread", priority = Thread.MAX_PRIORITY) {
            val audioAttributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build()

            val audioFormat = AudioFormat.Builder()
                .setSampleRate(sampleRate)
                .setChannelMask(AudioFormat.CHANNEL_OUT_STEREO)
                .setEncoding(AudioFormat.ENCODING_PCM_FLOAT)
                .build()

            val audioBufferSize = AudioTrack.getMinBufferSize(
                sampleRate,
                AudioFormat.CHANNEL_OUT_STEREO,
                AudioFormat.ENCODING_PCM_FLOAT
            )

            val buffer = FloatArray(audioBufferSize / Float.SIZE_BYTES)
            Arrays.fill(buffer, 0, buffer.size, 0.0f)

            val track = AudioTrack.Builder()
                .setAudioAttributes(audioAttributes)
                .setAudioFormat(audioFormat)
                .setBufferSizeInBytes(audioBufferSize)
                .setTransferMode(AudioTrack.MODE_STREAM)
                .setPerformanceMode(AudioTrack.PERFORMANCE_MODE_LOW_LATENCY)
                .build().also {
                    it.play()
                }

            isRunning.set(true)
            while (isRunning.get()) {
                if (isPaused.get()) {
                    try {
                        Thread.sleep(25)
                    } catch (ignored: InterruptedException) {}

                    continue
                }

                OpenMPT.decode(buffer, sampleRate)
                track.write(buffer, 0, buffer.size, AudioTrack.WRITE_BLOCKING)
                Arrays.fill(buffer, 0, buffer.size, 0.0f)
            }

            track.stop()
            track.release()

            executor = Optional.empty()
        })
    }

    fun pause(value: Boolean) {
        isPaused.set(value)
    }

    fun isPaused(): Boolean {
        return isPaused.get()
    }

    fun isStopped(): Boolean {
        return executor.isEmpty
    }

    fun stop() {
        isRunning.set(false)
        executor.ifPresent(Thread::join)
    }

    fun getRow(): Int {
        return if (isRunning.get()) {
            OpenMPT.getRow()
        } else {
            0
        }
    }

    fun getPattern(): Int {
        return if (isRunning.get()) {
            OpenMPT.getPattern()
        } else {
            0
        }
    }
}