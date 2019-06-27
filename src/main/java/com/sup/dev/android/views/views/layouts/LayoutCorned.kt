package com.sup.dev.android.views.views.layouts

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import android.util.AttributeSet
import android.widget.FrameLayout
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView


open class LayoutCorned @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0,
        @StyleRes defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val path = Path()
    private var paint: Paint? = null
    private var cornedSize = ToolsView.dpToPx(16)
    private var cornedTL = true
    private var cornedTR = true
    private var cornedBL = true
    private var cornedBR = true
    private var chipMode = false
    private var circleMode = false

    init {
        setWillNotDraw(false)

        val a = context.obtainStyledAttributes(attrs, R.styleable.LayoutCorned)
        cornedTL = a.getBoolean(R.styleable.LayoutCorned_LayoutCorned_cornedTL, true)
        cornedTR = a.getBoolean(R.styleable.LayoutCorned_LayoutCorned_cornedTR, true)
        cornedBL = a.getBoolean(R.styleable.LayoutCorned_LayoutCorned_cornedBL, true)
        cornedBR = a.getBoolean(R.styleable.LayoutCorned_LayoutCorned_cornedBR, true)
        chipMode = a.getBoolean(R.styleable.LayoutCorned_LayoutCorned_chipMode, false)
        circleMode = a.getBoolean(R.styleable.LayoutCorned_LayoutCorned_circleMode, false)
        cornedSize = a.getDimension(R.styleable.LayoutCorned_LayoutCorned_cornedSize, cornedSize)
        a.recycle()

    }

    override fun draw(canvas: Canvas?) {
        if (cornedSize > 0 && (cornedTL || cornedTR || cornedBL || cornedBR || chipMode || circleMode))
            canvas?.clipPath(path)
        if (paint != null && paint!!.color != 0) canvas?.drawPath(path, paint)

        super.draw(canvas)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        update()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (circleMode && measuredWidth < measuredHeight)
            super.onMeasure(heightMeasureSpec, heightMeasureSpec)
    }

    private fun update() {
        path.reset()

        var r = Math.min(Math.min(cornedSize, width.toFloat() / 2), height.toFloat() / 2)

        if (chipMode) r = Math.min(width.toFloat(), height.toFloat()) / 2

        if (cornedTL) path.addCircle(r, r, r, Path.Direction.CCW)
        else path.addRect(0f, 0f, r, r, Path.Direction.CCW)

        if (cornedTR) path.addCircle(width - r, r, r, Path.Direction.CCW)
        else path.addRect(width - r, 0f, width.toFloat(), r, Path.Direction.CCW)

        if (cornedBL) path.addCircle(r, height - r, r, Path.Direction.CCW)
        else path.addRect(0f, height - r, r, height.toFloat(), Path.Direction.CCW)

        if (cornedBR) path.addCircle(width - r, height - r, r, Path.Direction.CCW)
        else path.addRect(width - r, height - r, width.toFloat(), height.toFloat(), Path.Direction.CCW)

        path.addRect(r, 0f, width - r, r, Path.Direction.CCW)
        path.addRect(r, height.toFloat(), width - r, height - r, Path.Direction.CW)
        path.addRect(0f, r, width.toFloat(), height - r, Path.Direction.CCW)

        invalidate()
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

    fun setChipMode(chipMode: Boolean) {
        this.chipMode = chipMode
        update()
    }

    fun setCircleMode(circleMode: Boolean) {
        this.circleMode = circleMode
        requestLayout()
    }

    fun setCornedSize(dp: Int) {
        setCornedSizePx(ToolsView.dpToPx(dp).toInt())
    }

    fun setCornedSizePx(cornedSize: Int) {
        this.cornedSize = cornedSize.toFloat()
        update()
    }


    fun setBackgroundRes(r: Int) {
        setBackgroundColor(ToolsResources.getColor(r))
    }


    override fun setBackground(background: Drawable?) {

        if (paint == null) {
            paint = Paint()
            paint!!.isAntiAlias = true
            paint!!.color = 0
        }

        if (background != null && background is ColorDrawable) {
            paint!!.color = background.color
            super.setBackground(null)
        } else {
            paint!!.color = 0
            super.setBackground(background)
        }
    }

}
