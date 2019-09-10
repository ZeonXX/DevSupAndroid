package com.sup.dev.android.libs.screens.activity

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.annotation.CallSuper
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.ViewChipMini
import com.sup.dev.android.views.views.ViewIcon

abstract class SActivityType(
        val activity: SActivity
) {

    var screenNavigationVisible = true
    var screenNavigationAllowed = true
    var screenNavigationAnimation = true
    var screenNavigationShadowAvailable = true
    var screenHideBottomNavigationWhenKeyboard = true
    var useIconsFilters = true

    abstract fun onCreate()

    @CallSuper
    open fun onSetScreen(screen: Screen?){
        if (screen != null) {
            screenNavigationVisible = screen.isNavigationVisible
            screenNavigationAllowed = screen.isNavigationAllowed
            screenNavigationAnimation = screen.isNavigationAnimation
            screenNavigationShadowAvailable = screen.isNavigationShadowAvailable
            screenHideBottomNavigationWhenKeyboard = screen.isHideBottomNavigationWhenKeyboard
            updateNavigationVisible()
        }
    }

    open fun updateNavigationVisible() {

    }

    abstract fun getLayout(): Int

    open fun onViewBackPressed() {
        activity.onBackPressed()
    }

    open fun getNavigationDrawable(screen: Screen): Drawable? {
        if (Navigator.hasBackStack()) {
            val drawableAttr = ToolsResources.getDrawableAttr(screen.toolbarNavigationIcon)
            if (drawableAttr != null) return drawableAttr else return ToolsResources.getDrawable(screen.toolbarNavigationIcon)
        } else {
            return null
        }
    }

    //
    //  Navigation Item
    //

    open fun getExtraNavigationItem(): NavigationItem? {
        return null
    }

    open fun addNavigationView(view: View) {

    }

    open fun addNavigationDivider() {

    }

    fun addNavigationItem(icon: Int, text: Int, hided: Boolean, onClick: (View) -> Unit): NavigationItem {
        return addNavigationItem(icon, ToolsResources.s(text), hided, onClick)
    }

    abstract fun addNavigationItem(icon: Int, text: String, hided: Boolean, onClick: (View) -> Unit): NavigationItem

    abstract class NavigationItem {

        var view: View? = null
        var vIcon: ViewIcon? = null
        var vChip: ViewChipMini? = null
        var vText: TextView? = null

        abstract fun setVisible(visible: Boolean)

    }
}