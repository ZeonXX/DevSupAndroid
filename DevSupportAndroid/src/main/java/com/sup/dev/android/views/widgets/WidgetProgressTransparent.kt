package com.sup.dev.android.views.widgets

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.dialogs.DialogWidget


class WidgetProgressTransparent : Widget(0) {

    private var frameLayout: FrameLayout? = null
    private var progressBar: ProgressBar? = null

    public override fun instanceView(): View? {
        frameLayout = FrameLayout(SupAndroid.activity!!)
        progressBar = ProgressBar(SupAndroid.activity!!)

        frameLayout!!.addView(progressBar)

        (progressBar!!.layoutParams as ViewGroup.MarginLayoutParams).setMargins(ToolsView.dpToPx(24), ToolsView.dpToPx(24), ToolsView.dpToPx(24), ToolsView.dpToPx(23))

        return frameLayout
    }

    override fun onShow() {
        super.onShow()

        val dialog = viewWrapper as DialogWidget?
        dialog!!.window.setBackgroundDrawable(ColorDrawable(0x00000000))

    }

    //
    //  Setters
    //

    override fun setCancelable(cancelable: Boolean): WidgetProgressTransparent {
        super.setCancelable(cancelable)
        return this
    }


}
