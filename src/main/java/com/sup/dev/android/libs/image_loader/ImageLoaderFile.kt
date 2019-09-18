package com.sup.dev.android.libs.image_loader

import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.tools.ToolsFiles
import java.io.File
import java.io.IOException

class ImageLoaderFile(
        val file: File
) : ImageLoaderA() {

    override fun getKey() = "file_${file.absolutePath}_${w}_${h}"

    override fun load(): ByteArray? {
        try {
            return ToolsFiles.readFile(file)
        } catch (e: IOException) {
            err(e)
            return null
        }

    }

}