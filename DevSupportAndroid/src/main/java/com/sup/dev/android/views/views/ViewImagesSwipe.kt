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
import com.sup.dev.android.views.support.adapters.recycler_view.RecyclerCardAdapter
import com.sup.dev.android.views.cards.Card
import com.sup.dev.android.views.screens.SImageView


class ViewImagesSwipe @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs) {

    private val adapter = RecyclerCardAdapter()

    init {
        adapter.setCardW(RecyclerView.LayoutParams.WRAP_CONTENT)
        layoutManager = LinearLayoutManager(context, GridLayoutManager.HORIZONTAL, false)
        setAdapter(adapter)
    }

    fun clear() {
        adapter.clear()
    }

    fun add(id: Long, w: Int = 0, h: Int = 0, onClick: (() -> Unit)? = null) {
        adapter.add(CardSwipeId(id, w, h, onClick))
    }

    fun add(bitmap: Bitmap, w: Int = 0, h: Int = 0, onClick: (() -> Unit)? = null) {
        adapter.add(CardSwipeBitmap(bitmap, w, h, onClick))
    }

    //
    //  Card
    //
    private abstract inner class CardSwipe(val onClick: (() -> Unit)?) : Card() {

        override fun getLayout(): Int {
            return R.layout.view_image_swipe_card
        }

        override fun bindView(view: View) {
            val vImage: ImageView = view.findViewById(R.id.vImage)
            view.setOnClickListener {
                if (onClick == null) toImageView()
                else onClick.invoke()
            }
            set(view, vImage)
        }

        abstract fun set(view: View, vImage: ImageView)

        abstract fun toImageView()

    }


    private inner class CardSwipeBitmap(val bitmap: Bitmap, val w: Int = 0, val h: Int = 0, onClick: (() -> Unit)?) : CardSwipe(onClick) {

        override fun set(view: View, vImage: ImageView) {
            view.layoutParams.width = if (w > 0) w else ViewGroup.LayoutParams.WRAP_CONTENT
            view.layoutParams.height = if (h > 0) h else ViewGroup.LayoutParams.WRAP_CONTENT
            vImage.setImageBitmap(bitmap)
        }

        override fun toImageView() {
            Navigator.to(SImageView(bitmap))
        }

    }

    private inner class CardSwipeId(val id: Long, val w: Int = 0, val h: Int = 0, onClick: (() -> Unit)?) : CardSwipe(onClick) {

        override fun set(view: View, vImage: ImageView) {
            ToolsImagesLoader.load(id).size(w, h).into(vImage)
        }

        override fun toImageView() {
            Navigator.to(SImageView(id))
        }

    }
}
