package com.sup.dev.android.libs.image_loader

class ImageLoaderId(val imageId: Long) : ImageLoaderA() {


    companion object {
        var loader: (Long) -> ByteArray? = { throw RuntimeException("You must set your own loader!") }
    }

    override fun getKey() = "imgId_${imageId}_${w}_${h}"

    override fun load(): ByteArray? {
        if (imageId < 1) return null
        return loader.invoke(imageId)
    }


}