package com.sup.dev.android.views.settings

import android.content.Context
import android.os.Bundle
import android.os.Parcelable
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.support.design.widget.TextInputLayout
import android.util.AttributeSet
import android.view.View
import android.widget.EditText
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.android.views.support.watchers.TextWatcherChanged
import com.sup.dev.android.views.views.ViewErrorEditText


class SettingsField @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : Settings(context, attrs, R.layout.settings_field) {

    private val vIcon: ViewIcon = findViewById(R.id.vDevSupIcon)
    private val vField: ViewErrorEditText = findViewById(R.id.vDevSupField)
    private val vInputLayout: TextInputLayout = findViewById(R.id.vDevSupInputLayout)

    private var isError: Boolean = false
    private var checker: ((String) -> Boolean)? = null

    init {

        vField.id = View.NO_ID //   Чтоб система не востонавливала состояние

        val a = context.obtainStyledAttributes(attrs, R.styleable.SettingsField, 0, 0)
        val hint = a.getString(R.styleable.SettingsField_SettingsField_hint)
        var text = a.getString(R.styleable.SettingsField_SettingsField_text)
        val inputType = a.getInteger(R.styleable.SettingsField_android_inputType, vField.inputType)
        val icon = a.getResourceId(R.styleable.SettingsField_SettingsField_icon, 0)
        val maxLength = a.getInteger(R.styleable.SettingsField_SettingsField_maxLength, 0)
        val iconBackground = a.getResourceId(R.styleable.SettingsField_SettingsField_icon_background, 0)
        setIconBackground(iconBackground)
        a.recycle()

        vField.addTextChangedListener(TextWatcherChanged { s -> checkError() })

        setLineVisible(false)
        setIcon(icon)
        setText(text)
        setHint(hint)
        setInputType(inputType)
        setMaxLength(maxLength)
        vField.clearFocus()
    }

    //
    //  State
    //

    public override fun onSaveInstanceState(): Parcelable? {
        val bundle = Bundle()
        bundle.putParcelable("SUPER_STATE", super.onSaveInstanceState())
        bundle.putString("vText", vField.text.toString())
        return bundle
    }

    public override fun onRestoreInstanceState(state: Parcelable?) {
        var state = state
        if (state is Bundle) {
            val bundle = state as Bundle?
            setText(bundle!!.getString("vText"))
            state = bundle.getParcelable("SUPER_STATE")
        }
        super.onRestoreInstanceState(state)
    }

    private fun checkError() {
        if (checker == null) setError(false)
        if (checker != null) setError((!checker!!.invoke(getText())))
    }

    //
    //  Setters
    //

    fun setText(text: String?) {
        vField.setText(text)
        if (text != null) vField.setSelection(text.length)
    }

    @Suppress("UsePropertyAccessSyntax")
    fun setError(b: Boolean) {
        isError = b
        vField.setError(b)
    }

    fun setErrorChecker(checker: (String) -> Boolean) {
        this.checker = checker
        checkError()
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

    fun setHint(@StringRes hintRes: Int) {
        setHint(ToolsResources.getString(hintRes))
    }

    fun setHint(hint: String?) {
        vInputLayout.hint = hint
    }

    fun setInputType(inputType: Int) {
        vField.inputType = inputType
    }

    fun setMaxLength(max: Int) {
        vInputLayout.counterMaxLength = max
    }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        vField.isEnabled = enabled
        vInputLayout.isEnabled = enabled
    }

    //
    //  Getters
    //

    fun getText() = vField.text.toString()

    fun isError(): Boolean {
        checkError()
        return isError
    }

}