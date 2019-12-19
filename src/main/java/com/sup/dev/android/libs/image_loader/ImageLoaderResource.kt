package com.sup.dev.android.libs.image_loader

import android.widget.ImageView
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.java.libs.debug.err
import java.io.IOException

class ImageLoaderResource(
        val res: Int
) : ImageLoaderA() {

    init {
        noCash()
    }

    override fun getKey() = "res_${res}_${w}_${h}"

    override fun fastLoad(vImage: ImageView?): Boolean {
        if(vImage != null) {
            vImage.setImageResource(res)
            return true
        }else{
            return false
        }
    }

    override fun load(): ByteArray? {
        try {
            return ToolsResources.getDrawableAsBytes(res)
        } catch (e: IOException) {
            err(e)
            return null
        }

    }

}