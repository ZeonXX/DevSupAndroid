package com.sup.dev.android.utils.implementations;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsMetadata;
import com.sup.dev.java.libs.debug.Debug;

public class UtilsMetadataImpl implements UtilsMetadata {

    private final MediaMetadataRetriever retriever;

    public UtilsMetadataImpl() {
        retriever = null;
    }

    public UtilsMetadataImpl(String path) {
        retriever = wrap(path);
    }

    @Override
    public void parse(String path) {

        MediaMetadataRetriever retriever = wrap(path);

        Debug.log("getEmbeddedPicture", retriever.getEmbeddedPicture());
        Debug.log("getFrameAtTime", retriever.getFrameAtTime());
        Debug.log("CD_TRACK_NUMBER", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER));
        Debug.log("ALBUM", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        Debug.log("ARTIST", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        Debug.log("AUTHOR", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR));
        Debug.log("COMPOSER", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER));
        Debug.log("DATE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE));
        Debug.log("GENRE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
        Debug.log("TITLE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        Debug.log("YEAR", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR));
        Debug.log("DURATION", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        Debug.log("NUM_TRACKS", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS));
        Debug.log("WRITER", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER));
        Debug.log("MIMETYPE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE));
        Debug.log("ALBUMARTIST", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST));
        Debug.log("DISC_NUMBER", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER));
        Debug.log("COMPILATION", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPILATION));
        Debug.log("HAS_AUDIO", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO));
        Debug.log("HAS_VIDEO", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO));
        Debug.log("VIDEO_WIDTH", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        Debug.log("VIDEO_HEIGHT", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        Debug.log("BITRATE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
        Debug.log("LOCATION", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            Debug.log("VIDEO_ROTATION", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Debug.log("CAPTURE_FRAMERATE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE));

    }

    //
    //  Static
    //

    private MediaMetadataRetriever wrap(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        return retriever;
    }

    @Override
    public Bitmap getPreview(String path) {
        MediaMetadataRetriever retriever = wrap(path);

        byte[] embeddedPicture = retriever.getEmbeddedPicture();
        if (embeddedPicture != null) return SupAndroid.di.utilsBitmap().decode(embeddedPicture);

        return retriever.getFrameAtTime();
    }

    @Override
    public int getVideoWidth(String path) {
        return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
    }

    @Override
    public int getVideoHeight(String path) {
        return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
    }

    @Override
    public int getDurationMs(String path) {
        return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }

    @Override
    public int getTrackCount(String path) {
        return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER));
    }

    @Override
    public String getMimeType(String path) {
        return wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
    }

    @Override
    public boolean hasAudio(String path) {
        return wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO).toLowerCase().equals("yes");
    }

    @Override
    public boolean hasVideo(String path) {
        return wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO).toLowerCase().equals("yes");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public int getVideoRotation(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        return 0;
    }

    //
    //  Inited
    //

    @Override
    public Bitmap getPreview() {
        byte[] embeddedPicture = retriever.getEmbeddedPicture();
        if (embeddedPicture != null) return SupAndroid.di.utilsBitmap().decode(embeddedPicture);

        return retriever.getFrameAtTime();
    }

    @Override
    public int getVideoWidth() {
        return Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
    }


    @Override
    public int getVideoHeight() {
        return Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
    }

    @Override
    public int getDurationMs() {
        return Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }

    @Override
    public int getTrackCount() {
        return Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER));
    }

    @Override
    public String getMimeType() {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
    }

    @Override
    public boolean hasAudio() {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO).toLowerCase().equals("yes");
    }

    @Override
    public boolean hasVideo() {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO).toLowerCase().equals("yes");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public int getVideoRotation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            return Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        return 0;
    }


}
