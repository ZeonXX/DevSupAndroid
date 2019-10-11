package com.sup.dev.android.views.sheets

import android.widget.FrameLayout
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutCorned
import com.sup.dev.android.views.widgets.Widget
import com.sup.dev.android.views.widgets.WidgetViewWrapper

class Sheet(private val widget: Widget) : WidgetViewWrapper {

    private val vSheetRoot: FrameLayout = ToolsView.inflate(R.layout.sheet)
    private val vSheetViewContainer: LayoutCorned = vSheetRoot.findViewById(R.id.vSheetViewContainer)
    private var cancelable = true
    private var showed = true

    init {
        vSheetViewContainer.addView(widget.view)

        vSheetRoot.setOnClickListener {
            if(cancelable && widget.isEnabled && widget.onTryCancelOnTouchOutside()) hideWidget<Sheet>()
        }

        vSheetViewContainer.isEnabled = widget.isEnabled
        cancelable = widget.isCancelable

        setOnBack()
    }

    private fun setOnBack(){
        Navigator.addOnBack {
            if(showed){
                if(cancelable && widget.isEnabled){
                    hideWidget<Sheet>()
                }else{
                    setOnBack()
                }
                true
            }else{
                false
            }
        }
    }

    fun getView() = vSheetRoot

    fun show():Sheet{
        SupAndroid.activity?.addSheet(this)
        showed = true
        widget.onShow()
        return this
    }

    @Suppress("UNCHECKED_CAST")
    override fun <K : WidgetViewWrapper> hideWidget(): K {
        SupAndroid.activity?.removeSheet(this)
        showed = false
        widget.onHide()
        return this as K
    }

    @Suppress("UNCHECKED_CAST")
    override fun <K : WidgetViewWrapper> setWidgetCancelable(cancelable: Boolean): K {
        this.cancelable = cancelable
        return this as K
    }

    @Suppress("UNCHECKED_CAST")
    override fun <K : WidgetViewWrapper> setWidgetEnabled(enabled: Boolean): K {
        vSheetViewContainer.isEnabled = enabled
        return this as K
    }


}
