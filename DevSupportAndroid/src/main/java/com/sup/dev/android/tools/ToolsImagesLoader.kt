package com.sup.dev.android.tools

import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.SimpleTarget
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.glade.GlideApp
import com.sup.dev.android.libs.glade.GlideCallbackFinish
import com.sup.dev.android.libs.glade.GlideIdLoader
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.tools.ToolsThreads
import java.io.File
import java.io.InputStream




object ToolsImagesLoader {


    fun init() {
        Glide.get(SupAndroid.appContext!!).register(GlideIdLoader.GlideId::class.java, InputStream::class.java, GlideIdLoader.Factory())
    }

    fun glide()/*: RequestManager*/ {
      //  return Glide.with(SupAndroid.appContext!!)
    }

    fun load(file: File):GladeStub/*: DrawableRequestBuilder<File> */{
        return GladeStub()
      //  return glide()
      //          .load(file)
      //          .crossFade()
      //          .placeholder(R.color.focus)
    }

    //
    //  Loader  Id
    //

    var loaderId: (Long) -> ByteArray? = { throw RuntimeException("You must set your own loader!") }

    fun loadCash(id: Long, w: Int = 0, h: Int = 0) {
      // val load = glide().load(GlideIdLoader.GlideId(id))
      // if (w > 0 && h > 0) load.override(w, h)
      // log("XX Cash $id")
      // load.into(TargetStub())
    }

    fun load(id: Long, w: Int = 0, h: Int = 0):GladeStub/*: DrawableRequestBuilder<GlideIdLoader.GlideId>*/ {

        return GladeStub()
        //  log("XX Load $id")
      //  val glide = glide().load(GlideIdLoader.GlideId(id))
      //          .crossFade()
      //          .diskCacheStrategy(DiskCacheStrategy.ALL)
//
      //  if (w > 0 && h > 0) {
      //      val bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444)
      //      bitmap.eraseColor(ToolsResources.getColor(R.color.focus))
      //      glide.placeholder(BitmapDrawable(bitmap))
      //      glide.override(w, h)
      //  } else {
      //      glide.placeholder(R.color.focus)
      //  }
//
      //  return glide
    }

    fun loadGif(imageId: Long, gifId: Long, w: Int = 0, h: Int = 0, vImage: ImageView) {

      // load(imageId, w, h)
      //         .listener(GlideCallbackFinish {
      //             if (gifId > 0)
      //                 ToolsThreads.main(100) {
      //                     glide().load(GlideIdLoader.GlideId(gifId))
      //                             .dontAnimate()
      //                             .override(w, h)
      //                             .placeholder(vImage.drawable)
      //                             .into(vImage)
      //                 }
      //         })
      //         .into(vImage)
    }

    fun load(id: Long, callback: (ByteArray?) -> Unit) {
        ToolsThreads.thread {
            val bytes = loaderId.invoke(id)
            ToolsThreads.main { callback.invoke(bytes) }
        }
    }

    //
    //  Support
    //

    class GladeStub{

        fun override(w:Int, h:Int):GladeStub{
            return this
        }

        fun into(a:Any):GladeStub{
            return this
        }

        fun centerCrop():GladeStub{
            return this
        }

    }

  // class TargetStub<Y> : SimpleTarget<Y>() {
  //     override fun onResourceReady(resource: Y, glideAnimation: GlideAnimation<in Y>) {}
  // }


}