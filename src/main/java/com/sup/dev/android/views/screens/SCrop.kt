package com.sup.dev.android.views.screens

import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.dialogs.DialogWidget
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.android.views.views.cropper.ViewCropImage
import com.sup.dev.android.views.widgets.WidgetProgressTransparent
import com.sup.dev.java.tools.ToolsColor

class SCrop(
        bitmap: Bitmap,
        aw: Int,
        ah: Int,
        private val onCrop: ((SCrop, Bitmap, Int, Int, Int, Int) -> Unit)?
) : Screen(R.layout.screen_image_crop) {

    private val vRoot: View = findViewById(R.id.vRoot)
    private val vCropImageView: ViewCropImage = findViewById(R.id.vCrop)
    private val vFinish: View = findViewById(R.id.vFab)
    private val vAll: View = findViewById(R.id.vAll)
    private val vBack: ViewIcon = findViewById(R.id.vBack)

    private var autoBackOnCrop = true
    private var locked: Boolean = false

    private var dialogProgress: DialogWidget? = null

    constructor(bitmap: Bitmap, onCrop: ((SCrop, Bitmap, Int, Int, Int, Int) -> Unit)) : this(bitmap, 0, 0, onCrop) {}

    init {

        isBottomNavigationVisible = false
        isBottomNavigationAllowed = false
        isBottomNavigationAnimation = false
        statusBarColor = ToolsResources.getColor(R.color.black)

        val color = ToolsColor.setAlpha(70, (vRoot.background as ColorDrawable).color)
        vBack.setIconBackgroundColor(color)

        if (aw > 0 && ah > 0) vCropImageView.setAspectRatio(aw, ah)
        vCropImageView.setImageBitmap(bitmap)

        vAll.setOnClickListener { v -> vCropImageView.cropRect = Rect(0, 0, bitmap.width, bitmap.height) }

        vFinish.setOnClickListener { v ->
            if (onCrop != null) {
                if (autoBackOnCrop)
                    Navigator.back()
                else
                    setLock(false)

                val cropPoints = vCropImageView.cropPoints
                onCrop.invoke(this, vCropImageView.croppedImage!!, cropPoints[0].toInt(), cropPoints[1].toInt(), (cropPoints[2] - cropPoints[0]).toInt(), (cropPoints[5] - cropPoints[1]).toInt())
            }
        }
    }

    fun setLock(b: Boolean): SCrop {
        locked = b
        vFinish.isEnabled = !b
        vAll.isEnabled = !b
        if (b) {
            if (locked) dialogProgress = WidgetProgressTransparent().asDialogShow()
        } else {
            if (dialogProgress != null) {
                dialogProgress!!.hide()
                dialogProgress = null
            }
        }
        return this
    }

    fun setAutoBackOnCrop(autoBackOnCrop: Boolean): SCrop {
        this.autoBackOnCrop = autoBackOnCrop
        return this
    }

    override fun onBackPressed(): Boolean {
        return locked
    }

    fun back() {
        Navigator.back()
    }

}
