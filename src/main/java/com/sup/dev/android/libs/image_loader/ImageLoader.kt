package com.sup.dev.android.libs.image_loader

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.annotation.DrawableRes
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsBitmap
import com.sup.dev.android.tools.ToolsGif
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.java.classes.items.Item3
import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.tools.ToolsBytes
import com.sup.dev.java.tools.ToolsThreads
import java.io.File
import java.lang.ref.WeakReference
import java.util.ArrayList
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

object ImageLoader {

    internal val maxCashItemSize = 1024 * 1024 * 5
    internal val maxCashSize = 1024 * 1024 * 50
    internal val cash = ArrayList<Item3<String, Bitmap?, ByteArray?>>()
    internal var cashSize = 0
    internal val turn = ArrayList<ImageLink>()
    internal var threadPool: ThreadPoolExecutor = ThreadPoolExecutor(4, 4, 1, TimeUnit.MINUTES, LinkedBlockingQueue())

    init {
        SupAndroid.addOnLowMemory {
            clearCash()
        }
    }

    //
    //  Tools
    //

    fun load(any: Any): ImageLink? {
        if (any is File) return load(any)
        if (any is Int) return load(any)
        if (any is Long) return load(any)
        if (any is String) return load(any)
        if (any is ByteArray) return load(any)
        return null
    }

    fun load(file: File) = ImageLoaderFile(file)
    fun load(@DrawableRes res: Int) = ImageLoaderResource(res)
    fun load(id: Long) = ImageLoaderId(id)
    fun load(tag: String) = ImageLoaderTag(tag)
    fun load(bytes: ByteArray) = ImageLoaderBytes(bytes)

    fun loadGif(imageId: Long, gifId: Long, vImage: ImageView, vGifProgressBar: View?=null, onInit:(ImageLink)->Unit={}) {

        if(gifId == 0L){
            ToolsThreads.main { vGifProgressBar?.visibility = View.INVISIBLE }
            val load = load(imageId)
            onInit.invoke(load)
            load.into(vImage)
        } else if (imageId > 0) {
            val load = load(imageId)
            onInit.invoke(load)
            load.into(vImage) {
                val loadGif = load(gifId)
                onInit.invoke(loadGif)
                loadGif.gifProgressBar(vGifProgressBar).holder(vImage.drawable).into(vImage)
            }
        } else {
            val load = load(gifId)
            onInit.invoke(load)
            load.gifProgressBar(vGifProgressBar).holder(vImage.drawable).into(vImage)
        }
    }

    fun clear(imageId: Long) {
        load(imageId).clear()
    }

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

    fun load(loader: ImageLink) {

        if (loader.fastLoad(loader.vImage)) return

        if (loader.vGifProgressBar != null) loader.vGifProgressBar!!.visibility = View.INVISIBLE

        val cashItem = getFromCash(loader.getKey())
        if (cashItem != null) {
            if (!loader.intoCash) putImage(loader, cashItem.a2, false, cashItem.a3)
            return
        }

        try {
            putHolder(loader)
        } catch (e: OutOfMemoryError) {
            SupAndroid.onLowMemory()
            putHolder(loader)
        }

        ToolsThreads.thread {

            var bytes: ByteArray? = null
            if (!loader.noLoadFromCash) bytes = loader.getFromCash()

            if (bytes != null) {
                if (!loader.intoCash) ToolsThreads.thread { putImage(loader, parseImage(loader, bytes), false, bytes) }
                return@thread
            }

            ToolsThreads.main {


                turn.add(loader)

                for (l in turn) if (l.isKey(loader.getKey()) && l !== loader) return@main

                loadStart(loader)

            }

        }
    }

    private fun loadStart(loader: ImageLink) {
        threadPool.execute {
            try {
                loadNow(loader)
            } catch (ex: Throwable) {
                err(ex)
            }
        }
    }

    private fun putHolder(loader: ImageLink) {

        if (loader.vGifProgressBar != null) loader.vGifProgressBar!!.visibility = if (loader.allowGif) View.VISIBLE else View.INVISIBLE

        if (loader.customSetHolder != null) {
            loader.customSetHolder!!.invoke()
        } else {
            if (loader.vImage != null) {
                if (!loader.noHolder) {
                    if (loader.holder is Int) {
                        loader.vImage!!.setImageResource(loader.holder as Int)
                    } else if (loader.holder is Drawable) {
                        loader.vImage!!.setImageDrawable(loader.holder as Drawable)
                    } else if (loader.holder is Bitmap) {
                        loader.vImage!!.setImageBitmap(loader.holder as Bitmap)
                    } else if (loader.getW() != 0 && loader.getH() != 0) {
                        try {
                            val bitmap = Bitmap.createBitmap(loader.getW(), loader.getH(), Bitmap.Config.ARGB_4444)
                            bitmap.eraseColor(ToolsResources.getColor(R.color.focus))
                            loader.vImage!!.setImageBitmap(bitmap)
                        } catch (e: OutOfMemoryError) {
                            SupAndroid.onLowMemory()
                            val bitmap = Bitmap.createBitmap(loader.getW(), loader.getH(), Bitmap.Config.ARGB_4444)
                            bitmap.eraseColor(ToolsResources.getColor(R.color.focus))
                            loader.vImage!!.setImageBitmap(bitmap)
                        }
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

    fun addToCash(key: String, bitmap: Bitmap?, bytes: ByteArray?) {
        removeFromCash(key)
        if (bitmap == null && bytes == null) return
        val size = bitmap?.byteCount ?: bytes!!.size
        if (size > maxCashItemSize) return

        cashSize += size
        cash.add(Item3(key, bitmap, bytes))

        while (cashSize > maxCashSize && cash.isNotEmpty()) removeFromCash(cash[0].a1)
    }

    fun getFromCash(key: String): Item3<String, Bitmap?, ByteArray?>? {
        var item: Item3<String, Bitmap?, ByteArray?>? = null
        for (i in cash) if (i.a1 == key) {
            item = i
            break
        }
        return item
    }

    fun removeFromCash(key: String) {
        val item = getFromCash(key) ?: return

        cashSize -= item.a2?.byteCount ?: item.a3!!.size

        var i = 0
        while (i in 0 until cash.size) {
            if (cash[i].a1 == key) cash.removeAt(i--)
            i++
        }

    }

    fun clearCash() {
        cash.clear()
        cashSize = 0
    }

    //
    //  Methods
    //

    private fun loadNow(loader: ImageLink) {
        if (!turn.contains(loader)) return

        val loadedBytes = loader.startLoad()
        if (loadedBytes == null) {
            if (loader.onError != null) ToolsThreads.main { loader.onError?.invoke() }
            else if (loader.tryCount > 0) ToolsThreads.main(5000) {
                loader.tryCount--
                loadStart(loader)
            }
            return
        }
        var bytes = loadedBytes
        var bitmap: Bitmap? = null

        if (loader.allowGif && !ToolsBytes.isGif(loadedBytes)) {
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

    private fun parseImage(loader: ImageLink, bytes: ByteArray): Bitmap? {
        var bm = if (loader.noBitmap) null else ToolsBitmap.decode(bytes, loader.getW(), loader.getH(), loader.options, resizeByMinSide=loader.resizeByMinSide)
        if(bm != null) {
            if (loader.cropSquareCenter) bm = ToolsBitmap.cropCenterSquare(bm)
            if (loader.cropW > 0 && loader.cropH > 0) bm = ToolsBitmap.cropCenter(bm, loader.cropW, loader.cropH)
        }
        return bm
    }

    private fun putImage(loader: ImageLink, bm: Bitmap?, animate: Boolean, bytes: ByteArray?) {
        ToolsThreads.main {
            if (!loader.noCash && bm != null) addToCash(loader.getKey(), bm, bytes)

            if (loader.vImage != null && loader.isKey(loader.vImage!!.tag)) {
                if (loader.allowGif && bytes != null && ToolsBytes.isGif(bytes)) {
                    ToolsGif.iterator(bytes, WeakReference(loader.vImage!!), loader.getW(), loader.getH(), loader.resizeByMinSide) {
                        if (loader.vGifProgressBar != null) loader.vGifProgressBar!!.visibility = View.INVISIBLE
                    }
                } else {
                    if (loader.vImage != null && bm != null) loader.vImage!!.setImageDrawable(DrawableImageLoader(loader.vImage!!.context, bm, animate && loader.fade))
                }
            }
            loader.onLoaded.invoke(bytes)
            loader.onLoadedBitmap.invoke(bm)
        }
    }

}
