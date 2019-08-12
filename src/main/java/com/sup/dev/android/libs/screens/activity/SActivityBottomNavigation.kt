package com.sup.dev.android.libs.screens.activity

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.ViewChipMini
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.android.views.views.layouts.LayoutFrameMeasureCallback
import com.sup.dev.android.views.widgets.WidgetMenu
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.tools.ToolsThreads


abstract class SActivityBottomNavigation : SActivity() {

    companion object {

        fun setShadow(view: View) {
            view.setBackgroundDrawable(GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(0x30000000, 0x00000000)))
        }

    }

    private var widgetMenu: WidgetMenu? = null

    private var vContainer: LinearLayout? = null
    private var vLine: View? = null
    private var navigationVisible = true
    private var screenBottomNavigationVisible = true
    private var screenBottomNavigationAllowed = true
    private var screenBottomNavigationAnimation = true
    private var screenBottomNavigationShadowAvailable = true
    private var screenHideBottomNavigationWhenKeyboard = true
    private var lastH_P = 0
    private var maxH_P = 0
    private var lastH_L = 0
    private var maxH_L = 0
    private var skipNextNavigationAnimation = false

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        vContainer = findViewById(R.id.vScreenActivityBottomNavigationContainer)
        vLine = findViewById(R.id.vScreenActivityBottomNavigationLine)

        updateNavigationVisible()
        setShadow(vLine!!)

        (vActivityRoot as LayoutFrameMeasureCallback).onMeasure = { w, h ->
            if(ToolsAndroid.isScreenPortrait()){
                lastH_P = View.MeasureSpec.getSize(h)
                if(maxH_P < lastH_P) maxH_P = lastH_P
            }else{
                lastH_L = View.MeasureSpec.getSize(h)
                if(maxH_L < lastH_L) maxH_L = lastH_L
            }
            ToolsThreads.main(true) { updateNavigationVisible() }
        }
    }

    override fun getLayout() = R.layout.screen_activity_bottom_navigation

    fun addExtra(): NavigationItem {
        widgetMenu = WidgetMenu()

        return addIcon(vContainer!!.childCount, ToolsResources.getDrawableAttrId(R.attr.ic_menu_24dp)) { v -> widgetMenu!!.asSheetShow() }
    }

    fun setNavigationVisible(b: Boolean) {
        navigationVisible = b
        updateNavigationVisible()
    }

    override fun setScreen(screen: Screen?, animation: Navigator.Animation) {
        super.setScreen(screen, animation)
        if (screen != null) {
            screenBottomNavigationVisible = screen.isBottomNavigationVisible
            screenBottomNavigationAllowed = screen.isBottomNavigationAllowed
            screenBottomNavigationAnimation = screen.isBottomNavigationAnimation
            screenBottomNavigationShadowAvailable = screen.isBottomNavigationShadowAvailable
            screenHideBottomNavigationWhenKeyboard = screen.isHideBottomNavigationWhenKeyboard
            updateNavigationVisible()
        }
    }

    fun isKeyboardShown():Boolean{
        return if(ToolsAndroid.isScreenPortrait()) lastH_P < (maxH_P - ToolsView.dpToPx(50))
        else lastH_L < (maxH_L - ToolsView.dpToPx(50))
    }

    fun updateNavigationVisible() {
        if (navigationVisible && screenBottomNavigationAllowed && screenBottomNavigationVisible) {
            if (screenHideBottomNavigationWhenKeyboard && isKeyboardShown()) {
                vContainer!!.visibility = View.GONE
                vLine!!.visibility = View.GONE
                skipNextNavigationAnimation  = true
            } else {
                ToolsView.fromAlpha(vContainer!!, if (screenBottomNavigationAnimation && !skipNextNavigationAnimation) ToolsView.ANIMATION_TIME else 0)
                if (screenBottomNavigationShadowAvailable) {
                    ToolsView.fromAlpha(vLine!!, if (screenBottomNavigationAnimation && !skipNextNavigationAnimation) ToolsView.ANIMATION_TIME else 0)
                } else {
                    ToolsView.toAlpha(vLine!!, if (screenBottomNavigationAnimation && !skipNextNavigationAnimation) ToolsView.ANIMATION_TIME else 0) {
                        vLine!!.visibility = if (screenBottomNavigationAllowed) View.INVISIBLE else View.GONE
                    }
                }
            }
        } else {
            ToolsView.toAlpha(vContainer!!, if (screenBottomNavigationAnimation) ToolsView.ANIMATION_TIME else 0) {
                vContainer!!.visibility = if (screenBottomNavigationAllowed) View.INVISIBLE else View.GONE
            }
            ToolsView.toAlpha(vLine!!, if (screenBottomNavigationAnimation) ToolsView.ANIMATION_TIME else 0) {
                vLine!!.visibility = if (screenBottomNavigationAllowed) View.INVISIBLE else View.GONE
            }
        }
    }

    fun addIcon(@DrawableRes icon: Int, onClick: (View) -> Unit): NavigationItem {
        return addIcon(vContainer!!.childCount - if (widgetMenu == null) 0 else 1, icon, onClick)
    }

    protected fun addIcon(position: Int, @DrawableRes icon: Int, onClick: (View) -> Unit): NavigationItem {
        val navigationItem = NavigationItem(this)

        navigationItem.vIcon.setImageResource(icon)
        navigationItem.vIcon.setOnClickListener { onClick.invoke(it) }
        vContainer!!.addView(navigationItem.view, position)
        (navigationItem.view.layoutParams as LinearLayout.LayoutParams).weight = 1f
        (navigationItem.view.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.CENTER

        return navigationItem
    }

    fun clearNavigation() {
        widgetMenu!!.clear()
    }

    fun addNavigationView(view: View) {
        widgetMenu!!.addView(view)
    }

    fun hideNavigation() {
        widgetMenu!!.hide()
    }

    fun addNavigationItem(@StringRes text: Int, @DrawableRes icon: Int, onClick: () -> Unit): WidgetMenu {
        return addNavigationItem(ToolsResources.s(text), icon, onClick)
    }

    fun addNavigationItem(text: String, @DrawableRes icon: Int, onClick: () -> Unit): WidgetMenu {
        return widgetMenu!!.add(text) { w, c -> onClick.invoke() }.icon(icon)
    }


    //
    //   Navigation Item
    //

    class NavigationItem(context: Context) {

        val view: View = ToolsView.inflate(context, R.layout.screen_activity_bottom_navigation_item)
        val vIcon: ViewIcon
        val vChip: ViewChipMini

        init {
            vIcon = view.findViewById(R.id.vIcon)
            vChip = view.findViewById(R.id.vChip)
        }

    }


}