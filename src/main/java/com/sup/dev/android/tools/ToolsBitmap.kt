package com.sup.dev.android.tools

import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.DrawableRes
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.views.screens.SCrop
import com.sup.dev.java.classes.geometry.Dimensions
import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.tools.*
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import android.graphics.BitmapFactory
import java.lang.RuntimeException

object ToolsBitmap {

    fun cropCenterSquare(srcBmp: Bitmap): Bitmap {
        if (srcBmp.width == srcBmp.height) return srcBmp
        return if (srcBmp.width >= srcBmp.height)
            Bitmap.createBitmap(srcBmp, srcBmp.width / 2 - srcBmp.height / 2, 0, srcBmp.height, srcBmp.height)
        else
            Bitmap.createBitmap(srcBmp, 0, srcBmp.height / 2 - srcBmp.width / 2, srcBmp.width, srcBmp.width)
    }


    //
    //  Filters
    //

    fun filter(@DrawableRes resId: Int, color: Int): Bitmap? {
        return filter(getFromResources(resId), color)
    }

    fun filter(@DrawableRes resId: Int, color: Int, reduceAlpha: Boolean): Bitmap? {
        return filter(getFromResources(resId), color, reduceAlpha)
    }

    @JvmOverloads
    fun filter(bitmap: Bitmap?, color: Int, reduceAlpha: Boolean = false): Bitmap? {

        if (bitmap == null) return null

        val a = Color.alpha(color)
        val r = Color.red(color)
        val g = Color.green(color)
        val b = Color.blue(color)

        val w = bitmap.width
        val h = bitmap.height

        val output = Bitmap.createBitmap(w, h, bitmap.config)
        for (xx in 0 until bitmap.width)
            for (yy in 0 until bitmap.height) {

                var aa = Color.alpha(bitmap.getPixel(xx, yy))
                if (reduceAlpha) aa -= a
                if (aa < 0) aa = 0

                output.setPixel(xx, yy, Color.argb(aa, r, g, b))
            }
        return output
    }

    fun filterBlur(bitmap: Bitmap, arg: Float): Bitmap {
        val rs = RenderScript.create(SupAndroid.appContext!!)
        val input = Allocation.createFromBitmap(rs, bitmap)
        val output = Allocation.createTyped(rs, input.type)
        val script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))
        script.setRadius(arg)
        script.setInput(input)
        script.forEach(output)
        output.copyTo(bitmap)
        return bitmap
    }

    fun filterBlackAndWhite(bitmap: Bitmap?): Bitmap? {

        if (bitmap == null) return null

        val pixels = IntArray(bitmap.height * bitmap.width)
        bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)

        for (i in pixels.indices) {
            val n = (ToolsColor.red(pixels[i]) + ToolsColor.green(pixels[i]) + ToolsColor.blue(pixels[i])) / 3
            pixels[i] = ToolsColor.argb(ToolsColor.alpha(pixels[i]), n, n, n)
        }

        bitmap.setPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
        return bitmap
    }

    fun filterCircle(bitmap: Bitmap?): Bitmap? {

        if (bitmap == null) return null

        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(output)

        val color = -0xbdbdbe
        val paint = Paint()
        val rect = Rect(0, 0, bitmap.width, bitmap.height)

        paint.isAntiAlias = true
        canvas.drawARGB(0, 0, 0, 0)
        paint.color = color

        val r = (Math.min(bitmap.width, bitmap.height) / 2).toFloat()
        canvas.drawCircle((bitmap.width / 2).toFloat(), (bitmap.height / 2).toFloat(), r, paint)
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        canvas.drawBitmap(bitmap, rect, rect, paint)
        return output
    }

    fun filterShadow(bitmap: Bitmap?, l: Boolean, t: Boolean, r: Boolean, b: Boolean): Bitmap? {

        if (bitmap == null) return null

        val shadowWhite = 0x50202020
        val ca = Color.alpha(shadowWhite)
        val cr = Color.red(shadowWhite)
        val cg = Color.green(shadowWhite)
        val cb = Color.blue(shadowWhite)
        val w = bitmap.width
        val h = bitmap.height

        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, Bitmap.Config.ARGB_8888)
        for (xx in 0 until w)
            for (yy in 0 until h) {

                val aP = Color.alpha(bitmap.getPixel(xx, yy)) - ca
                if (aP <= 0) continue
                val c = Color.argb(aP, cr, cg, cb)

                if (l && t && xx != 0 && yy != 0) output.setPixel(xx - 1, yy - 1, c)
                if (r && t && xx != w - 1 && yy != 0) output.setPixel(xx + 1, yy - 1, c)
                if (t && yy != 0) output.setPixel(xx, yy - 1, c)
                if (l && b && xx != 0 && yy != h - 1) output.setPixel(xx - 1, yy + 1, c)
                if (r && b && xx != w - 1 && yy != h - 1) output.setPixel(xx + 1, yy + 1, c)
                if (b && yy != h - 1) output.setPixel(xx, yy + 1, c)
                if (l && xx != 0) output.setPixel(xx - 1, yy, c)
                if (r && xx != w - 1) output.setPixel(xx + 1, yy, c)
            }

        val canvas = Canvas(output)
        canvas.drawBitmap(bitmap, 0f, 0f, Paint())

        return output
    }

    fun filterHalo(bitmap: Bitmap): Bitmap? {
        return filterShadow(bitmap, true, true, true, true)
    }

    fun filterAlphaIncrement(@DrawableRes resId: Int, alphaIncrement: Int): Bitmap? {
        return filterAlphaIncrement(getFromResources(resId), alphaIncrement)
    }

    fun filterAlphaIncrement(bitmap: Bitmap?, alphaIncrement: Int): Bitmap? {

        if (bitmap == null) return null

        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, bitmap.config)
        for (xx in 0 until bitmap.width)
            for (yy in 0 until bitmap.height) {
                val pixel = bitmap.getPixel(xx, yy)

                var a = Color.alpha(pixel)
                a += alphaIncrement
                if (a < 0) a = 0

                output.setPixel(xx, yy, Color.argb(a, Color.red(pixel), Color.green(pixel), Color.blue(pixel)))
            }

        return output
    }

    //
    //  Get
    //

    fun decodeFull(bytes: ByteArray?): Bitmap? {
        return if (bytes == null) null else BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun decode(bytes: ByteArray?): Bitmap? {
        return if (bytes == null) null else decode(bytes, 0, 0, null)
    }

    fun decode(bytes: ByteArray?, opts: BitmapFactory.Options): Bitmap? {
        return if (bytes == null) null else decode(bytes, 0, 0, opts)
    }

    fun decode(bytes: ByteArray?, w: Int, h: Int, options: BitmapFactory.Options?, maxW: Int = 1920, maxH: Int = 1080): Bitmap? {
        var optionsV = options

        if (bytes == null) return null
        if (optionsV == null) optionsV = BitmapFactory.Options()

        if (w != 0 || h != 0 || maxW != 0 || maxH != 0) {
            var ww = maxW
            var hh = maxH
            if (w in 1 until ww) ww = w
            if (h in 1 until hh) hh = h
            optionsV.inJustDecodeBounds = true
            BitmapFactory.decodeByteArray(bytes, 0, bytes.size, optionsV)
            optionsV.inSampleSize = calculateInSampleSize(optionsV, ww, hh)
        }


        optionsV.inJustDecodeBounds = false
        var bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size, optionsV)
        if (bitmap != null && w != 0 && h != 0) {
            val inscribe = ToolsMath.inscribe(bitmap.width.toFloat(), bitmap.height.toFloat(), w.toFloat(), h.toFloat())

            if (bitmap.width.toFloat() != inscribe.w || bitmap.height.toFloat() != inscribe.h)
                bitmap = Bitmap.createScaledBitmap(bitmap, inscribe.w.toInt(), inscribe.h.toInt(), true)
        }


        return bitmap
    }

    fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {

        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {
            val heightRatio = Math.round(height.toFloat() / reqHeight.toFloat())
            val widthRatio = Math.round(width.toFloat() / reqWidth.toFloat())
            inSampleSize = if (heightRatio < widthRatio) heightRatio else widthRatio
        }

        return inSampleSize
    }

    @JvmOverloads
    fun getFromGallery(onLoad: (ByteArray) -> Unit, onError: () -> Unit = { ToolsToast.show(SupAndroid.TEXT_ERROR_CANT_LOAD_IMAGE) }) {
        ToolsIntent.getGalleryImage(onLoad, onError)
    }

    fun getFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }

        if (drawable is BitmapDrawable) {
            return drawable.bitmap
        }

        try {
            val bitmap: Bitmap

            if (drawable is ColorDrawable)
                bitmap = Bitmap.createBitmap(2, 2, Bitmap.Config.ARGB_8888)
            else
                bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)


            val canvas = Canvas(bitmap)
            drawable.setBounds(0, 0, canvas.width, canvas.height)
            drawable.draw(canvas)
            return bitmap
        } catch (e: Exception) {
            err(e)
            return null
        }

    }

    fun getFromResources(@DrawableRes res: Int): Bitmap? {
        return ToolsResources.getBitmap(res)
    }

    @Throws(IOException::class)
    fun getFromURL(src: String): Bitmap? {
        try {
            val url = URL(src)
            val connection = url.openConnection() as HttpURLConnection
            connection.readTimeout = 4000
            connection.doInput = true
            connection.connect()
            val input = connection.inputStream
            return BitmapFactory.decodeStream(input)
        } catch (e: Exception) {
            err(e)
            return null
        }
    }

    fun getFromURL(src: String, bitmapListener: (Bitmap?) -> Unit) {
        ToolsThreads.thread {
            var bitmap: Bitmap? = null
            try {
                bitmap = getFromURL(src)
            } catch (ex: IOException) {
                err(ex)
            }

            val fBitmap = bitmap
            ToolsThreads.main { bitmapListener.invoke(fBitmap) }
        }
    }

    fun getFromUri(uri: Uri, callbackResult: (Bitmap?) -> Unit, onPermissionPermissionRestriction: (String) -> Unit) {
        ToolsPermission.requestReadPermission({
            try {
                callbackResult.invoke(MediaStore.Images.Media.getBitmap(SupAndroid.activity!!.contentResolver, uri))
            } catch (ex: IOException) {
                err(ex)
                callbackResult.invoke(null)
            }
        }, onPermissionPermissionRestriction)
    }

    fun getFromFile(file: File, onComplete: (Bitmap?) -> Unit) {
        ToolsPermission.requestReadPermission({
            try {
                onComplete.invoke(decode(ToolsFiles.readFile(file)))
            } catch (e: IOException) {
                err(e)
                ToolsToast.show(SupAndroid.TEXT_ERROR_CANT_LOAD_IMAGE)
            }
        }, { ToolsToast.show(SupAndroid.TEXT_ERROR_PERMISSION_FILES) })
    }

    fun getFromGalleryCropped(ratioW: Int, ratioH: Int, autoBackOnCrop: Boolean, onCrop: ((SCrop?, Bitmap?, Int, Int, Int, Int) -> Unit)) {

        getFromGallery({ bytes ->
            val bitmap = ToolsBitmap.decode(bytes)
            if (bitmap == null) onCrop.invoke(null, null, 0, 0, 0, 0)
            else Navigator.to(SCrop(bitmap, ratioW, ratioH, onCrop).setAutoBackOnCrop(autoBackOnCrop))
        })
    }

    fun getFromGalleryCroppedAndScaled(w: Int, h: Int, autoBackOnCrop: Boolean, onCrop: ((SCrop?, Bitmap?, Int, Int, Int, Int) -> Unit)) {
        getFromGalleryCropped(w, h, autoBackOnCrop) { pCrop, bitmap, x, y, ww, hh -> onCrop.invoke(pCrop, Bitmap.createScaledBitmap(bitmap!!, w, h, true), x, y, ww, hh) }
    }


    //
    //  To
    //

    fun toBytes(bitmap: Bitmap, maxBytesSize: Int = Integer.MAX_VALUE): ByteArray? {
        var containsAlpha = false
        for (x in 0 until bitmap.width)
            for (y in 0 until bitmap.height)
                if (ToolsColor.alpha(bitmap.getPixel(x, y)) != 255) {
                    containsAlpha = true
                    break
                }

        if (containsAlpha) {
            val bytes = toPNGBytes(bitmap)
            if (bytes.size <= maxBytesSize)
                return bytes
        }

        var q = 100
        var bytes = toJPGBytes(bitmap, 100)
        while (bytes.size > maxBytesSize) {
            if (q == 2) return null
            q -= 2
            bytes = toJPGBytes(bitmap, q)
        }
        return bytes
    }

    fun toJPGBytes(bitmap: Bitmap, q: Int): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, q, stream)
        return stream.toByteArray()
    }

    fun toPNGBytes(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream)
        return stream.toByteArray()
    }

    fun drawableToBytes(@DrawableRes resId: Int): ByteArray {
        val d = SupAndroid.activity!!.resources.getDrawable(resId, SupAndroid.activity!!.theme)
        val bitmap = (d as BitmapDrawable).bitmap
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        return stream.toByteArray()
    }

    //
    //  Sizes
    //

    fun inscribe(bitmap: Bitmap, w: Int, h: Int): Dimensions {
        val inscribe = ToolsMath.inscribe(bitmap.width.toFloat(), bitmap.height.toFloat(), w.toFloat(), h.toFloat())

        if (bitmap.width.toFloat() > inscribe.w || bitmap.height.toFloat() > inscribe.h)
            return inscribe

        return Dimensions(bitmap.width.toFloat(), bitmap.height.toFloat())
    }

    fun keepMaxSizes(bitmap: Bitmap, w: Int, h: Int): Bitmap {

        var bm = bitmap
        val inscribe = ToolsMath.inscribe(bm.width.toFloat(), bm.height.toFloat(), w.toFloat(), h.toFloat())

        if (bm.width.toFloat() > inscribe.w || bm.height.toFloat() > inscribe.h)
            bm = Bitmap.createScaledBitmap(bm, inscribe.w.toInt(), inscribe.h.toInt(), true)

        return bm
    }


    fun keepMaxSides(bitmap: Bitmap, maxSideSize: Int): Bitmap {
        if (maxSideSize > 1000000) throw RuntimeException("Are ypu sure about that!? keepMaxSides sides=$maxSideSize")
        val w = bitmap.width
        val h = bitmap.height
        if (w <= maxSideSize && h <= maxSideSize) return bitmap

        val arg = ToolsMath.max(w, h) / maxSideSize.toFloat()
        return Bitmap.createScaledBitmap(bitmap, (w / arg).toInt(), (h / arg).toInt(), true)
    }

    fun keepMinSides(bitmap: Bitmap, minSideSize: Int): Bitmap {
        if (minSideSize > 1000000) throw RuntimeException("Are ypu sure about that!? keepMinSides sides=$minSideSize")
        val w = bitmap.width
        val h = bitmap.height
        if (w >= minSideSize && h >= minSideSize) return bitmap

        val arg = minSideSize.toFloat() / ToolsMath.max(w, h)
        return Bitmap.createScaledBitmap(bitmap, (w * arg).toInt(), (h * arg).toInt(), true)
    }

    fun keepSides(bitmap: Bitmap, sides: Int): Bitmap {
        return keepMinSides(keepMaxSides(bitmap, sides), sides)
    }

    fun resize(bitmap: Bitmap, w: Int, h: Int): Bitmap {
        if (w > 1000000 || h > 1000000) throw RuntimeException("Are ypu sure about that!? resize w=$w h=$h")
        return Bitmap.createScaledBitmap(bitmap, w, h, true)
    }

    fun resize(bitmap: Bitmap, w: Int): Bitmap {
        if (w > 1000000) throw RuntimeException("Are ypu sure about that!? resize w=$w")
        return Bitmap.createScaledBitmap(bitmap, w, w, true)
    }


}
