package com.sup.dev.android.views.splash

import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.models.EventConfigurationChanged
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutCorned
import com.sup.dev.android.views.widgets.Widget
import com.sup.dev.java.classes.geometry.Dimensions
import com.sup.dev.java.libs.eventBus.EventBus
import com.sup.dev.java.tools.ToolsMath

class Popup(
        widget: Widget
) : SplashView<Popup>(widget, R.layout.splash_popup) {

    private val eveentBus = EventBus.subscribe(EventConfigurationChanged::class){vSplashViewContainer.requestLayout()}
    private val constMaxH = ToolsView.dpToPx(600)
    private val constReserveH = ToolsView.dpToPx(40)
    private val constOffset = ToolsView.dpToPx(24)
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

    private fun updateWidow(needWBase: Int, needHBase: Int, maxWBase: Int, maxHBase: Int): Dimensions {

        var needWMapped = if (widget.getMinW() == null) needWBase else ToolsMath.max(needWBase, widget.getMinW()!!)
        var needHMapped = if (widget.getMinH() == null) needHBase else ToolsMath.max(needHBase, widget.getMinH()!!)
        if (widget.getMaxW() != null && needWMapped > widget.getMaxW()!!) needWMapped = widget.getMaxW()!!
        if (widget.getMaxH() != null && needHMapped > widget.getMaxH()!!) needHMapped = widget.getMaxH()!!

        val dimensions = Dimensions(needWMapped, needHMapped)
        val anchor = this.anchor ?: return dimensions

        val maxH = ToolsMath.min(constMaxH, maxHBase - (constOffset * 2)).toInt()
        val width = ToolsMath.min(needWMapped, maxWBase - (constOffset * 2).toInt())
        var height = needHMapped
        if (height > maxH + constReserveH) height = maxH

        val location = IntArray(2)
        anchor.getLocationOnScreen(location)

        var xV = ToolsMath.max(constOffset, location[0] + targetX) - width/2
        var yV = ToolsMath.max(constOffset, location[1] + targetY)
        if (xV + width > maxWBase - constOffset) xV = xV - ((xV + width) - maxWBase) - constOffset
        if (xV < constOffset) xV = constOffset
        if (yV + height > maxHBase - constOffset){
            if(widget.allowPopupMirrorHeight) yV = yV - height - widget.popupYMirrorOffset
            else yV = yV - ((height + yV) - maxHBase) - constOffset
        }
        if (yV < constOffset) yV = constOffset

        vSplashViewContainer.x = xV
        vSplashViewContainer.y = yV

        dimensions.set(width, height)

        return dimensions
    }


}
