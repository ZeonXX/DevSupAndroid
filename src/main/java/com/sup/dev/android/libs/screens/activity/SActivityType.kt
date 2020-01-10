package com.sup.dev.android.libs.screens.activity

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.annotation.CallSuper
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.views.ViewChipMini
import com.sup.dev.android.views.views.ViewIcon
import kotlin.reflect.KClass

abstract class SActivityType(
        val activity: SActivity
) {

    var screenNavigationVisible = true
    var screenNavigationAllowed = true
    var screenNavigationAnimation = false
    var screenNavigationShadowAvailable = true
    var screenHideBottomNavigationWhenKeyboard = true
    var screenBottomNavigationColor = 0

    private var iconsColorAccent:Int? = null
    private var iconsColor:Int? = null

    abstract fun onCreate()

    @CallSuper
    open fun onSetScreen(screen: Screen?) {
        if (screen != null) {
            screenNavigationVisible = screen.isNavigationVisible
            screenNavigationAllowed = screen.isNavigationAllowed
            screenNavigationAnimation = screen.isNavigationAnimation
            screenNavigationShadowAvailable = screen.isNavigationShadowAvailable
            screenHideBottomNavigationWhenKeyboard = screen.isHideBottomNavigationWhenKeyboard
            screenBottomNavigationColor = screen.navigationBarColor
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
        if (Navigator.hasBackStack() || screen.forceBackIcon) {
            val drawableAttr = ToolsResources.getDrawableAttr(screen.toolbarNavigationIcon)
            if (drawableAttr != null) return drawableAttr else return ToolsResources.getDrawable(screen.toolbarNavigationIcon)
        } else {
            return null
        }
    }

    fun getIconsColorAccent():Int{
        if(iconsColorAccent == null) iconsColorAccent = ToolsResources.getColorAttr(activity, R.attr.colorAccent)
        return iconsColorAccent!!
    }

    fun getIconsColor():Int{
        if(iconsColor == null) iconsColor = ToolsResources.getColorAttr(activity, R.attr.toolbar_content_color)
        return iconsColor!!
    }

    fun setIconsColorAccent(color:Int){ iconsColorAccent = color }
    fun setIconsColor(color:Int){ iconsColor = color }

    //
    //  Navigation Item
    //

    open fun getExtraNavigationItem(): NavigationItem? {
        return null
    }

    open fun addNavigationView(view: View, useIconsFilters: Boolean) {

    }

    open fun addNavigationDivider() {

    }

    fun addNavigationItem(icon: Int, text: Int, hided: Boolean, useIconsFilters: Boolean = false, onClick: (View) -> Unit, onLongClick: ((View) -> Unit)?): NavigationItem {
        return addNavigationItem(icon, ToolsResources.s(text), hided, useIconsFilters, onClick, onLongClick)
    }

    fun addNavigationItem(icon: Int, text: Int, hided: Boolean, useIconsFilters: Boolean = false, onClick: (View) -> Unit): NavigationItem {
        return addNavigationItem(icon, ToolsResources.s(text), hided, useIconsFilters, onClick, null)
    }

    fun addNavigationItem(icon: Int, text: String, hided: Boolean, useIconsFilters: Boolean = false, onClick: (View) -> Unit) = addNavigationItem(icon, text, hided, useIconsFilters, onClick, null)

    abstract fun addNavigationItem(icon: Int, text: String, hided: Boolean, useIconsFilters: Boolean = false, onClick: (View) -> Unit, onLongClick: ((View) -> Unit)?): NavigationItem

    abstract fun updateIcons()

    abstract class NavigationItem {

        val accentScreens = ArrayList<KClass<out Screen>>()
        var isDefoultAccentItem = false
        var view: View? = null
        var vIcon: ViewIcon? = null
        var vChip: ViewChipMini? = null
        var vText: TextView? = null

        abstract fun setVisible(visible: Boolean)

        open fun setChipText(text: String) {
            vChip?.setText(text)
        }

        fun setAccentScreen(vararg screenClass: KClass<out Screen>): NavigationItem {
            accentScreens.addAll(screenClass)
            return this
        }

        fun makeDefaultAccentItem(): NavigationItem {
            isDefoultAccentItem = true
            return this
        }

    }
}