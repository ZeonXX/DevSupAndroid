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
    private var onClickGlobal:(CardSwipe<Any>) -> Boolean = {false}

    init {
        adapter.setCardW(ViewGroup.LayoutParams.WRAP_CONTENT)
        adapter.setCardH(ViewGroup.LayoutParams.MATCH_PARENT)
        layoutManager = LinearLayoutManager(context, GridLayoutManager.HORIZONTAL, false)
        setAdapter(adapter)
    }

    fun clear() {
        adapter.clear()
    }

    fun add(id: Long, fullId:Long = id, onClick: ((Long) -> Unit)? = null, onLongClick: ((Long) -> Unit)? = null) {
        if (!adapter.isEmpty) adapter.add(CardSpace(ToolsView.dpToPx(4).toInt()))
        adapter.add(CardSwipeId(id, fullId, onClick, onLongClick))
    }

    fun add(bitmap: Bitmap, onClick: ((Bitmap) -> Unit)? = null, onLongClick: ((Bitmap) -> Unit)? = null) {
        if (!adapter.isEmpty) adapter.add(CardSpace(ToolsView.dpToPx(4).toInt()))
        adapter.add(CardSwipeBitmap(bitmap, onClick, onLongClick))
    }

    fun remove(index:Int){
        adapter.remove(adapter[CardSwipe::class][index])
    }

    fun scrollToEnd(){
        scrollToPosition(adapter.size()-1)
    }

    //
    //  Setters
    //

    fun setOnClickGlobal(onClickGlobal:(CardSwipe<Any>) -> Boolean){
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
                if(onClickGlobal(this as CardSwipe<Any>)) return@setOnClickListener
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
            onClick: ((Long) -> Unit)?,
            onLongClick: ((Long) -> Unit)? = null
    ) : CardSwipe<Long>(onClick, onLongClick) {

        override fun set(view: View, vImage: ImageView) {
            ToolsImagesLoader.load(id).into(vImage)
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
