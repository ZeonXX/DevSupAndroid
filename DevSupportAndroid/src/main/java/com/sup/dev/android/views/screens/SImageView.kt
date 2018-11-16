package com.sup.dev.android.views.screens

import android.graphics.Bitmap
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.tools.*
import com.sup.dev.android.views.support.DrawableGif
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.java.tools.ToolsBytes
import com.sup.dev.java.tools.ToolsThreads


class SImageView private constructor(
        private val bitmap: Bitmap?,
        private val id: Long)
    : Screen(R.layout.screen_image_view) {

    constructor(bitmap: Bitmap) : this(bitmap, 0)

    constructor(id: Long) : this(null, id)

    init {

        val vImage: ImageView = findViewById(R.id.vImage)
        val vDownload: ViewIcon = findViewById(R.id.vDownload)

        vImage.isClickable = false
        vDownload.setOnClickListener { v -> download() }

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

    private fun download() {
        val dialog = ToolsView.showProgressDialog(R.string.app_downloading)
        ToolsThreads.thread {
            if (bitmap != null)
                ToolsStorage.saveImageInDownloadFolder(bitmap)
            else if (id > 0) {
                ToolsImagesLoader.load(id).into { bytes ->
                    if (!ToolsBytes.isGif(bytes))
                        ToolsStorage.saveImageInDownloadFolder(ToolsBitmap.decode(bytes)!!) { f -> ToolsToast.show(R.string.app_done) }
                    else
                        ToolsStorage.saveFileInDownloadFolder(bytes!!, ".gif", { f -> ToolsToast.show(R.string.app_done) }, { ToolsToast.show(R.string.error_permission_files) })

                }
            }
            dialog.hide()
            ToolsToast.show(R.string.app_downloaded)
        }

    }

}
