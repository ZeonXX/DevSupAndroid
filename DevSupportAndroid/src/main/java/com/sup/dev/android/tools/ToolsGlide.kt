package com.sup.dev.android.tools

import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.GlideDrawable
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.sup.dev.android.app.SupAndroid
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.gif.GifDrawableTransformation



object ToolsGlide{

    fun resizeGif(gifBytes: ByteArray, w: Int, h: Int, centerCrop: Boolean, callback:(ByteArray)->Unit) {

        val bytes = Glide.with(SupAndroid.appContext)
                .load(gifBytes)
                .asGif()
                .toBytes()
                .transform(GifDrawableTransformation(CenterCrop(SupAndroid.appContext), Glide.get(SupAndroid.appContext).bitmapPool))
                .into(700, 700)
                .get()

        callback.invoke(bytes)



       // val glide = Glide.with(SupAndroid.appContext)
       //         .load(gifBytes)
       //         .override(w, h)
       // if (centerCrop) glide.centerCrop()
//
       // val gifDrawable = glide.into(w, h).get() as GifDrawable
       // callback.invoke(gifDrawable.data)

      //  ToolsThreads.main {
      //      glide.into(object : SimpleTarget<GlideDrawable>() {
      //          override fun onResourceReady(resource: GlideDrawable, glideAnimation: GlideAnimation<in GlideDrawable>) {
      //              val gifDrawable = resource as GifDrawable
      //
      //          }
      //      })
      //  }

    }


}