package com.sup.dev.android.views.dialogs

import android.content.Context
import android.os.Build
import android.util.DisplayMetrics
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatDialog
import android.view.*
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.activity.SActivity
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutCorned
import com.sup.dev.java.libs.debug.Debug
import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.libs.debug.log
import java.lang.IllegalArgumentException

@Suppress("UNCHECKED_CAST")
open class DialogSheet(protected val view: View) : AppCompatDialog(view.context) {

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        val window = window
        if (hasFocus && view.context is SActivity && window != null) {
            val activity = view.context!! as SActivity
            if (activity.isFullScreen) {
                window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (!activity.isFullScreen) {
                    window.decorView.systemUiVisibility = activity.screenStatusBarIsLight
                }
            }

           if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
               window.statusBarColor = activity.screenStatusBarColor
           }

        }
    }

    //
    //  Getters
    //

    var isEnabled: Boolean = false
        private set
    private var cancelable = true

    constructor(layoutRes: Int) : this(ToolsView.inflate<View>(layoutRes)) {}

    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setOnCancelListener { onHide() }

        val vRoot: ViewGroup = ToolsView.inflate(view.context, R.layout.dialog_sheet)
        val vContainer: LayoutCorned = vRoot.findViewById(R.id.vContainer)

        vContainer.setCornedSize(8)
        vContainer.setCornedBL(false)
        vContainer.setCornedBR(false)

        vContainer.isClickable = true // Чтоб не закрывался при нажатии на тело
        vContainer.addView(ToolsView.removeFromParent(view))
        setContentView(vRoot)

        window!!.setWindowAnimations(R.style.DialogSheetAnimation)
        window!!.setBackgroundDrawable(null)
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        vRoot.setOnClickListener { if (cancelable && isEnabled && onTryCancelOnTouchOutside()) hide() }

        Navigator.addOnScreenChanged {
            hide()
            true
        }
    }

    override fun onBackPressed() {
        if (onTryCancelOnTouchOutside()) super.onBackPressed()
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
        try {
            super.dismiss()
        } catch (e: IllegalArgumentException) {
            err(e)
        }
        onHide()
    }

    override fun show() {
        showDialog<DialogSheet>()
    }

    fun <K : DialogSheet> showDialog(): K {
        onShow()
        super.show()
        return this as K
    }

    //
    //  Setters
    //


    override fun setCancelable(cancelable: Boolean) {
        this.cancelable = cancelable
        setCanceledOnTouchOutside(cancelable && isEnabled)
        super.setCancelable(cancelable && isEnabled)
    }

    fun setDialogCancelable(cancelable: Boolean): DialogSheet {
        setCancelable(cancelable)
        return this
    }

    fun setEnabled(enabled: Boolean): DialogSheet {
        this.isEnabled = enabled
        setCanceledOnTouchOutside(cancelable && isEnabled)
        super.setCancelable(cancelable && isEnabled)
        return this
    }

    fun isCancelable(): Boolean {
        return cancelable
    }
}
