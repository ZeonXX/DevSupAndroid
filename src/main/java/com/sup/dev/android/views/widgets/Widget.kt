package com.sup.dev.android.views.widgets

import android.content.Context
import androidx.annotation.*
import android.view.View
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.navigator.NavigationAction
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.cards.CardWidget
import com.sup.dev.android.views.dialogs.DialogWidget
import com.sup.dev.android.views.popup.Popup
import com.sup.dev.android.views.popup.PopupWidget
import com.sup.dev.android.views.screens.SWidget
import com.sup.dev.android.views.sheets.Sheet
import com.sup.dev.android.views.views.layouts.LayoutMaxSizes

import com.sup.dev.java.tools.ToolsThreads

abstract class Widget(layoutRes: Int) {

    val view: View
    protected val vTitle: TextView?

    private var onHide: (Widget) -> Unit = {}
    var isEnabled = true
    var isCancelable = true
    protected var viewWrapper: WidgetViewWrapper? = null
    private var hideCalled = false

    //
    //  Getters
    //

    protected val context: Context
        get() = view.context

    init {
        view = if (layoutRes > 0) ToolsView.inflate(layoutRes) else instanceView()!!
        vTitle = findViewByIdNullable(R.id.vTitle)

        if (vTitle != null) {
            vTitle.text = null
            vTitle.visibility = View.GONE
        }
    }

    protected open fun instanceView(): View? {
        return null
    }

    fun hideCancel() {
        hideCalled = false
    }

    fun hide() {
        hideCalled = true
        ToolsThreads.main(true) {
            if (!hideCalled) return@main
            hideForce()
        }
    }

    fun hideForce(){
        hideCalled = true
        if (viewWrapper != null) viewWrapper!!.hideWidget<WidgetViewWrapper>()
    }

    protected fun <K : View> findViewById(@IdRes id: Int): K {
        return view.findViewById(id)
    }

    protected fun <K : View> findViewByIdNullable(@IdRes id: Int): K? {
        return view.findViewById(id)
    }

    //
    //  Callbacks
    //

    open fun onTryCancelOnTouchOutside(): Boolean {
        return true
    }

    @CallSuper
    open fun onShow() {
        if (vTitle != null) {
            ToolsView.setTextOrGone(vTitle, vTitle.text)
            if (viewWrapper is SWidget) {
                vTitle.visibility = View.GONE
                (viewWrapper as SWidget).setTitle(vTitle.text.toString())
            }
        }

    }

    @CallSuper
    open fun onHide() {
        onHide.invoke(this)
    }

    //
    //  Setters
    //

    open fun setTitle(@StringRes title: Int): Widget {
        return setTitle(ToolsResources.s(title))
    }

    open fun setTitle(title: String?): Widget {
        if (vTitle != null) ToolsView.setTextOrGone(vTitle, title)
        return this
    }

    fun setTitleGravity(gravity: Int): Widget {
        vTitle?.gravity = gravity
        return this
    }

    open fun setTitleBackgroundColorRes(@ColorRes color: Int): Widget {
        return setTitleBackgroundColor(ToolsResources.getColor(color))
    }

    open fun setTitleBackgroundColor(@ColorInt color: Int): Widget {
        vTitle?.setBackgroundColor(color)
        return this
    }

    open fun setOnHide(onHide: (Widget) -> Unit): Widget {
        this.onHide = onHide
        return this
    }

    open fun setEnabled(enabled: Boolean): Widget {
        this.isEnabled = enabled
        if (vTitle != null) vTitle.isEnabled = enabled
        if (viewWrapper != null) viewWrapper!!.setWidgetEnabled<WidgetViewWrapper>(enabled)
        return this
    }

    open fun setCancelable(cancelable: Boolean): Widget {
        this.isCancelable = cancelable
        if (viewWrapper != null) viewWrapper!!.setWidgetCancelable<WidgetViewWrapper>(cancelable)
        return this
    }

    //
    //  Support
    //

    fun asSheet(): Sheet {
        val sheet = Sheet(this)
        this.viewWrapper = sheet
        return sheet
    }

    open fun asSheetShow(): Sheet {
        val sheet = asSheet()
        sheet.show()
        return sheet
    }

    fun asDialog(): DialogWidget {
        val dialog = DialogWidget(this)
        this.viewWrapper = dialog
        return dialog
    }

    fun asDialogShow(): DialogWidget {
        val dialog = asDialog()
        dialog.show()
        return dialog
    }

    fun asPopup(): PopupWidget {
        val popup = PopupWidget(this)
        this.viewWrapper = popup
        return popup
    }

    fun asPopupShow(view: View): PopupWidget {
        val popup = asPopup()
        popup.show<Popup>(view)
        return popup
    }

    fun asPopupShow(view: View, x: Int, y: Int): PopupWidget {
        val popup = asPopup()
        popup.show<Popup>(view, x, y)
        return popup
    }

    fun showPopupWhenClick(view: View, willShow: (() -> Boolean)? = null): Widget {
        ToolsView.setOnClickCoordinates(view) { view1, x, y ->
            if (willShow == null || willShow.invoke()) asPopup().show<Popup>(view1, x, y)
            Unit
        }
        return this
    }

    fun showPopupWhenLongClick(view: View): Widget {
        ToolsView.setOnLongClickCoordinates(view) { view1, x, y ->
            asPopup().show<Popup>(view1, x, y)
            Unit
        }
        return this
    }

    fun showPopupWhenClickAndLongClick(view: View, willShowClick: () -> Boolean): Widget {
        return showPopupWhenClickAndLongClick(view, willShowClick, null)
    }

    fun showPopupWhenClickAndLongClick(view: View, willShowClick: (() -> Boolean)?, willShowLongClick: (() -> Boolean)?): Widget {
        ToolsView.setOnClickAndLongClickCoordinates(view) { view1, x, y, isClick ->
            if (isClick) {
                if (willShowClick == null || willShowClick.invoke()) asPopup().show<Popup>(view1, x, y)
            } else {
                if (willShowLongClick == null || willShowLongClick.invoke()) asPopup().show<Popup>(view1, x, y)
            }
            Unit
        }
        return this
    }

    fun showSheetWhenClickAndLongClick(view: View, willShowClick: () -> Boolean): Widget {
        return showSheetWhenClickAndLongClick(view, willShowClick, null)
    }

    fun showSheetWhenClickAndLongClick(view: View, willShowClick: (() -> Boolean)?, willShowLongClick: (() -> Boolean)?): Widget {
        ToolsView.setOnClickAndLongClickCoordinates(view) { _, _, _, isClick ->
            if (isClick) {
                if (willShowClick == null || willShowClick.invoke()) asSheetShow()
            } else {
                if (willShowLongClick == null || willShowLongClick.invoke())  asSheetShow()
            }
            Unit
        }
        return this
    }

    fun asScreen(): SWidget {
        val screen = SWidget(this)
        this.viewWrapper = screen
        return screen
    }

    fun asScreenTo(): SWidget {
        val screen = asScreen()
        Navigator.to(screen)
        return screen
    }

    fun asScreen(action: NavigationAction): SWidget {
        val screen = asScreen()
        Navigator.action(action, screen)
        return screen
    }

    fun asCard(): CardWidget {
        val card = CardWidget(this)
        this.viewWrapper = card
        return card
    }


}
