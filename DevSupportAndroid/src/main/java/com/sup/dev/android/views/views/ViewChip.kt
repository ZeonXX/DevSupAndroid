package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.*
import android.support.annotation.ColorInt
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.animations.AnimationFocus
import com.sup.dev.java.classes.animation.AnimationSpringColor
import com.sup.dev.java.tools.ToolsColor


class ViewChip @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    private val animationFocus: AnimationFocus
    private val animationBackground: AnimationSpringColor
    private val paint: Paint
    private val path: Path

    private val view: View
    private val vTextView: TextView
    private val vIcon: ViewCircleImage

    private var hasIcon = false
    private var selectionMode = false
    private var canUnselect = true
    private var canSelect = true
    private var useIconBackground = false
    private var background: Int = 0
    private var unselectedBackground: Int = 0

    private var isChipSelected = true

    var onActiveChange: ((Boolean)->Unit)? = null

    init {

        SupAndroid.initEditMode(this)
        val focusColor = ToolsResources.getColor(R.color.focus)
        unselectedBackground = focusColor

        setWillNotDraw(false)
        background = ToolsResources.getAccentColor(context)

        path = Path()
        paint = Paint()
        paint.isAntiAlias = true

        view = ToolsView.inflate(context, R.layout.view_chip)
        vTextView = view.findViewById(R.id.dev_sup_text)
        vIcon = view.findViewById(R.id.dev_sup_icon)

        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewChip, 0, 0)
        val text = a.getString(R.styleable.ViewChip_android_text)
        background = a.getColor(R.styleable.ViewChip_ViewChip_background, background)
        selectionMode = a.getBoolean(R.styleable.ViewChip_ViewChip_selectionMode, selectionMode)
        canSelect = a.getBoolean(R.styleable.ViewChip_ViewChip_canSelect, canSelect)
        canUnselect = a.getBoolean(R.styleable.ViewChip_ViewChip_canUnselect, canUnselect)
        unselectedBackground = a.getColor(R.styleable.ViewChip_ViewChip_unselectBackground, unselectedBackground)
        isChipSelected = a.getBoolean(R.styleable.ViewChip_ViewChip_selected, isChipSelected)
        useIconBackground = a.getBoolean(R.styleable.ViewChip_ViewChip_iconUseBackground, useIconBackground)
        val textColor = a.getColor(R.styleable.ViewChip_ViewChip_textColor, 0)
        val textStyle = a.getInteger(R.styleable.ViewChip_ViewChip_textStyle, 0)
        val icon = a.getResourceId(R.styleable.ViewChip_ViewChip_icon, 0)
        val iconPadding = a.getDimension(R.styleable.ViewChip_ViewChip_iconPadding, 0f)
        val size = a.getDimension(R.styleable.ViewChip_ViewChip_size, ToolsView.dpToPx(28f).toFloat())
        a.recycle()

        animationFocus = AnimationFocus(this, focusColor)
        animationBackground = AnimationSpringColor(if (isChipSelected) background else unselectedBackground, 200)

        vTextView.text = text
        addView(view)

        setSize(ToolsView.pxToDp(size))
        setIconPadding(ToolsView.pxToDp(iconPadding))
        setIcon(icon)
        setSelectionMode(selectionMode)
        if (textColor != 0)
            vTextView.setTextColor(textColor)
        else if (textStyle == 1)
            vTextView.setTypeface(vTextView.typeface, Typeface.BOLD)
        else if (textStyle == 2)
            vTextView.setTypeface(vTextView.typeface, Typeface.ITALIC)
        else if (textStyle == 3) vTextView.setTypeface(vTextView.typeface, Typeface.BOLD_ITALIC)
    }

    override fun onDraw(canvas: Canvas) {

        animationBackground.update()

        paint.color = animationBackground.getColor()
        canvas.drawPath(path, paint)

        super.onDraw(canvas)

        paint.color = animationFocus.update()
        canvas.drawPath(path, paint)

        if (animationBackground.isNeedUpdate()) invalidate()
    }


    fun performUserClick() {
        if (!selectionMode) return
        if (isChipSelected() && !canUnselect) return
        if (!isChipSelected() && !canSelect) return

        setChipSelectedAnimated(!isChipSelected())

        if (onActiveChange != null) onActiveChange!!.invoke(isChipSelected())
    }

    //
    //  Chip
    //


    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        recreateChip()
    }

    private fun recreateChip() {

        path.reset()

        if (height < width) {
            if (!hasIcon || useIconBackground || vIcon.isDisableCircle)
                path.addArc(RectF(0f, 0f, height.toFloat(), height.toFloat()), 90f, 180f)
            path.addArc(RectF((width - height).toFloat(), 0f, width.toFloat(), height.toFloat()), 270f, 180f)
            path.addRect((height / 2).toFloat(), 0f, (width - height / 2).toFloat(), height.toFloat(), Path.Direction.CCW)
        } else {
            if (!hasIcon || useIconBackground || vIcon.isDisableCircle)
                path.addCircle((width / 2).toFloat(), (height / 2).toFloat(), (width / 2).toFloat(), Path.Direction.CCW)
        }
    }

    private fun update() {
        (vTextView.layoutParams as FrameLayout.LayoutParams).setMargins(if (hasIcon) (vIcon.layoutParams.width / 5.0 * 4).toInt() else 0, 0, 0, 0)
        vIcon.visibility = if (hasIcon) View.VISIBLE else View.GONE
        vTextView.visibility = if (vTextView.text.length == 0) View.GONE else View.VISIBLE
        visibility = if (vIcon.visibility == View.VISIBLE || vTextView.visibility == View.VISIBLE) View.VISIBLE else View.GONE
        requestLayout()
    }

    private fun updateColors(animated: Boolean) {
        var bcColor = if (isChipSelected) background else unselectedBackground
        if (!isEnabled) bcColor = ToolsColor.setAlpha(ToolsColor.alpha(bcColor) - 80, bcColor)
        animationBackground.change(animated, bcColor)
        animationFocus.setClickAnimationEnabled(!isChipSelected)
        invalidate()
    }

    //
    //  Setters
    //


    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        vTextView.isEnabled = enabled
        vIcon.isEnabled = enabled
        updateColors(false)
    }

    fun setEnabledAnimated(enabled: Boolean) {
        super.setEnabled(enabled)
        vTextView.isEnabled = enabled
        vIcon.isEnabled = enabled
        updateColors(true)
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        super.setOnClickListener(l)
        isClickable = l != null
    }

    override fun setOnTouchListener(l: View.OnTouchListener?) {
        super.setOnTouchListener(l)
        if (l == null) animationFocus.resetTouchListener()
    }

    override fun setOnFocusChangeListener(l: View.OnFocusChangeListener?) {
        super.setOnFocusChangeListener(l)
        if (l == null) animationFocus.resetOnFocusChangedListener()
    }

    fun setIconPadding(dp: Int) {
        val px = ToolsView.dpToPx(dp.toFloat())
        vIcon.isDisableCircle = px > 0
        vIcon.setPadding(px, px, px, px)
        recreateChip()
    }

    fun setIcon(@DrawableRes icon: Int) {
        hasIcon = icon != 0

        if (icon == 0)
            vIcon.setImageBitmap(null)
        else
            vIcon.setImageResource(icon)
        update()

    }

    fun setIcon(bitmap: Bitmap?) {
        hasIcon = bitmap != null

        vIcon.setImageBitmap(bitmap)
        update()
    }

    fun setSize(dp: Int) {
        val size = ToolsView.dpToPx(dp.toFloat())
        vIcon.layoutParams.width = size
        vIcon.layoutParams.height = size
        vTextView.layoutParams.height = size
        vTextView.textSize = dp / 2.5f
        vTextView.setPadding(size / 3, 0, size / 3, 0)
    }

    fun setChipSelected(b: Boolean) {
        isChipSelected = b
        updateColors(false)
    }

    fun setChipSelectedAnimated(b: Boolean) {
        isChipSelected = b
        updateColors(true)
    }

    fun setText(@StringRes text: Int) {
        vTextView.setText(text)
        update()
    }

    fun setText(text: String?) {
        vTextView.text = text
        update()
    }

    fun setCanSelect(canSelect: Boolean) {
        this.canSelect = canSelect
    }

    fun setCanUnselect(canUnselect: Boolean) {
        this.canUnselect = canUnselect
    }

    fun setOnChipActiveChange(onActiveChange: ((Boolean) -> Unit)?) {
        this.onActiveChange = onActiveChange
    }

    fun setSelectionMode(selectionMode: Boolean) {
        this.selectionMode = selectionMode
        if (selectionMode) setOnClickListener{ vi -> performUserClick() }
        else setOnClickListener(null)
    }

    fun setChipBackgroundRes(@ColorRes background: Int) {
        setChipBackground(ToolsResources.getColor(background))
    }

    fun setChipBackground(@ColorInt background: Int) {
        this.background = background
        updateColors(false)
    }

    fun setChipBackgroundAnimated(@ColorInt background: Int) {
        this.background = background
        updateColors(true)
    }

    fun setUnselectedBackground(@ColorInt unselectedBackground: Int) {
        this.unselectedBackground = unselectedBackground
        updateColors(false)
    }

    fun setUnselectedBackgroundAnimated(@ColorInt unselectedBackground: Int) {
        this.unselectedBackground = unselectedBackground
        updateColors(true)
    }

    fun setUseIconBackground(useIconBackground: Boolean) {
        this.useIconBackground = useIconBackground
        recreateChip()
        invalidate()
    }

    //
    //  Getters
    //

    fun getText():String{
        return vTextView.text.toString()
    }

    fun isChipSelected(): Boolean {
        return isChipSelected
    }

    fun hasIcon(): Boolean {
        return hasIcon
    }

    companion object {

        //
        //  Static instance
        //

        fun instanceSelectionRadio(viewContext: Context, texts: Array<String>, onSelected: (String) -> Unit): Array<ViewChip?> {
            return instanceSelectionRadio(viewContext, texts, texts, onSelected)
        }

        fun <K> instanceSelectionRadio(viewContext: Context, texts: Array<String>, tags: Array<K>, onSelected: ((K)->Unit)?): Array<ViewChip?> {

            if (texts.size != tags.size)
                throw IllegalArgumentException("Texts and Tags lengths must be equals")

            val views:Array<ViewChip?> = arrayOfNulls<ViewChip?>(texts.size)
            for (i in texts.indices) {
                views[i] = instanceSelection(viewContext, texts[i]) { b ->
                    for (v in views) if (v !== views[i]) v!!.setChipSelectedAnimated(false)
                    if (onSelected != null) onSelected.invoke(tags[i])
                }
                views[i]!!.setCanUnselect(false)
                views[i]!!.setChipSelected(i == 0)
            }
            return views
        }

        fun <K> instanceSelection(viewContext: Context, text: String, tag: K, onSelectionChanged: ((Boolean, K)->Unit)?): ViewChip {
            return instanceSelection(viewContext, text) { b -> if (onSelectionChanged != null) onSelectionChanged!!.invoke(b, tag) }
        }

        fun instanceSelection(viewContext: Context, text: String, onSelectionChanged: (Boolean) -> Unit): ViewChip {
            val v = ViewChip(viewContext)
            v.setSelectionMode(true)
            v.setText(text)
            v.setOnChipActiveChange(onSelectionChanged)
            return v
        }
    }


}
