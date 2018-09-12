package com.sup.dev.android.views.popup

import android.support.annotation.CallSuper
import android.support.v7.widget.CardView
import android.view.View
import android.widget.PopupWindow
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsView


abstract class Popup(private val view: View) : PopupWindow(SupAndroid.activity!!) {
    var isEnabled: Boolean = false
        private set
    //
    //  Getters
    //


    var isCancelable: Boolean = false
        private set

    constructor(layoutRes: Int) : this(ToolsView.inflate<View>(layoutRes)) {}

    init {

        val vCard = ToolsView.inflate<CardView>(R.layout.view_card_6dp)
        ToolsView.removeFromParent(view)
        vCard.addView(view)
        setBackgroundDrawable(null)
        contentView = vCard
        isOutsideTouchable = true
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
        if (view.measuredHeight < ToolsView.dpToPx(240f/*Запас, чтоб не обрезать 2 пикселя*/))
            height = Math.min(view.measuredHeight, view.measuredHeight)
        else
            height = Math.min(view.measuredHeight, ToolsView.dpToPx(200f))

        if (x > -1 && y > -1) {
            x -= width / 2
            y -= anchor.height

            val p = IntArray(2)
            anchor.getLocationOnScreen(p)
            if (height + (anchor.height + y) + p[1] > ToolsAndroid.getScreenH())
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
        this.isCancelable = cancelable
        return this as K
    }

    open fun <K : Popup> setEnabled(enabled: Boolean): K {
        this.isEnabled = enabled
        return this as K
    }

}
