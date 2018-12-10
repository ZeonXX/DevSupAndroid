package com.sup.dev.android.views.screens

import android.graphics.Bitmap
import android.support.v4.view.ViewPager
import android.view.View
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.tools.*
import com.sup.dev.android.views.cards.Card
import com.sup.dev.android.views.support.DrawableGif
import com.sup.dev.android.views.support.adapters.pager.PagerCardAdapter
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.android.views.views.layouts.LayoutZoom
import com.sup.dev.java.tools.ToolsBytes
import com.sup.dev.java.tools.ToolsThreads


class SImageView private constructor()
    : Screen(R.layout.screen_image_view) {

    private val vPager: ViewPager = findViewById(R.id.vPager)
    private val vDownload: ViewIcon = findViewById(R.id.vDownload)
    private val adapterIn: PagerCardAdapter = PagerCardAdapter()

    constructor(vararg bitmaps: Bitmap) : this() {
        for (b in bitmaps) adapterIn.add(Page(b, 0L))
    }

    constructor(vararg ids: Long) : this() {
        for (id in ids) adapterIn.add(Page(null, id))
    }

    init {
        isBottomNavigationVisible = false
        isBottomNavigationAllowed = false
        isBottomNavigationAnimation = false

        vDownload.setOnClickListener { download() }
        vPager.adapter = adapterIn
        vPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(p0: Int) {
                for (i in 0 until adapterIn.size()) (adapterIn[i] as Page).update()
            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
            }

            override fun onPageSelected(p0: Int) {
            }

        })
    }

    private fun download() {
        (adapterIn[vPager.currentItem] as Page).download()
    }


    private class Page constructor(
            private val bitmap: Bitmap?,
            private val id: Long
    ) : Card() {

        override fun getLayout() = R.layout.screen_image_view_page

        override fun bindView(view: View) {
            val vImage: ImageView = view.findViewById(R.id.vImage)
            val vZoom: LayoutZoom = view.findViewById(R.id.vZoom)

            vZoom.reset()
            vImage.isClickable = false

            if (bitmap != null)
                vImage.setImageBitmap(bitmap)
            else if (id > 0)
                ToolsImagesLoader.load(id).into { bytes ->
                    if (bytes != null) {
                        if (ToolsBytes.isGif(bytes)) DrawableGif(bytes, vImage) { vImage.setImageDrawable(it) }
                        else vImage.setImageBitmap(ToolsBitmap.decode(bytes))
                    }
                }
        }

        fun download() {
            val dialog = ToolsView.showProgressDialog(R.string.app_downloading)
            ToolsThreads.thread {
                if (bitmap != null)
                    ToolsStorage.saveImageInDownloadFolder(bitmap)
                else if (id > 0) {
                    ToolsImagesLoader.load(id).into { bytes ->
                        if (!ToolsBytes.isGif(bytes))
                            ToolsStorage.saveImageInDownloadFolder(ToolsBitmap.decode(bytes)!!) { }
                        else
                            ToolsStorage.saveFileInDownloadFolder(bytes!!, ".gif", { }, { ToolsToast.show(R.string.error_permission_files) })

                    }
                }
                dialog.hide()
                ToolsToast.show(R.string.app_downloaded)
            }

        }
    }

}
