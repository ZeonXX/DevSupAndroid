package com.sup.dev.android.views.views.pager

import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsBitmap
import com.sup.dev.android.tools.ToolsImagesLoader
import com.sup.dev.android.tools.ToolsView
import java.lang.RuntimeException

class ViewPagerIndicatorImages @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : ViewPagerIndicatorViews(context, attrs) {


    var imageProvider: (Int, ImageView) -> Unit = { index,v ->throw RuntimeException("You must set provider") }

    override fun instanceView(index: Int): View {
        val v: View = ToolsView.inflate(context, R.layout.view_indicator_image)
        imageProvider.invoke(index, v.findViewById(R.id.vDevSupImage))
        return v
    }

    fun setImageBitmap(v: ImageView, bitmap: Bitmap) {
        v.setImageBitmap(ToolsBitmap.cropCenterSquare(ToolsBitmap.resize(bitmap, ToolsView.dpToPx(64).toInt())))
    }

    fun setImageId(v: ImageView, imageId:Long){
        ToolsImagesLoader.load(imageId).size(ToolsView.dpToPx(64).toInt(), ToolsView.dpToPx(64).toInt()).into(v)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)
        val count = childCount
        if (count == 0) return

        val selected = views[position]
        var old: View? = null
        val oldIndex = if (positionOffset > 0) position + 1 else position - 1
        if (positionOffset != 0f && oldIndex > -1 && oldIndex < count) {
            old = views[oldIndex]
        }

        for (i in 0 until count) {
            val vv = views[i]
            vv.alpha = 80f / 255f
        }

        if (positionOffset != 0f) {
            val arg = Math.abs(positionOffset)
            selected.alpha = (80 + ((255 - 80) * (1 - arg))) / 255f
            old?.alpha = (80 + ((255 - 80) * arg))  / 255f
        } else {
            selected.alpha = 1f
        }


    }

}