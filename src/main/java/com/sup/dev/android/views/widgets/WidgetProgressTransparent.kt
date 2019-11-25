package com.sup.dev.android.views.widgets

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.splash.Dialog

class WidgetProgressTransparent : Widget(0) {

    private var frameLayout: FrameLayout? = null
    private var progressBar: ProgressBar? = null

    public override fun instanceView(): View? {
        frameLayout = FrameLayout(SupAndroid.activity!!)
        progressBar = ProgressBar(SupAndroid.activity!!)

        frameLayout!!.addView(progressBar)

        (progressBar!!.layoutParams as ViewGroup.MarginLayoutParams).setMargins(ToolsView.dpToPx(24).toInt(), ToolsView.dpToPx(24).toInt(), ToolsView.dpToPx(24).toInt(), ToolsView.dpToPx(23).toInt())

        return frameLayout
    }

    override fun onShow() {
        super.onShow()

        if (viewWrapper is Dialog) {
            val dialog = viewWrapper as Dialog
            dialog.vSplashViewContainer.setBackgroundColor(0x00000000)
            dialog.vSplashViewContainer.invalidate()
        }

    }

    //
    //  Setters
    //

    override fun setCancelable(cancelable: Boolean): WidgetProgressTransparent {
        super.setCancelable(cancelable)
        return this
    }


}
