package com.sup.dev.android.views.dialogs

import com.sup.dev.android.views.widgets.Widget
import com.sup.dev.android.views.widgets.WidgetViewWrapper


class DialogWidget(private val widget: Widget) : Dialog(widget.view), WidgetViewWrapper {


    init {
        setCancelable(widget.isCancelable)
        setEnabled<DialogWidget>(widget.isEnabled)
    }

    override fun onShow() {
        super.onShow()
        widget.onShow()
    }

    override fun onHide() {
        super.onHide()
        widget.onHide()
    }

    //
    //  Setters
    //

    override fun <K : WidgetViewWrapper> hideWidget(): K {
        hide()
        return this as K
    }

    override fun <K : WidgetViewWrapper> setWidgetCancelable(cancelable: Boolean): K {
        return setDialogCancelable<Dialog>(cancelable) as K
    }

    override fun <K : WidgetViewWrapper> setWidgetEnabled(enabled: Boolean): K {
        return setEnabled<Dialog>(enabled) as K
    }


}
