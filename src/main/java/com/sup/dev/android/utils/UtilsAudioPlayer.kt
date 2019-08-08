package com.sup.dev.android.utils

import android.content.Context
import android.media.AudioFormat
import android.media.AudioManager
import android.media.AudioManager.STREAM_MUSIC
import android.media.AudioTrack
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.tools.ToolsThreads
import android.content.Context.AUDIO_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.java.libs.debug.err
import java.lang.IllegalStateException


class UtilsAudioPlayer {

    var onStep: (Long) -> Unit = {}
    private val stepTime = 100L
    private val sampleRate = 8000
    private val frameSize = 160
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

        val audioTrack = AudioTrack(STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM)
        var stop = false

        init {
            ToolsThreads.thread {
                var offset = 0

                audioTrack.play()
                try {
                    while (!stop && offset < byteArray.size) {
                        if (audioTrack.playState == AudioTrack.PLAYSTATE_PLAYING) {
                            audioTrack.write(byteArray, offset, frameSize)
                            offset += frameSize
                            ToolsThreads.main {
                                try {
                                    onStep.invoke(audioTrack.getPlaybackHeadPosition() / (sampleRate/1000L))
                                } catch (e: Exception) {
                                    if(e !is IllegalStateException)err(e)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    if(e !is IllegalStateException)err(e)
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