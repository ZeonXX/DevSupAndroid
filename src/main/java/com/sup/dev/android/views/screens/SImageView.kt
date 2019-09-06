package com.sup.dev.android.views.screens

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import androidx.viewpager.widget.ViewPager
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.tools.*
import com.sup.dev.android.views.cards.Card
import com.sup.dev.android.views.support.DrawableGif
import com.sup.dev.android.views.support.adapters.pager.PagerCardAdapter
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.android.views.views.layouts.LayoutZoom
import com.sup.dev.android.views.views.pager.ViewPagerIndicatorImages
import com.sup.dev.android.views.widgets.WidgetField
import com.sup.dev.java.tools.ToolsBytes
import com.sup.dev.java.tools.ToolsColor
import com.sup.dev.java.tools.ToolsThreads

class SImageView private constructor()
    : Screen(R.layout.screen_image_view) {

    private val vRoot: View = findViewById(R.id.vRoot)
    private val vCounterContainer: View = findViewById(R.id.vCounterContainer)
    private val vCounter: TextView = findViewById(R.id.vCounter)
    private val vPager: ViewPager = findViewById(R.id.vPager)
    private val vShare: ViewIcon = findViewById(R.id.vShare)
    private val vDownload: ViewIcon = findViewById(R.id.vDownload)
    private val vBack: ViewIcon = findViewById(R.id.vBack)
    private val vIndicator: ViewPagerIndicatorImages = findViewById(R.id.vIndicator)
    private val adapterIn: PagerCardAdapter = PagerCardAdapter()

    constructor(scrollTo: Int, bitmaps: Array<Bitmap>) : this() {
        for (b in bitmaps) adapterIn.add(Page(null, b, 0L))
        vPager.setCurrentItem(scrollTo, false)
        vCounterContainer.visibility = if (adapterIn.size() > 1) View.VISIBLE else View.GONE
        vIndicator.visibility = if (adapterIn.size() > 1) View.VISIBLE else View.GONE
        vIndicator.imageProvider = { index, v -> vIndicator.setImageBitmap(v, bitmaps[index]) }
        vIndicator.setPagerView(vPager)
    }

    constructor(scrollTo: Int, ids: Array<Long>) : this() {
        for (id in ids) adapterIn.add(Page(null, null, id))
        vPager.setCurrentItem(scrollTo, false)
        vCounterContainer.visibility = if (adapterIn.size() > 1) View.VISIBLE else View.GONE
        vIndicator.visibility = if (adapterIn.size() > 1) View.VISIBLE else View.GONE
        vIndicator.imageProvider = { index, v -> vIndicator.setImageId(v, ids[index]) }
        vIndicator.setPagerView(vPager)
    }

    constructor(scrollTo: Int, bytes: Array<ByteArray>) : this() {
        for (b in bytes) adapterIn.add(Page(b, null, 0L))
        vPager.setCurrentItem(scrollTo, false)
        vCounterContainer.visibility = if (adapterIn.size() > 1) View.VISIBLE else View.GONE
        vIndicator.visibility = if (adapterIn.size() > 1) View.VISIBLE else View.GONE
        vIndicator.imageProvider = { index, v -> vIndicator.setImageBitmap(v, ToolsBitmap.decode( bytes[index])!!) }
        vIndicator.setPagerView(vPager)
    }

    constructor(bitmap: Bitmap) : this() {
        adapterIn.add(Page(null, bitmap, 0L))
        vCounterContainer.visibility = if (adapterIn.size() > 1) View.VISIBLE else View.GONE
        vIndicator.visibility = View.GONE
    }

    constructor(id: Long) : this() {
        adapterIn.add(Page(null, null, id))
        vCounterContainer.visibility = if (adapterIn.size() > 1) View.VISIBLE else View.GONE
        vIndicator.visibility = View.GONE
    }

    constructor(bytes: ByteArray) : this() {
        adapterIn.add(Page(bytes, null, 0L))
        vCounterContainer.visibility = if (adapterIn.size() > 1) View.VISIBLE else View.GONE
        vIndicator.visibility = View.GONE
    }

    init {
        isNavigationVisible = false
        isNavigationAllowed = false
        isNavigationAnimation = false
        statusBarColor = ToolsResources.getColorAttr(R.attr.window_background)

        val color = ToolsColor.setAlpha(70, (vRoot.background as ColorDrawable).color)
        vDownload.setIconBackgroundColor(color)
        vShare.setIconBackgroundColor(color)
        vBack.setIconBackgroundColor(color)
        vCounterContainer.setBackgroundColor(color)

        vDownload.setOnClickListener { download() }
        vShare.setOnClickListener { share() }
        vPager.adapter = adapterIn
        vPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {

            override fun onPageScrollStateChanged(p0: Int) {

            }

            override fun onPageScrolled(p0: Int, p1: Float, p2: Int) {
                updateTitle()
            }

            override fun onPageSelected(p0: Int) {
                for (i in 0 until adapterIn.size()) (adapterIn[i] as Page).update()
            }

        })

        updateTitle()
    }

    private fun toggleInterface(){
        val toAlpha = vBack.visibility == View.VISIBLE

        ToolsView.alpha(vBack, toAlpha)
        ToolsView.alpha(vDownload, toAlpha)
        ToolsView.alpha(vShare, toAlpha)
        ToolsView.alpha(vCounterContainer, toAlpha)
        ToolsView.alpha(vIndicator, toAlpha || adapterIn.size() < 2)
    }

    private fun updateTitle() {
        vCounter.text = "${vPager.currentItem + 1} / ${adapterIn.size()}"
    }

    private fun download() {
        (adapterIn[vPager.currentItem] as Page).download()
    }

    private fun share() {
        (adapterIn[vPager.currentItem] as Page).share()
    }

    private inner class Page constructor(
            private val bytes: ByteArray?,
            private val bitmap: Bitmap?,
            private val id: Long
    ) : Card(R.layout.screen_image_view_page) {

        override fun bindView(view: View) {
            super.bindView(view)
            val vImage: ImageView = view.findViewById(R.id.vImage)
            val vZoom: LayoutZoom = view.findViewById(R.id.vZoom)

            vZoom.reset()
            vImage.isClickable = false
            vImage.setOnClickListener {
                toggleInterface()
            }

            if (bitmap != null)
                vImage.setImageBitmap(bitmap)
            else if (bytes != null) {
                vImage.setImageBitmap(ToolsBitmap.decode(bytes))
            } else if (id > 0)
                ToolsImagesLoader.load(id).into { bytes ->
                    if (bytes != null) {
                        if (ToolsBytes.isGif(bytes)) DrawableGif(bytes, vImage) { vImage.setImageDrawable(it) }
                        else vImage.setImageBitmap(ToolsBitmap.decode(bytes))
                    }
                }
        }

        fun download() {
            val dialog = ToolsView.showProgressDialog(SupAndroid.TEXT_APP_DOWNLOADING)
            ToolsThreads.thread {
                if (bitmap != null)
                    ToolsStorage.saveImageInDownloadFolder(bitmap)
                else if (id > 0) {
                    ToolsImagesLoader.load(id).into { bytes ->
                        if (!ToolsBytes.isGif(bytes))
                            ToolsStorage.saveImageInDownloadFolder(ToolsBitmap.decode(bytes)!!) { }
                        else
                            ToolsStorage.saveFileInDownloadFolder(bytes!!, ".gif", { }, { ToolsToast.show(SupAndroid.TEXT_ERROR_PERMISSION_FILES) })

                    }
                }
                dialog.hide()
                ToolsToast.show(SupAndroid.TEXT_APP_DOWNLOADED)
            }

        }

        fun share() {

            WidgetField()
                    .setHint(SupAndroid.TEXT_APP_MESSAGE)
                    .setOnCancel(SupAndroid.TEXT_APP_CANCEL)
                    .setOnEnter(SupAndroid.TEXT_APP_SHARE) { _, text ->
                        ToolsThreads.thread {
                            if (bitmap != null) {
                                ToolsIntent.shareImage(bitmap, text)
                            } else if (id > 0) {
                                val dialog = ToolsView.showProgressDialog()
                                ToolsImagesLoader.load(id).into { bytes ->
                                    ToolsThreads.thread {
                                        val bm = ToolsBitmap.decode(bytes)
                                        ToolsThreads.main {
                                            dialog.hide()
                                            if(bm == null){
                                                ToolsToast.show(SupAndroid.TEXT_ERROR_CANT_LOAD_IMAGE)
                                                return@main
                                            }
                                            ToolsIntent.shareImage(bm, text)
                                        }
                                    }
                                }
                            }
                        }
                    }
                    .asSheetShow()



        }
    }

}
