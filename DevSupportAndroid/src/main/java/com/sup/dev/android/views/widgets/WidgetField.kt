package com.sup.dev.android.views.widgets

import android.support.annotation.StringRes
import android.support.design.widget.TextInputLayout
import android.view.Gravity
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.ViewEditTextMedia
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.android.views.support.watchers.TextWatcherChanged
import com.sup.dev.android.views.support.watchers.TextWatcherRemoveHTML
import com.sup.dev.java.classes.items.Item2
import com.sup.dev.java.tools.ToolsThreads
import java.util.ArrayList


open class WidgetField : Widget(R.layout.widget_field) {

    private val vCopy: ViewIcon = view.findViewById(R.id.vCopy)
    private val vField: ViewEditTextMedia = view.findViewById(R.id.vField)
    private val vFieldLayout: TextInputLayout = view.findViewById(R.id.vFieldLayout)
    private val vCancel: Button = view.findViewById(R.id.vCancel)
    private val vEnter: Button = view.findViewById(R.id.vEnter)

    private val checkers = ArrayList<Item2<String, (String) -> Boolean>>()
    private var max: Int = 0
    private var min: Int = 0
    private var autoHideOnEnter = true
    private var autoHideOnCancel = true
    private var fastCopy: Boolean = false

    val text: String
        get() = vField.text.toString()

    init {

        vEnter.visibility = View.GONE
        vCancel.visibility = View.GONE
        vCopy.visibility = View.GONE

        vField.addTextChangedListener(TextWatcherRemoveHTML(vField))
        vField.addTextChangedListener(TextWatcherChanged { text -> check() })

        vField.setCallback { vField.setText(it) }

        vCopy.setOnClickListener { v ->
            setText(ToolsAndroid.getFromClipboard())
            if (fastCopy) vEnter.performClick()
        }
    }

    private fun check() {

        val text = text
        var error: String? = null

        for (pair in checkers)
            if (!((pair.a2!!.invoke(text)))) {
                error = pair.a1
                break
            }

        if (error != null) {
            vFieldLayout.error = if (error.isEmpty()) null else error
            vEnter.isEnabled = false
        } else {
            vFieldLayout.error = null
            vEnter.isEnabled = text.length >= min && (max == 0 || text.length <= max)
        }
    }

    override fun onShow() {
        super.onShow()
        ToolsView.showKeyboard(view.findViewById(R.id.vField))
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
        vCopy.visibility = View.VISIBLE
        return this
    }

    fun enableFastCopy(): WidgetField {
        vCopy.visibility = View.VISIBLE
        fastCopy = true
        return this
    }

    fun setMediaCallback(callback: (WidgetField, String) -> Unit): WidgetField {
        vField.setCallback { s -> callback.invoke(this, s) }
        return this
    }

    fun setMax(max: Int): WidgetField {
        this.max = max
        vFieldLayout.counterMaxLength = max
        return this
    }

    fun setMin(min: Int): WidgetField {
        this.min = min
        return this
    }

    fun setLinesCount(linesCount: Int): WidgetField {
        if (linesCount == 1) {
            vField.setSingleLine(true)
            vField.gravity = Gravity.CENTER or Gravity.LEFT
            vField.setLines(linesCount)
        } else {
            setMultiLine()
            vField.setLines(linesCount)
        }
        return this
    }

    fun setMultiLine(): WidgetField {
        vField.setSingleLine(false)
        vField.imeOptions = EditorInfo.IME_FLAG_NO_ENTER_ACTION
        vField.gravity = Gravity.TOP
        return this
    }

    fun addChecker(@StringRes errorText: Int, checker: (String) -> Boolean): WidgetField {
        return addChecker(ToolsResources.getString(errorText), checker)
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
        return setHint(ToolsResources.getString(hint))
    }

    fun setHint(hint: String?): WidgetField {
        vField.hint = hint
        return this
    }

    fun setInputType(type: Int): WidgetField {
        vField.inputType = type
        return this
    }

    fun setText(text: String?): WidgetField {
        vField.setText(text)
        vField.setSelection(vField.text!!.length)
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
        return setOnCancel(ToolsResources.getString(s))
    }

    fun setOnCancel(@StringRes s: Int, onCancel: (WidgetField) -> Unit): WidgetField {
        return setOnCancel(ToolsResources.getString(s), onCancel)
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
        return setOnEnter(ToolsResources.getString(s), onEnter)
    }

    @JvmOverloads
    fun setOnEnter(s: String?, onEnter: (WidgetField, String) -> Unit = { w, s -> }): WidgetField {
        ToolsView.setTextOrGone(vEnter, s)
        vEnter.setOnClickListener { v ->
            if (autoHideOnEnter)
                hide()
            else
                setEnabled(false)
            onEnter.invoke(this, vField.text.toString())
        }

        return this
    }

    override fun setEnabled(enabled: Boolean): WidgetField {
        super.setEnabled(enabled)
        vCancel.isEnabled = enabled
        vFieldLayout.isEnabled = enabled
        vEnter.isEnabled = enabled
        return this
    }


}
