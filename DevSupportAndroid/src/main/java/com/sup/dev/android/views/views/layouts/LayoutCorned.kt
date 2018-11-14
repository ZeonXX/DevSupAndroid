package com.sup.dev.android.views.views.layouts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.widget.FrameLayout
import com.sup.dev.android.tools.ToolsView

open class LayoutCorned @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private var paint: Paint? = null
    private val path: Path = Path()
    private var cornedSize = ToolsView.dpToPx(16).toFloat()
    private var cornedTL = true
    private var cornedTR = true
    private var cornedBL = true
    private var cornedBR = true

    init {
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

    override fun draw(canvas: Canvas?) {
        canvas?.clipPath(path)
        super.draw(canvas)
    }

    override fun onDraw(canvas: Canvas) {
        if (paint != null) canvas.drawPath(path, paint!!)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        update()
    }

    private fun update() {
        path.reset()

        val r = Math.min(Math.min(cornedSize, width.toFloat()), height.toFloat())

        if (cornedTL) path.addCircle(r, r, r, Path.Direction.CCW)
        else path.addRect(0f, 0f, r, r, Path.Direction.CCW)

        if (cornedTR) path.addCircle(width - r, r, r, Path.Direction.CCW)
        else path.addRect(width - r, 0f, width.toFloat(), r, Path.Direction.CCW)

        if (cornedBL) path.addCircle(r, height - r, r, Path.Direction.CCW)
        else path.addRect(0f, height-r, r, height.toFloat(), Path.Direction.CCW)

        if (cornedBR) path.addCircle(width - r, height - r, r, Path.Direction.CCW)
        else path.addRect(width - r, height-r, width.toFloat(), height.toFloat(), Path.Direction.CCW)

        path.addRect(r, 0f, width - r, r, Path.Direction.CCW)
        path.addRect(r, height.toFloat(), width - r, height - r, Path.Direction.CW)
        path.addRect(0f, r, width.toFloat(), height - r, Path.Direction.CCW)
    }

    fun setCornedTL(cornedTL: Boolean) {
        this.cornedTL = cornedTL
        update()
    }

    fun setCornedTR(cornedTR: Boolean) {
        this.cornedTR = cornedTR
        update()
    }

    fun setCornedBL(cornedBL: Boolean) {
        this.cornedBL = cornedBL
        update()
    }

    fun setCornedBR(cornedBR: Boolean) {
        this.cornedBR = cornedBR
        update()
    }

    fun setCornedSize(cornedSize: Float) {
        this.cornedSize = cornedSize
        update()
    }

}
