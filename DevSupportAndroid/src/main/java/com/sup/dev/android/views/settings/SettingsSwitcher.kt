package com.sup.dev.android.views.settings

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.Switch
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsView


class SettingsSwitcher @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : SettingsAction(context, attrs) {

    private val vSwitcher: Switch = Switch(context)

    private var onClickListener: View.OnClickListener? = null
    private var salient: Boolean = false

    //
    //  Getters
    //

    init {

        vSwitcher.isFocusable = false
        vSwitcher.setOnCheckedChangeListener { v, b ->
            setEnabledSubSettings(b)
            if (!salient) onClick()
        }

        val a = context.obtainStyledAttributes(attrs, R.styleable.SettingsSwitcher, 0, 0)
        val lineVisible = a.getBoolean(R.styleable.SettingsSwitcher_SettingsSwitcher_lineVisible, true)
        val title = a.getString(R.styleable.SettingsSwitcher_SettingsSwitcher_title)
        val subtitle = a.getString(R.styleable.SettingsSwitcher_SettingsSwitcher_subtitle)
        val icon = a.getResourceId(R.styleable.SettingsSwitcher_SettingsSwitcher_icon, 0)
        val checked = a.getBoolean(R.styleable.SettingsSwitcher_SettingsSwitcher_checked, false)
        val iconBackground = a.getColor(R.styleable.SettingsSwitcher_SettingsSwitcher_icon_background, 0)
        val iconPadding = a.getDimension(R.styleable.SettingsSwitcher_SettingsSwitcher_icon_padding, ToolsView.dpToPx(6).toFloat())
        a.recycle()

        setLineVisible(lineVisible)
        setTitle(title)
        setSubtitle(subtitle)
        setIcon(icon)
        setChecked(checked)
        setSubView(vSwitcher)
        setIconBackground(iconBackground)
        setIconPaddingPx(iconPadding)

        super.setOnClickListener { v ->
            setChecked(!vSwitcher.isChecked)
            onClick()
        }
    }

    private fun onClick() {
        if (onClickListener != null) onClickListener!!.onClick(this)
    }

    //
    //  State
    //

    public fun setChecked(checked:Boolean){
        salient = true
        vSwitcher.isChecked = checked
        salient = false
        setEnabledSubSettings(checked)
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("SUPER_STATE", super.onSaveInstanceState())
        bundle.putBoolean("checked", isChecked())
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable?) {
        var state = state
        if (state is Bundle) {
            val bundle = state as Bundle?
            salient = true
            setChecked(bundle!!.getBoolean("checked"))
            salient = false
            state = bundle.getParcelable("SUPER_STATE")
        }
        super.onRestoreInstanceState(state)
    }

    //
    //  Setters
    //

    override fun setOnClickListener(onClickListener: View.OnClickListener?) {
        this.onClickListener = onClickListener
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        vSwitcher.isEnabled = enabled
    }

    //
    //  Getters
    //

    fun isChecked() = vSwitcher.isChecked


}
