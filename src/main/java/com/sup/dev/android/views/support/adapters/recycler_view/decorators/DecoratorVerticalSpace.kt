package com.sup.dev.android.views.support.adapters.recycler_view.decorators

import android.graphics.Rect
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import com.sup.dev.android.tools.ToolsView


class DecoratorVerticalSpace constructor(dp: Int = 4) : androidx.recyclerview.widget.RecyclerView.ItemDecoration() {

    private val space: Int = ToolsView.dpToPx(dp).toInt()

    override fun getItemOffsets(outRect: Rect, view: View, parent: androidx.recyclerview.widget.RecyclerView, state: androidx.recyclerview.widget.RecyclerView.State) {
        outRect.bottom = space
    }
}