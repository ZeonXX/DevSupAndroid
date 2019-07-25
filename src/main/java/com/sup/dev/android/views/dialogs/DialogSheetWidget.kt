package com.sup.dev.android.views.dialogs

import com.sup.dev.android.views.widgets.Widget
import com.sup.dev.android.views.widgets.WidgetViewWrapper
import com.sup.dev.java.libs.debug.log

class DialogSheetWidget(private val widget: Widget) : DialogSheet(widget.view), WidgetViewWrapper {

    init {
        setCancelable(widget.isCancelable)
        setEnabled(widget.isEnabled)
    }

    override fun onTryCancelOnTouchOutside(): Boolean {
        return widget.onTryCancelOnTouchOutside()
    }

    override fun onShow() {
        super.onShow()
        log("Show " + widget)
        widget.onShow()
    }

    override fun onHide() {
        super.onHide()
        log("Hide " + widget)
        widget.onHide()
    }

    override fun <K : WidgetViewWrapper> hideWidget(): K {
        hide()
        return this as K
    }

    override fun <K : WidgetViewWrapper> setWidgetCancelable(cancelable: Boolean): K {
        return setDialogCancelable(cancelable) as K
    }

    override fun <K : WidgetViewWrapper> setWidgetEnabled(enabled: Boolean): K {
        return setEnabled(enabled) as K
    }


}
