package com.sup.dev.android.utils;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsBitmap;
import com.sup.dev.java.libs.debug.Debug;

public class UtilsMetadata{

    private final MediaMetadataRetriever retriever;

    public UtilsMetadata() {
        retriever = null;
    }

    public UtilsMetadata(String path) {
        retriever = wrap(path);
    }

    public void parse(String path) {

        MediaMetadataRetriever retriever = wrap(path);

        Debug.INSTANCE.print("getEmbeddedPicture", retriever.getEmbeddedPicture());
        Debug.INSTANCE.print("getFrameAtTime", retriever.getFrameAtTime());
        Debug.INSTANCE.print("CD_TRACK_NUMBER", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER));
        Debug.INSTANCE.print("ALBUM", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM));
        Debug.INSTANCE.print("ARTIST", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST));
        Debug.INSTANCE.print("AUTHOR", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR));
        Debug.INSTANCE.print("COMPOSER", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER));
        Debug.INSTANCE.print("DATE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE));
        Debug.INSTANCE.print("GENRE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE));
        Debug.INSTANCE.print("TITLE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE));
        Debug.INSTANCE.print("YEAR", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR));
        Debug.INSTANCE.print("DURATION", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
        Debug.INSTANCE.print("NUM_TRACKS", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS));
        Debug.INSTANCE.print("WRITER", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER));
        Debug.INSTANCE.print("MIMETYPE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE));
        Debug.INSTANCE.print("ALBUMARTIST", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST));
        Debug.INSTANCE.print("DISC_NUMBER", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER));
        Debug.INSTANCE.print("COMPILATION", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPILATION));
        Debug.INSTANCE.print("HAS_AUDIO", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO));
        Debug.INSTANCE.print("HAS_VIDEO", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO));
        Debug.INSTANCE.print("VIDEO_WIDTH", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
        Debug.INSTANCE.print("VIDEO_HEIGHT", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
        Debug.INSTANCE.print("BITRATE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE));
        Debug.INSTANCE.print("LOCATION", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            Debug.INSTANCE.print("VIDEO_ROTATION", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Debug.INSTANCE.print("CAPTURE_FRAMERATE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE));

    }

    //
    //  Static
    //

    private MediaMetadataRetriever wrap(String path) {
        MediaMetadataRetriever retriever = new MediaMetadataRetriever();
        retriever.setDataSource(path);
        return retriever;
    }

    public Bitmap getPreview(String path) {
        MediaMetadataRetriever retriever = wrap(path);

        byte[] embeddedPicture = retriever.getEmbeddedPicture();
        if (embeddedPicture != null) return ToolsBitmap.decode(embeddedPicture);

        return retriever.getFrameAtTime();
    }

    public int getVideoWidth(String path) {
        return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
    }

    public int getVideoHeight(String path) {
        return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
    }

    public int getDurationMs(String path) {
        return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }

    public int getTrackCount(String path) {
        return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER));
    }

    public String getMimeType(String path) {
        return wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
    }

    public boolean hasAudio(String path) {
        return wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO).toLowerCase().equals("yes");
    }

    public boolean hasVideo(String path) {
        return wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO).toLowerCase().equals("yes");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public int getVideoRotation(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        return 0;
    }

    //
    //  Inited
    //

    public Bitmap getPreview() {
        byte[] embeddedPicture = retriever.getEmbeddedPicture();
        if (embeddedPicture != null) return ToolsBitmap.decode(embeddedPicture);

        return retriever.getFrameAtTime();
    }

    public int getVideoWidth() {
        return Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
    }

    public int getVideoHeight() {
        return Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
    }

    public int getDurationMs() {
        return Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION));
    }

    public int getTrackCount() {
        return Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER));
    }

    public String getMimeType() {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE);
    }

    public boolean hasAudio() {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO).toLowerCase().equals("yes");
    }

    public boolean hasVideo() {
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO).toLowerCase().equals("yes");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public int getVideoRotation() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            return Integer.parseInt(retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION));
        return 0;
    }


}
