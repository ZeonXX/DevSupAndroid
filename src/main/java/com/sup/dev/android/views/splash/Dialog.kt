package com.sup.dev.android.views.splash

import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.views.layouts.LayoutMaxSizes
import com.sup.dev.android.views.widgets.Widget

class Dialog(widget: Widget) : SplashView<Dialog>(widget, R.layout.splash_dialog) {

    val vLayoutMaxSizes:LayoutMaxSizes = getView().findViewById(R.id.vSplashMaxSizesLayout)

    override fun isDestroyScreenAnimation() = false

    override fun getNavigationBarColor() = ToolsResources.getColorAttr(R.attr.widget_background)

    init {
        vLayoutMaxSizes.onmeasureCall = {
            vLayoutMaxSizes.setMaxWidthParentPercent((if (ToolsAndroid.isScreenPortrait()) 90 else 70).toFloat())
        }
        vLayoutMaxSizes.setMaxWidth(600)
        vLayoutMaxSizes.setUseScreenWidthAsParent(true)
        vLayoutMaxSizes.setAlwaysMaxW(true)
        vLayoutMaxSizes.setChildAlwaysMaxW(true)
    }

}
