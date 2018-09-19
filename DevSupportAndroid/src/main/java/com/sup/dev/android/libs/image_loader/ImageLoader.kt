package com.sup.dev.android.libs.image_loader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsBitmap
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.java.classes.collections.CashBytes
import com.sup.dev.java.libs.debug.Debug
import com.sup.dev.java.tools.ToolsThreads
import java.util.ArrayList
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit


object ImageLoader {

    internal val bitmapCash = CashBytes<Any>(1024 * 1024 * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) 20 else 5)
    internal val turn = ArrayList<ImageLoaderA>()
    internal var threadPool: ThreadPoolExecutor = ThreadPoolExecutor(2, 4, 1, TimeUnit.MINUTES, LinkedBlockingQueue())

    fun OPTIONS_RGB_565(): BitmapFactory.Options {
        val opt = BitmapFactory.Options()
        opt.inPreferredConfig = Bitmap.Config.RGB_565
        return opt
    }

    //
    //  Public
    //

    fun unsubscribe(vImage: ImageView?) {

        if (vImage == null) return

        var i = 0
        while (i < turn.size) {
            if (turn[i].vImage === vImage)
                turn.removeAt(i--)
            i++
        }

    }

    fun load(loader: ImageLoaderA) {

        val bytes = if (loader.noLoadFromCash) null else bitmapCash[loader.getKey()]

        if (bytes != null) {
            bitmapCash.reorderTop(loader.getKey()!!)
            if (loader.onLoaded != null) loader.onLoaded.invoke(bytes)
            if (loader.vImage != null) putImage(loader, parseImage(loader, bytes), false)
            return
        }

        if (loader.vImage != null) {
            if (loader.holder > 0) {
                loader.vImage!!.setImageResource(loader.holder)
            } else if (loader.w != 0 && loader.h != 0) {
                val bitmap = Bitmap.createBitmap(loader.w, loader.h, Bitmap.Config.ARGB_4444)
                bitmap.eraseColor(ToolsResources.getColor(R.color.focus))
                loader.vImage!!.setImageBitmap(bitmap)
            } else {
                loader.vImage!!.setImageDrawable(ColorDrawable(ToolsResources.getColor(R.color.focus)))
            }

            unsubscribe(loader.vImage)

        } else {
            if (loader.onSetHolder != null) loader.onSetHolder.invoke()
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

    //
    //  Methods
    //

    private fun loadNow(loader: ImageLoaderA) {

        if (!turn.contains(loader)) return

        val loadedBytes = loader.startLoad()
        if(loadedBytes == null)return
        val bitmap = parseImage(loader, loadedBytes)
        val bytes = if (loader.cashScaledBytes) ToolsBitmap.toJPGBytes(bitmap, 100) else loadedBytes

        ToolsThreads.main {

            if (!loader.noCash) bitmapCash.add(loader.getKey(), bytes)
            var i = 0
            while (i < turn.size) {
                val l = turn[i]
                if (l.isKey(loader.getKey())) {
                    if (l.onLoaded != null) l.onLoaded.invoke(bytes)
                    if (l.vImage != null) putImage(l, bitmap, true)
                    turn.removeAt(i--)
                }
                i++
            }

            Unit

        }

    }

    private fun parseImage(loader: ImageLoaderA, bytes: ByteArray?): Bitmap {
        var bm = ToolsBitmap.decode(bytes, loader.w, loader.h, loader.options, loader.cropSquareCenter)!!
        if (loader.cropSquareCenter) bm = ToolsBitmap.cropCenterSquare(bm)
        return bm
    }

    private fun putImage(loader: ImageLoaderA, bm: Bitmap, animate: Boolean) {
        ToolsThreads.main {
            if (loader.isKey(loader.vImage!!.getTag()))
                loader.vImage!!.setImageDrawable(DrawableImageLoader(loader.vImage!!.getContext(), bm, animate && loader.fade))
        }
    }

}
