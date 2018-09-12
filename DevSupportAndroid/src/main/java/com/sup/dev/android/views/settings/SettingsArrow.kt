package com.sup.dev.android.views.settings

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources


class SettingsArrow @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : SettingsAction(context, attrs) {

    private val vArrow: ImageView

    init {

        vArrow = ImageView(context)
        vArrow.setImageDrawable(ToolsResources.getDrawableFromAttr(R.attr.ic_keyboard_arrow_right))


        val a = context.obtainStyledAttributes(attrs, R.styleable.SettingsArrow, 0, 0)
        val lineVisible = a.getBoolean(R.styleable.SettingsArrow_SettingsArrow_lineVisible, true)
        val title = a.getString(R.styleable.SettingsArrow_SettingsArrow_title)
        val subtitle = a.getString(R.styleable.SettingsArrow_SettingsArrow_subtitle)
        val icon = a.getResourceId(R.styleable.SettingsArrow_SettingsArrow_icon, 0)
        val iconBackground = a.getColor(R.styleable.SettingsArrow_SettingsArrow_icon_background, 0)
        setIconBackground(iconBackground)
        a.recycle()

        setLineVisible(lineVisible)
        setTitle(title)
        setSubtitle(subtitle)
        setIcon(icon)

        setSubView(vArrow)
    }

    //
    //  Setters
    //

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
    }

}