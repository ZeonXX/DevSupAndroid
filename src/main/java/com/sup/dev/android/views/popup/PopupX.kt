package com.sup.dev.android.views.popup


import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.splash.SplashView
import com.sup.dev.android.views.widgets.Widget
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.tools.ToolsMath
import com.sup.dev.java.tools.ToolsThreads

class PopupX(
        widget: Widget
) : SplashView<PopupX>(widget, R.layout.splash_popup) {

    private var anchor: View? = null
    private var targetX = 0f
    private var targetY = 0f
    private var isUpdating = false

    override fun isDestroyScreenAnimation() = false

    init {
        vSplashViewContainer.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (!isShowed()) return@addOnLayoutChangeListener
            updateWidow()
        }

    }

    fun setAnchor(anchor: View) = setAnchor(anchor, 0, 0)

    fun setAnchor(anchor: View, x: Int, y: Int) = setAnchor(anchor, x.toFloat(), y.toFloat())

    fun setAnchor(anchor: View, x: Float, y: Float): PopupX {
        this.targetX = x
        this.targetY = y
        this.anchor = anchor
        updateWidow()
        return this
    }

    private fun updateWidow() {
        val anchor = this.anchor ?: return
        if (isUpdating) return
        isUpdating = true

        log("----------------------------------------------")

        log("start targt[$targetX][$targetY]")

        val screenW = ToolsAndroid.getScreenW()
        val screenH = ToolsAndroid.getScreenH()
        log("screen wh[$screenW][$screenH]")

        widget.view.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        widget.view.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        vSplashViewContainer.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        vSplashViewContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        vSplashViewContainer.measure(View.MeasureSpec.makeMeasureSpec(screenW, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(screenH, View.MeasureSpec.AT_MOST))
        val mW = vSplashViewContainer.measuredWidth
        val mH = vSplashViewContainer.measuredHeight
        log("measure wh[$mW][$mH]")


        val maxH = ToolsMath.min(ToolsView.dpToPx(400), screenH - ToolsView.dpToPx(64))
        val reservH = ToolsView.dpToPx(40)
        var width = mW
        var height = mH
        if (height > maxH + reservH) height = ToolsView.dpToPx(maxH).toInt()
        log("sizes wh[$width][$height]")


        val location = IntArray(2)
        anchor.getLocationOnScreen(location)
        log("location xy[${location[0]}][${location[1]}]")


        var xV = location[0] + targetX
        var yV = location[1] + targetY
        if (width + xV > screenW) xV -= width - anchor.width
        if (height + yV > screenH) yV += height
        log("position xy[${xV}][${yV}]")

        if(vSplashViewContainer.x == xV
                && vSplashViewContainer.y == yV
                && vSplashViewContainer.layoutParams.width == width
                &&  vSplashViewContainer.layoutParams.height == height
        ) return

        vSplashViewContainer.x = xV
        vSplashViewContainer.y = yV
        vSplashViewContainer.layoutParams.width = width
        vSplashViewContainer.layoutParams.height = height
        ToolsThreads.main(true) { vSplashViewContainer.requestLayout() }
        log("result xy[${vSplashViewContainer.x}][${vSplashViewContainer.y}]  wh[${vSplashViewContainer.layoutParams.width}][${vSplashViewContainer.layoutParams.height}]")

        isUpdating = false
    }


}
