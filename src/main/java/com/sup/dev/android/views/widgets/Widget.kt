package com.sup.dev.android.views.widgets

import androidx.annotation.*
import android.view.View
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.navigator.NavigationAction
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.cards.CardWidget
import com.sup.dev.android.views.splash.Popup
import com.sup.dev.android.views.screens.SWidget
import com.sup.dev.android.views.splash.Dialog
import com.sup.dev.android.views.splash.Sheet
import com.sup.dev.android.views.splash.Splash
import com.sup.dev.android.views.views.layouts.LayoutCorned
import com.sup.dev.java.tools.ToolsThreads

abstract class Widget(layoutRes: Int) {

    val view: View
    protected val vTitle: TextView?

    private var onHide: (Widget) -> Unit = {}
    var isEnabled = true
    var isCancelable = true
    private var isHided = true
    var isCompanion = false
    var noBackground = false
    private var maxW: Int? = null
    private var maxH: Int? = null
    private var minW: Int? = null
    private var minH: Int? = null
    protected var viewWrapper: WidgetViewWrapper<out Any>? = null
    //  Popup
    var popupYMirrorOffset = 0
    var allowPopupMirrorHeight = false

    init {
        view = if (layoutRes > 0) ToolsView.inflate(layoutRes) else instanceView()!!
        view.isClickable = true //  Чтоб не закрывался при косании
        vTitle = findViewByIdNullable(R.id.vTitle)

        if (vTitle != null) {
            vTitle.text = null
            vTitle.visibility = View.GONE
        }
    }

    protected open fun instanceView(): View? {
        return null
    }

    fun hide() {
        isHided = true
        if (viewWrapper != null) viewWrapper!!.hide()
    }

    protected fun <K : View> findViewById(@IdRes id: Int): K {
        return view.findViewById(id)
    }

    protected fun <K : View> findViewByIdNullable(@IdRes id: Int): K? {
        return view.findViewById(id)
    }

    //
    //  Getters
    //

    fun isHided() = isHided

    fun isShoved() = !isHided

    fun getMaxW() = maxW
    fun getMaxH() = maxH
    fun getMinW() = minW
    fun getMinH() = minH

    //
    //  Callbacks
    //

    open fun onTryCancelOnTouchOutside(): Boolean {
        return true
    }

    open fun onBackPressed(): Boolean {
        return true
    }

    @CallSuper
    open fun onShow() {
        isHided = false
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
        isHided = true
        onHide.invoke(this)
    }

    //
    //  Setters
    //

    open fun makeCompanion(): Widget {
        this.isCompanion = true
        return this
    }

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
        ToolsThreads.main {
            if (vTitle != null) vTitle.isEnabled = enabled
            if (viewWrapper != null) viewWrapper!!.setWidgetEnabled(enabled)
        }
        return this
    }

    open fun setCancelable(cancelable: Boolean): Widget {
        this.isCancelable = cancelable
        ToolsThreads.main {
            if (viewWrapper != null) viewWrapper!!.setWidgetCancelable(cancelable)
        }
        return this
    }

    open fun setTitleSize(sp: Float): Widget {
        vTitle?.textSize = sp
        return this
    }

    open fun allowPopupMirrorHeight(): Widget {
        this.allowPopupMirrorHeight = true; return this
    }

    open fun setPopupYMirrorOffset(offset:Int):Widget{
        this.popupYMirrorOffset = offset;return this
    }

    //
    //  Sizes
    //

    open fun setMaxSizes(maxW: Int?, maxH: Int?): Widget {
        this.maxW = maxW
        this.maxH = maxH
        return this
    }

    open fun setMinSizes(minW: Int?, minH: Int?): Widget {
        this.minW = minW
        this.minH = minH
        return this
    }

    open fun setSizes(w: Int?, h: Int?): Widget {
        this.maxW = w
        this.maxH = h
        this.minW = w
        this.minH = h
        return this
    }

    open fun setMaxW(maxW: Int?): Widget {
        this.maxW = maxW; return this
    }

    open fun setMaxH(maxH: Int?): Widget {
        this.maxH = maxH; return this
    }

    open fun setMinW(minW: Int?): Widget {
        this.minW = minW; return this
    }

    open fun setMinH(minH: Int?): Widget {
        this.minH = minH; return this
    }

    open fun setSizeW(w: Int?): Widget {
        this.maxW = w;this.minW = w; return this
    }

    open fun setSizeH(h: Int?): Widget {
        this.maxH = h;this.minH = h; return this
    }

    //
    //  Support
    //

    fun fixForAndroid9():Widget {   //  В Android 9 есть баг, свзяанный с clipPath у LayoutCorned. Из-за него может пропадать часть View внутри диалогов.
        ToolsThreads.main(true) {
            val vX: LayoutCorned? = ToolsView.findViewOnParents(view, R.id.vSplashViewContainer)
            vX?.makeSoftware()
        }
        return this
    }

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

    fun asSplash(): Splash {
        val splash = Splash(this)
        this.viewWrapper = splash
        return splash
    }

    open fun asSplashShow(): Splash {
        val splash = asSplash()
        splash.show()
        return splash
    }

    fun asDialog(): Dialog {
        val dialog = Dialog(this)
        this.viewWrapper = dialog
        return dialog
    }

    fun asDialogShow(): Dialog {
        val dialog = asDialog()
        dialog.show()
        return dialog
    }

    fun asPopup(): Popup {
        val popup = Popup(this)
        this.viewWrapper = popup
        return popup
    }

    fun asPopupShow(view: View) = asPopupShow(view, 0, 0)

    fun asPopupShow(view: View, x: Float, y: Float) = asPopupShow(view, x.toInt(), y.toInt())

    fun asPopupShow(view: View, x: Int, y: Int): Popup {
        val popup = asPopup()
        popup.setAnchor(view, x, y)
        popup.show()
        return popup
    }

    fun showPopupWhenClick(view: View, willShow: (() -> Boolean)? = null): Widget {
        ToolsView.setOnClickCoordinates(view) { view1, x, y ->
            if (willShow == null || willShow.invoke()) asPopup().setAnchor(view1, x, y).show()
        }
        return this
    }

    fun showPopupWhenLongClick(view: View): Widget {
        ToolsView.setOnLongClickCoordinates(view) { view1, x, y ->
            asPopup().setAnchor(view1, x, y).show()
            Unit
        }
        return this
    }

    fun showPopupWhenClickAndLongClick(view: View, willShowClick: () -> Boolean): Widget {
        return showPopupWhenClickAndLongClick(view, willShowClick, null)
    }

    fun showPopupWhenClickAndLongClick(view: View, willShowClick: (() -> Boolean)?, willShowLongClick: (() -> Boolean)?): Widget {
        ToolsView.setOnClickAndLongClickCoordinates(view,
                { if (willShowClick == null || willShowClick.invoke()) asPopup().setAnchor(it.view, it.x, it.y).show() },
                { if (willShowLongClick == null || willShowLongClick.invoke()) asPopup().setAnchor(it.view, it.x, it.y).show() })
        return this
    }

    fun showSheetWhenClickAndLongClick(view: View, willShowClick: () -> Boolean): Widget {
        return showSheetWhenClickAndLongClick(view, willShowClick, null)
    }

    fun showSheetWhenClickAndLongClick(view: View, willShowClick: (() -> Boolean)?, willShowLongClick: (() -> Boolean)?): Widget {
        ToolsView.setOnClickAndLongClickCoordinates(view,
                {if (willShowClick == null || willShowClick.invoke()) asSheetShow()},
                {if (willShowLongClick == null || willShowLongClick.invoke()) asSheetShow()})
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
