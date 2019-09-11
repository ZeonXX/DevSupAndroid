package com.sup.dev.android.utils

import android.media.AudioFormat
import android.media.AudioManager.STREAM_MUSIC
import android.media.AudioManager.STREAM_VOICE_CALL
import android.media.AudioTrack
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.tools.ToolsThreads


class UtilsAudioPlayer {

    var onStep: (Long) -> Unit = {}
    var useProximity = false
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
        ToolsAndroid.releaseAudioFocus()
    }

    fun resume() {
        subPlayer?.audioTrack?.play()
        ToolsAndroid.requestAudioFocus()
    }


    private inner class SubPlayer(
            val byteArray: ByteArray,
            val onStop: () -> Unit
    ) {

        var utilsProximity:UtilsProximity? = null
        var stop = false
        var offset = 0
        var audioTrack: AudioTrack? = null
        var playbackOffset = 0

        init {
            startPlay(STREAM_MUSIC)
            if (useProximity) {
                utilsProximity = UtilsProximity {
                    if (useProximity) {
                        startPlay(if (it) STREAM_VOICE_CALL else STREAM_MUSIC)
                    }
                }
            }
        }

        @Suppress("DEPRECATION")
        fun startPlay(stream: Int) {
            if(stop){
                utilsProximity?.release()
                return
            }
            if(this.audioTrack != null){
                playbackOffset += this.audioTrack!!.playbackHeadPosition
            }
            SupAndroid.activity!!.volumeControlStream = stream
            val audioTrack = AudioTrack(stream, sampleRate, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, minBufferSize, AudioTrack.MODE_STREAM)
            this.audioTrack = audioTrack
            ToolsThreads.thread {

                ToolsAndroid.requestAudioFocus()
                audioTrack.play()
                try {
                    while (!stop && this.audioTrack == audioTrack && offset < byteArray.size) {
                        if (audioTrack.playState == AudioTrack.PLAYSTATE_PLAYING) {
                            audioTrack.write(byteArray, offset, frameSize)
                            offset += frameSize
                            ToolsThreads.main {
                                try {
                                    onStep.invoke((audioTrack.playbackHeadPosition + playbackOffset) / (sampleRate / 1000L))
                                } catch (e: Exception) {
                                    if (e !is IllegalStateException) err(e)
                                }
                            }
                        }
                    }
                } catch (e: Exception) {
                    if (e !is IllegalStateException) err(e)
                }
                try {
                    audioTrack.stop()
                }catch (e:Exception){
                    err(e)
                }
                audioTrack.release()
                if (this.audioTrack == audioTrack || stop) {
                    stop = true
                    ToolsAndroid.releaseAudioFocus()
                    utilsProximity?.release()
                    ToolsThreads.main { onStop.invoke() }
                }
            }
        }

        fun stop() {
            stop = true
            utilsProximity?.release()
        }

    }


}