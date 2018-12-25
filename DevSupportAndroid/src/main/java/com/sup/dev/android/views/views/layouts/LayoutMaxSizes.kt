package com.sup.dev.android.views.views.layouts

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.View
import android.view.View.MeasureSpec.*
import android.view.ViewGroup
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsPaint
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView


open class LayoutMaxSizes constructor(context: Context, attrs: AttributeSet? = null) : ViewGroup(context, attrs) {

    private var maxWidth: Int = 0
    private var maxHeight: Int = 0
    private val reserveWidth: Int
    private val reserveHeight: Int
    private var maxWidthPercent: Float = 0.toFloat()
    private var maxHeightPercent: Float = 0.toFloat()
    private var alwaysMaxW = false
    private var alwaysMaxH = false
    private var childAlwaysMaxW = false
    private var childAlwaysMaxH = false
    private var useScreenWidthAsParent = false
    private var useScreenHeightAsParent = false
    private var allowChildMaxW = false
    private var allowChildMaxH = false
    private var fadeWSize: Int = 0
    private var fadeHSize: Int = 0
    private var fadeColor: Int = 0

    private var isCroppedW: Boolean = false
    private var isCroppedH: Boolean = false

    init {

        SupAndroid.initEditMode(this)

        setWillNotDraw(false)

        val a = getContext().obtainStyledAttributes(attrs, R.styleable.LayoutMaxSizes, 0, 0)
        maxWidth = a.getDimension(R.styleable.LayoutMaxSizes_LayoutMaxSizes_maxWidth, maxWidth.toFloat()).toInt()
        maxHeight = a.getDimension(R.styleable.LayoutMaxSizes_LayoutMaxSizes_maxHeight, maxHeight.toFloat()).toInt()
        reserveWidth = a.getDimension(R.styleable.LayoutMaxSizes_LayoutMaxSizes_reserveWidth, 0F).toInt()
        reserveHeight = a.getDimension(R.styleable.LayoutMaxSizes_LayoutMaxSizes_reserveHeight, 0F).toInt()
        maxWidthPercent = a.getFloat(R.styleable.LayoutMaxSizes_LayoutMaxSizes_maxWidthParentPercent, maxWidthPercent)
        maxHeightPercent = a.getFloat(R.styleable.LayoutMaxSizes_LayoutMaxSizes_maxHeightParentPercent, maxHeightPercent)
        alwaysMaxW = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_alwaysMaxW, alwaysMaxW)
        alwaysMaxH = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_alwaysMaxH, alwaysMaxH)
        childAlwaysMaxW = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_childAlwaysMaxW, childAlwaysMaxW)
        childAlwaysMaxH = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_childAlwaysMaxH, childAlwaysMaxH)
        useScreenWidthAsParent = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_useScreenWidthAsParent, useScreenWidthAsParent)
        useScreenHeightAsParent = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_useScreenHeightAsParent, useScreenHeightAsParent)
        fadeWSize = a.getDimension(R.styleable.LayoutMaxSizes_LayoutMaxSizes_fadeWSize, fadeWSize.toFloat()).toInt()
        fadeHSize = a.getDimension(R.styleable.LayoutMaxSizes_LayoutMaxSizes_fadeHSize, fadeHSize.toFloat()).toInt()
        fadeColor = a.getColor(R.styleable.LayoutMaxSizes_LayoutMaxSizes_fadeColor, fadeColor)
        allowChildMaxW = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_allowChildMaxW, allowChildMaxW)
        allowChildMaxH = a.getBoolean(R.styleable.LayoutMaxSizes_LayoutMaxSizes_allowChildMaxH, allowChildMaxH)
        a.recycle()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        for (i in 0 until childCount)
            getChildAt(i).layout(0, 0, getChildAt(i).measuredWidth, getChildAt(i).measuredHeight)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var w = if (useScreenWidthAsParent) ToolsAndroid.getScreenW() else View.MeasureSpec.getSize(widthMeasureSpec)
        var h = if (useScreenHeightAsParent) ToolsAndroid.getScreenH() else View.MeasureSpec.getSize(heightMeasureSpec)

        if (maxWidthPercent != 0f) {
            val arg = (w / 100f * maxWidthPercent).toInt()
            maxWidth = if (maxWidth == 0 || maxWidth > arg) arg else maxWidth
        }

        if (maxHeightPercent != 0f) {
            val arg = (h / 100f * maxHeightPercent).toInt()
            maxHeight = if (maxHeight == 0 || maxHeight > arg) arg else maxHeight
        }

        if (maxWidth > 0) w = maxWidth
        if (maxHeight > 0) h = maxHeight

        var maxChildW = 0
        var maxChildH = 0
        for (i in 0 until childCount) {
            getChildAt(i).measure(
                    View.MeasureSpec.makeMeasureSpec(w, if (childAlwaysMaxW) EXACTLY else if (allowChildMaxW) UNSPECIFIED else AT_MOST),
                    View.MeasureSpec.makeMeasureSpec(h, if (childAlwaysMaxH) EXACTLY else if (allowChildMaxH) UNSPECIFIED else AT_MOST))
            maxChildW = Math.max(getChildAt(i).measuredWidth, maxChildW)
            maxChildH = Math.max(getChildAt(i).measuredHeight, maxChildH)
        }


        isCroppedW = maxChildW > w
        isCroppedH = maxChildH > h

        setMeasuredDimension(if (alwaysMaxW) maxWidth else if (w == 0) maxChildW else Math.min(w, maxChildW), if (alwaysMaxH) maxHeight else if (h == 0) maxChildH else Math.min(h, maxChildH))

    }

    override fun onDrawForeground(canvas: Canvas) {
        super.onDrawForeground(canvas)


        if (fadeColor == 0 && (fadeHSize != 0 || fadeWSize != 0) && (isCroppedH || isCroppedW))
            fadeColor = ToolsResources.getColor(R.color.focus)

        if (fadeWSize != 0 && isCroppedW && fadeColor != 0) ToolsPaint.gradientLineLeftRight(canvas, fadeColor, fadeWSize)
        if (fadeHSize != 0 && isCroppedH && fadeColor != 0) ToolsPaint.gradientLineBottomTop(canvas, fadeColor, fadeHSize)

    }

    //
    //  Setters
    //


    fun setFadeHSize(dp: Int) {
        this.fadeHSize = ToolsView.dpToPx(dp).toInt()
        invalidate()
    }

    fun setFadeWSize(dp: Int) {
        this.fadeWSize = ToolsView.dpToPx(dp).toInt()
        invalidate()
    }

    fun setFadeColor(fadeColor: Int) {
        this.fadeColor = fadeColor
        invalidate()
    }

    fun setMaxWidth(maxWidthDp: Int) {
        this.maxWidth = ToolsView.dpToPx(maxWidthDp).toInt()
        requestLayout()
    }

    fun setMaxHeight(maxHeightDp: Int) {
        this.maxHeight = ToolsView.dpToPx(maxHeightDp).toInt()
        requestLayout()
    }

    fun setAlwaysMaxW(b: Boolean) {
        this.alwaysMaxW = b
        requestLayout()
    }

    fun setChildAlwaysMaxW(childAlwaysMaxW: Boolean) {
        this.childAlwaysMaxW = childAlwaysMaxW
    }

    fun setChildAlwaysMaxH(childAlwaysMaxH: Boolean) {
        this.childAlwaysMaxH = childAlwaysMaxH
    }

    fun setAlwaysMaxH(b: Boolean) {
        this.alwaysMaxH = b
        requestLayout()
    }

    fun setMaxHeightParentPercent(maxHeightPercent: Float) {
        this.maxHeightPercent = maxHeightPercent
        requestLayout()
    }

    fun setMaxWidthParentPercent(maxWidthPercent: Float) {
        this.maxWidthPercent = maxWidthPercent
        requestLayout()
    }

    fun setUseScreenWidthAsParent(useScreenWidthAsParent: Boolean) {
        this.useScreenWidthAsParent = useScreenWidthAsParent
        requestLayout()
    }

    fun setUseScreenHeightAsParent(useScreenHeightAsParent: Boolean) {
        this.useScreenHeightAsParent = useScreenHeightAsParent
        requestLayout()
    }

    //
    //  Getters
    //

    fun isCroppedW() = isCroppedW

    fun isCroppedH() = isCroppedH

    fun getMaxWidth() = maxWidth

    fun getMaxHeight() = maxHeight


}

