package com.sup.dev.android.libs.screens.activity

import android.content.Context
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.ViewChip
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.android.views.widgets.WidgetMenu


abstract class SActivityBottomNavigation : SActivity() {

    private var widgetMenu: WidgetMenu? = null

    private var vContainerRoot: ViewGroup? = null
    private var vContainer: LinearLayout? = null
    private var vLine: View? = null
    private var navigationVisible = true
    private var screenBottomNavigationVisible = true
    private var screenBottomNavigationAllowed = true
    private var screenBottomNavigationAnimation = true
    private var screenBottomNavigationLineAllowed = true

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        vContainerRoot = findViewById(R.id.screen_activity_bottom_navigation_container_root)
        vContainer = findViewById(R.id.screen_activity_bottom_navigation_container)
        vLine = findViewById(R.id.screen_activity_bottom_navigation_line)

        updateNavigationVisible()
    }

    fun addExtra(): NavigationItem {
        widgetMenu = WidgetMenu()

        return addIcon(vContainer!!.childCount, ToolsResources.getDrawableFromAttrId(R.attr.ic_menu)) { v -> widgetMenu!!.asSheetShow() }
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
            screenBottomNavigationLineAllowed = screen.isBottomNavigationLineAllowed
            updateNavigationVisible()
        }
    }

    private fun updateNavigationVisible() {
        if (navigationVisible && screenBottomNavigationAllowed && screenBottomNavigationVisible)
            ToolsView.fromAlpha(vContainerRoot!!, if (screenBottomNavigationAnimation) ToolsView.ANIMATION_TIME else 0)
        else
            ToolsView.toAlpha(vContainerRoot!!, if (screenBottomNavigationAnimation) ToolsView.ANIMATION_TIME else 0) {
                vContainerRoot!!.visibility = if (screenBottomNavigationAllowed) View.INVISIBLE else View.GONE
                Unit
            }

        if (screenBottomNavigationLineAllowed)
            ToolsView.fromAlpha(vLine!!, if (screenBottomNavigationAnimation) ToolsView.ANIMATION_TIME else 0)
        else
            ToolsView.toAlpha(vLine!!, if (screenBottomNavigationAnimation) ToolsView.ANIMATION_TIME else 0) {
                vLine!!.visibility = View.GONE
                Unit
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
        return widgetMenu!!.add(text) { w, c -> onClick.invoke() }.icon(icon)
    }

    override fun getLayout(): Int {
        return R.layout.screen_activity_bottom_navigation
    }

    //
    //   Navigation Item
    //

    class NavigationItem(context: Context) {

        val view: View = ToolsView.inflate(context, R.layout.screen_activity_bottom_navigation_item)
        val vIcon: ViewIcon
        val vChip: ViewChip

        init {
            vIcon = view.findViewById(R.id.icon)
            vChip = view.findViewById(R.id.chip)
        }

    }


}