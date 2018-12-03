package com.sup.dev.android.libs.image_loader

import android.widget.ImageView
import com.sup.dev.java.libs.debug.Debug
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.tools.ToolsNetwork
import java.io.IOException


class ImageLoaderUrl(private val url: String) : ImageLoaderA() {

    init {
        setKey("url_$url")
    }

    override fun load(): ByteArray? {
        try {
            return ToolsNetwork.getBytesFromURL(url)
        } catch (e: IOException) {
            log(e)
            return null
        }

    }
}