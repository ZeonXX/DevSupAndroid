package com.sup.dev.android.tools

import android.view.View
import android.widget.ImageView
import com.sup.dev.android.libs.image_loader.ImageLoaderA
import com.sup.dev.android.libs.image_loader.ImageLoaderFile
import com.sup.dev.android.libs.image_loader.ImageLoaderId
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

    fun loadGif(imageId: Long, gifId: Long, w: Int = 0, h: Int = 0, vImage: ImageView, vGifProgressBar: View? = null, onError:()->Unit={}) {
        load(imageId).size(w, h).gifProgressBar(vGifProgressBar).setOnError(onError).into(vImage) {
            if (gifId > 0) load(gifId).asGif().gifProgressBar(vGifProgressBar).holder(vImage.drawable).into(vImage)
        }
    }

    fun clear(imageId: Long) {
        load(imageId).clear()
    }


}