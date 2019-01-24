package com.sup.dev.android.views.settings

import android.content.Context
import android.util.AttributeSet
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.widgets.WidgetMenu

class SettingsSelection  constructor(context: Context, attrs: AttributeSet? = null) : Settings(context, attrs) {

    val menu: WidgetMenu = WidgetMenu()

    init {

        menu.showPopupWhenClick(view)
        menu.setOnGlobalSelected { w, t -> setSubtitle(t) }

    }

}
