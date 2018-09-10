package com.sup.dev.android.views.widgets

import android.support.annotation.StringRes
import android.view.View
import android.widget.Button
import android.widget.TimePicker
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView


class WidgetChooseTime : Widget(R.layout.widget_choose_time) {

    private val vTimePicker: TimePicker
    private val vCancel: Button
    private val vEnter: Button

    private var autoHideOnEnter = true

    init {

        vTimePicker = view!!.findViewById(R.id.time_picker)
        vCancel = view.findViewById(R.id.cancel)
        vEnter = view.findViewById(R.id.enter)

        vCancel.visibility = View.GONE
        vEnter.visibility = View.GONE

        vTimePicker.setIs24HourView(true)
    }


    fun setTime(h: Int, m: Int): WidgetChooseTime {
        vTimePicker.currentHour = h
        vTimePicker.currentMinute = m
        return this
    }

    fun setOnEnter(@StringRes s: Int): WidgetChooseTime {
        return setOnEnter(ToolsResources.getString(s))
    }

    fun setOnEnter(@StringRes s: Int, onEnter: (WidgetChooseTime, Int, Int) -> Unit): WidgetChooseTime {
        return setOnEnter(ToolsResources.getString(s), onEnter)
    }

    @JvmOverloads
    fun setOnEnter(s: String?, onEnter: (WidgetChooseTime, Int, Int) -> Unit = { w, x, y -> }): WidgetChooseTime {
        ToolsView.setTextOrGone(vEnter, s!!)
        vEnter.setOnClickListener { v ->
            if (autoHideOnEnter)
                hide()
            else
                setEnabled<Widget>(false)
            onEnter.invoke(this, vTimePicker.currentHour, vTimePicker.currentMinute)
        }

        return this
    }

    fun setAutoHideOnEnter(autoHideOnEnter: Boolean): WidgetChooseTime {
        this.autoHideOnEnter = autoHideOnEnter
        return this
    }

    fun setOnCancel(onCancel: (WidgetChooseTime) -> Unit = {}): WidgetChooseTime {
        return setOnCancel(null, onCancel)
    }

    fun setOnCancel(@StringRes s: Int, onCancel: (WidgetChooseTime) -> Unit): WidgetChooseTime {
        return setOnCancel(ToolsResources.getString(s), onCancel)
    }

    @JvmOverloads
    fun setOnCancel(s: String?, onCancel: (WidgetChooseTime) -> Unit = {}): WidgetChooseTime {
        super.setOnHide <Widget>{ b -> onCancel.invoke(this) }
        ToolsView.setTextOrGone(vCancel, s!!)
        vCancel.setOnClickListener { v ->
            hide()
            onCancel.invoke(this)
        }
        return this
    }

}