package com.sup.dev.android.utils

import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.os.Build
import android.support.annotation.RequiresApi
import com.sup.dev.android.tools.ToolsBitmap
import com.sup.dev.java.libs.debug.Debug

class UtilsMetadata(var retriever: MediaMetadataRetriever? = null) {

    constructor (path: String): this(wrap(path))

    fun parse(path: String) {

        val retriever = wrap(path)

        Debug.print("getEmbeddedPicture", retriever.getEmbeddedPicture())
        Debug.print("getFrameAtTime", retriever.getFrameAtTime())
        Debug.print("CD_TRACK_NUMBER", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER))
        Debug.print("ALBUM", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM))
        Debug.print("ARTIST", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST))
        Debug.print("AUTHOR", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_AUTHOR))
        Debug.print("COMPOSER", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPOSER))
        Debug.print("DATE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DATE))
        Debug.print("GENRE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_GENRE))
        Debug.print("TITLE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE))
        Debug.print("YEAR", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_YEAR))
        Debug.print("DURATION", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))
        Debug.print("NUM_TRACKS", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_NUM_TRACKS))
        Debug.print("WRITER", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_WRITER))
        Debug.print("MIMETYPE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE))
        Debug.print("ALBUMARTIST", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUMARTIST))
        Debug.print("DISC_NUMBER", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DISC_NUMBER))
        Debug.print("COMPILATION", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_COMPILATION))
        Debug.print("HAS_AUDIO", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO))
        Debug.print("HAS_VIDEO", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO))
        Debug.print("VIDEO_WIDTH", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH))
        Debug.print("VIDEO_HEIGHT", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT))
        Debug.print("BITRATE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_BITRATE))
        Debug.print("LOCATION", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_LOCATION))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1)
            Debug.print("VIDEO_ROTATION", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION))
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            Debug.print("CAPTURE_FRAMERATE", retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CAPTURE_FRAMERATE))

    }

    //
    //  Inited
    //

    fun getPreview(): Bitmap? {
        val embeddedPicture = retriever!!.getEmbeddedPicture()
        return if (embeddedPicture != null) ToolsBitmap.decode(embeddedPicture) else retriever!!.getFrameAtTime()

    }

    fun getVideoWidth(): Int {
        return Integer.parseInt(retriever!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH))
    }

    fun getVideoHeight(): Int {
        return Integer.parseInt(retriever!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT))
    }

    fun getDurationMs(): Int {
        return Integer.parseInt(retriever!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))
    }

    fun getTrackCount(): Int {
        return Integer.parseInt(retriever!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER))
    }

    fun getMimeType(): String {
        return retriever!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
    }

    fun hasAudio(): Boolean {
        return retriever!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO).toLowerCase() == "yes"
    }

    fun hasVideo(): Boolean {
        return retriever!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO).toLowerCase() == "yes"
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    fun getVideoRotation(): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) Integer.parseInt(retriever!!.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)) else 0
    }
    //
    //  Static
    //

    companion object {
        private fun wrap(path: String): MediaMetadataRetriever {
            val retriever = MediaMetadataRetriever()
            retriever.setDataSource(path)
            return retriever
        }

        fun getPreview(path: String): Bitmap? {
            val retriever = wrap(path)

            val embeddedPicture = retriever.embeddedPicture
            return if (embeddedPicture != null) ToolsBitmap.decode(embeddedPicture) else retriever.frameAtTime

        }

        fun getVideoWidth(path: String): Int {
            return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH))
        }

        fun getVideoHeight(path: String): Int {
            return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT))
        }

        fun getDurationMs(path: String): Int {
            return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION))
        }

        fun getTrackCount(path: String): Int {
            return Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_CD_TRACK_NUMBER))
        }

        fun getMimeType(path: String): String {
            return wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
        }

        fun hasAudio(path: String): Boolean {
            return wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_AUDIO).toLowerCase() == "yes"
        }

        fun hasVideo(path: String): Boolean {
            return wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_HAS_VIDEO).toLowerCase() == "yes"
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        fun getVideoRotation(path: String): Int {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) Integer.parseInt(wrap(path).extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_ROTATION)) else 0
        }

    }



}
