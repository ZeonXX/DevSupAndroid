package com.sup.dev.android.views.widgets

import android.support.annotation.StringRes
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.settings.SettingsField
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.android.views.support.watchers.TextWatcherChanged
import com.sup.dev.java.classes.items.Item2
import com.sup.dev.java.tools.ToolsThreads
import java.util.ArrayList

open class WidgetField : Widget(R.layout.widget_field) {

    private val vIcon: ViewIcon = view.findViewById(R.id.vIcon)
    private val vFieldWidget: SettingsField = view.findViewById(R.id.vField)
    private val vCancel: Button = view.findViewById(R.id.vCancel)
    private val vEnter: Button = view.findViewById(R.id.vEnter)

    private val checkers = ArrayList<Item2<String, (String) -> Boolean>>()
    private var max: Int = 0
    private var min: Int = 0
    private var autoHideOnEnter = true
    private var autoHideOnCancel = true

    init {

        vEnter.visibility = View.GONE
        vCancel.visibility = View.GONE
        vIcon.visibility = View.GONE

        vFieldWidget.vField.addTextChangedListener(TextWatcherChanged { text -> check() })

        vFieldWidget.vField.setCallback { vFieldWidget.setText(it) }
    }

    private fun check() {

        val text = getText()
        var error: String? = null

        for (pair in checkers)
            if (!((pair.a2.invoke(text)))) {
                error = pair.a1
                break
            }

        if (error != null) {
            vFieldWidget.vFieldLayout.error = if (error.isEmpty()) null else error
            vEnter.isEnabled = false
        } else {
            vFieldWidget.vFieldLayout.error = null
            vEnter.isEnabled = text.length >= min && (max == 0 || text.length <= max)
        }
    }

    override fun onShow() {
        super.onShow()
        ToolsView.showKeyboard(vFieldWidget.vField)
    }

    override fun onHide() {
        super.onHide()
        ToolsThreads.main(500) { ToolsView.hideKeyboard() } //  Без задержки будет скрываться под клавиатуру и оставаться посреди экрана
    }

    //
    //  Setters
    //

    override fun setTitle(title: String?): WidgetField {
        super.setTitle(title)
        return this
    }

    override fun setTitle(title: Int): WidgetField {
        super.setTitle(title)
        return this
    }

    fun enableCopy(): WidgetField {
        setIcon(R.drawable.ic_content_copy_white_24dp) { setText(ToolsAndroid.getFromClipboard()) }
        return this
    }

    fun enableFastCopy(): WidgetField {
        setIcon(R.drawable.ic_content_copy_white_24dp) {
            setText(ToolsAndroid.getFromClipboard())
            vEnter.performClick()
        }
        return this
    }

    fun setIcon(icon: Int, onClick: (WidgetField) -> Unit) {
        vIcon.visibility = View.VISIBLE
        vIcon.setImageResource(icon)
        vIcon.setOnClickListener { v -> onClick.invoke(this) }
    }

    fun setMediaCallback(callback: (WidgetField, String) -> Unit): WidgetField {
        vFieldWidget.vField.setCallback { s -> callback.invoke(this, s) }
        return this
    }

    fun setMax(max: Int): WidgetField {
        this.max = max
        vFieldWidget.vFieldLayout.counterMaxLength = max
        return this
    }

    fun setMin(min: Int): WidgetField {
        this.min = min
        return this
    }

    fun setLinesCount(linesCount: Int): WidgetField {
        if (linesCount == 1) {
            vFieldWidget.vField.setSingleLine(true)
            vFieldWidget.vField.gravity = Gravity.CENTER or Gravity.LEFT
            vFieldWidget.vField.setLines(linesCount)
        } else {
            setMultiLine()
            vFieldWidget.vField.setLines(linesCount)
        }
        return this
    }

    fun setMultiLine(): WidgetField {
        vFieldWidget.vField.setSingleLine(false)
        vFieldWidget.vField.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        vFieldWidget.vField.gravity = Gravity.TOP
        return this
    }

    fun addChecker(@StringRes errorText: Int, checker: (String) -> Boolean): WidgetField {
        return addChecker(ToolsResources.s(errorText), checker)
    }

    fun addChecker(checker: (String) -> Boolean): WidgetField {
        return addChecker(null, checker)
    }

    fun addChecker(errorText: String?, checker: (String) -> Boolean): WidgetField {
        checkers.add(Item2(errorText ?: "", checker))
        check()
        return this
    }

    fun setHint(@StringRes hint: Int): WidgetField {
        return setHint(ToolsResources.s(hint))
    }

    fun setHint(hint: String?): WidgetField {
        vFieldWidget.vFieldLayout.hint = hint
        return this
    }

    fun setInputType(type: Int): WidgetField {
        vFieldWidget.vField.inputType = type
        return this
    }

    fun setText(text: String?): WidgetField {
        vFieldWidget.setText(text)
        vFieldWidget.vField.setSelection(getText().length)
        return this
    }

    fun setAutoHideOnEnter(autoHideOnEnter: Boolean): WidgetField {
        this.autoHideOnEnter = autoHideOnEnter
        return this
    }

    fun setAutoHideOnCancel(autoHideOnCancel: Boolean): WidgetField {
        this.autoHideOnCancel = autoHideOnCancel
        return this
    }

    fun setOnCancel(@StringRes s: Int): WidgetField {
        return setOnCancel(ToolsResources.s(s))
    }

    fun setOnCancel(@StringRes s: Int, onCancel: (WidgetField) -> Unit): WidgetField {
        return setOnCancel(ToolsResources.s(s), onCancel)
    }

    @JvmOverloads
    fun setOnCancel(s: String?, onCancel: (WidgetField) -> Unit = {}): WidgetField {
        ToolsView.setTextOrGone(vCancel, s)
        vCancel.visibility = View.VISIBLE
        vCancel.setOnClickListener { v ->
            if (autoHideOnCancel) hide()
            else setEnabled(false)
            onCancel.invoke(this)
        }
        return this
    }


    fun setOnEnter(@StringRes s: Int, onEnter: (WidgetField, String) -> Unit = { w, s -> }): WidgetField {
        return setOnEnter(ToolsResources.s(s), onEnter)
    }

    @JvmOverloads
    fun setOnEnter(s: String?, onEnter: (WidgetField, String) -> Unit = { w, s -> }): WidgetField {
        ToolsView.setTextOrGone(vEnter, s)
        vEnter.setOnClickListener { v ->
            if (autoHideOnEnter)
                hide()
            else
                setEnabled(false)
            onEnter.invoke(this, getText())
        }

        return this
    }

    override fun setEnabled(enabled: Boolean): WidgetField {
        super.setEnabled(enabled)
        vCancel.isEnabled = enabled
        vFieldWidget.vFieldLayout.isEnabled = enabled
        vEnter.isEnabled = enabled
        return this
    }

    //
    //  Getters
    //

    fun getText() = vFieldWidget.getText()


}
