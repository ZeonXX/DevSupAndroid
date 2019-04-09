package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet
import com.sup.dev.android.models.EventStyleChanged
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.java.libs.eventBus.EventBus

class ViewCircleImage constructor(context: Context, attrs: AttributeSet? = null) : android.support.v7.widget.AppCompatImageView(context, attrs) {

    companion object {
        var SQUARE_GLOBAL_CORNED = ToolsView.dpToPx(10)
        var SQUARE_GLOBAL_MODE = true
    }

    private var squareMode = SQUARE_GLOBAL_MODE
    private var squareCorned = SQUARE_GLOBAL_CORNED
    private var useGlobalStyle = true
    private var backgroundColorCircle = 0x00000000
    private val path = Path()
    private var ww = -1
    private var hh = -1

    private val eventBus = EventBus.subscribe(EventStyleChanged::class){
        if(useGlobalStyle) {
            squareMode = SQUARE_GLOBAL_MODE
            squareCorned = SQUARE_GLOBAL_CORNED
            invalidate()
        }
    }

    init {

    }

    override fun draw(canvas: Canvas) {
        if (ww != width || hh != height) {
            this.ww = ww
            this.hh = hh

            if (squareMode) {
                path.reset()
                val dp = squareCorned
                path.addCircle(dp, dp, dp, Path.Direction.CCW)
                path.addCircle(width - dp, dp, dp, Path.Direction.CCW)
                path.addCircle(dp, height - dp, dp, Path.Direction.CCW)
                path.addCircle(width - dp, height - dp, dp, Path.Direction.CCW)
                path.addRect(0f, dp, width + 0f, height - dp, Path.Direction.CCW)
                path.addRect(dp, 0f, width - dp, dp, Path.Direction.CCW)
                path.addRect(dp, height - dp, width - dp, height + 0f, Path.Direction.CCW)
                canvas.clipPath(path)
            } else {
                path.reset()
                path.addCircle((width / 2).toFloat(), (height / 2).toFloat(), (Math.min(width, height) / 2).toFloat(), Path.Direction.CCW)
                canvas.clipPath(path)
            }
        }
        if (backgroundColorCircle != 0x00000000)
            canvas.drawColor(backgroundColorCircle)
        super.draw(canvas)
    }

    fun setSquareMode(squareMode: Boolean) {
        this.squareMode = squareMode
        this.ww = -1
        this.hh = -1
        invalidate()
    }

    fun isSquareMode() = squareMode

    fun getSquareCorned() = squareCorned

    fun setBackgroundColorCircleRes(color: Int) {
        setBackgroundColorCircle(ToolsResources.getColor(color))
    }

    fun setBackgroundColorCircle(color: Int) {
        this.backgroundColorCircle = color
        invalidate()
    }


}
