package com.sup.dev.android.views.settings

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.util.AttributeSet
import android.view.View
import android.widget.Switch
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsView


class SettingsSwitcher @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : Settings(context, attrs) {

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

        val a = context.obtainStyledAttributes(attrs, R.styleable.Settings, 0, 0)
        val checked = a.getBoolean(R.styleable.Settings_Settings_checked, false)
        a.recycle()

        setChecked(checked)
        setSubView(vSwitcher)

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
