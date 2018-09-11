package com.sup.dev.android.views.views.layouts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout


open class LayoutChip @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private var paint: Paint? = null
    private val path: Path

    init {
        path = Path()
        setWillNotDraw(false)
    }

    override fun setBackgroundDrawable(background: Drawable?) {
        super.setBackgroundDrawable(null)

        if (paint == null) {
            paint = Paint()
            paint!!.isAntiAlias = true
            paint!!.color = 0x00000000
        }

        if (background != null && background is ColorDrawable)
            paint!!.color = background.color

    }

    override fun onDraw(canvas: Canvas) {
        if (paint != null) canvas.drawPath(path, paint!!)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        path.reset()
        if (height < width) {
            path.addArc(RectF(0f, 0f, height.toFloat(), height.toFloat()), 90f, 180f)
            path.addArc(RectF((width - height).toFloat(), 0f, width.toFloat(), height.toFloat()), 270f, 180f)
            path.addRect((height / 2).toFloat(), 0f, (width - height / 2).toFloat(), height.toFloat(), Path.Direction.CCW)
        } else {
            path.addCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat(), Path.Direction.CCW)

        }
    }

}
