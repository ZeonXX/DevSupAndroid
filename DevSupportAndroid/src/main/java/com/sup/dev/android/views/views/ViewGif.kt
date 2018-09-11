package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Movie
import android.util.AttributeSet
import android.view.View
import com.sup.dev.android.R
import com.sup.dev.java.tools.ToolsThreads
import java.io.ByteArrayInputStream


class ViewGif(context: Context, attrs: AttributeSet) : View(context, attrs) {

    private var movie: Movie? = null
    private var movieStart: Long = 0
    private var currentAnimationTime: Int = 0
    private var left: Float = 0.toFloat()
    private var top: Float = 0.toFloat()
    private var scale: Float = 0.toFloat()
    private var measuredMovieWidth: Int = 0
    private var measuredMovieHeight: Int = 0
    @Volatile
    var isPaused: Boolean = false
        private set
    //
    //  Getters
    //


    var gifBytes: ByteArray? = null
        private set

    private var onGifLoaded: (() -> Unit)? = null

    val isPlaying: Boolean
        get() = !this.isPaused


    init {
        setLayerType(View.LAYER_TYPE_SOFTWARE, null)

        val array = context.obtainStyledAttributes(attrs, R.styleable.ViewGif, 0, R.style.Widget_GifView)
        val movieResourceId = array.getResourceId(R.styleable.ViewGif_android_src, 0)
        array.recycle()

        if (movieResourceId != 0)
            movie = Movie.decodeStream(resources.openRawResource(movieResourceId))
    }


    //
    //  Methods
    //

    fun setGif(gifBytes: ByteArray) {
        this.gifBytes = gifBytes
        val onGifLoaded = this.onGifLoaded
        ToolsThreads.thread {
            movie = Movie.decodeStream(ByteArrayInputStream(gifBytes))
            ToolsThreads.main {
                if (onGifLoaded === this.onGifLoaded) {
                    requestLayout()
                    if (onGifLoaded != null) onGifLoaded.invoke()
                }
            }
        }
    }

    fun clear() {
        movie = null
        onGifLoaded = null
        requestLayout()
    }


    fun play() {
        if (this.isPaused) {
            this.isPaused = false
            movieStart = android.os.SystemClock.uptimeMillis() - currentAnimationTime
            invalidate()
        }
    }

    fun pause() {
        if (!this.isPaused) {
            this.isPaused = true

            invalidate()
        }

    }

    private fun updateAnimationTime() {
        val now = android.os.SystemClock.uptimeMillis()

        if (movieStart == 0L) {
            movieStart = now
        }

        var dur = movie!!.duration()

        if (dur == 0) {
            dur = DEFAULT_MOVIE_VIEW_DURATION
        }

        currentAnimationTime = ((now - movieStart) % dur).toInt()
    }

    private fun drawMovieFrame(canvas: Canvas) {

        movie!!.setTime(currentAnimationTime)

        canvas.save()
        canvas.scale(scale, scale)
        movie!!.draw(canvas, left / scale, top / scale)
        canvas.restore()
    }

    //
    //  Events
    //


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        if (movie == null) {
            setMeasuredDimension(suggestedMinimumWidth, suggestedMinimumHeight)
            return
        }

        val arg1 = View.MeasureSpec.getSize(widthMeasureSpec).toFloat() / movie!!.width().toFloat()
        val arg2 = View.MeasureSpec.getSize(heightMeasureSpec).toFloat() / movie!!.height().toFloat()

        if (View.MeasureSpec.getSize(widthMeasureSpec) == 0 || View.MeasureSpec.getSize(heightMeasureSpec) == 0
                || View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.UNSPECIFIED && movie!!.height() * Math.max(arg1, arg2) > View.MeasureSpec.getSize(heightMeasureSpec))
            scale = Math.max(arg1, arg2)
        else
            scale = Math.min(arg1, arg2)

        measuredMovieWidth = (movie!!.width() * scale).toInt()
        measuredMovieHeight = (movie!!.height() * scale).toInt()

        setMeasuredDimension(measuredMovieWidth, measuredMovieHeight)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        left = (width - measuredMovieWidth) / 2f
        top = (height - measuredMovieHeight) / 2f
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        if (movie != null) {
            if (!isPaused) {
                updateAnimationTime()
                drawMovieFrame(canvas)
                invalidate()
            } else {
                drawMovieFrame(canvas)
            }
        }
    }

    override fun onScreenStateChanged(screenState: Int) {
        super.onScreenStateChanged(screenState)
        invalidate()
    }

    override fun onVisibilityChanged(changedView: View, visibility: Int) {
        super.onVisibilityChanged(changedView, visibility)
        invalidate()
    }

    override fun onWindowVisibilityChanged(visibility: Int) {
        super.onWindowVisibilityChanged(visibility)
        invalidate()
    }

    //
    //  Setters
    //

    fun setOnGifLoaded(onGifLoaded: ()->Unit) {
        this.onGifLoaded = onGifLoaded
    }

    companion object {

        private val DEFAULT_MOVIE_VIEW_DURATION = 1000
    }

}