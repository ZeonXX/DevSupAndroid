package com.sup.dev.android.views.drawables

import android.graphics.*
import android.graphics.drawable.Drawable
import android.widget.ImageView

import com.sup.dev.java.tools.ToolsThreads
import java.io.ByteArrayInputStream

class DrawableGif : Drawable {

    private var movie: Movie? = null
    private var movieStart: Long = 0
    private var left: Float = 0.toFloat()
    private var top: Float = 0.toFloat()
    private var scale: Float = 0.toFloat()
    private var bytes: ByteArray
    private var lastBounds: Rect = Rect()

    constructor(bytes: ByteArray, vImage: ImageView? = null) : super() {
        this.bytes = bytes
        ToolsThreads.thread {
            movie = Movie.decodeStream(ByteArrayInputStream(bytes))
            ToolsThreads.main {
                movieStart = System.currentTimeMillis()
                updateBounds()
                invalidateSelf()
                if (vImage != null) vImage.setImageDrawable(this)
            }
        }
    }

    constructor(movie: Movie) : super() {
        this.movie = movie
        this.bytes = ByteArray(0)
        movieStart = System.currentTimeMillis()
        updateBounds()
        invalidateSelf()
    }


    override fun draw(canvas: Canvas) {

        if (movie == null) return

        movie!!.setTime(((System.currentTimeMillis() - movieStart) % (movie!!.duration() + 1)).toInt())
        canvas.scale(scale, scale)
        movie!!.draw(canvas, left / scale, top / scale)

        invalidateSelf()
    }

    override fun setAlpha(i: Int) {

    }

    override fun setColorFilter(colorFilter: ColorFilter?) {

    }

    override fun getOpacity(): Int {
        return PixelFormat.TRANSLUCENT
    }

    override fun onBoundsChange(bounds: Rect) {
        lastBounds = bounds
        super.onBoundsChange(updateBounds())

    }

    fun updateBounds(): Rect {

        if (movie == null) return lastBounds

        val arg1 = bounds.width().toFloat() / movie!!.width().toFloat()
        val arg2 = bounds.height().toFloat() / movie!!.height().toFloat()

        if (bounds.width() == 0 || bounds.height() == 0) scale = Math.max(arg1, arg2)
        else scale = Math.min(arg1, arg2)

        val measuredMovieWidth = (movie!!.width() * scale).toInt()
        val measuredMovieHeight = (movie!!.height() * scale).toInt()

        left = (bounds.width() - measuredMovieWidth) / 2f
        top = (bounds.height() - measuredMovieHeight) / 2f

        return Rect(0, 0, 50, 50)
    }


}