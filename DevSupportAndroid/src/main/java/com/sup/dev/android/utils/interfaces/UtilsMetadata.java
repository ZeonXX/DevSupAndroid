package com.sup.dev.android.utils.interfaces;

import android.graphics.Bitmap;

public interface UtilsMetadata {

    void parse(String path);

    //
    //  Static
    //

    Bitmap getPreview(String path);

    int getVideoWidth(String path);

    int getVideoHeight(String path);

    int getDurationMs(String path);

    int getTrackCount(String path);

    String getMimeType(String path);

    boolean hasAudio(String path);

    boolean hasVideo(String path);

    int getVideoRotation(String path);

    //
    //  Inited
    //

    Bitmap getPreview();

    int getVideoWidth();

    int getVideoHeight();

    int getDurationMs();

    int getTrackCount();

    String getMimeType();

    boolean hasAudio();

    boolean hasVideo();

    int getVideoRotation();

}
