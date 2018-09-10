package com.sup.dev.android.libs.image_loader

import android.widget.ImageView
import com.sup.dev.java.classes.providers.Provider1


class ImageLoaderId(private val imageId: Long) : ImageLoaderA() {

    init {
        setKey(asKey(imageId))
    }

    override fun load(): ByteArray? {
        return loader!!.provide(imageId)
    }

    companion object {

        var loader: Provider1<Long, ByteArray>? = null

        fun load(imageId: Long, onLoaded: (ByteArray) -> Unit) {
            load(imageId, null, onLoaded)
        }

        @JvmOverloads
        fun load(imageId: Long, vImage: ImageView? = null, onLoaded:(ByteArray) -> Unit = {}) {
            ImageLoader.load(ImageLoaderId(imageId).setImage(vImage).onLoaded(onLoaded))
        }

        fun clearCash(imageId: Long) {
            ImageLoader.bitmapCash.remove(asKey(imageId))
        }

        private fun asKey(imageId: Long): String {
            return "imgId_$imageId"
        }

        fun replace(imageId: Long, bytes: ByteArray) {
            ImageLoader.bitmapCash.replace(asKey(imageId), bytes)
        }
    }


}