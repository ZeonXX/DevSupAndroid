package com.sup.dev.android.views.support.adapters.pager

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sup.dev.android.views.support.adapters.CardAdapter
import com.sup.dev.android.views.support.adapters.NotifyItem
import com.sup.dev.android.views.cards.Card
import com.sup.dev.java.classes.collections.HashList
import java.util.ArrayList

open class PagerCardAdapter : PagerAdapter(), CardAdapter {

    private val holders = ArrayList<Holder>()
    val items: ArrayList<Card> = ArrayList()
    private val viewCash = HashList<Any, View>()

    private var notifyCount = 0

    val isEmpty: Boolean
        get() = items.isEmpty()

    val views: ArrayList<View>
        get() {

            val list = ArrayList<View>()
            for (h in holders)
                list.add(h.itemView)

            return list
        }

    //
    //  Adapter
    //

    override fun notifyUpdate() {
        notifyDataSetChanged()
    }

    override fun instantiateItem(parent: ViewGroup, p: Int): Any {
        val holder = getFreeHolder(parent)
        parent.addView(holder.itemView)

        var i = p
        while (i < p + notifyCount && i < items.size) {
            if (items[realPosition(p)] is NotifyItem)
                (items[realPosition(p)] as NotifyItem).notifyItem()
            i++
        }

        val card = items[realPosition(p)]
        val frame = holder.itemView
        holder.item = card
        holder.position = p

        var cardView = viewCash.removeOne(card::class)
        if (cardView == null) cardView = card.instanceView(frame)

        frame.removeAllViews()
        frame.addView(cardView)
        frame.tag = card::class
        (cardView.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.CENTER

        card.bindCardView(cardView)
        if (frame.width == 0) frame.requestLayout()

        return card
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        for(h in getHoldersForItem(obj)) if (h.itemView == view) return true
        return false
    }

    override fun destroyItem(parent: ViewGroup, position: Int, ob: Any) {
        for(h in getHoldersForPosition(position)){
            parent.removeView((h.itemView))
            if (h.itemView.childCount != 0) viewCash.add(h.itemView.tag, h.itemView.getChildAt(0))
            h.itemView.removeAllViews()
            h.position = -1
            h.item?.detachView()
            h.item = null
        }
    }

    override fun getItemPosition(ob: Any): Int {
        val position = indexOf(ob as Card)
        val positionX = if (position < 0) POSITION_NONE else position
        return positionX
    }

    override fun getCount(): Int {
        return items.size
    }

    //
    //  Items
    //

    fun updateAll() {
        for (c in items) c.update()
    }

    override operator fun get(i: Int): Card {
        return items[realPosition(i)]
    }

    fun add(card: Card) {
        card.setCardAdapter(this)
        items.add(card)
        notifyDataSetChanged()
    }

    override fun add(i: Int, card: Card) {
        card.setCardAdapter(this)
        items.add(i, card)
        notifyDataSetChanged()
    }


    fun set(items: List<Card>) {
        this.items.clear()
        this.items.addAll(items)
        notifyDataSetChanged()
    }

    override fun remove(card: Card) {
        if (items.remove(card)) notifyDataSetChanged()
    }

    fun remove(position: Int) {
        items.removeAt(realPosition(position))
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
    }

    override fun indexOf(card: Card): Int {
        return items.indexOf(card)
    }

    override fun indexOf(checker: (Card) -> Boolean): Int {
        for (c in items) if (checker.invoke(c)) return indexOf(c)
        return -1
    }

    override fun <K : Card> find(checker: (Card) -> Boolean): K? {
        for (c in items) if (checker.invoke(c)) return c as K
        return null
    }

    override operator fun contains(card: Card): Boolean {
        return indexOf(card) > -1
    }

    open fun realPosition(position: Int): Int {
        return position
    }

    override fun size(): Int {
        return items.size
    }

    override fun getView(card: Card): View? {
        for (h in holders) if (h.item === card) return h.itemView.getChildAt(0)
        return null
    }

    @Suppress("UNCHECKED_CAST")
    fun <K : PagerCardAdapter> setNotifyCount(notifyCount: Int): K {
        this.notifyCount = notifyCount
        return this as K
    }

    override fun isVisible(card: Card): Boolean {
        return getView(card) != null
    }

    fun getViewIfVisible(position: Int): View? {
        for (h in holders)
            if (h.item === items[position])
                return h.itemView
        return null
    }

    //
    //  Holder
    //

    fun getHolderForView(view:View):Holder?{
        for (holder in holders) if (holder.itemView.getChildAt(0) == view) return holder
        return null
    }

    fun getHoldersForItem(item: Any): ArrayList<Holder> {
        val list = ArrayList<Holder>()
        for (holder in holders) if (holder.item == item) list.add(holder)
        return list
    }

    private fun getHoldersForPosition(position: Int): ArrayList<Holder> {
        val list = ArrayList<Holder>()
        for (holder in holders) if (holder.position == position) list.add(holder)
        return list
    }

    private fun getFreeHolder(parent: ViewGroup): Holder {
        for (holder in holders) if (holder.itemView.parent == null) return holder
        val holder = Holder(parent.context)
        holders.add(holder)

        return holder
    }

    class Holder constructor(context: Context) {

        var item: Card? = null
        var position = -1
        var itemView: FrameLayout = FrameLayout(context)

    }
}