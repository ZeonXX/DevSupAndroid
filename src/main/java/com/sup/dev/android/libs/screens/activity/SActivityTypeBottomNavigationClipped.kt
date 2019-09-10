package com.sup.dev.android.libs.screens.activity

import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutFrameMeasureCallback
import com.sup.dev.android.views.views.layouts.LayoutNavigationClipped
import com.sup.dev.android.views.widgets.WidgetMenu
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.tools.ToolsThreads

open class SActivityTypeBottomNavigationClipped(
        activity: SActivity
) : SActivityType(activity) {

    private var widgetMenu: WidgetMenu? = null

    private var lastH_P = 0
    private var maxH_P = 0
    private var lastH_L = 0
    private var maxH_L = 0
    private var skipNextNavigationAnimation = false
    private var visibleItemIndex = 0

    private var vContainer: LayoutNavigationClipped? = null
    private var extraNavigationItem: SActivityType.NavigationItem? = null

    override fun getLayout() = R.layout.screen_activity_bottom_navigation_cliped

    override fun onCreate() {

        vContainer = activity.findViewById(R.id.vScreenActivityBottomNavigationContainer)

        updateNavigationVisible()

        (activity.vActivityRoot as LayoutFrameMeasureCallback).onMeasure = { _, h ->
            if (ToolsAndroid.isScreenPortrait()) {
                lastH_P = View.MeasureSpec.getSize(h)
                if (maxH_P < lastH_P) maxH_P = lastH_P
            } else {
                lastH_L = View.MeasureSpec.getSize(h)
                if (maxH_L < lastH_L) maxH_L = lastH_L
            }
            ToolsThreads.main(true) { updateNavigationVisible() }
        }
    }

    override fun updateNavigationVisible() {
        if (screenNavigationAllowed && screenNavigationVisible) {
            if (screenHideBottomNavigationWhenKeyboard && isKeyboardShown()) {
                vContainer!!.visibility = View.GONE
                skipNextNavigationAnimation = true
            } else {
                ToolsView.fromAlpha(vContainer!!, if (screenNavigationAnimation && !skipNextNavigationAnimation) ToolsView.ANIMATION_TIME else 0)
            }
        } else {
            ToolsView.toAlpha(vContainer!!, if (screenNavigationAnimation) ToolsView.ANIMATION_TIME else 0) {
                vContainer!!.visibility = if (screenNavigationAllowed) View.INVISIBLE else View.GONE
            }
        }
    }

    fun isKeyboardShown(): Boolean {
        return if (ToolsAndroid.isScreenPortrait()) lastH_P < (maxH_P - ToolsView.dpToPx(50))
        else lastH_L < (maxH_L - ToolsView.dpToPx(50))
    }

    //
    //  Navigation Item
    //

    override fun addNavigationItem(icon: Int, text: String, hided: Boolean, useIconsFilters: Boolean, onClick: (View) -> Unit): SActivityType.NavigationItem {
        if (hided) {
            val item = NavigationItem()
            if (widgetMenu == null) {
                widgetMenu = WidgetMenu()
                extraNavigationItem = addNavigationItem(R.drawable.ic_menu_white_24dp, "", false, useIconsFilters) { widgetMenu!!.asSheetShow() }
            }
            widgetMenu!!.add(text) { _, c -> onClick.invoke(c.getView()!!) }.icon(icon)
            //   Не работает. Иконки пропадают     //if(useIconsFilters) .iconFilter(ToolsResources.getColorAttr(R.attr.toolbar_content_color_secondary))
            widgetMenu!!.finishItemBuilding()
            item.menuIndex = widgetMenu!!.getItemsCount() - 1
            return item
        } else {

            val item = NavigationItem()

            if (visibleItemIndex == 2) item.view = vContainer!!.getItemMain()
            else if (visibleItemIndex < 2) item.view = vContainer!!.getItem(visibleItemIndex)
            else if (visibleItemIndex > 2) item.view = vContainer!!.getItem(visibleItemIndex - 1)

            item.vIcon = item.view?.findViewById(R.id.vNavigationItemIcon)
            item.vChip = item.view?.findViewById(R.id.vNavigationItemChip)
            item.vText = item.view?.findViewById(R.id.vNavigationItemText)

            item.vIcon?.setImageResource(icon)
            if (useIconsFilters) item.vIcon?.setFilter(ToolsResources.getColorAttr(R.attr.toolbar_content_color))
            item.view?.setOnClickListener(onClick)
            item.vChip?.visibility = View.GONE
            item.vText?.text = text

            visibleItemIndex++
            return item
        }
    }

    override fun getExtraNavigationItem() = extraNavigationItem

    inner class NavigationItem : SActivityType.NavigationItem() {

        var menuIndex: Int? = null

        override fun setVisible(visible: Boolean) {
            view?.visibility = if (visible) View.VISIBLE else View.GONE
            if (menuIndex != null && widgetMenu != null) widgetMenu!!.setItemVisible(menuIndex!!, visible)
        }

    }
}