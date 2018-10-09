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

    fun load(id: Long): ImageLoaderA {
        return ImageLoaderId(id)
    }

    //
    //  Loader  Id
    //

    fun loadGif(imageId: Long, gifId: Long, w: Int = 0, h: Int = 0, vImage: ImageView, vGifProgressBar: View? = null) {
        load(imageId).size(w, h).gifProgressBar(vGifProgressBar).into(vImage)
        {
            if (gifId > 0) load(gifId).asGif().gifProgressBar(vGifProgressBar).holder(vImage.drawable).into(vImage)
        }
    }


}