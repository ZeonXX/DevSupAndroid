package com.sup.dev.android.views.widgets

import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.support.adapters.recycler_view.RecyclerCardAdapter
import com.sup.dev.android.views.cards.CardDivider
import com.sup.dev.android.views.cards.CardDividerTitleMini
import com.sup.dev.android.views.cards.CardMenu
import com.sup.dev.java.libs.debug.Debug

open class WidgetMenu : WidgetRecycler() {

    val myAdapter: RecyclerCardAdapter = RecyclerCardAdapter()
    private var onGlobalSelected: (WidgetMenu, String?) -> Unit = { _, _ -> }

    private var prefCount = 0
    private var autoHide = true

    init {
        vRecycler.layoutManager = LinearLayoutManager(view.context)
        setAdapter<WidgetRecycler>(myAdapter)
    }

    override fun onShow() {
        vRecycler.isVerticalScrollBarEnabled = true
        super.onShow()
        finishItemBuilding()
    }

    fun setAutoHide(b: Boolean): WidgetMenu {
        autoHide = b
        return this
    }

    override fun setTitle(title: Int): WidgetMenu {
        super.setTitle(title)
        return this
    }

    override fun setTitle(title: String?): WidgetMenu {
        super.setTitle(title)
        return this
    }

    override fun setTitleBackgroundColor(color: Int): WidgetMenu {
        super.setTitleBackgroundColor(color)
        return this
    }

    override fun setTitleBackgroundColorRes(color: Int): WidgetMenu {
        super.setTitleBackgroundColorRes(color)
        return this
    }

    //
    //  Item
    //

    private var buildItem: Item? = null
    private var skipThisItem = false
    private var skipGroup = false

    fun setItemVisible(index: Int, visible: Boolean) {
        if (myAdapter.size() > index) {
            (myAdapter.get(index) as CardMenu).setVisible(visible)
        }
    }

    fun getItemsCount() = myAdapter.size()

    fun clear() {
        finishItemBuilding()
        myAdapter.clear()
        prefCount = 0
    }

    private fun add(item: Item) {

        item.card = CardMenu()
        item.card?.text = item.text
        item.card?.setIcon(item.icon)
        item.card?.setIcon(item.iconDrawable)
        item.card?.setIconFilter(item.iconFilter)
        item.card?.setBackground(item.bg)
        if (item.textColor != null) item.card?.setTextColor(item.textColor!!)
        item.card?.setOnClick { _, _, _ ->
            item.onClick.invoke(this, item.card!!)
            onGlobalSelected.invoke(this, item.text)
            if (autoHide) hide()
        }
        if(item.onLongClick != null) {
            item.card?.setOnLongClick { _ ->
                item.onLongClick!!.invoke(this, item.card!!)
                onGlobalSelected.invoke(this, item.text)
                if (autoHide) hide()
            }
        }


        if (item.preferred) {
            if (prefCount == 0) myAdapter.add(0, CardDivider())
            myAdapter.add(prefCount, item.card!!)
            prefCount++
        } else {
            myAdapter.add(item.card!!)
        }

    }

    fun group(@StringRes title: Int): WidgetMenu {
        return group(ToolsResources.s(title))
    }

    fun group(@StringRes title: Int, divider: Boolean): WidgetMenu {
        return group(ToolsResources.s(title), divider)
    }

    @JvmOverloads
    fun group(title: String?, divider: Boolean = false): WidgetMenu {
        finishItemBuilding()
        myAdapter.add(CardDividerTitleMini().setText(title).setDividerBottom(divider))
        return this
    }

    fun setOnGlobalSelected(onGlobalSelected: (WidgetMenu, String?) -> Unit) {
        this.onGlobalSelected = onGlobalSelected
    }

    fun finishItemBuilding() {
        if (buildItem != null) {
            val i = buildItem
            buildItem = null
            if (!skipThisItem && !skipGroup) add(i!!)
            skipThisItem = false
        }
    }

    fun add(@StringRes text: Int): WidgetMenu {
        return add(ToolsResources.s(text))
    }

    fun add(@StringRes text: Int, onClick: (WidgetMenu, CardMenu) -> Unit = { _, _ -> }): WidgetMenu {
        return add(ToolsResources.s(text), onClick)
    }

    fun add(text: String, onClick: (WidgetMenu, CardMenu) -> Unit = { _, _ -> }): WidgetMenu {
        finishItemBuilding()
        buildItem = Item()
        buildItem!!.text = text
        buildItem!!.onClick = onClick
        return this
    }

    fun text(@StringRes text: Int): WidgetMenu {
        return text(ToolsResources.s(text))
    }

    fun text(text: String): WidgetMenu {
        buildItem!!.text = text
        return this
    }

    fun icon(icon: Int): WidgetMenu {
        buildItem!!.icon = icon
        return this
    }

    fun icon(icon: Drawable): WidgetMenu {
        buildItem!!.iconDrawable = icon
        return this
    }

    fun iconFilter(iconFilter: Int): WidgetMenu {
        buildItem!!.iconFilter = iconFilter
        return this
    }

    fun backgroundRes(@ColorRes color: Int): WidgetMenu {
        return background(ToolsResources.getColor(color))
    }

    fun backgroundRes(@ColorRes color: Int, condition: () -> Boolean): WidgetMenu {
        return if (condition.invoke())
            background(ToolsResources.getColor(color))
        else
            this
    }

    fun background(@ColorInt color: Int): WidgetMenu {
        buildItem!!.bg = color
        return this
    }

    fun textColorRes(@ColorRes color: Int): WidgetMenu {
        return textColor(ToolsResources.getColor(color))
    }

    fun textColorRes(@ColorRes color: Int, condition: () -> Boolean): WidgetMenu {
        return if (condition.invoke())
            textColor(ToolsResources.getColor(color))
        else
            this
    }

    fun textColor(@ColorInt color: Int): WidgetMenu {
        buildItem!!.textColor = color
        return this
    }

    fun preferred(b: Boolean): WidgetMenu {
        buildItem!!.preferred = b
        return this
    }

    fun onClick(onClick: (WidgetMenu, CardMenu) -> Unit): WidgetMenu {
        buildItem!!.onClick = onClick
        return this
    }
    fun onLongClick(onLongClick: (WidgetMenu, CardMenu) -> Unit): WidgetMenu {
        buildItem!!.onLongClick = onLongClick
        return this
    }

    fun condition(b: Boolean): WidgetMenu {
        skipThisItem = !b
        return this
    }

    fun groupCondition(b: Boolean): WidgetMenu {
        finishItemBuilding()
        skipGroup = !b
        return this
    }

    fun reverseGroupCondition(): WidgetMenu {
        finishItemBuilding()
        skipGroup = !skipGroup
        return this
    }

    fun clearGroupCondition(): WidgetMenu {
        finishItemBuilding()
        skipGroup = false
        return this
    }

    //
    //  Item
    //

    private inner class Item {

        var card: CardMenu? = null
        var onClick: (WidgetMenu, CardMenu) -> Unit = { _, _ -> }
        var onLongClick: ((WidgetMenu, CardMenu) -> Unit)? = null
        var text = ""
        var icon = 0
        var iconFilter:Int? = null
        var iconDrawable: Drawable? = null
        var bg = 0
        var textColor: Int? = null
        var preferred = false

    }


}
