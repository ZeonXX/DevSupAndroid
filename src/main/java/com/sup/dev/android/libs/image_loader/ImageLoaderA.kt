package com.sup.dev.android.libs.image_loader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.sup.dev.android.tools.ToolsCash


abstract class ImageLoaderA {

    internal var vImage: ImageView? = null
    internal var vGifProgressBar: View? = null
    internal var onLoaded: (ByteArray?) -> Unit = {}
    internal var onError: (() -> Unit)? = null
    internal var onSetHolder: () -> Unit = {}
    internal var customSetHolder: (() -> Unit)? = null
    internal var cropSquareCenter: Boolean = false
    internal var options: BitmapFactory.Options? = null
    internal var w = 0
    internal var h = 0
    internal var allowGif = true
    internal var showGifLoadingProgress = false
    internal var sizeArd = 1f
    internal var holder: Any? = null
    internal var noHolder = false
    internal var fade = true
    internal var cashScaledBytes = false
    internal var noCash = false
    internal var noLoadFromCash = false
    internal var autoCash = true
    internal var autoCashMaxSize: Int = 1024 * 1024 * 2

    var tryCount = 2


    fun into(vImage: ImageView?) {
        this.vImage = vImage
        if (vImage != null) vImage.tag = getKey()
        ImageLoader.load(this)
    }

    fun into(onLoaded: (ByteArray?) -> Unit) {
        setOnLoaded(onLoaded)
        ImageLoader.load(this)
    }

    fun into(vImage: ImageView?, onLoaded: (ByteArray?) -> Unit) {
        setOnLoaded(onLoaded)
        into(vImage)
    }

    fun intoCash() {
        ImageLoader.load(this)
    }

    //
    //  SetUp
    //
    fun gifProgressBar(vGifProgressBar: View?): ImageLoaderA {
        this.vGifProgressBar = vGifProgressBar
        return this
    }

    fun disallowGif(): ImageLoaderA {
        allowGif = false
        return this
    }

    fun showGifLoadingProgress(): ImageLoaderA {
        showGifLoadingProgress = true
        return this
    }

    fun sizeArd(sizeArd: Float): ImageLoaderA {
        this.sizeArd = sizeArd
        return this
    }


    fun size(w: Int, h: Int): ImageLoaderA {
        this.w = w
        this.h = h
        return this
    }

    abstract fun getKey(): String

    fun noHolder(): ImageLoaderA {
        noHolder = true
        return this
    }

    fun setOnLoaded(onLoaded: (ByteArray?) -> Unit): ImageLoaderA {
        this.onLoaded = onLoaded
        return this
    }

    fun setOnError(onError: (() -> Unit)?): ImageLoaderA {
        this.onError = onError
        return this
    }

    fun setCustomSetHolder(customSetHolder: (() -> Unit)?): ImageLoaderA {
        this.customSetHolder = customSetHolder
        return this
    }

    fun setOnSetHolder(onSetHolder: () -> Unit): ImageLoaderA {
        this.onSetHolder = onSetHolder
        return this
    }

    fun cropSquare(): ImageLoaderA {
        this.cropSquareCenter = true
        return this
    }

    fun options(options: BitmapFactory.Options): ImageLoaderA {
        this.options = options
        return this
    }

    fun cashScaledBytes(): ImageLoaderA {
        this.cashScaledBytes = true
        return this
    }

    fun holder(holder: Int?): ImageLoaderA {
        this.holder = holder
        return this
    }

    fun holder(holder: Drawable?): ImageLoaderA {
        this.holder = holder
        return this
    }

    fun holder(holder: Bitmap?): ImageLoaderA {
        this.holder = holder
        return this
    }

    fun noFade(): ImageLoaderA {
        this.fade = false
        return this
    }

    fun noCash(): ImageLoaderA {
        this.noCash = false
        return this
    }

    fun noLoadFromCash(): ImageLoaderA {
        this.noLoadFromCash = true
        return this
    }

    fun isKey(key: Any?): Boolean {
        return key === this.getKey() || key != null && key == this.getKey()
    }

    fun startLoad(): ByteArray? {
        val bytes = if (!noLoadFromCash) getFromCash() else null
        if (bytes != null) return bytes
        val data = load()
        if (data != null && autoCash && data.size <= autoCashMaxSize) ToolsCash.put(data, "" + getKey().replace("/", "_").hashCode())
        return data
    }

    fun getFromCash() = ToolsCash.get("" + getKey().replace("/", "_").hashCode())

    abstract fun load(): ByteArray?

    //
    //  Getters
    //

    fun clear() {
        ToolsCash.clear("" + getKey().replace("/", "_").hashCode())
        ImageLoader.removeFromCash(getKey())
    }

}
