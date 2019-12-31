package com.sup.dev.android.libs.image_loader

import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.tools.ToolsFiles
import java.io.File
import java.io.IOException

class ImageLoaderFile(
        val file: File
) : ImageLink() {

    override fun equalsTo(imageLoader: ImageLink): Boolean {
       return file.absolutePath == (imageLoader as ImageLoaderFile).file.absolutePath
    }

    override fun getKeyOfImage() = "file_${file.absolutePath}"

    override fun load(): ByteArray? {
        try {
            return ToolsFiles.readFile(file)
        } catch (e: IOException) {
            err(e)
            return null
        }

    }
    override fun copyLocal() = ImageLoaderFile(file)

}