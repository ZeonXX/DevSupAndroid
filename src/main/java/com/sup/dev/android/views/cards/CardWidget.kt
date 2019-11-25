package com.sup.dev.android.views.cards

import android.view.View
import android.view.ViewGroup
import com.sup.dev.android.views.widgets.Widget
import com.sup.dev.android.views.widgets.WidgetViewWrapper

class CardWidget(private val widget: Widget) : Card(0), WidgetViewWrapper<CardWidget> {

    override fun instanceView(vParent: ViewGroup): View {
        return widget.view
    }

    override fun bindView(view: View) {
        super.bindView(view)
        widget.onShow()
    }

    override fun hide(): CardWidget {
        adapter!!.remove(this)
        return this
    }

    override fun setWidgetCancelable(cancelable: Boolean): CardWidget {
        return this
    }

    override fun setWidgetEnabled(enabled: Boolean): CardWidget {
        return this
    }
}
