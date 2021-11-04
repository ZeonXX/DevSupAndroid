package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.support.AnimationFocus
import com.sup.dev.java.classes.animation.AnimationSpringColor
import com.sup.dev.java.tools.ToolsColor

class ViewIcon @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : android.support.v7.widget.AppCompatImageView(context, attrs) {

    private val paint: Paint
    private val animationFocus: AnimationFocus
    private val animationSelectedBackground: AnimationSpringColor

    private var src = 0
    private var bitmap: Bitmap? = null
    private var filter = 0
    private var srcSelect = 0
    private var filterSelect = 0
    private var accentColor: Int = 0
    private var paddingCircle: Float = 0.toFloat()
    private var background = 0
    private var useActiveBackground = true
    private var circleColor = -0x1
    private var circleSize = 0f
    private var transparentOnDisabled = true
    private var startPadding = -1

    //
    //  Getters
    //

    var isIconSelected: Boolean = false
        set(selected) {
            field = selected
            updateIcon()
            animationFocus.setClickAnimationEnabled(!selected)
        }

    init {

        SupAndroid.initEditMode(this)

        accentColor = ToolsResources.getAccentColor(context)
        var focusColor = ToolsResources.getColor(R.color.focus)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewIcon, 0, 0)

        src = a.getResourceId(R.styleable.ViewIcon_android_src, src)
        val enabled = a.getBoolean(R.styleable.ViewIcon_android_enabled, true)
        srcSelect = a.getResourceId(R.styleable.ViewIcon_ViewIcon_srcSelect, srcSelect)
        filter = a.getColor(R.styleable.ViewIcon_ViewIcon_filter, filter)
        filterSelect = a.getColor(R.styleable.ViewIcon_ViewIcon_filterSelect, filterSelect)
        accentColor = a.getColor(R.styleable.ViewIcon_ViewIcon_accentColor, accentColor)
        focusColor = a.getColor(R.styleable.ViewIcon_ViewIcon_focusColor, focusColor)
        paddingCircle = a.getDimension(R.styleable.ViewIcon_ViewIcon_padding, paddingCircle)
        background = a.getColor(R.styleable.ViewIcon_ViewIcon_background, background)
        useActiveBackground = a.getBoolean(R.styleable.ViewIcon_ViewIcon_useActiveBackground, useActiveBackground)
        circleColor = a.getColor(R.styleable.ViewIcon_ViewIcon_circleColor, circleColor)
        circleSize = a.getDimension(R.styleable.ViewIcon_ViewIcon_circleSize, circleSize)
        transparentOnDisabled = a.getBoolean(R.styleable.ViewIcon_ViewIcon_transparent_on_disabled, transparentOnDisabled)
        a.recycle()

        animationFocus = AnimationFocus(this, focusColor)
        animationSelectedBackground = AnimationSpringColor(0x0000000, 200)

        paint = Paint()
        paint.style = Paint.Style.FILL
        paint.isAntiAlias = true

        isEnabled = enabled
        updateIcon()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        updateIcon()
    }

    //
    //  Draw
    //

    override fun onDraw(canvas: Canvas) {

        animationSelectedBackground.update()

        val p = paddingCircle
        val x = (width / 2).toFloat()
        val y = (height / 2).toFloat()
        val r = width / 2 - p

        if (background != 0) {
            paint.color = if (isEnabled) background else ToolsColor.setAlpha(106, background)
            canvas.drawCircle(x, y, r, paint)
        }

        if (useActiveBackground) {
            paint.color = animationSelectedBackground.getColor()
            canvas.drawCircle(x, y, r, paint)
        }


        if (circleSize != 0f) {
            paint.style = Paint.Style.STROKE
            paint.color = if (transparentOnDisabled && !isEnabled) ToolsColor.setAlpha(106, circleColor) else circleColor
            paint.strokeWidth = circleSize
            canvas.drawCircle(x, y, r - circleSize / 2, paint)
            paint.style = Paint.Style.FILL
        }

        super.onDraw(canvas)

        paint.color = animationFocus.update()
        canvas.drawCircle(x, y, r, paint)

        if (animationSelectedBackground.isNeedUpdate())
            invalidate()

    }

    private fun updateIcon() {

        animationSelectedBackground.to(if (isIconSelected) if (isEnabled) accentColor else ToolsColor.setAlpha(106, accentColor) else 0x00000000)

        if (src > 0)
            super.setImageResource(if (isIconSelected && srcSelect != 0) srcSelect else src)

        val myFilter = if (isIconSelected && filterSelect != 0) filterSelect else filter
        if (myFilter != 0) setColorFilter(myFilter, PorterDuff.Mode.SRC_ATOP)

        setAlpha(if (!isEnabled && transparentOnDisabled) 106 else 255)
    }


    //
    //  Setters
    //

    fun setTransparentOnDisabled(transparentOnDisabled: Boolean) {
        this.transparentOnDisabled = transparentOnDisabled
        updateIcon()
    }

    override fun setOnTouchListener(l: View.OnTouchListener?) {
        super.setOnTouchListener(l)
        if (l == null) animationFocus.resetTouchListener()
    }

    override fun setOnFocusChangeListener(l: View.OnFocusChangeListener?) {
        super.setOnFocusChangeListener(l)
        if (l == null) animationFocus.resetOnFocusChangedListener()
    }

    fun setIconBackgroundColor(color: Int) {
        this.background = color
        invalidate()
    }

    override fun setImageResource(resId: Int) {
        this.src = resId
        if(startPadding == -1)startPadding = paddingLeft
        setPadding(startPadding, startPadding, startPadding, startPadding)
        scaleType = ScaleType.FIT_CENTER
        updateIcon()
    }

    override fun setImageBitmap(bm: Bitmap?) {
        this.src = 0
        if(startPadding == -1)startPadding = paddingLeft
        setPadding(0, 0, 0, 0)
        scaleType = ScaleType.CENTER_CROP
        super.setImageBitmap(bm)
        updateIcon()
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        animationFocus.updateFocusColor()
        updateIcon()
    }

    fun setPaddingCircle(padding: Float) {
        this.paddingCircle = padding
        invalidate()
    }

    fun setFilter(filter: Int) {
        this.filter = filter
        updateIcon()
    }
}

