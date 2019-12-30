package com.sup.dev.android.libs.image_loader

import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.tools.ToolsNetwork
import java.io.IOException


class ImageLoaderUrl(
        private val url: String
) : ImageLink() {

    override fun equalsTo(imageLoader: ImageLink): Boolean {
        return url == (imageLoader as ImageLoaderUrl).url
    }

    override fun getKeyOfImage() = "url_${url}"

    override fun load(): ByteArray? {
        try {
            return ToolsNetwork.getBytesFromURL(url)
        } catch (e: IOException) {
            err(e)
            return null
        }

    }
}