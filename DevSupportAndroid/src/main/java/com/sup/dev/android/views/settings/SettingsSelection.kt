package com.sup.dev.android.views.settings

import android.content.Context
import android.util.AttributeSet
import com.sup.dev.android.views.widgets.WidgetMenu

class SettingsSelection @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : SettingsAction(context, attrs) {

    val menu: WidgetMenu

    init {

        menu = WidgetMenu()
        menu.showPopupWhenClick(view)
        menu.setOnGlobalSelected { w, t -> setSubtitle(t) }

        val a = context.obtainStyledAttributes(attrs, R.styleable.SettingsSelection, 0, 0)
        val lineVisible = a.getBoolean(R.styleable.SettingsSelection_SettingsSelection_lineVisible, true)
        val title = a.getString(R.styleable.SettingsSelection_SettingsSelection_title)
        val subtitle = a.getString(R.styleable.SettingsSelection_SettingsSelection_subtitle)
        val icon = a.getResourceId(R.styleable.SettingsSelection_SettingsSelection_icon, 0)
        val iconBackground = a.getColor(R.styleable.SettingsSelection_SettingsSelection_icon_background, 0)
        setIconBackground(iconBackground)
        a.recycle()

        setLineVisible(lineVisible)
        setTitle(title)
        setSubtitle(subtitle ?: "-")
        setIcon(icon)
    }

}
