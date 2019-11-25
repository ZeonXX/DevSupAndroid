package com.sup.dev.android.views.popup

import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.views.widgets.Widget
import com.sup.dev.android.views.widgets.WidgetViewWrapper

class PopupWidget(private val widget: Widget) : Popup(widget.view), WidgetViewWrapper<PopupWidget> {

    init {
        setPopupCancelable<Popup>(widget.isCancelable)
        setPopupEnabled<Popup>(widget.isEnabled)
        val divider: View? = widget.view.findViewById(R.id.vDivider)
        if(divider != null) divider.visibility = View.GONE
    }

    override fun onShow() {
        super.onShow()
        widget.onShow()
    }

    override fun onHide() {
        super.onHide()
        widget.onHide()
    }

    @Suppress("UNCHECKED_CAST")
    override fun <K : Popup> hidePopup(): K {
        dismiss()
        return this as K
    }

    //
    //  Setters
    //

    @Suppress("UNCHECKED_CAST")
    override fun <K : Popup> setPopupCancelable(cancelable: Boolean): K {
        super.setPopupCancelable<Popup>(cancelable)
        return this as K
    }

    @Suppress("UNCHECKED_CAST")
    override fun <K : Popup> setPopupEnabled(enabled: Boolean): K {
        super.setPopupEnabled<Popup>(enabled)
        return this as K
    }

    @Suppress("UNCHECKED_CAST")
    override fun hide(): PopupWidget {
        return hidePopup()
    }

    @Suppress("UNCHECKED_CAST")
    override fun  setWidgetCancelable(cancelable: Boolean): PopupWidget {
        return setPopupCancelable(cancelable)
    }

    @Suppress("UNCHECKED_CAST")
    override fun setWidgetEnabled(enabled: Boolean): PopupWidget {
        return setPopupEnabled(enabled)
    }


}
