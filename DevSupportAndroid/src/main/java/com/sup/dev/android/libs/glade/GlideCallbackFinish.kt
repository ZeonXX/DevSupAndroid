package com.sup.dev.android.libs.glade

import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.sup.dev.java.libs.debug.Debug
import com.sup.dev.java.libs.debug.log
import java.lang.Exception

class GlideCallbackFinish<T, R>(val callback: () -> Unit) : RequestListener<T, R> {

    override fun onResourceReady(resource: R, model: T, target: Target<R>?, isFromMemoryCache: Boolean, isFirstResource: Boolean): Boolean {
        log("XXX GlideCallbackFinish onResourceReady")
        callback.invoke()
        return false
    }

    override fun onException(e: Exception?, model: T, target: Target<R>?, isFirstResource: Boolean): Boolean {
        log("XXX GlideCallbackFinish onException")
        if(e != null) Debug.log(e)
        else Debug.error("Exception while loading glide image exception[$e] model[$model] target[$target] isFirstResource[$isFirstResource]")
        return false
    }


}