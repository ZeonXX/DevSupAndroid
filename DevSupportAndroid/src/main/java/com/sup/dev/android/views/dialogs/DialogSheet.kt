package com.sup.dev.android.views.dialogs

import android.os.Build
import android.support.annotation.CallSuper
import android.support.v7.app.AppCompatDialog
import android.view.*
import android.widget.FrameLayout
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.java.libs.debug.Debug
import com.sup.dev.java.libs.debug.log


open class DialogSheet(protected val view: View) : AppCompatDialog(view.context) {
    //
    //  Getters
    //

    var isEnabled: Boolean = false
        private set
    private var cancelable = true

    constructor(layoutRes: Int) : this(ToolsView.inflate<View>(layoutRes)) {}

    init {

        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setOnCancelListener { dialogInterface -> onHide() }

        val vRoot: ViewGroup = ToolsView.inflate(view.context, R.layout.dialog_sheet)
        val vContainer: ViewGroup = vRoot.findViewById(R.id.vContainer)

        vContainer.isClickable = true // Чтоб не закрывался при нажатии на тело
        vContainer.addView(ToolsView.removeFromParent(view))
        vContainer.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        (vContainer.layoutParams as FrameLayout.LayoutParams).gravity = Gravity.BOTTOM
        setContentView(vRoot)

        window!!.setWindowAnimations(R.style.DialogSheetAnimation)
        window!!.setBackgroundDrawable(null)
        window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        window!!.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window!!.statusBarColor = 0x00000000
        }

        vRoot.y = (-ToolsAndroid.getBottomNavigationBarHeight()).toFloat()

        vRoot.setOnClickListener { v -> if (cancelable && isEnabled && onTryCancelOnTouchOutside()) hide() }

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
