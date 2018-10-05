package com.sup.dev.android.libs.image_loader

class ImageLoaderBytes(key: Any, private val bytes: ByteArray) : ImageLoaderA() {

    init {
        setKey("bytes_$key")
    }

    override fun load(): ByteArray {
        return bytes
    }
}