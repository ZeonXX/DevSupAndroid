package com.sup.dev.android.views.splash

import android.view.ViewGroup
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.widgets.Widget
import com.sup.dev.android.views.widgets.WidgetViewWrapper
import com.sup.dev.java.tools.ToolsThreads

abstract class SplashView<K : Any>(
        val widget: Widget,
        var res:Int
) : WidgetViewWrapper<K> {

    val vSplashRoot: ViewGroup = ToolsView.inflate(res)
    val vSplashViewContainer: ViewGroup = vSplashRoot.findViewById(R.id.vSplashViewContainer)
    var cancelable = true

    abstract fun isDestroyScreenAnimation():Boolean

    abstract fun getNavigationBarColor():Int

    init {
        vSplashViewContainer.addView(ToolsView.removeFromParent(widget.view))

        vSplashRoot.setOnClickListener {
            if(cancelable && widget.isEnabled && widget.onTryCancelOnTouchOutside()) hide()
        }

        vSplashViewContainer.isEnabled = widget.isEnabled
        cancelable = widget.isCancelable

        setOnBack()
    }

    private fun setOnBack(){
        Navigator.addOnBack {
            if(SupAndroid.activity?.isSplashShowed(this) == true){
                if(cancelable && widget.isEnabled){
                    hide()
                }else{
                    setOnBack()
                }
                true
            }else{
                false
            }
        }
    }

    fun getView() = vSplashRoot

    @Suppress("UNCHECKED_CAST")
    fun show():K{
        SupAndroid.activity?.addSplash(this)
        ToolsThreads.main(true) { widget.onShow() } //  Чтоб все успело инициализиоваться
        return this as K
    }

    @Suppress("UNCHECKED_CAST")
    override fun hide(): K {
        SupAndroid.activity?.removeSplash(this)
        widget.onHide()
        return this as K
    }

    @Suppress("UNCHECKED_CAST")
    override fun setWidgetCancelable(cancelable: Boolean): K {
        this.cancelable = cancelable
        return this as K
    }

    @Suppress("UNCHECKED_CAST")
    override fun setWidgetEnabled(enabled: Boolean): K {
        vSplashViewContainer.isEnabled = enabled
        return this as K
    }


}