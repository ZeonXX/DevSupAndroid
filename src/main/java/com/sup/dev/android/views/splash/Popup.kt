package com.sup.dev.android.views.splash

import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutCorned
import com.sup.dev.android.views.widgets.Widget
import com.sup.dev.java.classes.geometry.Dimensions
import com.sup.dev.java.tools.ToolsMath

class Popup(
        widget: Widget
) : SplashView<Popup>(widget, R.layout.splash_popup) {

    private val constMaxH = ToolsView.dpToPx(600)
    private val constReserveH = ToolsView.dpToPx(40)
    private val constOffset = ToolsView.dpToPx(16)
    private var anchor: View? = null
    private var targetX = 0f
    private var targetY = 0f

    override fun isDestroyScreenAnimation() = false

    init {
        (vSplashViewContainer as LayoutCorned).onMeasure = { w, h, maxW, maxH -> updateWidow(w, h, maxW, maxH) }
    }

    fun setAnchor(anchor: View) = setAnchor(anchor, 0, 0)

    fun setAnchor(anchor: View, x: Int, y: Int) = setAnchor(anchor, x.toFloat(), y.toFloat())

    fun setAnchor(anchor: View, x: Float, y: Float): Popup {
        this.targetX = x
        this.targetY = y
        this.anchor = anchor
        return this
    }

    private fun updateWidow(mW: Int, mH: Int, maxWBase: Int, maxHBase: Int): Dimensions {
        val dimensions = Dimensions(mW, mH)
        val anchor = this.anchor ?: return dimensions

        val maxH = ToolsMath.min(constMaxH, maxHBase - (constOffset * 2)).toInt()
        val width = ToolsMath.min(mW, maxWBase - (constOffset * 2).toInt())
        var height = mH
        if (height > maxH + constReserveH) height = maxH

        val location = IntArray(2)
        anchor.getLocationOnScreen(location)

        var xV = ToolsMath.max(constOffset, location[0] + targetX)
        var yV = ToolsMath.max(constOffset, location[1] + targetY)
        if (width + xV > maxWBase) xV -= (width + xV) - maxWBase + constOffset
        if (height + yV > maxHBase - constOffset) yV -= (height + yV) - maxHBase + constOffset



        vSplashViewContainer.x = xV
        vSplashViewContainer.y = yV

        dimensions.set(width, height)

        return dimensions
    }


}
