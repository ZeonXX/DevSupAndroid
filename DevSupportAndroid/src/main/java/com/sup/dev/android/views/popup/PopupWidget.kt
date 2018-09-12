package com.sup.dev.android.views.popup

import com.sup.dev.android.views.widgets.Widget
import com.sup.dev.android.views.widgets.WidgetViewWrapper


class PopupWidget(private val widget: Widget) : Popup(widget.view), WidgetViewWrapper {

    init {
        setCancelable<Popup>(widget.isCancelable)
        setEnabled<Popup>(widget.isEnabled)
    }

    override fun onShow() {
        super.onShow()
        widget.onShow()
    }

    override fun onHide() {
        super.onHide()
        widget.onHide()
    }

    override fun <K : Popup> hide(): K {
        dismiss()
        return this as K
    }

    //
    //  Setters
    //

    override fun <K : Popup> setCancelable(cancelable: Boolean): K {
        super.setCancelable<Popup>(cancelable)
        return this as K
    }

    override fun <K : Popup> setEnabled(enabled: Boolean): K {
        super.setEnabled<Popup>(enabled)
        return this as K
    }

    override fun <K : WidgetViewWrapper> hideWidget(): K {
        return hide<Popup>() as K
    }

    override fun <K : WidgetViewWrapper> setWidgetCancelable(cancelable: Boolean): K {
        return setCancelable<Popup>(cancelable) as K
    }

    override fun <K : WidgetViewWrapper> setWidgetEnabled(enabled: Boolean): K {
        return setEnabled<Popup>(enabled) as K
    }


}
