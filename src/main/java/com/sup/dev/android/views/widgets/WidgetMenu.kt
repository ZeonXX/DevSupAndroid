package com.sup.dev.android.views.widgets

import android.graphics.drawable.Drawable
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.cards.*
import com.sup.dev.android.views.support.adapters.recycler_view.RecyclerCardAdapter
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.java.libs.debug.log

open class WidgetMenu : WidgetRecycler() {

    val myAdapter: RecyclerCardAdapter = RecyclerCardAdapter()
    private var onGlobalSelected: (WidgetMenu, String?) -> Unit = { _, _ -> }

    private var prefCount = 0
    private var autoHide = true

    init {
        vRecycler.layoutManager = LinearLayoutManager(view.context)
        setAdapter<WidgetRecycler>(myAdapter)
        vRoot.minimumWidth = ToolsView.dpToPx(256).toInt()
        vRecycler.itemAnimator = null
    }

    override fun onShow() {
        vRecycler.isVerticalScrollBarEnabled = true
        super.onShow()
        finishItemBuilding()
        iconBuilder?.finishItemBuilding()

        for (c in myAdapter.get(CardSpoiler::class)) if (c.cards.isEmpty()) myAdapter.remove(c)
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

    fun addTitleView(view: View):WidgetMenu{
        myAdapter.add(0, CardView(view))
        return this
    }

    //
    //  Item
    //

    private var buildItem: Item? = null
    private var skipThisItem = false
    private var skipGroup = false
    private var spoiler:CardSpoiler? = null

    fun getMenuItem(index: Int):CardMenu{
        return myAdapter.get(index) as CardMenu
    }

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
        item.card?.chipText = item.chipText
        item.card?.setIcon(item.icon)
        item.card?.setIcon(item.iconDrawable)
        item.card?.setIconFilter(item.iconFilter)
        item.card?.setBackground(item.bg)
        if (item.textColor != null) item.card?.setTextColor(item.textColor!!)
        item.card?.setOnClick {
            if(isHided) return@setOnClick
            item.onClick.invoke(ClickEvent(this, item.card!!))
            onGlobalSelected.invoke(this, item.text)
            if (autoHide) hide()
        }
        if(item.onLongClick != null) {
            item.card?.setOnLongClick { _ ->
                item.onLongClick!!.invoke(ClickEvent(this, item.card!!))
                onGlobalSelected.invoke(this, item.text)
                if (autoHide) hide()
            }
        }

        if(spoiler != null){
            spoiler!!.add(item.card!!)
        }else{
            if (item.preferred) {
                if (prefCount == 0) myAdapter.add(0, CardDivider())
                myAdapter.add(prefCount, item.card!!)
                prefCount++
            } else {
                myAdapter.add(item.card!!)
            }
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

    fun add(@StringRes text: Int, onClick: (ClickEvent) -> Unit = { }): WidgetMenu {
        return add(ToolsResources.s(text), onClick)
    }

    fun add(text: String, onClick: (ClickEvent) -> Unit = { }): WidgetMenu {
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

    fun chipText(@StringRes chipText: Int): WidgetMenu {
        return chipText(ToolsResources.s(chipText))
    }

    fun chipText(chipText: String): WidgetMenu {
        buildItem!!.chipText = chipText
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

    fun onClick(onClick: (ClickEvent) -> Unit): WidgetMenu {
        buildItem!!.onClick = onClick
        return this
    }

    fun onLongClick(onLongClick: (ClickEvent) -> Unit): WidgetMenu {
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

    fun spoiler(name:String, backgroundColor:Int?=null, textColor:Int?=null): WidgetMenu{
        finishItemBuilding()
        val card = CardSpoiler()
        this.spoiler = card
        card.setRecyclerView(vRecycler)
        card.setTitle(name)
        card.setDividerTopVisible(false)
        card.setDividerVisible(false)
        if(backgroundColor != null) card.setBackgroundColor(backgroundColor)
        if(textColor != null) card.setTextColor(textColor)
        myAdapter.add(card)
        return this
    }

    fun stopSpoiler(){
        spoiler = null
    }

    private inner class Item {

        var card: CardMenu? = null
        var onClick: (ClickEvent) -> Unit = {  }
        var onLongClick: ((ClickEvent) -> Unit)? = null
        var text = ""
        var chipText = ""
        var icon = 0
        var iconFilter:Int? = null
        var iconDrawable: Drawable? = null
        var bg = 0
        var textColor: Int? = null
        var preferred = false

    }

    public class ClickEvent(
            val widget:WidgetMenu,
            val card:CardMenu
    ){}

    //
    //  Icon
    //

    private var iconBuilder:IconBuilder? = null

    fun iconBuilder():IconBuilder{
        if(iconBuilder == null) iconBuilder = IconBuilder()
        return iconBuilder!!
    }

    inner class IconBuilder{

        val cardContainer = CardIconsContainer()
        private var buildItem: Icon? = null
        private var skipThisItem = false
        private var skipGroup = false

        init {
            myAdapter.add(0, cardContainer)
        }

        fun finishItemBuilding() {
            log("finishItemBuilding [$buildItem]")
            if (buildItem != null) {
                val i = buildItem
                buildItem = null
                if (!skipThisItem && !skipGroup) add(i!!)
                skipThisItem = false
            }
        }

        fun add(drawable:Drawable, onClick:(ClickEventIocn)->Unit):IconBuilder{
            finishItemBuilding()
            buildItem = Icon()
            buildItem!!.iconDrawable = drawable
            buildItem!!.onClick = onClick
            return this
        }

        fun condition(b: Boolean): IconBuilder {
            skipThisItem = !b
            return this
        }

        private fun add(item: Icon) {
            log("add [$item]")

            item.vIcon = ToolsView.inflate(R.layout.z_icon)
            item.vIcon?.setImageDrawable(item.iconDrawable)
            if(item.iconFilter != null)item.vIcon?.setFilter(item.iconFilter!!)
            item.vIcon?.setOnClickListener {
                if(isHided) return@setOnClickListener
                item.onClick.invoke(ClickEventIocn(this@WidgetMenu, item))
                if (autoHide) hide()
            }
            if(item.onLongClick != null) {
                item.vIcon?.setOnLongClickListener{
                    item.onLongClick!!.invoke(ClickEventIocn(this@WidgetMenu, item))
                    if (autoHide) hide()
                    return@setOnLongClickListener true
                }
            }

            cardContainer.vLinear.addView(item.vIcon)

        }


    }

    inner class Icon{

        var vIcon:ViewIcon? = null
        var onClick: (ClickEventIocn) -> Unit = {  }
        var onLongClick: ((ClickEventIocn) -> Unit)? = null
        var iconDrawable: Drawable? = null
        var iconFilter:Int? = null

    }

    public class ClickEventIocn(
            val widget:WidgetMenu,
            val icon:Icon
    ){}

    class CardIconsContainer : Card(0){

        val vFrame = FrameLayout(SupAndroid.activity!!)
        val vLinear = LinearLayout(SupAndroid.activity!!)

        init {
            vFrame.addView(vLinear, 0)
            vLinear.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            vLinear.layoutParams.height =  ViewGroup.LayoutParams.WRAP_CONTENT
            (vLinear.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.RIGHT
        }

        override fun instanceView(): View {
            return vFrame
        }

    }

}
