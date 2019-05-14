package com.sup.dev.android.views.dialogs

import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatDialog
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutMaxSizes


abstract class Dialog(protected val view: View) : AppCompatDialog(SupAndroid.activity!!) {

    //
    //  Getters
    //

    var isEnabled: Boolean = false
        private set
    private var cancelable: Boolean = false

    constructor(layoutRes: Int) : this(ToolsView.inflate<View>(layoutRes)) {}

    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setOnCancelListener { dialogInterface -> onHide() }

        val layoutMaxSizes = object : LayoutMaxSizes(SupAndroid.activity!!) {
            override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
                setMaxWidthParentPercent((if (ToolsAndroid.isScreenPortrait()) 90 else 70).toFloat())
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
            }
        }

        layoutMaxSizes.id = R.id.vDialogLayoutMaxSizes
        layoutMaxSizes.setMaxWidth(600)
        layoutMaxSizes.setUseScreenWidthAsParent(true)
        layoutMaxSizes.setAlwaysMaxW(true)
        layoutMaxSizes.setChildAlwaysMaxW(true)
        layoutMaxSizes.addView(ToolsView.removeFromParent(view), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        setContentView(layoutMaxSizes)
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window!!.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT) //  Без этой строки диалог умирает при повороте экрана

        Navigator.addOnScreenChanged {
            hide()
            true
        }
    }

    override fun onBackPressed() {
        if(onTryCancelOnTouchOutside()) super.onBackPressed()
    }

    open fun onTryCancelOnTouchOutside(): Boolean {
        return true
    }

    @CallSuper
    protected open fun onShow() {

    }

    @CallSuper
    protected open fun onHide() {

    }

    override fun hide() {
        super.dismiss()
    }

    override fun show() {
        showDialog<Dialog>()
    }

    fun <K : Dialog> showDialog(): K {
        onShow()
        super.show()
        return this as K
    }

    //
    //  Setters
    //


    override fun setCancelable(cancelable: Boolean) {
        this.cancelable = cancelable
        super.setCancelable(cancelable)
    }

    fun <K : Dialog> setDialogCancelable(cancelable: Boolean): K {
        setCancelable(cancelable)
        return this as K
    }

    fun <K : Dialog> setEnabled(enabled: Boolean): K {
        this.isEnabled = enabled
        setCanceledOnTouchOutside(isCancelable() && isEnabled)
        return this as K
    }

    fun isCancelable(): Boolean {
        return cancelable
    }
}
