package com.sup.dev.android.views.widgets

import android.support.annotation.StringRes
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.java.tools.ToolsThreads
import java.util.ArrayList


class WidgetCheckBoxes : Widget(R.layout.widget_container) {

    private val items = ArrayList<Item>()
    private val vOptionsContainer: LinearLayout
    private val vCancel: Button
    private val vEnter: Button

    private var autoHideOnEnter = true

    //
    //  Item
    //

    private var buildItem: Item? = null
    private var skipThisItem = false
    private var skipGroup = false

    init {

        vOptionsContainer = view!!.findViewById(R.id.content_container)
        vCancel = view.findViewById(R.id.cancel)
        vEnter = view.findViewById(R.id.enter)

        vCancel.visibility = View.GONE
        vEnter.visibility = View.GONE
    }

    override fun onShow() {
        super.onShow()
        finishItemBuilding()
    }

    private fun add(item: Item) {
        items.add(item)
    }

    private fun finishItemBuilding() {
        if (buildItem != null) {
            val i = buildItem
            buildItem = null
            if (!skipThisItem && !skipGroup) add(i!!)
        }
    }

    fun add(@StringRes text: Int): WidgetCheckBoxes {
        return add(ToolsResources.getString(text))
    }

    fun add(text: String?): WidgetCheckBoxes {
        finishItemBuilding()
        buildItem = Item()
        buildItem!!.v.text = text
        return this
    }

    fun onChange(onChange: (WidgetCheckBoxes, Boolean) -> Unit): WidgetCheckBoxes {
        buildItem!!.onChange = onChange
        return this
    }

    fun onSelected(onSelected: (WidgetCheckBoxes) -> Unit): WidgetCheckBoxes {
        buildItem!!.onSelected = onSelected
        return this
    }

    fun onNotSelected(onNotSelected: (WidgetCheckBoxes) -> Unit): WidgetCheckBoxes {
        buildItem!!.onNotSelected = onNotSelected
        return this
    }

    fun text(@StringRes text: Int): WidgetCheckBoxes {
        return text(ToolsResources.getString(text))
    }

    fun text(text: String?): WidgetCheckBoxes {

        buildItem!!.v.text = text
        return this
    }

    fun checked(b: Boolean): WidgetCheckBoxes {
        buildItem!!.v.isChecked = b
        return this
    }

    fun condition(b: Boolean): WidgetCheckBoxes {
        skipThisItem = !b
        return this
    }

    fun groupCondition(b: Boolean): WidgetCheckBoxes {
        skipGroup = !b
        return this
    }

    fun reverseGroupCondition(): WidgetCheckBoxes {
        skipGroup = !skipGroup
        return this
    }

    fun clearGroupCondition(): WidgetCheckBoxes {
        skipGroup = false
        return this
    }

    //
    //  Setters
    //

    fun setOnEnter(@StringRes s: Int): WidgetCheckBoxes {
        return setOnEnter(ToolsResources.getString(s))
    }

    fun setOnEnter(s: String?): WidgetCheckBoxes {
        ToolsView.setTextOrGone(vEnter, s!!)
        vEnter.setOnClickListener { vi ->
            if (autoHideOnEnter)
                hide()
            else
                setEnabled(false)
            for (i in 0 until vOptionsContainer.childCount) {
                val v = vOptionsContainer.getChildAt(i) as CheckBox
                val item = v.tag as Item
                item.onChange.invoke(this, v.isChecked)
                if (v.isChecked) item.onSelected.invoke(this)
                if (!v.isChecked) item.onNotSelected.invoke(this)
            }
        }
        return this
    }

    fun setAutoHideOnEnter(autoHideOnEnter: Boolean): WidgetCheckBoxes {
        this.autoHideOnEnter = autoHideOnEnter
        return this
    }

    fun setOnCancel(onCancel: (WidgetCheckBoxes) -> Unit = {}): WidgetCheckBoxes {
        return setOnCancel(null, onCancel)
    }

    fun setOnCancel(@StringRes s: Int, onCancel: (WidgetCheckBoxes) -> Unit): WidgetCheckBoxes {
        return setOnCancel(ToolsResources.getString(s), onCancel)
    }

    @JvmOverloads
    fun setOnCancel(s: String?, onCancel: (WidgetCheckBoxes) -> Unit = {}): WidgetCheckBoxes {
        super.setOnHide { b -> onCancel.invoke(this) }
        ToolsView.setTextOrGone(vCancel, s!!)
        vCancel.setOnClickListener { v ->
            hide()
            onCancel.invoke(this)
        }

        return this
    }

    //
    //  Item
    //

    private inner class Item {

        val v: CheckBox = CheckBox(SupAndroid.activity!!)

        var onChange: (WidgetCheckBoxes, Boolean) -> Unit = { w, b -> run { } }
        var onSelected: (WidgetCheckBoxes) -> Unit = {}
        var onNotSelected: (WidgetCheckBoxes) -> Unit = {}

        init {
            v.tag = this
            vOptionsContainer.addView(v)
            if (vOptionsContainer.childCount > 1)
                (v.layoutParams as ViewGroup.MarginLayoutParams).topMargin = ToolsView.dpToPx(16f)

        }

    }


}
