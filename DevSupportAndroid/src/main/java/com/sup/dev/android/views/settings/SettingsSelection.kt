package com.sup.dev.android.views.settings

import android.content.Context
import android.util.AttributeSet
import com.sup.dev.android.views.widgets.WidgetMenu

class SettingsSelection  constructor(context: Context, attrs: AttributeSet? = null) : Settings(context, attrs) {

    val menu: WidgetMenu = WidgetMenu()

    init {

        view.setOnClickListener{ menu.asSheetShow()}
        menu.setOnGlobalSelected { w, t -> setSubtitle(t) }

    }

}
