package com.sup.dev.android.libs.image_loader

class ImageLoaderId(val imageId: Long) : ImageLink() {

    companion object {
        var loader: (Long) -> ByteArray? = { throw RuntimeException("You must set your own loader!") }
    }

    override fun equalsTo(imageLoader: ImageLink): Boolean {
        return imageId == (imageLoader as ImageLoaderId).imageId
    }

    override fun getKeyOfImage() = "imgId_${imageId}"

    override fun load(): ByteArray? {
        if (imageId < 1) return null
        return loader.invoke(imageId)
    }

    override fun copyLocal() = ImageLoaderId(imageId)

}