package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.Bitmap
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsBitmap
import com.sup.dev.android.tools.ToolsImagesLoader
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.cards.Card
import com.sup.dev.android.views.cards.CardSpace
import com.sup.dev.android.views.screens.SImageView
import com.sup.dev.android.views.support.adapters.recycler_view.RecyclerCardAdapter
import com.sup.dev.java.tools.ToolsThreads


class ViewImagesSwipe constructor(
        context: Context,
        attrs: AttributeSet? = null
) : FrameLayout(context, attrs) {

    /*

     */

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
        vNext.setImageResource(ToolsResources.getDrawableAttrId(R.attr.ic_keyboard_arrow_right_24dp))
        vBack.setImageResource(ToolsResources.getDrawableAttrId(R.attr.ic_keyboard_arrow_left_24dp))

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
                updateVisibility()
            }
        })
        updateVisibility()
    }

    private fun updateVisibility() {
        vNext.visibility = if (lastItem() == -1 || lastItem() >= adapter.size() - 1) View.INVISIBLE else View.VISIBLE
        vBack.visibility = if (firstItem() < 1) View.INVISIBLE else View.VISIBLE
    }

    private fun lastItem() = (vRecycler.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()

    private fun firstItem() = (vRecycler.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition()

    fun clear() {
        adapter.clear()
        updateVisibility()
    }

    fun add(id: Long, fullId: Long = id, w: Int = 0, h: Int = 0, onClick: ((Long) -> Unit)? = null) {
        add(id, fullId, w, h, onClick, null)
    }

    fun add(id: Long, fullId: Long = id, w: Int = 0, h: Int = 0, onClick: ((Long) -> Unit)? = null, onLongClick: ((Long) -> Unit)? = null) {
        if (!adapter.isEmpty) adapter.add(CardSpace(0, 4))
        adapter.add(CardSwipeId(id, fullId, w, h, onClick, onLongClick))
    }

    fun add(bitmap: Bitmap, onClick: ((Bitmap) -> Unit)? = null) {
        add(bitmap, onClick, null)
    }

    fun add(bitmap: Bitmap, onClick: ((Bitmap) -> Unit)? = null, onLongClick: ((Bitmap) -> Unit)? = null) {
        if (!adapter.isEmpty) adapter.add(CardSpace(0, 4))
        adapter.add(CardSwipeBitmap(bitmap, onClick, onLongClick))
    }

    fun add(bytes: ByteArray, onClick: ((ByteArray) -> Unit)? = null) {
        add(bytes, onClick, null)
    }

    fun add(bytes: ByteArray, onClick: ((ByteArray) -> Unit)? = null, onLongClick: ((ByteArray) -> Unit)? = null) {
        if (!adapter.isEmpty) adapter.add(CardSpace(0, 4))
        adapter.add(CardSwipeBytes(bytes, onClick, onLongClick))
    }

    fun remove(index: Int) {
        adapter.remove(adapter[CardSwipe::class][index])
        ToolsThreads.main(true) { updateVisibility() }
    }

    fun remove(bytes: ByteArray) {
        for (c in adapter[CardSwipeBytes::class]) {
            if (c.bytes === bytes) adapter.remove(c)
        }
        ToolsThreads.main(true) { updateVisibility() }
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

    fun getArrayBytes(): Array<ByteArray> {
        val array = adapter[CardSwipeBytes::class]
        return Array(array.size) { array[it].bytes }
    }

    fun size() = adapter.size(CardSwipe::class)

    fun isEmpty() = adapter.isEmpty

    fun isNotEmpty() = !adapter.isEmpty

    //
    //  Card
    //

    abstract inner class CardSwipe<K>(
            val onClick: ((K) -> Unit)?,
            val onLongClick: ((K) -> Unit)?
    ) : Card(R.layout.view_image_swipe_card) {

        override fun bindView(view: View) {
            super.bindView(view)
            val vImage: ImageView = view.findViewById(R.id.vImage)
            view.setOnClickListener {
                if (onClickGlobal(this as CardSwipe<Any>)) {
                    return@setOnClickListener
                } else if (onClick == null) {
                    toImageView()
                } else {
                    onClick.invoke(getSource())
                }
            }
            if (onLongClick != null)
                view.setOnLongClickListener {
                    onLongClick.invoke(getSource())
                    true
                }
            set(view, vImage)
            updateVisibility()
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
            updateVisibility()
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
            ToolsImagesLoader.load(id).size(w, h).setOnSetHolder { ToolsThreads.main(10) { updateVisibility() } }.setOnLoaded { ToolsThreads.main(10) { updateVisibility() } }.into(vImage)
        }

        override fun toImageView() {
            val array = getArrayIds()
            var index = 0
            for (i in 0 until array.size) if (array[i] == fullId) index = i
            Navigator.to(SImageView(index, array))
        }

        override fun getSource() = id
    }

    inner class CardSwipeBytes(
            val bytes: ByteArray,
            onClick: ((ByteArray) -> Unit)?,
            onLongClick: ((ByteArray) -> Unit)? = null
    ) : CardSwipe<ByteArray>(onClick, onLongClick) {

        override fun set(view: View, vImage: ImageView) {
            vImage.setImageBitmap(ToolsBitmap.decode(bytes))
            updateVisibility()
        }

        override fun toImageView() {
            val array = getArrayBytes()
            var index = 0
            for (i in 0 until array.size) if (array[i] === bytes) index = i
            Navigator.to(SImageView(index, array))
        }

        override fun getSource() = bytes

    }

}
