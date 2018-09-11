package com.sup.dev.android.views.widgets

import android.content.Context
import android.support.annotation.*
import android.view.View
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.navigator.NavigationAction
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.cards.CardWidget
import com.sup.dev.android.views.dialogs.DialogSheetWidget
import com.sup.dev.android.views.dialogs.DialogWidget
import com.sup.dev.android.views.popup.Popup
import com.sup.dev.android.views.popup.PopupWidget
import com.sup.dev.android.views.screens.SWidget
import com.sup.dev.java.classes.providers.Provider
import com.sup.dev.java.tools.ToolsThreads


abstract class Widget(layoutRes: Int) {

    val view: View
    protected val vTitle: TextView?

    private var onHide: (Widget) -> Unit = {}
    var isEnabled = true
    var isCancelable = true
    protected var viewWrapper: WidgetViewWrapper? = null

    //
    //  Getters
    //

    protected val context: Context
        get() = view!!.context

    init {
        view = if (layoutRes > 0) ToolsView.inflate(layoutRes) else instanceView()!!
        vTitle = findViewById(R.id.title)

        if (vTitle != null) {
            vTitle.text = null
            vTitle.visibility = View.GONE
        }
    }

    protected open fun instanceView(): View? {
        return null
    }

    fun hide() {
        ToolsThreads.main {
            if (viewWrapper != null) viewWrapper!!.hideWidget<WidgetViewWrapper>()
            Unit
        }
    }

    protected fun <K : View> findViewById(@IdRes id: Int): K {
        return view!!.findViewById(id)
    }

    //
    //  Callbacks
    //

    fun onTryCancelOnTouchOutside(): Boolean {
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

    open fun <K : Widget> setTitle(@StringRes title: Int): K {
        return setTitle(ToolsResources.getString(title))
    }

    open fun <K : Widget> setTitle(title: String?): K {
        if (vTitle != null) ToolsView.setTextOrGone(vTitle, title!!)
        return this as K
    }

    fun <K : Widget> setTitleBackgroundColorRes(@ColorRes color: Int): K {
        return setTitleBackgroundColor(ToolsResources.getColor(color))
    }

    fun <K : Widget> setTitleBackgroundColor(@ColorInt color: Int): K {
        vTitle?.setBackgroundColor(color)
        return this as K
    }

    fun <K : Widget> setOnHide(onHide: (Widget) -> Unit): K {
        this.onHide = onHide
        return this as K
    }

    open fun <K : Widget> setEnabled(enabled: Boolean): K {
        this.isEnabled = enabled
        if (vTitle != null) vTitle.isEnabled = enabled
        if (viewWrapper != null) viewWrapper!!.setWidgetEnabled<WidgetViewWrapper>(enabled)
        return this as K
    }

    fun <K : Widget> setCancelable(cancelable: Boolean): K {
        this.isCancelable = cancelable
        if (viewWrapper != null) viewWrapper!!.setWidgetCancelable<WidgetViewWrapper>(cancelable)
        return this as K
    }

    //
    //  Support
    //

    fun asSheet(): DialogSheetWidget {
        val sheet = DialogSheetWidget(this)
        this.viewWrapper = sheet
        return sheet
    }

    fun asSheetShow(): DialogSheetWidget {
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

    @JvmOverloads
    fun showPopupWhenClick(view: View, willShow: Provider<Boolean>? = null): Widget {
        ToolsView.setOnClickCoordinates(view) { view1, x, y ->
            if (willShow == null || willShow.provide()!!) asPopup().show<Popup>(view1, x, y)
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

    fun showPopupWhenClickAndLongClick(view: View, willShowClicik: Provider<Boolean>): Widget {
        return showPopupWhenClickAndLongClick(view, willShowClicik, null)
    }

    fun showPopupWhenClickAndLongClick(view: View, willShowClick: Provider<Boolean>?, willShowLongClick: Provider<Boolean>?): Widget {
        ToolsView.setOnClickAndLongClickCoordinates(view) { view1, x, y, isClick ->
            if (isClick) {
                if (willShowClick == null || willShowClick.provide()!!) asPopup().show<Popup>(view1, x, y)
            } else {
                if (willShowLongClick == null || willShowLongClick.provide()!!) asPopup().show<Popup>(view1, x, y)
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
