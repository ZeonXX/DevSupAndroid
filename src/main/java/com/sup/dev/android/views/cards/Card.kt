package com.sup.dev.android.views.cards

import android.content.Context
import android.view.View
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.adapters.CardAdapter


abstract class Card(
    private val layout:Int
) {

    var adapter: CardAdapter? = null
    private var view:View? = null
    var isBinding = false

    var tag: Any? = null

    //
    //  Bind
    //

    fun update() {
        if(isBinding) return
        val view = getView()
        if (view != null) bindCardView(view)
    }

    fun bindCardView(view: View) {
        isBinding = true
        this.view = view
        view.tag = this
        bindView(view)
        isBinding = false
    }

    open fun bindView(view: View) {
    }

    protected open fun instanceView(): View {
        return View(SupAndroid.appContext)
    }

    fun getView(): View? {
        val view = if(adapter == null)  null else adapter!!.getView(this)
        if(view != null){
            this.view = view
            view.tag = this
        }

        if(this.view != null && this.view!!.tag != this) this.view = null

        return this.view
    }

    fun remove(){
        if(adapter != null) adapter!!.remove(this)
    }

    //
    //  Adapter
    //

    open fun instanceView(context: Context): View {
        return if (layout > 0) ToolsView.inflate(context, layout) else instanceView()
    }

    open fun setCardAdapter(adapter: CardAdapter?) {
        this.adapter = adapter
    }


}
