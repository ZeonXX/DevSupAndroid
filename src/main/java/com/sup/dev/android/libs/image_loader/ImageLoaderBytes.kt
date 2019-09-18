package com.sup.dev.android.libs.image_loader

class ImageLoaderBytes(
        private val key: Any,
        private val bytes: ByteArray
) : ImageLoaderA() {

    override fun getKey() = "bytes_${key}_${w}_${h}"

    override fun load(): ByteArray {
        return bytes
    }
}