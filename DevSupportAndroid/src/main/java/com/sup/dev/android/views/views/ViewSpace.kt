package com.sup.dev.android.views.views

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.sup.dev.android.tools.ToolsView


class ViewSpace(context: Context, wDP: Int, hDP: Int) : View(context) {

    private val w: Int = ToolsView.dpToPx(wDP.toFloat())
    private val h: Int = ToolsView.dpToPx(hDP.toFloat())

    override fun setLayoutParams(params: ViewGroup.LayoutParams) {
        params.width = w
        params.height = h
        super.setLayoutParams(params)
    }
}
