package com.sup.dev.android.views.settings

import android.content.Context
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources


class SettingsTitle @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : Settings(context, attrs, R.layout.settings_title) {

    private val vTitle: TextView


    init {

        vTitle = findViewById(R.id.dev_sup_title)

        val a = context.obtainStyledAttributes(attrs, R.styleable.SettingsTitle, 0, 0)
        val lineVisible = a.getBoolean(R.styleable.SettingsTitle_SettingsTitle_lineVisible, true)
        val title = a.getString(R.styleable.SettingsTitle_SettingsTitle_title)
        a.recycle()

        setLineVisible(lineVisible)
        setTitle(title)
    }

    //
    //  Setters
    //

    fun setTitle(@StringRes title: Int) {
        setTitle(ToolsResources.getString(title))
    }

    fun setTitle(title: String?) {
        vTitle.text = title
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        vTitle.isEnabled = enabled
    }
}
