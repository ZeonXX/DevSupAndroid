package com.sup.dev.android.views.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.support.design.chip.Chip
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.DrawableBitmapCircle


class ViewChip(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : Chip(context, attributeSet, defStyleAttr) {

    var autoControlVisibility = true

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    companion object {

        fun instance(context: Context, name: String? = null, tag: Any? = null): ViewChip {
            val v: ViewChip = ToolsView.inflate(context, R.layout.z_chip)
            v.text = name
            v.tag = tag
            return v
        }

        fun instanceOutline(context: Context, name: String? = null, tag: Any? = null): ViewChip {
            val v: ViewChip = ToolsView.inflate(context, R.layout.z_chip_outline)
            v.text = name
            v.tag = tag
            return v
        }

        fun instanceChoose(context: Context, name: String? = null, tag: Any? = null): ViewChip {
            val v: ViewChip = ToolsView.inflate(context, R.layout.z_chip_choose)
            v.text = name
            v.tag = tag
            return v
        }

        fun instanceChooseOutline(context: Context, name: String? = null, tag: Any? = null): ViewChip {
            val v: ViewChip = ToolsView.inflate(context, R.layout.z_chip_choose_outline)
            v.text = name
            v.tag = tag
            return v
        }

        fun instanceMini(context: Context, name: String? = null, tag: Any? = null): ViewChip {
            val v: ViewChip = ToolsView.inflate(context, R.layout.z_chip_mini)
            v.text = name
            v.tag = tag
            return v
        }

        fun instanceMiniOutline(context: Context, name: String? = null, tag: Any? = null): ViewChip {
            val v: ViewChip = ToolsView.inflate(context, R.layout.z_chip_mini_outline)
            v.text = name
            v.tag = tag
            return v
        }

    }

    private var chipIconPadding: Int = 0

    init {
        updateChipVisible()
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        if (params != null) {
            if (params.height < 0) params.height = ToolsView.dpToPx(32)
            setChipSizePx(params.height)
        }
    }

    fun setChipSizePx(size: Int) {
        layoutParams.height = size
        gravity = Gravity.CENTER
        textAlignment = View.TEXT_ALIGNMENT_GRAVITY
        textSize = size / 6f
        chipEndPadding = 0f
        updatePadding()
        chipIconSize = size.toFloat() - chipIconPadding
    }

    private fun updatePadding() {
        if (text.isEmpty() || layoutParams == null) {
            textEndPadding = 0f
            textStartPadding = 0f
            chipStartPadding = 0f
        } else {
            textStartPadding = when {
                layoutParams == null -> 0f
                chipIcon == null -> layoutParams.height / 6f
                else -> layoutParams.height / 4f
            }
            textEndPadding = layoutParams.height / 6f

            if(chipIcon != null) chipStartPadding = 0f
            else chipStartPadding = ToolsView.dpToPx(4f).toFloat()
        }

    }

    fun updateChipVisible() {

        if (!autoControlVisibility) return

        if (chipIcon == null && text.isEmpty())
            visibility = View.GONE
        else
            visibility = View.VISIBLE
    }

    fun setChipIconPadding(chipIconPadding: Int) {
        this.chipIconPadding = chipIconPadding
        setChipSizePx(layoutParams.height)
    }

    fun setIcon(src: Int) {
        setIcon(if (src > 0) ToolsResources.getBitmap(src) else null)
    }

    fun setIcon(bitmap: Bitmap?) {
        if (bitmap != null) chipIcon = DrawableBitmapCircle(bitmap)
        else chipIcon = null
        updateChipVisible()
        updatePadding()
    }

    fun setBackgroundRes(res: Int) {
        setBackground(ToolsResources.getColor(res))
    }

    fun setBackground(color: Int) {
        chipBackgroundColor = ColorStateList.valueOf(color)
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        updateChipVisible()
        updatePadding()
    }


}