package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.libs.image_loader.ImageLoader
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.screens.SImageView
import com.sup.dev.android.views.views.layouts.LayoutCorned
import com.sup.dev.java.classes.items.Item2

class ViewImagesContainer @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null
) : LinearLayout(context, attrs) {

    companion object {

        private val cash = ArrayList<LayoutCorned>()

        private fun putToCash(v: LayoutCorned) {
            cash.add(v)
        }

        private fun getViewFromCash(): LayoutCorned? {
            if (cash.isEmpty()) return null
            else return ToolsView.removeFromParent(cash.removeAt(0))
        }

    }

    private val rowH = ToolsView.dpToPx(108).toInt()
    private val itemW = ToolsView.dpToPx(108).toInt()
    private val items = ArrayList<Item<out Any>>()
    private var itemIndex = 0
    private var onClickGlobal: (Item<Any>) -> Boolean = { false }

    init {
        orientation = VERTICAL
    }

    fun add(vararg drawable: Drawable, onClick: ((Drawable) -> Unit)? = null, onLongClick: ((Drawable) -> Unit)? = null) {
        for (i in drawable) items.add(ItemDrawable(i, onClick, onLongClick))
        rebuild()
    }

    fun add(vararg uris: Uri, onClick: ((Uri) -> Unit)? = null, onLongClick: ((Uri) -> Unit)? = null) {
        for (i in uris) items.add(ItemUri(i, onClick, onLongClick))
        rebuild()
    }

    fun add(vararg bitmaps: Bitmap, onClick: ((Bitmap) -> Unit)? = null, onLongClick: ((Bitmap) -> Unit)? = null) {
        for (bitmap in bitmaps) items.add(ItemBitmap(bitmap, onClick, onLongClick))
        rebuild()
    }

    fun add(id: Long, fullId: Long = id, w: Int = 0, h: Int = 0, onClick: ((Item2<Long, Long>) -> Unit)? = null, onLongClick: ((Item2<Long, Long>) -> Unit)? = null) {
        items.add(ItemId(id, fullId, w, h, onClick, onLongClick))
        rebuild()
    }

    fun clear() {
        for (i in items) {
            i.vImage.setImageDrawable(null)
            putToCash(i.vContainer)
        }
        items.clear()
        rebuild()
    }

    fun rebuild() {
        itemIndex = 0
        removeAllViews()

        when {
            items.size == 1 -> addView(items[0].vContainer)
            items.size == 2 -> addItems(2)
            items.size == 3 -> addItems(3)
            items.size == 4 -> {;addItems(2);addItems(2); }
            items.size == 5 -> {;addItems(2);addItems(3); }
            items.size == 6 -> {;addItems(4);addItems(2); }
            items.size == 7 -> {;addItems(4);addItems(3); }
            items.size == 8 -> {;addItems(2);addItems(3);addItems(3); }
            items.size == 9 -> {;addItems(2);addItems(3);addItems(4); }
            items.size == 10 -> {;addItems(3);addItems(4);addItems(3); }
        }

        if (layoutParams != null) layoutParams.height = childCount * rowH
    }

    private fun getBitmapsArray(): Array<Bitmap> {
        val list = ArrayList<Bitmap>()
        for (i in items) if (i is ItemBitmap) list.add(i.bitmap)
        return list.toTypedArray()
    }

    private fun getDrawablesArray(): Array<Drawable> {
        val list = ArrayList<Drawable>()
        for (i in items) if (i is ItemDrawable) list.add(i.drawable)
        return list.toTypedArray()
    }

    private fun getUriArray(): Array<Uri> {
        val list = ArrayList<Uri>()
        for (i in items) if (i is ItemUri) list.add(i.uri)
        return list.toTypedArray()
    }

    private fun getIdsArray(): Array<Long> {
        val list = ArrayList<Long>()
        for (i in items) if (i is ItemId) list.add(i.fullId)
        return list.toTypedArray()
    }

    private fun addItems(count: Int) {

        val vLinear = LinearLayout(context)
        vLinear.orientation = HORIZONTAL
        addView(vLinear)
        (vLinear.layoutParams as LayoutParams).weight = 1f
        (vLinear.layoutParams as LayoutParams).topMargin = if (childCount > 1) ToolsView.dpToPx(2).toInt() else 0

        for (i in 0 until count) {
            val vLinearV = getChildAt(childCount - 1) as LinearLayout
            val item = items[itemIndex++]
            ToolsView.removeFromParent(item.vContainer)
            vLinearV.addView(item.vContainer)
            (item.vContainer.layoutParams as LayoutParams).weight = 1f
            (item.vContainer.layoutParams as LayoutParams).leftMargin = if (vLinearV.childCount > 1) ToolsView.dpToPx(2).toInt() else 0
            (item.vContainer.layoutParams as LayoutParams).width = ViewGroup.LayoutParams.MATCH_PARENT
            (item.vContainer.layoutParams as LayoutParams).height = rowH
        }
    }

    override fun setLayoutParams(params: ViewGroup.LayoutParams?) {
        super.setLayoutParams(params)
        rebuild()
    }

    @Suppress("UNCHECKED_CAST")
    private abstract inner class Item<K>(
            val onClick: ((K) -> Unit)?,
            val onLongClick: ((K) -> Unit)?
    ) {

        val vContainer: LayoutCorned = getViewFromCash() ?: ToolsView.inflate(R.layout.view_images_container_item)
        val vImage: ImageView = vContainer.findViewById(R.id.vSupportImageView)

        init {
            vImage.setOnClickListener {
                when {
                    onClickGlobal(this as Item<Any>) -> return@setOnClickListener
                    onClick == null -> toImageView()
                    else -> onClick.invoke(getSource())
                }
            }
            if (onLongClick != null)
                vImage.setOnLongClickListener {
                    onLongClick.invoke(getSource())
                    true
                }
        }

        abstract fun toImageView()

        abstract fun getSource(): K

    }

    private inner class ItemBitmap(
            val bitmap: Bitmap,
            onClick: ((Bitmap) -> Unit)?,
            onLongClick: ((Bitmap) -> Unit)? = null
    ) : Item<Bitmap>(onClick, onLongClick) {

        init {
            vImage.setImageBitmap(bitmap)
        }

        override fun toImageView() {
            val array = getBitmapsArray()
            var index = 0
            for (i in 0 until array.size) if (array[i] == bitmap) index = i
            Navigator.to(SImageView(index, array))
        }

        override fun getSource() = bitmap

    }

    private inner class ItemDrawable(
            val drawable: Drawable,
            onClick: ((Drawable) -> Unit)?,
            onLongClick: ((Drawable) -> Unit)? = null
    ) : Item<Drawable>(onClick, onLongClick) {

        init {
            vImage.setImageDrawable(drawable)
        }

        override fun toImageView() {
            //  val array = getDrawablesArray()
            //  var index = 0
            //  for (i in 0 until array.size) if (array[i] == drawable) index = i
            //  Navigator.to(SImageView(index, array))
        }

        override fun getSource() = drawable

    }

    private inner class ItemUri(
            val uri: Uri,
            onClick: ((Uri) -> Unit)?,
            onLongClick: ((Uri) -> Unit)? = null
    ) : Item<Uri>(onClick, onLongClick) {

        init {
            vImage.setImageURI(uri)
        }

        override fun toImageView() {
            // val array = getUriArray()
            // var index = 0
            // for (i in 0 until array.size) if (array[i] == uri) index = i
            // Navigator.to(SImageView(index, array))
        }

        override fun getSource() = uri

    }

    private inner class ItemId(
            val id: Long,
            val fullId: Long,
            val w: Int,
            val h: Int,
            onClick: ((Item2<Long, Long>) -> Unit)?,
            onLongClick: ((Item2<Long, Long>) -> Unit)? = null
    ) : Item<Item2<Long, Long>>(onClick, onLongClick) {

        init {
            ImageLoader.load(id).size(w, h).into(vImage)
        }

        override fun toImageView() {
            toImageView(fullId)
        }

        override fun getSource() = Item2(id, fullId)

    }

    //
    //  To Image View
    //

    fun toImageView(fullId: Long) {
        val array = getIdsArray()
        var index = 0
        for (i in 0 until array.size) if (array[i] == fullId) index = i
        Navigator.to(SImageView(index, array))
    }

}