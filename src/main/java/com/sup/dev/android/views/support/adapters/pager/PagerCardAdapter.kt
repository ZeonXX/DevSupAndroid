package com.sup.dev.android.views.support.adapters.pager

import android.content.Context
import androidx.viewpager.widget.PagerAdapter
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.adapters.CardAdapter
import com.sup.dev.android.views.support.adapters.NotifyItem
import com.sup.dev.android.views.cards.Card
import com.sup.dev.java.classes.collections.HashList
import java.util.ArrayList
import kotlin.reflect.KClass


open class PagerCardAdapter : androidx.viewpager.widget.PagerAdapter(), CardAdapter {

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
    //  items
    //

    override fun instantiateItem(parent: ViewGroup, position: Int): Any {
        val holder = getFreeHolder(parent)
        parent.addView(holder.itemView)
        holder.itemView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        holder.itemView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        var i = position
        while (i < position + notifyCount && i < items.size) {
            if (items[i] is NotifyItem)
                (items[i] as NotifyItem).notifyItem()
            i++
        }
        removeItemFromHolders(items[position])
        holder.item = items[position]

        val card = items[position]
        val frame = holder.itemView

        val tag = frame.tag

        if (frame.childCount != 0  && tag != null)
            viewCash.add(tag, frame.getChildAt(0))
        frame.removeAllViews()

        var cardView = viewCash.removeOne(card::class)
        if (cardView == null)
            cardView = card.instanceView(frame.context)


        frame.addView(ToolsView.removeFromParent(cardView))
        frame.tag = card::class
        (cardView.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.CENTER

        card.bindView(cardView)

        return holder
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean {
        return obj is PagerCardAdapter.Holder && obj.itemView === view
    }

    override fun destroyItem(parent: ViewGroup, position: Int, `object`: Any) {
        parent.removeView((`object` as Holder).itemView)
    }

    override fun getItemPosition(`object`: Any): Int {
        return androidx.viewpager.widget.PagerAdapter.POSITION_NONE
    }

    fun getItemId(position: Int): Int {
        return position
    }

    fun getHolders(): ArrayList<Holder> {

        val list = ArrayList<Holder>()
        for (h in holders)
            list.add(h)

        return list
    }

    //
    //  Items
    //

    override fun getCount(): Int {
        return items.size
    }

    override operator fun get(position: Int): Card {
        return items[realPosition(position)]
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

    override fun remove(item: Card) {
        if (items.remove(item))
            notifyDataSetChanged()
    }

    fun remove(position: Int) {
        items.removeAt(realPosition(position))
        notifyDataSetChanged()
    }

    fun clear() {
        items.clear()
    }

    override fun indexOf(item: Card): Int {
        return items.indexOf(item)
    }

    override operator fun contains(card: Card): Boolean {
        return indexOf(card) > -1
    }

    protected open fun realPosition(position: Int): Int {
        return position
    }

    override fun size(): Int {
        return items.size
    }

    override fun getView(card: Card): View? {
        val holders = getHolders()
        for (h in holders) {
            if (h.item === card) return h.itemView
        }
        return null
    }

    fun <K : PagerCardAdapter> setNotifyCount(notifyCount: Int): K {
        this.notifyCount = notifyCount
        return this as K
    }

    private fun removeItemFromHolders(item: Card) {
        for (h in holders)
            if (h.item === item)
                h.item = null
    }

    //
    //  Getters
    //

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

    private fun getFreeHolder(parent: ViewGroup): Holder {

        for (holder in holders)
            if (holder.itemView.parent == null)
                return holder

        val holder = Holder(parent.context)
        holders.add(holder)

        return holder
    }

    class Holder constructor(context: Context) {

        var item: Card? = null
        var itemView: FrameLayout

        init {
            this.itemView = FrameLayout(context)
        }
    }
}