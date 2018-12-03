package com.sup.dev.android.views.widgets

import android.graphics.Bitmap
import android.provider.MediaStore
import android.support.v7.widget.GridLayoutManager
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.*
import com.sup.dev.android.views.support.adapters.recycler_view.RecyclerCardAdapter
import com.sup.dev.android.views.cards.Card
import com.sup.dev.android.views.dialogs.DialogSheetWidget
import com.sup.dev.android.views.dialogs.DialogWidget
import com.sup.dev.java.libs.debug.Debug
import com.sup.dev.java.tools.ToolsBytes
import com.sup.dev.java.tools.ToolsFiles
import com.sup.dev.java.tools.ToolsNetwork
import com.sup.dev.java.tools.ToolsThreads
import java.io.File
import java.io.IOException


open class WidgetChooseImage : WidgetRecycler() {

    private val DP = ToolsView.dpToPx(1)
    private val myAdapter: RecyclerCardAdapter = RecyclerCardAdapter()

    private var onSelected: (WidgetChooseImage, ByteArray) -> Unit = { widgetChooseImage, bytes -> }
    private var imagesLoaded: Boolean = false
    private var spanCount = 3

    init {
        val vFabGalleryContainer: View = ToolsView.inflate(R.layout.view_fab)
        val vFabLinkContainer: View = ToolsView.inflate(R.layout.view_fab)
        val vFabGallery: ImageView = vFabGalleryContainer.findViewById(R.id.vFab)
        val vFabLink: ImageView = vFabLinkContainer.findViewById(R.id.vFab)
        vContainer.addView(vFabGalleryContainer)
        vContainer.addView(vFabLinkContainer)

        (vFabLinkContainer.getLayoutParams() as ViewGroup.MarginLayoutParams).rightMargin = ToolsView.dpToPx(72)

        spanCount = if (ToolsAndroid.isScreenPortrait()) 3 else 6
        vRecycler.layoutManager = GridLayoutManager(view.context, spanCount)

        vFabGallery.setImageResource(R.drawable.ic_landscape_white_24dp)
        vFabLink.setImageResource(R.drawable.ic_insert_link_white_24dp)
        vFabGallery.setOnClickListener { v -> openGallery() }
        vFabLink.setOnClickListener { v -> showLink() }

        setAdapter<WidgetRecycler>(myAdapter)
    }

    override fun onShow() {
        super.onShow()
        loadImages()

        (vRecycler.layoutParams as ViewGroup.MarginLayoutParams).setMargins(0, 0, 0, 0)
        vRecycler.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT

        if (viewWrapper is DialogWidget)
            (vRecycler.layoutParams as ViewGroup.MarginLayoutParams).setMargins(ToolsView.dpToPx(8), ToolsView.dpToPx(2), ToolsView.dpToPx(8), 0)
        else if (viewWrapper is DialogSheetWidget)
            vRecycler.layoutParams.height = ToolsView.dpToPx(320)
    }

    private fun loadImages() {
        if (imagesLoaded) return

        ToolsPermission.requestReadPermission({
            imagesLoaded = true
            val projection = arrayOf(MediaStore.Images.ImageColumns.DATA)
            val cursor = SupAndroid.appContext!!.contentResolver.query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    projection, null, null,
                    MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC")

            while (cursor!!.moveToNext()) myAdapter.add(CardImage(File(cursor.getString(0))))
        }, {
            ToolsToast.show(R.string.error_permission_files)
            hide()
        })


    }

    private fun loadLink(link: String) {
        var progress = ToolsView.showProgressDialog()
        ToolsNetwork.getBytesFromURL(link, onResult = {
            progress.hide()
            if (it == null || !ToolsBytes.isImage(it)) ToolsToast.show(R.string.error_cant_load_image)
            else onSelected(it)
        })
    }

    private fun showLink() {
        WidgetField()
                .setMediaCallback { w, s ->
                    w.hide()
                    loadLink(s)
                }
                .enableFastCopy()
                .setHint(R.string.app_link)
                .setOnEnter(R.string.app_choose) { w, s -> loadLink(s) }
                .setOnCancel(R.string.app_cancel)
                .asSheetShow()

    }

    private fun openGallery() {
        ToolsBitmap.getFromGallery({ bytes ->
            try {
                onSelected(bytes)
            } catch (e: IOException) {
                Debug.log(e)
                ToolsToast.show(R.string.error_cant_load_image)
            }
        })
    }

    private fun onSelected(bytes: ByteArray) {
        onSelected.invoke(this, bytes)
        hide()
    }


    //
    //  Setters
    //

    fun setOnSelected(onSelected: (WidgetChooseImage, ByteArray) -> Unit): WidgetChooseImage {
        this.onSelected = onSelected
        return this
    }

    fun setOnSelectedBitmap(callback: (WidgetChooseImage, Bitmap) -> Unit): WidgetChooseImage {
        this.onSelected = { w, bytes -> callback.invoke(this, ToolsBitmap.decode(bytes)!!) }
        return this
    }

    //
    //  Card
    //

    private inner class CardImage(private val file: File) : Card() {

        override fun getLayout(): Int {
            return R.layout.sheet_choose_image_card
        }

        override fun bindView(view: View) {
            val vImage = view.findViewById<ImageView>(R.id.vImage)
            vImage.setOnClickListener { v -> onClick() }
            ToolsImagesLoader.load(file).size(512, 512).cropSquare().into(vImage)

            val index = adapter!!.indexOf(this)
            val arg = index % spanCount
            view.setPadding(if (arg == 0) 0 else DP, if (index < spanCount) 0 else DP, if (arg == spanCount-1) 0 else DP, DP)

        }

        fun onClick() {
            val d = ToolsView.showProgressDialog()
            ToolsThreads.thread {
                try {
                    val bytes = ToolsFiles.readFile(file)
                    ToolsThreads.main {
                        d.hide()
                        onSelected(bytes)
                    }
                } catch (e: Exception) {
                    Debug.log(e)
                    ToolsThreads.main { d.hide() }
                    ToolsToast.show(R.string.error_cant_load_image)
                }
            }
        }

    }

}
