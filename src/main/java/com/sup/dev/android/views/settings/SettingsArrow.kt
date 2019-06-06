package com.sup.dev.android.views.settings

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources

class SettingsArrow @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : Settings(context, attrs) {

    private val vArrow: ImageView = ImageView(context)

    init {

        vArrow.setImageDrawable(ToolsResources.getDrawableAttr(R.attr.ic_keyboard_arrow_right_24dp))

        setSubView(vArrow)
    }

}