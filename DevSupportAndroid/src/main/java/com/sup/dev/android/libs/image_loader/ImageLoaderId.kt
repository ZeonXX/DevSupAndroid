package com.sup.dev.android.libs.image_loader

import android.widget.ImageView


class ImageLoaderId(private val imageId: Long) : ImageLoaderA() {

    init {
        setKey(asKey(imageId))
    }

    override fun load(): ByteArray? {
        return loader!!.invoke(imageId)
    }

    override fun setOnLoaded(onLoaded: (ByteArray?) -> Unit): ImageLoaderId {
        super.setOnLoaded(onLoaded)
        return this
    }


    companion object {

        var loader: ((Long)->ByteArray)? = null

        fun load(imageId: Long, onLoaded: (ByteArray?) -> Unit) {
            load(imageId, null, onLoaded)
        }

        @JvmOverloads
        fun load(imageId: Long, vImage: ImageView? = null, onLoaded: (ByteArray?) -> Unit = {}) {
            ImageLoader.load(ImageLoaderId(imageId).setImage(vImage).setOnLoaded(onLoaded))
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