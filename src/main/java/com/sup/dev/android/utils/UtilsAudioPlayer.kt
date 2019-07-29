package com.sup.dev.android.utils

import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioTrack
import com.sup.dev.java.tools.ToolsThreads

class UtilsAudioPlayer {

    var onStep: (Long)-> Unit = {}
    private val stepTime = 100L
    private val sampleRate = 8000
    private val minBufferSize = AudioTrack.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT)

    private var subPlayer: SubPlayer? = null

    fun play(bytes: ByteArray, onStop: () -> Unit = {}) {
        subPlayer = SubPlayer(bytes, onStop)
    }

    fun stop() {
        subPlayer?.stop()
        subPlayer = null
    }

    fun pause() {
        subPlayer?.audioTrack?.pause()
    }

    fun resume() {
        subPlayer?.audioTrack?.play()
    }


    private inner class SubPlayer(
            val byteArray: ByteArray,
            onStop: () -> Unit
    ) {

        val audioTrack = AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM)
        var stop = false

        init {
            ToolsThreads.thread {
                var offset = 0
                val startTime =  System.currentTimeMillis()
                var lastStep = startTime
                audioTrack.play()
                while (!stop && offset < byteArray.size) {
                    audioTrack.write(byteArray, offset, 160)
                    offset += 160
                    ToolsThreads.sleep(10)
                    if(lastStep + stepTime < System.currentTimeMillis()){
                        lastStep = System.currentTimeMillis()
                        ToolsThreads.main {onStep.invoke(System.currentTimeMillis() - startTime)}
                    }
                }
                audioTrack.stop()
                audioTrack.release()
                ToolsThreads.main { onStop.invoke() }
            }
        }

        fun stop() {
            stop = true
        }

    }


}