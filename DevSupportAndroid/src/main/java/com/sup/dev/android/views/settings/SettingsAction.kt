package com.sup.dev.android.views.settings

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.java.libs.debug.Debug


open class SettingsAction @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : Settings(context, attrs, R.layout.settings_action) {

    private val vIcon: ViewIcon = findViewById(R.id.vDevSupIcon)
    private val vTitle: TextView = findViewById(R.id.vDevSupTitle)
    private val vSubtitle: TextView = findViewById(R.id.vDevSupSubtitle)
    private val vSubViewContainer: ViewGroup = findViewById(R.id.vDevSupContainer)

    init {

        val a = context.obtainStyledAttributes(attrs, R.styleable.SettingsAction, 0, 0)
        val lineVisible = a.getBoolean(R.styleable.SettingsAction_SettingsAction_lineVisible, true)
        val title = a.getString(R.styleable.SettingsAction_SettingsAction_title)
        val subtitle = a.getString(R.styleable.SettingsAction_SettingsAction_subtitle)
        val icon = a.getResourceId(R.styleable.SettingsAction_SettingsAction_icon, 0)
        val iconBackground = a.getColor(R.styleable.SettingsAction_SettingsAction_icon_background, 0)
        a.recycle()

        setLineVisible(lineVisible)
        setTitle(title)
        setSubtitle(subtitle)
        setIcon(icon)
        setIconBackground(iconBackground)
    }

    fun setSubView(view: View?) {
        vSubViewContainer.removeAllViews()
        if (view != null)
            vSubViewContainer.addView(view)
    }

    override fun setOnTouchListener(l: View.OnTouchListener) {
        super.setOnTouchListener(l)
    }

    //
    //  Setters
    //

    fun setTitle(@StringRes titleRes: Int) {
        setTitle(context.getString(titleRes))
    }

    fun setTitle(title: String?) {
        vTitle.text = title
        vTitle.visibility = if (title != null && !title.isEmpty()) View.VISIBLE else View.GONE
    }

    fun setSubtitle(@StringRes subtitleRes: Int) {
        setSubtitle(context.getString(subtitleRes))
    }

    fun setSubtitle(subtitle: String?) {
        vSubtitle.text = subtitle
        vSubtitle.visibility = if (subtitle != null && !subtitle.isEmpty()) View.VISIBLE else View.GONE
    }

    fun setIcon(@DrawableRes icon: Int) {
        if (icon == 0)
            vIcon.setImageBitmap(null)
        else
            vIcon.setImageResource(icon)
        vIcon.visibility = if (icon == 0) View.GONE else View.VISIBLE
    }

    fun setIconBackground(color: Int) {
        vIcon.setIconBackgroundColor(color)
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        vTitle.isEnabled = enabled
        vSubtitle.isEnabled = enabled
    }

}
