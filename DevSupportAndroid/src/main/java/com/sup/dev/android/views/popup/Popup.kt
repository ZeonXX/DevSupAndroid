package com.sup.dev.android.views.popup

import android.graphics.drawable.ColorDrawable
import android.support.annotation.CallSuper
import android.view.View
import android.widget.PopupWindow
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutCorned


abstract class Popup(private val view: View) : PopupWindow(SupAndroid.activity!!) {

    private var isEnabled: Boolean = true

    constructor(layoutRes: Int) : this(ToolsView.inflate<View>(layoutRes)) {}

    init {

        val vCorned = LayoutCorned(view.context)
        vCorned.setBackgroundColor(ToolsResources.getColorFromAttr(R.attr.popup_background))
        ToolsView.removeFromParent(view)
        vCorned.addView(view)
        setBackgroundDrawable(ColorDrawable(0x00000000))

        setOutsideTouchable(true)

        contentView = vCorned
        isFocusable = true

    }

    @CallSuper
    protected open fun onShow() {

    }

    @CallSuper
    protected open fun onHide() {

    }

    open fun <K : Popup> hide(): K {
        dismiss()
        return this as K
    }


    fun <K : Popup> show(anchor: View): K {
        return show(anchor, -1, -1)
    }

    fun <K : Popup> show(anchor: View, x: Int, y: Int): K {
        var x = x
        var y = y

        onShow()

        view.measure(View.MeasureSpec.makeMeasureSpec(ToolsAndroid.getScreenW(), View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(ToolsAndroid.getScreenH(), View.MeasureSpec.AT_MOST))
        width = view.measuredWidth
        if (view.measuredHeight < ToolsView.dpToPx(340/*Запас, чтоб не обрезать 2 пикселя*/))
            height = view.measuredHeight
        else
            height = Math.min(view.measuredHeight, ToolsView.dpToPx(300))

        if (x > -1 && y > -1) {
            x -= width / 2
            y -= anchor.height

            val location = IntArray(2)
            anchor.getLocationOnScreen(location)
            if (height + (anchor.height + y) + location[1] > ToolsAndroid.getScreenH())
                y += anchor.height

            showAsDropDown(anchor, x, y)
        } else {
            showAsDropDown(anchor)
        }

        return this as K
    }

    //
    //  Setters
    //

    open fun <K : Popup> setCancelable(cancelable: Boolean): K {
        isOutsideTouchable = !cancelable
        return this as K
    }

    open fun <K : Popup> setEnabled(enabled: Boolean): K {
        this.isEnabled = enabled
        return this as K
    }

}
