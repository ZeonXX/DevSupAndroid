package com.sup.dev.android.views.widgets

import androidx.annotation.StringRes
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.settings.SettingsField
import com.sup.dev.android.views.support.watchers.TextWatcherChanged
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.java.classes.items.Item2
import com.sup.dev.java.tools.ToolsThreads
import java.util.*

open class WidgetFieldTwo : Widget(R.layout.widget_field_two) {

    private val vIcon: ViewIcon = view.findViewById(R.id.vIcon)
    private val vFieldWidget_1: SettingsField = view.findViewById(R.id.vField_1)
    private val vFieldWidget_2: SettingsField = view.findViewById(R.id.vField_2)
    private val vCancel: Button = view.findViewById(R.id.vCancel)
    private val vEnter: Button = view.findViewById(R.id.vEnter)

    private val checkers_1 = ArrayList<Item2<String, (String) -> Boolean>>()
    private var max_1: Int = 0
    private var min_1: Int = 0

    private val checkers_2 = ArrayList<Item2<String, (String) -> Boolean>>()
    private var max_2: Int = 0
    private var min_2: Int = 0

    private var autoHideOnEnter = true
    private var autoHideOnCancel = true

    init {

        vEnter.visibility = View.GONE
        vCancel.visibility = View.GONE
        vIcon.visibility = View.GONE

        vFieldWidget_1.vField.addTextChangedListener(TextWatcherChanged { text -> check() })
        vFieldWidget_1.vField.setCallback { vFieldWidget_1.setText(it) }

        vFieldWidget_2.vField.addTextChangedListener(TextWatcherChanged { text -> check() })
        vFieldWidget_2.vField.setCallback { vFieldWidget_2.setText(it) }
    }

    private fun check(){
        vEnter.isEnabled = check_1() && check_2()
    }

    private fun check_1():Boolean {

        val text = getText_1()
        var error: String? = null

        for (pair in checkers_1)
            if (!((pair.a2.invoke(text)))) {
                error = pair.a1
                break
            }

        if (error != null) {
            vFieldWidget_1.vFieldLayout.error = if (error.isEmpty()) null else error
            return false
        } else {
            vFieldWidget_1.vFieldLayout.error = null
            return text.length >= min_1 && (max_1 == 0 || text.length <= max_1)
        }
    }

    private fun check_2():Boolean {

        val text = getText_2()
        var error: String? = null

        for (pair in checkers_2)
            if (!((pair.a2.invoke(text)))) {
                error = pair.a1
                break
            }

        if (error != null) {
            vFieldWidget_2.vFieldLayout.error = if (error.isEmpty()) null else error
            return false
        } else {
            vFieldWidget_2.vFieldLayout.error = null
            return text.length >= min_2 && (max_2 == 0 || text.length <= max_2)
        }
    }

    override fun onShow() {
        super.onShow()
        ToolsView.showKeyboard(vFieldWidget_1.vField)
        vFieldWidget_1.vField.setSelection(getText_1().length)
    }

    override fun onHide() {
        super.onHide()
        ToolsThreads.main(500) { ToolsView.hideKeyboard() } //  Без задержки будет скрываться под клавиатуру и оставаться посреди экрана
    }

    //
    //  Setters
    //

    override fun setTitle(title: String?): WidgetFieldTwo {
        super.setTitle(title)
        return this
    }

    override fun setTitle(title: Int): WidgetFieldTwo {
        super.setTitle(title)
        return this
    }

    fun setIcon(icon: Int, onClick: (WidgetFieldTwo) -> Unit) {
        vIcon.visibility = View.VISIBLE
        vIcon.setImageResource(icon)
        vIcon.setOnClickListener { v -> onClick.invoke(this) }
    }

    fun setMediaCallback_1(callback: (WidgetFieldTwo, String) -> Unit): WidgetFieldTwo {
        vFieldWidget_1.vField.setCallback { s -> callback.invoke(this, s) }
        return this
    }

    fun setMediaCallback_2(callback: (WidgetFieldTwo, String) -> Unit): WidgetFieldTwo {
        vFieldWidget_2.vField.setCallback { s -> callback.invoke(this, s) }
        return this
    }

    fun setMax_1(max: Int): WidgetFieldTwo {
        this.max_1 = max
        vFieldWidget_1.vFieldLayout.counterMaxLength = max
        return this
    }

    fun setMax_2(max: Int): WidgetFieldTwo {
        this.max_2 = max
        vFieldWidget_2.vFieldLayout.counterMaxLength = max
        return this
    }

    fun setMin_1(min: Int): WidgetFieldTwo {
        this.min_1 = min
        return this
    }

    fun setMin_2(min: Int): WidgetFieldTwo {
        this.min_2 = min
        return this
    }

    fun setLinesCount_1(linesCount: Int): WidgetFieldTwo {
        if (linesCount == 1) {
            vFieldWidget_1.vField.setSingleLine(true)
            vFieldWidget_1.vField.gravity = Gravity.CENTER or Gravity.LEFT
            vFieldWidget_1.vField.setLines(linesCount)
        } else {
            setMultiLine_1()
            vFieldWidget_1.vField.setLines(linesCount)
        }
        return this
    }

    fun setLinesCount_2(linesCount: Int): WidgetFieldTwo {
        if (linesCount == 1) {
            vFieldWidget_2.vField.setSingleLine(true)
            vFieldWidget_2.vField.gravity = Gravity.CENTER or Gravity.LEFT
            vFieldWidget_2.vField.setLines(linesCount)
        } else {
            setMultiLine_2()
            vFieldWidget_2.vField.setLines(linesCount)
        }
        return this
    }

    fun setMultiLine_1(): WidgetFieldTwo {
        vFieldWidget_1.vField.setSingleLine(false)
        vFieldWidget_1.vField.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        vFieldWidget_1.vField.gravity = Gravity.TOP
        return this
    }

    fun setMultiLine_2(): WidgetFieldTwo {
        vFieldWidget_2.vField.setSingleLine(false)
        vFieldWidget_2.vField.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        vFieldWidget_2.vField.gravity = Gravity.TOP
        return this
    }

    fun addChecker_1(@StringRes errorText: Int, checker: (String) -> Boolean): WidgetFieldTwo {
        return addChecker_1(ToolsResources.s(errorText), checker)
    }

    fun addChecker_1(checker: (String) -> Boolean): WidgetFieldTwo {
        return addChecker_1(null, checker)
    }

    fun addChecker_1(errorText: String?, checker: (String) -> Boolean): WidgetFieldTwo {
        checkers_1.add(Item2(errorText ?: "", checker))
        check_1()
        return this
    }

    fun addChecker_2(@StringRes errorText: Int, checker: (String) -> Boolean): WidgetFieldTwo {
        return addChecker_2(ToolsResources.s(errorText), checker)
    }

    fun addChecker_2(checker: (String) -> Boolean): WidgetFieldTwo {
        return addChecker_2(null, checker)
    }

    fun addChecker_2(errorText: String?, checker: (String) -> Boolean): WidgetFieldTwo {
        checkers_2.add(Item2(errorText ?: "", checker))
        check_2()
        return this
    }

    fun setHint_1(@StringRes hint: Int): WidgetFieldTwo {
        return setHint_1(ToolsResources.s(hint))
    }

    fun setHint_1(hint: String?): WidgetFieldTwo {
        vFieldWidget_1.vFieldLayout.hint = hint
        return this
    }

    fun setHint_2(@StringRes hint: Int): WidgetFieldTwo {
        return setHint_2(ToolsResources.s(hint))
    }

    fun setHint_2(hint: String?): WidgetFieldTwo {
        vFieldWidget_2.vFieldLayout.hint = hint
        return this
    }

    fun setInputType_1(type: Int): WidgetFieldTwo {
        vFieldWidget_1.vField.inputType = type
        return this
    }

    fun setInputType_2(type: Int): WidgetFieldTwo {
        vFieldWidget_2.vField.inputType = type
        return this
    }

    fun setText_1(text: String?): WidgetFieldTwo {
        vFieldWidget_1.setText(text)
        vFieldWidget_1.vField.setSelection(getText_1().length)
        return this
    }

    fun setText_2(text: String?): WidgetFieldTwo {
        vFieldWidget_2.setText(text)
        vFieldWidget_2.vField.setSelection(getText_2().length)
        return this
    }

    fun setAutoHideOnEnter(autoHideOnEnter: Boolean): WidgetFieldTwo {
        this.autoHideOnEnter = autoHideOnEnter
        return this
    }

    fun setAutoHideOnCancel(autoHideOnCancel: Boolean): WidgetFieldTwo {
        this.autoHideOnCancel = autoHideOnCancel
        return this
    }

    fun setOnCancel(@StringRes s: Int): WidgetFieldTwo {
        return setOnCancel(ToolsResources.s(s))
    }

    fun setOnCancel(@StringRes s: Int, onCancel: (WidgetFieldTwo) -> Unit): WidgetFieldTwo {
        return setOnCancel(ToolsResources.s(s), onCancel)
    }

    fun setOnCancel(s: String?, onCancel: (WidgetFieldTwo) -> Unit = {}): WidgetFieldTwo {
        ToolsView.setTextOrGone(vCancel, s)
        vCancel.visibility = View.VISIBLE
        vCancel.setOnClickListener { v ->
            if (autoHideOnCancel) hide()
            else setEnabled(false)
            onCancel.invoke(this)
        }
        return this
    }


    fun setOnEnter(@StringRes s: Int, onEnter: (WidgetFieldTwo, String, String) -> Unit = { w, s1, s2 -> }): WidgetFieldTwo {
        return setOnEnter(ToolsResources.s(s), onEnter)
    }

    fun setOnEnter(s: String?, onEnter: (WidgetFieldTwo, String, String) -> Unit = { w, s1, s2 -> }): WidgetFieldTwo {
        ToolsView.setTextOrGone(vEnter, s)
        vEnter.setOnClickListener { v ->
            if (autoHideOnEnter)
                hide()
            else
                setEnabled(false)
            onEnter.invoke(this, getText_1(), getText_2())
        }

        return this
    }

    override fun setEnabled(enabled: Boolean): WidgetFieldTwo {
        super.setEnabled(enabled)
        vCancel.isEnabled = enabled
        vFieldWidget_1.vFieldLayout.isEnabled = enabled
        vFieldWidget_2.vFieldLayout.isEnabled = enabled
        vEnter.isEnabled = enabled
        return this
    }

    //
    //  Getters
    //

    fun getText_1() = vFieldWidget_1.getText()

    fun getText_2() = vFieldWidget_2.getText()


}
