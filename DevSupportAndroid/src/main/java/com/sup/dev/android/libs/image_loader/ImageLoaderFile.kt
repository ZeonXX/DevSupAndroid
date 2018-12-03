package com.sup.dev.android.libs.image_loader

import com.sup.dev.java.libs.debug.Debug
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.tools.ToolsFiles
import java.io.File
import java.io.IOException

class ImageLoaderFile(val file: File) : ImageLoaderA() {

    init {
        setKey("file_" + file.absolutePath)
    }

    override fun load(): ByteArray? {
        try {
            return ToolsFiles.readFile(file)
        } catch (e: IOException) {
            log(e)
            return null
        }

    }

}