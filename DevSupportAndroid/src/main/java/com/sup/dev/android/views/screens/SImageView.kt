package com.sup.dev.android.views.screens

import android.graphics.Bitmap
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.image_loader.ImageLoaderId
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.tools.ToolsBitmap
import com.sup.dev.android.tools.ToolsStorage
import com.sup.dev.android.tools.ToolsToast
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.ViewGifImage
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.java.tools.ToolsThreads


class SImageView private constructor(private val bitmap: Bitmap?, private val id: Long, private val isGif: Boolean) : Screen(R.layout.screen_image_view) {

    constructor(bitmap: Bitmap) : this(bitmap, 0, false) {}

    constructor(id: Long) : this(null, id, false) {}

    constructor(id: Long, isGif: Boolean) : this(null, id, isGif) {}

    init {

        val vImage = findViewById<ViewGifImage>(R.id.image)
        val vDownload = findViewById<ViewIcon>(R.id.download)

        vImage.isClickable = false
        vDownload.setOnClickListener { v -> download() }

        if (bitmap != null)
            vImage.setImageBitmap(bitmap)
        else if (id > 0)
            vImage.init(if (isGif) 0 else id, if (isGif) id else 0)


    }

    private fun download() {
        val dialog = ToolsView.showProgressDialog(SupAndroid.TEXT_APP_DOWNLOADING)
        ToolsThreads.thread {
            if (bitmap != null)
                ToolsStorage.saveImageInDownloadFolder(bitmap, null!!)
            else if (id > 0) {
                ImageLoaderId.load(id) { bytes ->
                    if (!isGif)
                        ToolsStorage.saveImageInDownloadFolder(ToolsBitmap.decode(bytes)!!) { f -> ToolsToast.show(SupAndroid.TEXT_APP_DONE) }
                    else
                        ToolsStorage.saveFileInDownloadFolder(bytes, ".gif", { f -> ToolsToast.show(SupAndroid.TEXT_APP_DONE) }, { ToolsToast.show(SupAndroid.TEXT_ERROR_PERMISSION_READ_FILES) })
                }
            }
            dialog.hide()
            ToolsToast.show(SupAndroid.TEXT_APP_DOWNLOADED)
        }

    }

}
