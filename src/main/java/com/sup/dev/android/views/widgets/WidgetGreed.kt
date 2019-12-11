package com.sup.dev.android.views.widgets

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.cards.Card
import com.sup.dev.android.views.splash.Dialog
import com.sup.dev.android.views.splash.Sheet
import com.sup.dev.android.views.support.adapters.recycler_view.RecyclerCardAdapter

open class WidgetGreed : WidgetRecycler(R.layout.widget_greed) {

    private val DP = ToolsView.dpToPx(1).toInt()
    private val myAdapter: RecyclerCardAdapter = RecyclerCardAdapter()
    private val vEmptyText: TextView = findViewById(R.id.vEmptyText)

    private var onSelected: (WidgetGreed, Int) -> Unit = { _, _ -> }
    private var spanCount = 0

    init {
        vEmptyText.visibility = View.GONE

        spanCount = if (ToolsAndroid.isScreenPortrait()) 6 else 12
        vRecycler.layoutManager = GridLayoutManager(view.context, spanCount)
        ToolsView.setRecyclerAnimation(vRecycler)

        setAdapter<WidgetRecycler>(myAdapter)
    }

    override fun onShow() {
        super.onShow()

        (vRecycler.layoutParams as ViewGroup.MarginLayoutParams).setMargins(0, 0, 0, 0)
        vRecycler.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        if (viewWrapper is Dialog)
            (vRecycler.layoutParams as ViewGroup.MarginLayoutParams).setMargins(ToolsView.dpToPx(8).toInt(), ToolsView.dpToPx(2).toInt(), ToolsView.dpToPx(8).toInt(), 0)
        else if (viewWrapper is Sheet)
            vRecycler.layoutParams.height = ToolsView.dpToPx(320).toInt()
    }

    //
    //  Setters
    //

    fun addAttr(attr: Int, onSelectedItem: (WidgetGreed, Int) -> Unit = { _, _ -> }) {
        myAdapter.add(CardImage(0, attr, onSelectedItem))
    }

    fun add(res: Int, onSelectedItem: (WidgetGreed, Int) -> Unit = { _, _ -> }) {
        myAdapter.add(CardImage(res, 0, onSelectedItem))
    }

    fun setOnSelected(onSelected: (WidgetGreed, Int) -> Unit): WidgetGreed {
        this.onSelected = onSelected
        return this
    }

    //
    //  Card
    //

    private inner class CardImage(
            val res: Int,
            val attr: Int,
            val onSelectedItem: (WidgetGreed, Int) -> Unit
    ) : Card(R.layout.widget_greed_card) {

        override fun bindView(view: View) {
            super.bindView(view)
            val vImage: ImageView = view.findViewById(R.id.vImage)

            vImage.setOnClickListener {
                onSelectedItem.invoke(this@WidgetGreed, myAdapter.indexOf(this))
                onSelected.invoke(this@WidgetGreed, myAdapter.indexOf(this))
                hide()
            }
            if(res > 0) vImage.setImageResource(res)
            else  vImage.setImageDrawable(ToolsResources.getDrawableAttr(attr))

            val index = adapter!!.indexOf(this)
            val arg = index % spanCount
            view.setPadding(if (arg == 0) 0 else DP, if (index < spanCount) 0 else DP, if (arg == spanCount - 1) 0 else DP, DP)

        }

    }

}
