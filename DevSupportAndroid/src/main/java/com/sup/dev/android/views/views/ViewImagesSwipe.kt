package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsImagesLoader
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.adapters.recycler_view.RecyclerCardAdapter
import com.sup.dev.android.views.cards.Card
import com.sup.dev.android.views.cards.CardSpace
import com.sup.dev.android.views.screens.SImageView


class ViewImagesSwipe constructor(
        context: Context,
        attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    private val vRecycler = RecyclerView(context)
    private val vNext: ViewIcon = ToolsView.inflate(R.layout.z_icon)
    private val vBack: ViewIcon = ToolsView.inflate(R.layout.z_icon)
    private val adapter = RecyclerCardAdapter()
    private var onClickGlobal: (CardSwipe<Any>) -> Boolean = { false }

    init {
        adapter.setCardW(ViewGroup.LayoutParams.WRAP_CONTENT)
        adapter.setCardH(ViewGroup.LayoutParams.MATCH_PARENT)
        vRecycler.layoutManager = LinearLayoutManager(context, GridLayoutManager.HORIZONTAL, false)
        vRecycler.adapter = adapter

        vNext.setIconBackgroundColor(ToolsResources.getColor(R.color.focus_dark))
        vBack.setIconBackgroundColor(ToolsResources.getColor(R.color.focus_dark))
        vNext.setImageResource(R.drawable.ic_keyboard_arrow_right_white_24dp)
        vBack.setImageResource(R.drawable.ic_keyboard_arrow_left_white_24dp)

        addView(vRecycler)
        addView(vNext)
        addView(vBack)
        vNext.layoutParams.width = ToolsView.dpToPx(48).toInt()
        vNext.layoutParams.height = ToolsView.dpToPx(48).toInt()
        (vNext.layoutParams as LayoutParams).gravity = Gravity.RIGHT or Gravity.CENTER
        (vNext.layoutParams as LayoutParams).rightMargin = ToolsView.dpToPx(8).toInt()
        vBack.layoutParams.width = ToolsView.dpToPx(48).toInt()
        vBack.layoutParams.height = ToolsView.dpToPx(48).toInt()
        (vBack.layoutParams as LayoutParams).gravity = Gravity.LEFT or Gravity.CENTER
        (vBack.layoutParams as LayoutParams).leftMargin = ToolsView.dpToPx(8).toInt()

        vNext.setOnClickListener {
            if (lastItem() < adapter.size() - 1) {
                if (adapter.get(lastItem()) is CardSpace) vRecycler.smoothScrollToPosition(lastItem() + 1)
                else vRecycler.smoothScrollToPosition(lastItem() + 2)
            }
        }
        vBack.setOnClickListener {
            if (firstItem() > 0) {
                if (adapter.get(firstItem()) is CardSpace) vRecycler.smoothScrollToPosition(firstItem() - 1)
                else vRecycler.smoothScrollToPosition(firstItem() - 2)
            }
        }
        vRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                if (newState == 0) updateVisibility()
            }
        })
    }

    private fun updateVisibility() {
        vNext.visibility = if (lastItem() < adapter.size() - 1) View.VISIBLE else View.INVISIBLE
        vBack.visibility = if (firstItem() > 0) View.VISIBLE else View.INVISIBLE
    }

    private fun lastItem() = (vRecycler.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

    private fun firstItem() = (vRecycler.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

    fun clear() {
        adapter.clear()
        updateVisibility()
    }

    fun add(id: Long, fullId: Long = id, w: Int = 0, h: Int = 0, onClick: ((Long) -> Unit)? = null, onLongClick: ((Long) -> Unit)? = null) {
        if (!adapter.isEmpty) adapter.add(CardSpace(0,4))
        adapter.add(CardSwipeId(id, fullId, w, h, onClick, onLongClick))
        updateVisibility()
    }

    fun add(bitmap: Bitmap, onClick: ((Bitmap) -> Unit)? = null, onLongClick: ((Bitmap) -> Unit)? = null) {
        if (!adapter.isEmpty) adapter.add(CardSpace(0,4))
        adapter.add(CardSwipeBitmap(bitmap, onClick, onLongClick))
        updateVisibility()
    }

    fun remove(index: Int) {
        adapter.remove(adapter[CardSwipe::class][index])
        updateVisibility()
    }

    fun scrollToEnd() {
        vRecycler.scrollToPosition(adapter.size() - 1)
    }

    //
    //  Setters
    //

    fun setOnClickGlobal(onClickGlobal: (CardSwipe<Any>) -> Boolean) {
        this.onClickGlobal = onClickGlobal
    }

    //
    //  Getters
    //

    fun getArrayIds(): Array<Long> {
        val array = adapter[CardSwipeId::class]
        return Array(array.size) { array[it].fullId }
    }

    fun getArrayBitmaps(): Array<Bitmap> {
        val array = adapter[CardSwipeBitmap::class]
        return Array(array.size) { array[it].bitmap }
    }

    fun size() = adapter.size(CardSwipe::class)

    //
    //  Card
    //

    abstract inner class CardSwipe<K>(
            val onClick: ((K) -> Unit)?,
            val onLongClick: ((K) -> Unit)?
    ) : Card() {

        override fun getLayout(): Int {
            return R.layout.view_image_swipe_card
        }

        override fun bindView(view: View) {
            val vImage: ImageView = view.findViewById(R.id.vImage)
            view.setOnClickListener {
                if (onClickGlobal(this as CardSwipe<Any>)) return@setOnClickListener
                else if (onClick == null) toImageView()
                else onClick.invoke(getSource())
            }
            if (onLongClick != null)
                view.setOnLongClickListener {
                    onLongClick.invoke(getSource())
                    true
                }
            set(view, vImage)
        }

        abstract fun set(view: View, vImage: ImageView)

        abstract fun toImageView()

        abstract fun getSource(): K

    }


    inner class CardSwipeBitmap(
            val bitmap: Bitmap,
            onClick: ((Bitmap) -> Unit)?,
            onLongClick: ((Bitmap) -> Unit)? = null
    ) : CardSwipe<Bitmap>(onClick, onLongClick) {

        override fun set(view: View, vImage: ImageView) {
            vImage.setImageBitmap(bitmap)
        }

        override fun toImageView() {
            val array = getArrayBitmaps()
            var index = 0
            for (i in 0 until array.size) if (array[i] == bitmap) index = i
            Navigator.to(SImageView(index, array))
        }

        override fun getSource() = bitmap

    }

    inner class CardSwipeId(
            val id: Long,
            val fullId: Long,
            val w: Int,
            val h: Int,
            onClick: ((Long) -> Unit)?,
            onLongClick: ((Long) -> Unit)? = null
    ) : CardSwipe<Long>(onClick, onLongClick) {

        override fun set(view: View, vImage: ImageView) {
            ToolsImagesLoader.load(id).size(w, h).into(vImage)
        }

        override fun toImageView() {
            val array = getArrayIds()
            var index = 0
            for (i in 0 until array.size) if (array[i] == fullId) index = i
            Navigator.to(SImageView(index, array))
        }

        override fun getSource() = id
    }
}
