package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.util.AttributeSet

class ViewCircleImage constructor(context: Context, attrs: AttributeSet? = null) : android.support.v7.widget.AppCompatImageView(context, attrs) {

    private val path = Path()
    var isDisableCircle = false

    override fun draw(canvas: Canvas) {
        if (!isDisableCircle) {
            path.reset()
            path.addCircle((width / 2).toFloat(), (height / 2).toFloat(), (Math.min(width, height) / 2).toFloat(), Path.Direction.CCW)
            canvas.clipPath(path)
        }
        super.draw(canvas)
    }


}
