package com.sup.dev.android.libs.image_loader

import android.widget.ImageView
import com.sup.dev.java.libs.debug.Debug
import com.sup.dev.java.tools.ToolsFiles
import java.io.File
import java.io.IOException


class ImageLoaderFile(private val file: File) : ImageLoaderA() {

    init {
        setKey(asKey(file))
    }

    override fun load(): ByteArray? {
        try {
            return ToolsFiles.readFile(file)
        } catch (e: IOException) {
            Debug.log(e)
            return null
        }

    }

    companion object {

        fun load(file: File, onLoaded: (ByteArray)->Unit) {
            load(file, null, onLoaded)
        }

        @JvmOverloads
        fun load(file: File, vImage: ImageView? = null, onLoaded: (ByteArray)->Unit = {}) {
            ImageLoader.load(ImageLoaderFile(file).setImage(vImage).setOnLoaded(onLoaded))
        }

        fun clearCash(file: File) {
            ImageLoader.bitmapCash.remove(asKey(file))
        }

        private fun asKey(file: File): String {
            return "file_" + file.absolutePath
        }

        fun replace(file: File, bytes: ByteArray) {
            ImageLoader.bitmapCash.replace(asKey(file), bytes)
        }
    }
}