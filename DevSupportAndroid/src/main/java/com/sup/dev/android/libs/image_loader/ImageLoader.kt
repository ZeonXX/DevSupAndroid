package com.sup.dev.android.libs.image_loader

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.View
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsBitmap
import com.sup.dev.android.tools.ToolsCash
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.support.DrawableGif
import com.sup.dev.java.classes.collections.CashBytes
import com.sup.dev.java.libs.debug.Debug
import com.sup.dev.java.tools.ToolsBytes
import com.sup.dev.java.tools.ToolsThreads
import java.util.ArrayList
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ImageLoader {

    internal val bitmapCash = CashBytes<Any>(1024 * 1024 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 20 else 5)
    internal val turn = ArrayList<ImageLoaderA>()
    internal var threadPool: ThreadPoolExecutor = ThreadPoolExecutor(2, 4, 1, TimeUnit.MINUTES, LinkedBlockingQueue())

    //
    //  Public
    //

    fun unsubscribe(vImage: ImageView) {
        var i = 0
        while (i < turn.size) {
            if (turn[i].vImage === vImage) {
                turn.removeAt(i--)
            }
            i++
        }

    }

    fun load(loader: ImageLoaderA) {

        var bytes = if (loader.noLoadFromCash) null else bitmapCash[loader.getKey()]
        if (bytes == null && !loader.noLoadFromCash) bytes = loader.getFromCash()

        if (loader.vGifProgressBar != null) loader.vGifProgressBar!!.visibility = if (loader.isGif) View.VISIBLE else View.INVISIBLE

        if (bytes != null) {
            loader.isGif = loader.isGif || ToolsBytes.isGif(bytes)
            bitmapCash.reorderTop(loader.getKey())
            putImage(loader, parseImage(loader, bytes), false, bytes)
            return
        }

        if (loader.vImage != null) {
            if (!loader.noHolder) {
                if (loader.holder is Int) loader.vImage!!.setImageResource(loader.holder as Int)
                else if (loader.holder is Drawable) loader.vImage!!.setImageDrawable(loader.holder as Drawable)
                else if (loader.holder is Bitmap) loader.vImage!!.setImageBitmap(loader.holder as Bitmap)
                else if (loader.w != 0 && loader.h != 0) {
                    val bitmap = Bitmap.createBitmap(loader.w, loader.h, Bitmap.Config.ARGB_4444)
                    bitmap.eraseColor(ToolsResources.getColor(R.color.focus))
                    loader.vImage!!.setImageBitmap(bitmap)
                } else {
                    loader.vImage!!.setImageDrawable(ColorDrawable(ToolsResources.getColor(R.color.focus)))
                }
            }

            unsubscribe(loader.vImage!!)

        } else {
            loader.onSetHolder.invoke()
        }

        turn.add(loader)

        for (l in turn) if (l.isKey(loader.getKey()) && l !== loader) return

        threadPool.execute {
            try {
                loadNow(loader)
            } catch (ex: Throwable) {
                Debug.log(ex)
            }
        }
    }

    fun clear(key: String) {
        ToolsCash.clear(key)
        bitmapCash.remove(key)
    }

    //
    //  Methods
    //

    private fun loadNow(loader: ImageLoaderA) {

        if (!turn.contains(loader)) return

        val loadedBytes = loader.startLoad()
        if (loadedBytes == null) return
        var bytes = loadedBytes
        var bitmap: Bitmap? = null

        loader.isGif = loader.isGif || ToolsBytes.isGif(loadedBytes)

        if (!loader.isGif) {
            bitmap = parseImage(loader, loadedBytes)
            if (loader.cashScaledBytes) bytes = ToolsBitmap.toJPGBytes(bitmap, 100)
        }

        ToolsThreads.main {

            if (!loader.noCash) bitmapCash.add(loader.getKey(), bytes)
            var i = 0
            while (i < turn.size) {
                val l = turn[i]
                if (l.isKey(loader.getKey())) {
                    turn.removeAt(i--)
                    putImage(l, bitmap, true, bytes)
                }
                i++
            }

            Unit

        }

    }

    private fun parseImage(loader: ImageLoaderA, bytes: ByteArray): Bitmap {
        var bm = ToolsBitmap.decode(bytes, loader.w, loader.h, loader.options, loader.cropSquareCenter)!!
        if (loader.cropSquareCenter) bm = ToolsBitmap.cropCenterSquare(bm)
        return bm
    }

    private fun putImage(loader: ImageLoaderA, bm: Bitmap?, animate: Boolean, bytes: ByteArray) {
        ToolsThreads.main {
            if (loader.vImage != null && loader.isKey(loader.vImage!!.getTag())) {
                if (loader.isGif) {
                    DrawableGif(bytes, loader.vImage!!, loader.cropSquareCenter) {
                        if (loader.vImage != null && loader.isKey(loader.vImage!!.getTag())) loader.vImage!!.setImageDrawable(it)
                        if (loader.vGifProgressBar != null) loader.vGifProgressBar!!.visibility = View.INVISIBLE
                    }
                } else {
                   if(loader.vImage != null && bm != null) loader.vImage!!.setImageDrawable(DrawableImageLoader(loader.vImage!!.context, bm, animate && loader.fade))
                }
            }
            loader.onLoaded.invoke(bytes)
        }
    }

}
