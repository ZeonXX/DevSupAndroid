package com.sup.dev.android.views.support

import android.graphics.*
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView

import com.sup.dev.java.tools.ToolsThreads
import java.io.ByteArrayInputStream

class DrawableGif : Drawable {

    private var center:Boolean = false
    private var movie: Movie? = null
    private var movieStart = 0L
    private var left = 0f
    private var top = 0f
    private var scale = 0f
    private var bytes: ByteArray
    private var testW = 0
    private var testH = 0

    constructor(bytes: ByteArray, vImage: ImageView, center:Boolean = false, onReady:(DrawableGif)->Unit={}) : super() {
        this.bytes = bytes
        this.center = center
        vImage.setLayerType(View.LAYER_TYPE_SOFTWARE, null)
        ToolsThreads.thread {
            movie = Movie.decodeStream(ByteArrayInputStream(bytes))
            ToolsThreads.main {
                movieStart = System.currentTimeMillis()
                testW = vImage.width
                testH = vImage.height
                onReady.invoke(this)
            }
        }
    }

    override fun draw(canvas: Canvas) {

        if (movie == null) return

        movie!!.setTime(((System.currentTimeMillis() - movieStart) % (movie!!.duration() + 1)).toInt())
        canvas.scale(scale, scale)
        movie!!.draw(canvas, left / scale, top / scale)

        invalidateSelf()
    }

    override fun getIntrinsicWidth(): Int {
        return testW
    }

    override fun getIntrinsicHeight(): Int {
        return testH
    }

    override fun onBoundsChange(bounds: Rect) {
        if (movie != null) {

            val arg1 = bounds.width().toFloat() / movie!!.width().toFloat()
            val arg2 = bounds.height().toFloat() / movie!!.height().toFloat()

            if (bounds.width() == 0 || bounds.height() == 0) scale =  Math.max(arg1, arg2)
            else scale = if(center) Math.max(arg1, arg2) else Math.min(arg1, arg2)


            val measuredMovieWidth = (movie!!.width() * scale).toInt()
            val measuredMovieHeight = (movie!!.height() * scale).toInt()

            left = (bounds.width() - measuredMovieWidth) / 2f
            top = (bounds.height() - measuredMovieHeight) / 2f

        }

        super.onBoundsChange(bounds)

    }


    override fun setAlpha(i: Int) {

    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

}