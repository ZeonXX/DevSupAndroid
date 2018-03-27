package com.sup.dev.android.utils.interfaces;

import android.support.annotation.NonNull;

public interface UtilsMediaPlayer {

    void setMedia(@NonNull String mediaUri);

    //
    //  Control
    //

    void play(@NonNull String mediaUri);

    void pause();

    void resume();

    void stop();

    //
    //  Setters
    //

    void setLooping(boolean looping);

    void setAudioStreamType(int streamType);

    //
    //  Getters
    //

    boolean isPlaying();


}
