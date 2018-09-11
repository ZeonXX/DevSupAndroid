package com.sup.dev.android.views.widgets

import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter
import com.sup.dev.android.views.dialogs.DialogSheetWidget
import com.sup.dev.android.views.popup.PopupWidget


open class WidgetRecycler : Widget(R.layout.widget_recycler) {

    protected val vRoot: ViewGroup = findViewById(R.id.root)
    protected val vRecycler: RecyclerView = findViewById(R.id.recycler)
    protected val vContainer: ViewGroup = findViewById(R.id.container)

    protected var adapter: RecyclerCardAdapter? = null

    override fun onShow() {
        super.onShow()

        vRecycler.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        vRecycler.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        if (viewWrapper is DialogSheetWidget) {
            vRecycler.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        } else if (viewWrapper is PopupWidget) {
            vRecycler.layoutParams.width = ToolsView.dpToPx(200f)
        }
    }

    fun addView(view: View) {
        vRoot.addView(view, 0)
    }

    //
    //  Setters
    //

    fun <K : WidgetRecycler> setAdapter(adapter: RecyclerCardAdapter): K {
        vRecycler.adapter = adapter
        return this as K
    }

}
