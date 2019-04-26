package com.sup.dev.android.tools

import android.view.View
import android.widget.ImageView
import com.sup.dev.android.libs.image_loader.ImageLoaderA
import com.sup.dev.android.libs.image_loader.ImageLoaderFile
import com.sup.dev.android.libs.image_loader.ImageLoaderId
import com.sup.dev.java.tools.ToolsMath
import java.io.File


object ToolsImagesLoader {

    fun load(file: File): ImageLoaderA {
        return ImageLoaderFile(file)
    }


    //
    //  Loader  Id
    //

    fun load(id: Long): ImageLoaderA {
        return ImageLoaderId(id)
    }

    fun loadGif(
            imageId: Long,
            gifId: Long,
            w: Int = 0,
            h: Int = 0,
            vImage: ImageView,
            vGifProgressBar: View? = null,
            sizeArd: Float = 1f,
            minGifSize: Float = ToolsView.dpToPx(128),
            onError: (() -> Unit)? = null
    ) {
        var sizeArd = sizeArd

        if (gifId > 0 && (w < minGifSize || h < minGifSize)) sizeArd = minGifSize/ToolsMath.min(w, h)

        if (imageId > 0) {
            load(imageId).sizeArd(sizeArd).size(w, h).gifProgressBar(vGifProgressBar).setOnError(onError).into(vImage) {
                if (gifId > 0) load(gifId).showGifLoadingProgress().sizeArd(sizeArd).gifProgressBar(vGifProgressBar).holder(vImage.drawable).into(vImage)
            }
        } else {
            if (gifId > 0) load(gifId).showGifLoadingProgress().sizeArd(sizeArd).size(w, h).gifProgressBar(vGifProgressBar).holder(vImage.drawable).into(vImage)
        }
    }

    fun clear(imageId: Long) {
        load(imageId).clear()
    }


}