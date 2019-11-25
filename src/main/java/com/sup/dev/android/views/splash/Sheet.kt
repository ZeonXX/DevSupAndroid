package com.sup.dev.android.views.splash

import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.widgets.Widget

class Sheet(widget: Widget) : SplashView<Sheet>(widget, R.layout.splash_sheet) {

    override fun isDestroyScreenAnimation() = false

    override fun getNavigationBarColor() = ToolsResources.getColorAttr(R.attr.widget_background)

}
