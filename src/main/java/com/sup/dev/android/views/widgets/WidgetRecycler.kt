package com.sup.dev.android.views.widgets

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.adapters.recycler_view.RecyclerCardAdapter
import com.sup.dev.android.views.popup.PopupWidget
import com.sup.dev.android.views.popup.PopupX
import com.sup.dev.android.views.splash.Sheet
import com.sup.dev.java.libs.debug.log

open class WidgetRecycler(
        r:Int = R.layout.widget_recycler
) : Widget(r) {

    protected val vRoot: ViewGroup = findViewById(R.id.vRoot)
    protected val vRecycler: RecyclerView = findViewById(R.id.vRecycler)
    protected val vContainer: ViewGroup = findViewById(R.id.vContainer)

    protected var adapter: RecyclerCardAdapter? = null

    init {
        vRecycler.isVerticalScrollBarEnabled = false
        updateLayoutsSettings()
    }

    override fun onShow() {
        super.onShow()
        updateLayoutsSettings()
    }

    private fun updateLayoutsSettings(){
        vRecycler.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        vRecycler.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        if (viewWrapper is Sheet || viewWrapper is PopupX) vRecycler.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
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
