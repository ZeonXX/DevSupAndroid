package com.sup.dev.android.views.popup

import android.graphics.drawable.ColorDrawable
import androidx.annotation.CallSuper
import android.view.View
import android.widget.PopupWindow
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutCorned
import com.sup.dev.java.libs.debug.log


@Suppress("UNCHECKED_CAST")
abstract class Popup(private val view: View) : PopupWindow(SupAndroid.activity!!) {

    private var isEnabled: Boolean = true
    private var anchor:View?= null
    private var targetX = 0
    private var targetY = 0
    private var isUpdating = false

    constructor(layoutRes: Int) : this(ToolsView.inflate<View>(layoutRes)) {}

    init {
        val vCorned = LayoutCorned(view.context)
        vCorned.setBackgroundColor(ToolsResources.getColorAttr(R.attr.popup_background))
        ToolsView.removeFromParent(view)
        vCorned.addView(view)
        setBackgroundDrawable(ColorDrawable(0x00000000))

        setOutsideTouchable(true)

        contentView = vCorned
        isFocusable = true


        view.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            if (!isShowing) return@addOnLayoutChangeListener
            updateWidow()
        }


    }

    @CallSuper
    protected open fun onShow() {

    }

    @CallSuper
    protected open fun onHide() {

    }

    open fun <K : Popup> hidePopup(): K {
        dismiss()
        return this as K
    }


    fun <K : Popup> show(anchor: View): K {
        return show(anchor, -1, -1)
    }

    fun <K : Popup> show(anchor: View, x: Int, y: Int): K {
        this.targetX = x
        this.targetY = y
        this.anchor = anchor
        onShow()
        updateWidow()
        return this as K
    }

    private fun updateWidow() {
        val anchor = this.anchor ?: return
        if(isUpdating) return
        isUpdating = true

        var xV = targetX
        var yV = targetY

        view.measure(View.MeasureSpec.makeMeasureSpec(ToolsAndroid.getScreenW(), View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(ToolsAndroid.getScreenH(), View.MeasureSpec.AT_MOST))
        width = view.measuredWidth
        if (view.measuredHeight < ToolsView.dpToPx(540/*Запас, чтоб не обрезать 2 пикселя*/))
            height = view.measuredHeight
        else
            height = ToolsView.dpToPx(500).toInt()

        dismiss()

        if (xV > -1 && yV > -1) {
            xV -= width / 2
            yV -= anchor.height

            val location = IntArray(2)
            anchor.getLocationOnScreen(location)
            if (height + (anchor.height + yV) + location[1] > ToolsAndroid.getScreenH())
                yV += anchor.height

            showAsDropDown(anchor, xV, yV)
        } else {
            showAsDropDown(anchor)
        }
        isUpdating = false

    }

    //
    //  Setters
    //

    open fun <K : Popup> setPopupCancelable(cancelable: Boolean): K {
        isOutsideTouchable = !cancelable
        return this as K
    }

    open fun <K : Popup> setPopupEnabled(enabled: Boolean): K {
        this.isEnabled = enabled
        return this as K
    }

}
