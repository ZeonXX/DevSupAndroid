package com.sup.dev.android.views.views.pager

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsBitmap
import com.sup.dev.android.tools.ToolsView
import java.lang.RuntimeException

class ViewPagerIndicatorImages @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPagerIndicatorViews(context, attrs) {


    var imageProvider: (Int, ImageView) -> Unit = { index,v ->throw RuntimeException("You must set provider") }

    override fun instanceView(index: Int): View {
        val v: ImageView = ToolsView.inflate(context, R.layout.view_indicator_image)
        imageProvider.invoke(index, v)
        return v
    }

    fun setImageBitmap(v: ImageView, bitmap: Bitmap) {
        v.setImageBitmap(ToolsBitmap.cropCenterSquare(ToolsBitmap.resize(bitmap, 64)))
    }

}