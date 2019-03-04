package com.sup.dev.android.views.screens

import android.graphics.Bitmap
import android.graphics.Rect
import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.views.dialogs.DialogWidget
import com.sup.dev.android.views.views.cropper.ViewCropImage
import com.sup.dev.android.views.widgets.WidgetProgressTransparent


class SCrop(
        bitmap: Bitmap,
        aw: Int,
        ah: Int,
        private val onCrop: Function2<SCrop, Bitmap, Unit>?
) : Screen(R.layout.screen_image_crop) {


    private var onHide: () -> Unit = {}
    private val vCropImageView: ViewCropImage = findViewById(R.id.vCrop)
    private val vFinish: View = findViewById(R.id.vFab)
    private val vAll: View = findViewById(R.id.vAll)

    private var autoBackOnCrop = true
    private var locked: Boolean = false

    private var dialogProgress: DialogWidget? = null

    constructor(bitmap: Bitmap, onCrop: Function2<SCrop, Bitmap, Unit>) : this(bitmap, 0, 0, onCrop) {}

    init {

        isBottomNavigationVisible = false
        isBottomNavigationAllowed = false
        isBottomNavigationAnimation = false

        if (aw > 0 && ah > 0) vCropImageView.setAspectRatio(aw, ah)
        vCropImageView.setImageBitmap(bitmap)

        vAll.setOnClickListener { v -> vCropImageView.cropRect = Rect(0, 0, bitmap.width, bitmap.height) }

        vFinish.setOnClickListener { v ->
            if (onCrop != null) {
                if (autoBackOnCrop)
                    Navigator.back()
                else
                    setLock(false)

                onCrop.invoke(this, vCropImageView.croppedImage!!)
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

    override fun onDestroy() {
        super.onDestroy()
        onHide.invoke()
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

    fun setOnHide(onHide:()->Unit):SCrop{
        this.onHide = onHide
        return this
    }

}
