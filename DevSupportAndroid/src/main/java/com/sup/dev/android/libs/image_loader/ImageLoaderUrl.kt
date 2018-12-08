package com.sup.dev.android.libs.image_loader

import com.sup.dev.java.libs.debug.err
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
            error(e)
            return null
        }

    }
}