package com.sup.dev.android.libs.image_loader

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import com.sup.dev.android.tools.ToolsCash
import com.sup.dev.android.views.views.ViewAvatar
import com.sup.dev.android.views.views.ViewAvatarTitle
import com.sup.dev.java.tools.ToolsMath
import java.lang.RuntimeException


abstract class ImageLink {

    private var fullImageLoader: ImageLink? = null
    private var previewImageLoader: ImageLink? = null

    internal var vImage: ImageView? = null
    internal var vGifProgressBar: View? = null
    internal var onLoaded: (ByteArray?) -> Unit = {}
    internal var onLoadedBitmap: (Bitmap?) -> Unit = {}
    internal var onError: (() -> Unit)? = null
    internal var onSetHolder: () -> Unit = {}
    internal var customSetHolder: (() -> Unit)? = null
    internal var options: BitmapFactory.Options? = null

    internal var cropSquareCenter = false
    internal var w = 0
    internal var h = 0
    internal var minW = 0
    internal var minH = 0
    internal var maxW = 0
    internal var maxH = 0
    internal var cropW = 0
    internal var cropH = 0
    internal var allowGif = true
    internal var holder: Any? = null
    internal var noHolder = false
    internal var fade = true
    internal var cashScaledBytes = false
    internal var noCash = false
    internal var noLoadFromCash = false
    internal var autoCash = true
    internal var autoDiskCashMaxSize = 1024 * 1024 * 2
    internal var intoCash = false
    internal var noBitmap = false
    internal var resizeByMinSide = false
    internal var autocropIfLostBounds = true

    var tryCount = 2

    fun getParamsSum(): String {
        return "$cropSquareCenter$w$h$minW$minH$maxW$maxH$cropW$cropH$allowGif$noHolder$fade$cashScaledBytes$noCash$noLoadFromCash$autoCash$autoDiskCashMaxSize$intoCash$noBitmap$resizeByMinSide"
    }

    fun copy(): ImageLink {
        val link = copyLocal()
        link.cropSquareCenter = this.cropSquareCenter
        link.w = this.w
        link.h = this.h
        link.minW = this.minW
        link.minH = this.minH
        link.cropW = this.cropW
        link.cropH = this.cropH
        link.allowGif = this.allowGif
        link.holder = this.holder
        link.noHolder = this.noHolder
        link.fade = this.fade
        link.cashScaledBytes = this.cashScaledBytes
        link.noCash = this.noCash
        link.noLoadFromCash = this.noLoadFromCash
        link.autoCash = this.autoCash
        link.autoDiskCashMaxSize = this.autoDiskCashMaxSize
        link.intoCash = this.intoCash
        link.noBitmap = this.noBitmap
        link.resizeByMinSide = this.resizeByMinSide
        link.autocropIfLostBounds = this.autocropIfLostBounds
        return link
    }

    override fun equals(other: Any?): Boolean {
        if (javaClass == other?.javaClass && getParamsSum() == (other as ImageLink).getParamsSum()) return equalsTo(other)
        return false
    }

    abstract fun copyLocal(): ImageLink

    abstract fun equalsTo(imageLoader: ImageLink): Boolean

    fun into(vImage: ImageView?) {
        this.vImage = vImage
        if (vImage != null) vImage.tag = getKey()
        ImageLoader.load(this)
    }

    fun into(vImage: ViewAvatarTitle?) {
        into(vImage?.vAvatar)
    }

    fun into(vImage: ViewAvatar?) {
        into(vImage?.vImageView)
    }

    fun intoBytes(onLoadedBytes: (ByteArray?) -> Unit) {
        noBitmap = true
        setOnLoadedBytes(onLoadedBytes)
        ImageLoader.load(this)
    }

    fun intoBitmap(onLoadedBitmap: (Bitmap?) -> Unit) {
        setOnLoadedBitmap(onLoadedBitmap)
        ImageLoader.load(this)
    }

    fun into(vImage: ImageView?, onLoaded: (ByteArray?) -> Unit) {
        setOnLoadedBytes(onLoaded)
        into(vImage)
    }

    fun intoCash() {
        intoCash = true
        ImageLoader.load(this)
    }

    fun clear() {
        ToolsCash.clear("" + getKey().replace("/", "_").hashCode())
        ImageLoader.removeFromCash(getKey())
    }
    //
    //  SetUp
    //

    fun crop(sides: Int) = crop(sides, sides)

    fun crop(w: Int, h: Int): ImageLink {
        cropW = w
        cropH = h
        if (cropH > 1 && cropW < 1) throw RuntimeException("cropW[$cropW] can't be < 1")
        if (cropW > 1 && cropH < 1) throw RuntimeException("cropH[$cropH] can't be < 1")
        return this
    }

    fun resizeByMinSide(): ImageLink {
        this.resizeByMinSide = true
        return this
    }

    fun gifProgressBar(vGifProgressBar: View?): ImageLink {
        this.vGifProgressBar = vGifProgressBar
        return this
    }

    fun disallowGif(): ImageLink {
        allowGif = false
        return this
    }

    fun fullImageLoader(imageLoader: ImageLink): ImageLink {
        this.fullImageLoader = imageLoader
        return this
    }

    fun previewImageLoader(imageLoader: ImageLink): ImageLink {
        this.previewImageLoader = imageLoader
        return this
    }

    fun size(side: Int) = size(side, side)

    fun size(w: Int, h: Int): ImageLink {
        this.w = w
        this.h = h
        if (h > 1 && w < 1) throw RuntimeException("w[$w] can't be < 1")
        if (w > 1 && h < 1) throw RuntimeException("h[$h] can't be < 1")
        return this
    }

    fun minSize(side: Int) = minSize(side, side)

    fun minSize(w: Int, h: Int): ImageLink {
        this.minW = w
        this.minH = h
        if (minH > 1 && minW < 1) throw RuntimeException("minW[$minW] can't be < 1")
        if (minW > 1 && minH < 1) throw RuntimeException("minH[$minH] can't be < 1")
        return this
    }

    fun maxSize(side: Int) = maxSize(side, side)

    fun maxSize(w: Int, h: Int): ImageLink {
        this.maxW = w
        this.maxH = h
        if (maxH > 1 && maxW < 1) throw RuntimeException("maxW[$maxW] can't be < 1")
        if (maxW > 1 && maxH < 1) throw RuntimeException("maxH[$maxH] can't be < 1")
        return this
    }

    fun getKey(): String {
        generateSizesIfNeed()
        return getKeyOfImage() + ":" + getParamsSum().hashCode()
    }

    protected abstract fun getKeyOfImage(): String

    fun noHolder(): ImageLink {
        noHolder = true
        return this
    }

    fun setOnLoadedBytes(onLoaded: (ByteArray?) -> Unit): ImageLink {
        this.onLoaded = onLoaded
        return this
    }

    fun setOnLoadedBitmap(onLoadedBitmap: (Bitmap?) -> Unit): ImageLink {
        this.onLoadedBitmap = onLoadedBitmap
        return this
    }

    fun setOnError(onError: (() -> Unit)?): ImageLink {
        this.onError = onError
        return this
    }

    fun setCustomSetHolder(customSetHolder: (() -> Unit)?): ImageLink {
        this.customSetHolder = customSetHolder
        return this
    }

    fun setOnSetHolder(onSetHolder: () -> Unit): ImageLink {
        this.onSetHolder = onSetHolder
        return this
    }

    fun cropSquare(): ImageLink {
        this.cropSquareCenter = true
        return this
    }

    fun options(options: BitmapFactory.Options): ImageLink {
        this.options = options
        return this
    }

    fun cashScaledBytes(): ImageLink {
        this.cashScaledBytes = true
        return this
    }

    fun holder(holder: Int?): ImageLink {
        this.holder = holder
        return this
    }

    fun holder(holder: Drawable?): ImageLink {
        this.holder = holder
        return this
    }

    fun holder(holder: Bitmap?): ImageLink {
        this.holder = holder
        return this
    }

    fun noFade(): ImageLink {
        this.fade = false
        return this
    }

    fun noAutocropIfLostBounds(): ImageLink {
        this.autocropIfLostBounds = false
        return this
    }

    fun noCash(): ImageLink {
        this.noCash = false
        return this
    }

    fun noLoadFromCash(): ImageLink {
        this.noLoadFromCash = true
        return this
    }

    fun isKey(key: Any?): Boolean {
        return key != null && key == this.getKey()
    }

    fun startLoad(): ByteArray? {
        val bytes = if (!noLoadFromCash) getFromCash() else null
        if (bytes != null) return bytes
        val data = load()
        if (data != null && autoCash && data.size <= autoDiskCashMaxSize) ToolsCash.put(data, "" + getKey().replace("/", "_").hashCode())
        return data
    }

    fun getFromCash() = ToolsCash.get("" + getKey().replace("/", "_").hashCode())

    abstract fun load(): ByteArray?

    open fun fastLoad(vImage: ImageView?): Boolean {
        return false
    }

    //
    //  Getters
    //

    protected var generatedW = 0
    protected var generatedH = 0
    protected var generated = false

    fun generateSizesIfNeed() {
        if (generated) return

        if (cropW > 0 && cropH > 0) {
            this.generatedW = cropW
            this.generatedH = cropH
            generated = true
            return
        }

        var generatedW = (if (w == 0) cropW else w).toFloat()
        var generatedH = (if (h == 0) cropH else h).toFloat()

        if (maxW > 0 && maxH > 0) {
            val inscribeMax = ToolsMath.inscribeInBounds(generatedW, generatedH, maxW.toFloat(), maxH.toFloat())
            generatedW = inscribeMax.w
            generatedH = inscribeMax.h
        }

        if (minW > 0 && minH > 0) {
            val inscribeMin = ToolsMath.inscribeOutBounds(generatedW, generatedH, minW.toFloat(), minH.toFloat())
            generatedW = inscribeMin.w
            generatedH = inscribeMin.h
        }

        if (minW > 0 && minH > 0 && maxW > 0 && maxH > 0) {
            if (minW > maxW) throw RuntimeException("minW[$minW] > maxW[$maxW]")
            if (minH > maxH) throw RuntimeException("minW[$minH] > maxW[$maxH]")
        }

        //if(generatedW > generatedW.toInt()) generatedW += ToolsView.dpToPx(2)
        //if(generatedH > generatedH.toInt()) generatedH += ToolsView.dpToPx(2)

        if (autocropIfLostBounds) {
            if (generatedW < minW) {
                crop(minW, maxH)
                generateSizesIfNeed()
                return
            }
            if (generatedH < minH) {
                crop(maxW, minH)
                generateSizesIfNeed()
                return
            }
        }

        this.generatedW = generatedW.toInt()
        this.generatedH = generatedH.toInt()
        generated = true
    }

    fun getW(): Int {
        generateSizesIfNeed()
        return generatedW
    }

    fun getH(): Int {
        generateSizesIfNeed()
        return generatedH
    }

    fun getCropW(): Int {
        generateSizesIfNeed()
        return cropW
    }

    fun getCropH(): Int {
        generateSizesIfNeed()
        return cropH
    }

    fun getFullImageLoader() = fullImageLoader

    fun getPreviewImageLoader() = previewImageLoader


}
