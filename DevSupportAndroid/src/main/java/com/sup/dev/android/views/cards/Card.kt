package com.sup.dev.android.views.cards

import android.content.Context
import android.support.annotation.LayoutRes
import android.view.View
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.adapters.CardAdapter


abstract class Card {

    var adapter: CardAdapter? = null

    var tag: Any? = null

    //
    //  Bind
    //

    @LayoutRes
    abstract fun getLayout(): Int

    fun update() {
        if (adapter == null) return

        val view = adapter!!.getView(this)

        if (view != null) bindView(view)
    }

    abstract fun bindView(view: View)

    protected open fun instanceView(): View? {
        return null
    }

    //
    //  Adapter
    //

    open fun instanceView(context: Context): View? {
        val layout = getLayout()
        return if (layout > 0) ToolsView.inflate(context, getLayout()) else instanceView()
    }

    open fun setCardAdapter(adapter: CardAdapter?) {
        this.adapter = adapter
    }


}
