package com.sup.dev.android.views.cards

import android.content.Context
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.view.View
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.adapters.CardAdapter


abstract class Card {

    var adapter: CardAdapter? = null

    var tag: Any? = null

    //
    //  Bind
    //

    @LayoutRes
    abstract fun getLayout(): Int

    fun update() {
        val view = getView()
        if (view != null) bindView(view)
    }

    open fun bindView(view: View) {
    }

    protected open fun instanceView(): View {
        return View(SupAndroid.appContext)
    }

    protected fun getView(): View? {
        if(adapter == null) return null
        return adapter!!.getView(this)
    }

    //
    //  Adapter
    //

    open fun instanceView(context: Context): View {
        val layout = getLayout()
        return if (layout > 0) ToolsView.inflate(context, getLayout()) else instanceView()
    }

    open fun setCardAdapter(adapter: CardAdapter?) {
        this.adapter = adapter
    }


}
