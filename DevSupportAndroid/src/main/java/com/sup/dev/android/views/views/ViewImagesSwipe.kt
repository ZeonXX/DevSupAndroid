package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsImagesLoader
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.adapters.recycler_view.RecyclerCardAdapter
import com.sup.dev.android.views.cards.Card
import com.sup.dev.android.views.cards.CardSpace
import com.sup.dev.android.views.screens.SImageView


class ViewImagesSwipe constructor(
        context: Context,
        attrs: AttributeSet? = null
) : RecyclerView(context, attrs) {

    private val adapter = RecyclerCardAdapter()

    init {
        adapter.setCardW(ViewGroup.LayoutParams.WRAP_CONTENT)
        adapter.setCardH(ViewGroup.LayoutParams.MATCH_PARENT)
        layoutManager = LinearLayoutManager(context, GridLayoutManager.HORIZONTAL, false)
        setAdapter(adapter)
    }

    fun clear() {
        adapter.clear()
    }

    fun add(id: Long, w: Int = 0, h: Int = 0, onClick: ((Long) -> Unit)? = null, onLongClick: ((Long) -> Unit)? = null) {
        if (!adapter.isEmpty) adapter.add(CardSpace(ToolsView.dpToPx(4)))
        adapter.add(CardSwipeId(id, w, h, onClick, onLongClick))
    }

    fun add(bitmap: Bitmap, onClick: ((Bitmap) -> Unit)? = null, onLongClick: ((Bitmap) -> Unit)? = null) {
        if (!adapter.isEmpty) adapter.add(CardSpace(ToolsView.dpToPx(4)))
        adapter.add(CardSwipeBitmap(bitmap, onClick, onLongClick))
    }

    fun getArrayIds(): Array<Long> {
        val array = adapter[CardSwipeId::class]
        return Array(array.size) { array[it].id }
    }

    fun getArrayBitmaps(): Array<Bitmap> {
        val array = adapter[CardSwipeBitmap::class]
        return Array(array.size) { array[it].bitmap }
    }

    //
    //  Card
    //

    private abstract inner class CardSwipe<K>(
            val onClick: ((K) -> Unit)?,
            val onLongClick: ((K) -> Unit)?
    ) : Card() {

        override fun getLayout(): Int {
            return R.layout.view_image_swipe_card
        }

        override fun bindView(view: View) {
            val vImage: ImageView = view.findViewById(R.id.vImage)
            view.setOnClickListener {
                if (onClick == null) toImageView()
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


    private inner class CardSwipeBitmap(
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

    private inner class CardSwipeId(
            val id: Long,
            val w: Int = 0,
            val h: Int = 0,
            onClick: ((Long) -> Unit)?,
            onLongClick: ((Long) -> Unit)? = null
    ) : CardSwipe<Long>(onClick, onLongClick) {

        override fun set(view: View, vImage: ImageView) {
            ToolsImagesLoader.load(id).size(w, h).into(vImage)
        }

        override fun toImageView() {
            val array = getArrayIds()
            var index = 0
            for (i in 0 until array.size) if (array[i] == id) index = i
            Navigator.to(SImageView(index, array))
        }

        override fun getSource() = id
    }
}
