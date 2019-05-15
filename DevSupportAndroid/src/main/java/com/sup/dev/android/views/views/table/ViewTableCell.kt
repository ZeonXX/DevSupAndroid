package com.sup.dev.android.views.views.table

import android.graphics.Bitmap
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsImagesLoader
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView

class ViewTableCell constructor(val vTableRow: ViewTableRow) : FrameLayout(vTableRow.context) {

    init {
        resetMinSizes()
        setBackgroundColor(ToolsResources.getColorAttr(R.attr.content_background))
        setPadding(ToolsView.dpToPx(8).toInt(), ToolsView.dpToPx(8).toInt(), ToolsView.dpToPx(8).toInt(), ToolsView.dpToPx(8).toInt())
        ToolsView.setOnClickCoordinates(this) { v, x, y -> vTableRow.vTable.onCellClicked.invoke(this, x, y) }
    }

    fun resetMinSizes() {
        minimumWidth = vTableRow.vTable.getMinCellW().toInt()
        minimumHeight = vTableRow.vTable.getMinCellH().toInt()
        requestLayout()
    }

    private fun resetView(view: View) {
        clear()
        addView(view)
        (view.layoutParams as LayoutParams).gravity = Gravity.CENTER
    }

    fun setContentText(text: String) {
        val vText: TextView = ToolsView.inflate(R.layout.z_text_body)
        vText.text = text
        resetView(vText)
    }

    fun setContentImage(bitmap: Bitmap) {
        val vImage: ImageView = ImageView(context)
        vImage.setImageBitmap(bitmap)
        resetView(vImage)
    }

    fun setContentImageId(imageId: Long) {
        val vImage: ImageView = ImageView(context)
        resetView(vImage)
        ToolsImagesLoader.load(imageId).into(vImage)
    }

    fun clear() {
        removeAllViews()
    }

    fun hasContent() = childCount > 0

}
