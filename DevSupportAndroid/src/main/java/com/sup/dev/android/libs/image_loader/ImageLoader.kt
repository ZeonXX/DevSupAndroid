package com.sup.dev.android.libs.image_loader

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsBitmap
import com.sup.dev.android.tools.ToolsGif
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.support.DrawableGif
import com.sup.dev.java.classes.items.Item3
import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.tools.ToolsBytes
import com.sup.dev.java.tools.ToolsThreads
import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ImageLoader {

    internal val maxCashItemSize = 1024 * 1024 * 4
    internal val maxCashSize = 1024 * 1024 * 100
    internal val cash = ArrayList<Item3<String, Bitmap, ByteArray>>()
    internal var cashSize = 0
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

        if (loader.vGifProgressBar != null) loader.vGifProgressBar!!.visibility = View.INVISIBLE

        val cashItem = getFromCash(loader.getKey())
        if (cashItem != null) {
            putImage(loader, cashItem.a2, false, cashItem.a3)
            return
        }

        var bytes: ByteArray? = null
        if (!loader.noLoadFromCash) bytes = loader.getFromCash()


        putHolder(loader)

        if (bytes != null) {
            loader.isGif = loader.isGif || ToolsBytes.isGif(bytes)
            ToolsThreads.thread { putImage(loader, parseImage(loader, bytes), true, bytes) }
            return
        }

        turn.add(loader)

        for (l in turn) if (l.isKey(loader.getKey()) && l !== loader) return

        threadPool.execute {
            try {
                loadNow(loader)
            } catch (ex: Throwable) {
                err(ex)
            }
        }
    }

    private fun putHolder(loader: ImageLoaderA) {

        if (loader.vGifProgressBar != null) loader.vGifProgressBar!!.visibility = if (loader.isGif) View.VISIBLE else View.INVISIBLE

        if (loader.customSetHolder != null) {
            loader.customSetHolder!!.invoke()
        } else {
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
            }
        }

        loader.onSetHolder.invoke()
    }

    //
    //  Cash
    //

    fun addToCash(key: String, bitmap: Bitmap, bytes: ByteArray) {
        removeFromCash(key)
        val size = bitmap.width * bitmap.height * 4 + bytes.size
        if (size > maxCashItemSize) return

        cashSize += size
        cash.add(Item3(key, bitmap, bytes))

        while (cashSize > maxCashSize && cash.isNotEmpty()) removeFromCash(cash[0].a1)
    }

    fun getFromCash(key: String): Item3<String, Bitmap, ByteArray>? {
        var item: Item3<String, Bitmap, ByteArray>? = null
        for (i in cash) if (i.a1 == key) {
            item = i
            break
        }
        return item
    }

    fun removeFromCash(key: String) {
        val item = getFromCash(key)
        if (item == null) return

        cashSize -= item.a2.width * item.a2.height * 4 + item.a3.size

        var i = 0
        while (i in 0 until cash.size) {
            if (cash[i].a1 == key) cash.removeAt(i--)
            i++
        }

    }

    //
    //  Methods
    //

    private fun loadNow(loader: ImageLoaderA) {
        if (!turn.contains(loader)) return

        val loadedBytes = loader.startLoad()
        if (loadedBytes == null) {
            ToolsThreads.main { loader.onError.invoke() }
            return
        }
        var bytes = loadedBytes
        var bitmap: Bitmap? = null

        loader.isGif = loader.isGif || ToolsBytes.isGif(loadedBytes)

        if (!loader.isGif) {
            bitmap = parseImage(loader, loadedBytes)
            if (loader.cashScaledBytes && bitmap != null) bytes = ToolsBitmap.toJPGBytes(bitmap, 100)
        }

        ToolsThreads.main {

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

    private fun parseImage(loader: ImageLoaderA, bytes: ByteArray): Bitmap? {
        var bm = ToolsBitmap.decode(bytes, loader.w, loader.h, loader.options, loader.cropSquareCenter)
        if (loader.cropSquareCenter && bm != null) bm = ToolsBitmap.cropCenterSquare(bm)
        return bm
    }

    private fun putImage(loader: ImageLoaderA, bm: Bitmap?, animate: Boolean, bytes: ByteArray) {
        ToolsThreads.main {
            if (!loader.noCash && bm != null) addToCash(loader.getKey(), bm, bytes)
            if (loader.vImage != null && loader.isKey(loader.vImage!!.getTag())) {
                if (loader.isGif && ToolsBytes.isGif(bytes)) {
                    ToolsGif.iterator(bytes, WeakReference(loader.vImage!!), loader.sizeArd){
                        if (loader.vGifProgressBar != null) loader.vGifProgressBar!!.visibility = View.INVISIBLE
                    }
                } else {
                    var bitmap = bm
                    if(bitmap != null && loader.sizeArd != 1f) bitmap = ToolsBitmap.resize(bitmap, (bitmap.width * loader.sizeArd).toInt(), (bitmap.height * loader.sizeArd).toInt())
                    if (loader.vImage != null && bitmap != null) loader.vImage!!.setImageDrawable(DrawableImageLoader(loader.vImage!!.context, bitmap, animate && loader.fade))
                }
            }
            loader.onLoaded.invoke(bytes)
        }
    }

}
