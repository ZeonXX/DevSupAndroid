package com.sup.dev.android.views.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.support.design.chip.Chip
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.DrawableBitmapCircle
import com.sup.dev.java.libs.debug.log


class ViewChip(context: Context, attributeSet: AttributeSet?, defStyleAttr: Int) : Chip(context, attributeSet, defStyleAttr) {

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

    private var chipIconPadding = 0f
    var autoControlVisibility = true

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attributeSet: AttributeSet?) : this(context, attributeSet, 0)

    init {
        updateChipVisible()
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        if (params != null) {
            if (params.height < 0) params.height = ToolsView.dpToPx(32).toInt()
            setChipSizePx(params.height)
        }
    }

    fun setChipSizePx(size: Int) {
        layoutParams.height = size
        chipIconSize = size.toFloat() - chipIconPadding
        closeIconStartPadding = 0f
        closeIconEndPadding = 0f
        chipStartPadding = 0f
        chipEndPadding = 0f
        updatePadding()
    }

    private fun updatePadding() {
        if (text.isEmpty() || layoutParams == null) {
            textEndPadding = 0f
            textStartPadding = 0f

            if(chipIcon != null){
                chipStartPadding = (layoutParams.height-chipIconSize)/2
                chipEndPadding = chipStartPadding
            }else{
                chipStartPadding = 0f
                chipEndPadding = 0f
            }
        } else {
            textEndPadding = layoutParams.height / 5f
            textSize = ToolsView.pxToSp(layoutParams.height / 2.3f)

            textStartPadding = when {
                layoutParams == null -> 0f
                chipIcon == null -> textEndPadding
                else -> ToolsView.dpToPx(4)
            }

            if (layoutParams.height < 60) {
                textSize = ToolsView.pxToSp(layoutParams.height / 2f)
                textStartPadding += ToolsView.dpToPx(1)
                textEndPadding = ToolsView.dpToPx(1f)

                if (text.length == 1) {
                    textStartPadding += ToolsView.dpToPx(1)
                    textEndPadding += ToolsView.dpToPx(2)
                }

            }
        }

    }

    fun updateChipVisible() {

        if (!autoControlVisibility) return

        if (chipIcon == null && text.isEmpty())
            visibility = View.GONE
        else
            visibility = View.VISIBLE
    }

    fun setChipIconPadding(chipIconPadding: Float) {
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