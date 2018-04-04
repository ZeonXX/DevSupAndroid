package com.sup.dev.android.utils.implementations;

import android.media.MediaPlayer;
import android.net.Uri;
import android.support.annotation.NonNull;

import java.io.IOException;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsMediaPlayer;
import com.sup.dev.java.libs.debug.Debug;

public class UtilsMediaPlayerImpl implements UtilsMediaPlayer, MediaPlayer.OnPreparedListener {

    private int streamType;
    private boolean looping;
    private MediaPlayer mediaPlayer;
    private boolean playing;

    public void setMedia(@NonNull String mediaUri) {
        setMedia(Uri.parse(mediaUri));
    }

    public void setMedia(@NonNull Uri mediaUri) {
        try {

            stop();
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(streamType);
            mediaPlayer.setLooping(looping);
            mediaPlayer.setDataSource(SupAndroid.di.appContext(), mediaUri);
            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setVolume(1, 1);

        } catch (IOException ex) {
            Debug.log(ex);
        }
    }

    //
    //  Control
    //

    public void play(@NonNull String mediaUri) {
        play(Uri.parse(mediaUri));
    }

    public void play(@NonNull Uri mediaUri) {
        setMedia(mediaUri);
        playing = true;
        mediaPlayer.prepareAsync();
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void resume() {
        mediaPlayer.start();
    }

    public void stop() {
        if (mediaPlayer == null) return;
        playing = false;
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    //
    //  Callback
    //

    @Override
    public void onPrepared(MediaPlayer mp) {
        if (mediaPlayer != null && mediaPlayer == mp) mediaPlayer.start();
    }

    //
    //  Setters
    //

    public void setLooping(boolean looping) {
        this.looping = looping;
        if (mediaPlayer != null) mediaPlayer.setLooping(looping);
    }

    public void setAudioStreamType(int streamType) {
        this.streamType = streamType;
        if (mediaPlayer != null) mediaPlayer.setAudioStreamType(streamType);
    }

    //
    //  Getters
    //

    public boolean isPlaying() {
        return playing;
    }




}
