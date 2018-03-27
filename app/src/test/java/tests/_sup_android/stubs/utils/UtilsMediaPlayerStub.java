package tests._sup_android.stubs.utils;

import android.support.annotation.NonNull;

import com.sup.dev.android.utils.interfaces.UtilsMediaPlayer;

public class UtilsMediaPlayerStub implements UtilsMediaPlayer {
    @Override
    public void setMedia(@NonNull String mediaUri) {

    }

    @Override
    public void play(@NonNull String mediaUri) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void setLooping(boolean looping) {

    }

    @Override
    public void setAudioStreamType(int streamType) {

    }

    @Override
    public boolean isPlaying() {
        return false;
    }
}
