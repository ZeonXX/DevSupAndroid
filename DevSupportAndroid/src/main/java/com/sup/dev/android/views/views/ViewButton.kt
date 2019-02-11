package com.sup.dev.android.views.views

import android.content.Context
import android.util.AttributeSet
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources

class ViewButton @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : android.support.design.button.MaterialButton(context, attrs) {

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        if(!enabled) setTextColor(ToolsResources.getColor(R.color.focus_dark))
    }

}
