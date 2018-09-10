package com.sup.dev.android.libs.image_loader

import android.widget.ImageView
import com.sup.dev.java.libs.debug.Debug
import com.sup.dev.java.tools.ToolsNetwork
import java.io.IOException


class ImageLoaderUrl(private val url: String) : ImageLoaderA() {

    init {
        setKey(asKey(url))
    }

    override fun load(): ByteArray? {
        try {
            return ToolsNetwork.getBytesFromURL(url)
        } catch (e: IOException) {
            Debug.log(e)
            return null
        }

    }

    companion object {

        fun load(url: String, onLoaded: (ByteArray) -> Unit) {
            load(url, null, onLoaded)
        }

        @JvmOverloads
        fun load(url: String, vImage: ImageView? = null, onLoaded: (ByteArray) -> Unit = {}) {
            ImageLoader.load(ImageLoaderUrl(url).setImage(vImage).onLoaded(onLoaded))
        }

        fun clearCash(url: String) {
            ImageLoader.bitmapCash.remove(asKey(url))
        }

        fun replace(url: String, bytes: ByteArray) {
            ImageLoader.bitmapCash.replace(asKey(url), bytes)
        }

        private fun asKey(url: String): String {
            return "url_$url"
        }
    }


}