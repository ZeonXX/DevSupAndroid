package com.sup.dev.android.libs.image_loader

class ImageLoaderId(private val imageId: Long) : ImageLoaderA() {


    companion object {
        var loader:(Long) -> ByteArray? = { throw RuntimeException("You must set your own loader!") }
    }

    init {
        setKey("imgId_$imageId")
    }

    override fun load(): ByteArray? {
        return loader.invoke(imageId)
    }


}