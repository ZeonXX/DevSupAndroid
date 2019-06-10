package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsImagesLoader
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.screens.SImageView
import com.sup.dev.android.views.views.layouts.LayoutCorned


class ViewImagesContainer @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    private val rowH = ToolsView.dpToPx(108).toInt()
    private val items = ArrayList<Item>()
    private var itemIndex = 0

    init {
        orientation = VERTICAL
    }

    fun add(vararg bitmaps: Bitmap) {
        for (bitmap in bitmaps) {
            val item = ItemBitmap(bitmap)
            items.add(item)
        }
        rebuild()
    }

    fun add(vararg ids: Long) {
        for (id in ids) {
            val item = ItemId(id)
            items.add(item)
        }
        rebuild()
    }

    fun clear() {
        items.clear()
        rebuild()
    }

    fun rebuild() {
        itemIndex = 0
        removeAllViews()

        if (items.size == 1) {
            addView(items[0].vContainer)
        }
        if (items.size == 2) {
            addLinear()
            addItem()
            addItem()
        }
        if (items.size == 3) {
            addLinear()
            addItem()
            addItem()
            addItem()
        }
        if (items.size == 4) {
            addLinear()
            addItem()
            addItem()
            addLinear()
            addItem()
            addItem()
        }
        if (items.size == 5) {
            addLinear()
            addItem()
            addItem()
            addLinear()
            addItem()
            addItem()
            addItem()
        }

        if (items.size == 6) {
            addLinear()
            addItem()
            addItem()
            addItem()
            addItem()
            addLinear()
            addItem()
            addItem()
        }

        if (items.size == 7) {
            addLinear()
            addItem()
            addItem()
            addItem()
            addItem()
            addLinear()
            addItem()
            addItem()
            addItem()
        }

        if (items.size == 8) {
            addLinear()
            addItem()
            addItem()
            addLinear()
            addItem()
            addItem()
            addItem()
            addLinear()
            addItem()
            addItem()
            addItem()
        }
        if (items.size == 9) {
            addLinear()
            addItem()
            addItem()
            addLinear()
            addItem()
            addItem()
            addItem()
            addLinear()
            addItem()
            addItem()
            addItem()
            addItem()
        }
        if (items.size == 10) {
            addLinear()
            addItem()
            addItem()
            addItem()
            addLinear()
            addItem()
            addItem()
            addItem()
            addItem()
            addLinear()
            addItem()
            addItem()
            addItem()
        }

        if (layoutParams != null) layoutParams.height = childCount * rowH
    }

    private fun getBitmapsArray(): Array<Bitmap> {
        val list = ArrayList<Bitmap>()
        for (i in items) if (i is ItemBitmap) list.add(i.bitmap)
        return list.toTypedArray()
    }

    private fun getIdsArray(): Array<Long> {
        val list = ArrayList<Long>()
        for (i in items) if (i is ItemId) list.add(i.id)
        return list.toTypedArray()
    }

    private fun addLinear(): LinearLayout {
        val vLinear = LinearLayout(context)
        vLinear.orientation = HORIZONTAL
        addView(vLinear)
        (vLinear.layoutParams as LayoutParams).weight = 1f
        (vLinear.layoutParams as LayoutParams).topMargin = if (childCount > 1) ToolsView.dpToPx(2).toInt() else 0
        return vLinear
    }

    private fun addItem() {
        val vLinear = getChildAt(childCount - 1) as LinearLayout
        val item = items[itemIndex++]
        ToolsView.removeFromParent(item.vContainer)
        vLinear.addView(item.vContainer)
        (item.vContainer.layoutParams as LayoutParams).weight = 1f
        (item.vContainer.layoutParams as LayoutParams).leftMargin = if (vLinear.childCount > 1) ToolsView.dpToPx(2).toInt() else 0
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        rebuild()
    }

    private abstract inner class Item {

        val vContainer: LayoutCorned = ToolsView.inflate(R.layout.view_images_container_item)
        val vImage: ImageView = vContainer.findViewById(R.id.vSupportImageView)

    }

    private inner class ItemBitmap(
            val bitmap: Bitmap
    ) : Item() {

        init {
            vImage.setImageBitmap(bitmap)
            vImage.setOnClickListener {
                SImageView(items.indexOf(this), getBitmapsArray())
            }
        }

    }

    private inner class ItemId(
            val id: Long
    ) : Item() {

        init {
            ToolsImagesLoader.load(id).into(vImage)
            vImage.setOnClickListener {
                SImageView(items.indexOf(this), getIdsArray())
            }
        }

    }
}