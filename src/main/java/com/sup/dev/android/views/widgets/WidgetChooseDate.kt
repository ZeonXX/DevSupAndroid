package com.sup.dev.android.views.widgets

import androidx.annotation.StringRes
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import java.util.*

class WidgetChooseDate : Widget(R.layout.widget_choose_date) {

    private val vDatePicker: DatePicker = view.findViewById(R.id.vDatePicker)
    private val vCancel: Button = view.findViewById(R.id.vCancel)
    private val vEnter: Button = view.findViewById(R.id.vEnter)

    private var autoHideOnEnter = true

    init {

        isUseMoreScreenSpace = true
        vCancel.visibility = View.GONE
        vEnter.visibility = View.GONE
    }

    fun setOnEnter(@StringRes s: Int): WidgetChooseDate {
        return setOnEnter(ToolsResources.s(s))
    }

    fun setOnEnter(@StringRes s: Int, onEnter: (WidgetChooseDate, Long) -> Unit): WidgetChooseDate {
        return setOnEnter(ToolsResources.s(s), onEnter)
    }

    @JvmOverloads
    fun setOnEnter(s: String?, onEnter: (WidgetChooseDate, Long) -> Unit = { _, _ -> }): WidgetChooseDate {
        ToolsView.setTextOrGone(vEnter, s)
        vEnter.setOnClickListener {
            if (autoHideOnEnter)
                hide()
            else
                setEnabled(false)

            val calendar = GregorianCalendar.getInstance()
            calendar.set(Calendar.YEAR, vDatePicker.year)
            calendar.set(Calendar.MONTH, vDatePicker.month)
            calendar.set(Calendar.DAY_OF_MONTH, vDatePicker.dayOfMonth)

            onEnter.invoke(this, calendar.timeInMillis)
        }

        return this
    }

    fun setAutoHideOnEnter(autoHideOnEnter: Boolean): WidgetChooseDate {
        this.autoHideOnEnter = autoHideOnEnter
        return this
    }

    fun setOnCancel(onCancel: (WidgetChooseDate) -> Unit = {}): WidgetChooseDate {
        return setOnCancel(null, onCancel)
    }

    fun setOnCancel(@StringRes s: Int, onCancel: (WidgetChooseDate) -> Unit): WidgetChooseDate {
        return setOnCancel(ToolsResources.s(s), onCancel)
    }

    fun setOnCancel(s: String?, onCancel: (WidgetChooseDate) -> Unit = {}): WidgetChooseDate {
        super.setOnHide { onCancel.invoke(this) }
        ToolsView.setTextOrGone(vCancel, s)
        vCancel.setOnClickListener {
            hide()
            onCancel.invoke(this)
        }
        return this
    }


}