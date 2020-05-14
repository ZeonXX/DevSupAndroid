package com.sup.dev.android.views.splash

import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.widgets.Widget

class Splash(widget: Widget) : SplashView<Splash>(widget, R.layout.splash_splash) {

    override fun isDestroyScreenAnimation() = true

    override fun getNavigationBarColor() = ToolsResources.getColorAttr(R.attr.colorSurface)

}
