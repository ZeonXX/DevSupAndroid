package com.sup.dev.android.views.widgets

import androidx.annotation.StringRes
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.cards.CardDividerTitleMini
import java.util.ArrayList


class WidgetCheckBoxes : Widget(R.layout.widget_container) {

    private val vOptionsContainer: LinearLayout = view.findViewById(R.id.vContentContainer)
    private val vCancel: Button = view.findViewById(R.id.vCancel)
    private val vEnter: Button = view.findViewById(R.id.vEnter)

    private var autoHideOnEnter = true

    //
    //  Item
    //

    private var buildItem: Item? = null
    private var skipThisItem = false
    private var skipGroup = false

    init {
        isUseMoreScreenSpace = true
        vCancel.visibility = View.GONE
        vEnter.visibility = View.GONE
    }

    override fun onShow() {
        super.onShow()
        finishItemBuilding()
    }

    private fun finishItemBuilding() {
        if (buildItem != null) {
            val i = buildItem
            buildItem = null
        }
    }

    fun add(@StringRes text: Int): WidgetCheckBoxes {
        return add(ToolsResources.s(text))
    }

    fun add(@StringRes text: Int, key: Any): WidgetCheckBoxes {
        return add(ToolsResources.s(text), key)
    }

    fun add(text: String): WidgetCheckBoxes {
        return add(text, text)
    }

    fun add(text: String, key: Any): WidgetCheckBoxes {
        finishItemBuilding()
        buildItem = Item(key)
        buildItem!!.v.text = text
        return this
    }

    fun onChange(onChange: (WidgetCheckBoxes, Item, Boolean) -> Unit): WidgetCheckBoxes {
        buildItem!!.onChange = onChange
        return this
    }

    fun onSelected(onSelected: (WidgetCheckBoxes, Item) -> Unit): WidgetCheckBoxes {
        buildItem!!.onSelected = onSelected
        return this
    }

    fun onNotSelected(onNotSelected: (WidgetCheckBoxes, Item) -> Unit): WidgetCheckBoxes {
        buildItem!!.onNotSelected = onNotSelected
        return this
    }

    fun text(@StringRes text: Int): WidgetCheckBoxes {
        return text(ToolsResources.s(text))
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

    fun group(@StringRes title: Int): WidgetCheckBoxes {
        return group(ToolsResources.s(title))
    }

    fun group(@StringRes title: Int, divider: Boolean): WidgetCheckBoxes {
        return group(ToolsResources.s(title), divider)
    }

    @JvmOverloads
    fun group(title: String?, divider: Boolean = false): WidgetCheckBoxes {
        finishItemBuilding()
        val card = CardDividerTitleMini().setText(title).setDividerBottom(divider)
        val v = card.instanceView(context)
        card.bindCardView(v)
        vOptionsContainer.addView(v)
        if (vOptionsContainer.childCount > 1)
            (v.layoutParams as ViewGroup.MarginLayoutParams).topMargin = ToolsView.dpToPx(8f).toInt()
        return this
    }

    //
    //  Setters
    //

    fun setOnEnter(@StringRes s: Int): WidgetCheckBoxes {
        return setOnEnter(ToolsResources.s(s))
    }

    fun setOnEnter(s: String?): WidgetCheckBoxes {
        ToolsView.setTextOrGone(vEnter, s)
        vEnter.setOnClickListener { vi ->
            for (i in 0 until vOptionsContainer.childCount) {
                if (vOptionsContainer.getChildAt(i) !is CheckBox) continue
                val v = vOptionsContainer.getChildAt(i) as CheckBox
                val item = v.tag as Item
                if (v.isChecked) item.onSelected.invoke(this, item)
                if (!v.isChecked) item.onNotSelected.invoke(this, item)
            }
            if (autoHideOnEnter)
                hide()
            else
                setEnabled(false)
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
        return setOnCancel(ToolsResources.s(s), onCancel)
    }

    @JvmOverloads
    fun setOnCancel(s: String?, onCancel: (WidgetCheckBoxes) -> Unit = {}): WidgetCheckBoxes {
        super.setOnHide { b -> onCancel.invoke(this) }
        ToolsView.setTextOrGone(vCancel, s)
        vCancel.setOnClickListener { v ->
            hide()
            onCancel.invoke(this)
        }

        return this
    }

    //
    //  Item
    //

    public inner class Item(val key: Any) {

        val v: CheckBox = CheckBox(SupAndroid.activity!!)

        var onChange: (WidgetCheckBoxes, Item, Boolean) -> Unit = { w, i, b -> run { } }
        var onSelected: (WidgetCheckBoxes, Item) -> Unit = { w, i -> }
        var onNotSelected: (WidgetCheckBoxes, Item) -> Unit = { w, i -> }

        init {
            v.tag = this
            v.setOnCheckedChangeListener { w, b -> onChange.invoke(this@WidgetCheckBoxes, this, v.isChecked) }
            vOptionsContainer.addView(v)
            if (vOptionsContainer.childCount > 1)
                (v.layoutParams as ViewGroup.MarginLayoutParams).topMargin = ToolsView.dpToPx(8f).toInt()

        }

    }


}
