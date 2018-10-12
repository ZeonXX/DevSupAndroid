package com.sup.dev.android.views.widgets

import android.graphics.drawable.ColorDrawable
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.ProgressBar
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.dialogs.DialogWidget
import com.sup.dev.java.tools.ToolsThreads


class WidgetProgressTransparent : Widget(0) {

    private var frameLayout: FrameLayout? = null
    private var progressBar: ProgressBar? = null
    private var invisibleTime = 1000L

    public override fun instanceView(): View? {
        frameLayout = FrameLayout(SupAndroid.activity!!)
        progressBar = ProgressBar(SupAndroid.activity!!)

        frameLayout!!.addView(progressBar)

        (progressBar!!.layoutParams as ViewGroup.MarginLayoutParams).setMargins(ToolsView.dpToPx(24), ToolsView.dpToPx(24), ToolsView.dpToPx(24), ToolsView.dpToPx(23))

        return frameLayout
    }

    override fun onShow() {
        super.onShow()

        frameLayout!!.visibility = View.INVISIBLE
        var invisibleTime = 0L

        if (viewWrapper is DialogWidget) {
            invisibleTime = this.invisibleTime
            val dialog = viewWrapper as DialogWidget?
            if (invisibleTime > 0) {
                dialog!!.window.setBackgroundDrawable(ColorDrawable(0x00000000))
                dialog.window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
        }

        if (invisibleTime > 0)
            ToolsThreads.main(invisibleTime) {
                ToolsView.fromAlpha(frameLayout as View)
                if (viewWrapper is DialogWidget) (viewWrapper as DialogWidget).window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
            }
    }

    //
    //  Setters
    //

    override fun setCancelable(cancelable: Boolean): WidgetProgressTransparent {
        super.setCancelable(cancelable)
        return this
    }

    fun setInvisibleTime(invisibleTime: Long): WidgetProgressTransparent {
        this.invisibleTime = invisibleTime
        return this
    }


}
