package com.sup.dev.android.views.adapters.recycler_view.decorators

import android.graphics.Rect
import android.support.v7.widget.RecyclerView
import android.view.View
import com.sup.dev.android.tools.ToolsView


class DecoratorVerticalSpace constructor(dp: Int = 4) : RecyclerView.ItemDecoration() {

    private val space: Int = ToolsView.dpToPx(dp.toFloat())

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State?) {
        outRect.bottom = space
    }
}