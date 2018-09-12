package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.Bitmap
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter
import com.sup.dev.android.views.cards.Card


class ViewImagesSwipe @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : RecyclerView(context, attrs) {

    private val adapter = RecyclerCardAdapter()

    init {

        layoutManager = LinearLayoutManager(context, GridLayoutManager.HORIZONTAL, false)
        setAdapter(adapter)
    }

    fun add(image: Bitmap) {
        adapter.add(CardSwipe(image))
    }

    //
    //  Card
    //

    private inner class CardSwipe(val bitmap: Bitmap?) : Card() {

        override fun getLayout(): Int {
            return R.layout.view_image_swipe_card
        }

        override fun bindView(view: View) {
            val vImage = view.findViewById<ImageView>(R.id.vImage)

            vImage.setImageBitmap(bitmap)
        }
    }
}
