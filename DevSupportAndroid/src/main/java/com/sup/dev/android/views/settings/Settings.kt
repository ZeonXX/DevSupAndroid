package com.sup.dev.android.views.settings

import android.content.Context
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import java.util.ArrayList


abstract class Settings(context: Context, attrs: AttributeSet?, @LayoutRes layoutRes: Int) : FrameLayout(context, attrs) {

    private val line: View
    val view: View

    private var subSettings: ArrayList<Settings>? = null
    //
    //  Getters
    //

    var isSubSettingsEnabled = true
        private set


    init {

        SupAndroid.initEditMode(this)

        val a = context.obtainStyledAttributes(attrs, R.styleable.Settings, 0, 0)
        isFocusable = a.getBoolean(R.styleable.Settings_android_focusable, true)
        a.recycle()

        view = ToolsView.inflate(this, layoutRes)
        addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        line = View(context)
        addView(line, ViewGroup.LayoutParams.MATCH_PARENT, 1)
        line.setBackgroundColor(ToolsResources.getColor(R.color.grey_600))
        (line.layoutParams as ViewGroup.MarginLayoutParams).setMargins(ToolsView.dpToPx(8f), 0, ToolsView.dpToPx(8f), 0)
        (line.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.BOTTOM
    }

    //
    //  Setters
    //

    fun addSubSettings(settings: Settings) {
        if (subSettings == null) subSettings = ArrayList()
        subSettings!!.add(settings)
        settings.isEnabled = isSubSettingsEnabled && isEnabled
    }

    fun setEnabledSubSettings(enabled: Boolean) {
        isSubSettingsEnabled = enabled
        if (subSettings != null)
            for (settings in subSettings!!)
                settings.isEnabled = isSubSettingsEnabled && isEnabled
    }

    fun setLineVisible(b: Boolean) {
        line.visibility = if (b) View.VISIBLE else View.GONE
    }

    @CallSuper
    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        view.isEnabled = enabled
        setEnabledSubSettings(enabled)
        if (subSettings != null)
            for (settings in subSettings!!)
                settings.isEnabled = isSubSettingsEnabled && isEnabled
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        view.setOnClickListener(l)
        view.isFocusable = l != null
    }
}
