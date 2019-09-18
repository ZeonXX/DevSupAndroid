package com.sup.dev.android.libs.image_loader

import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.tools.ToolsNetwork
import java.io.IOException


class ImageLoaderUrl(
        private val url: String
) : ImageLoaderA() {

    override fun getKey() = "url_${url}_${w}_${h}"

    override fun load(): ByteArray? {
        try {
            return ToolsNetwork.getBytesFromURL(url)
        } catch (e: IOException) {
            err(e)
            return null
        }

    }
}