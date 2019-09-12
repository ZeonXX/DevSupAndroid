package com.sup.dev.android.libs.screens.activity

import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutFrameMeasureCallback
import com.sup.dev.android.views.widgets.WidgetMenu
import com.sup.dev.java.tools.ToolsThreads

open class SActivityTypeBottomNavigation(
        activity:SActivity
) : SActivityType(activity) {

    companion object {

        fun setShadow(view: View) {
            view.background = GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(0x30000000, 0x00000000))
        }

    }

    private var widgetMenu: WidgetMenu? = null

    private var lastH_P = 0
    private var maxH_P = 0
    private var lastH_L = 0
    private var maxH_L = 0
    private var skipNextNavigationAnimation = false

    private var vContainer: LinearLayout? = null
    private var vLine: View? = null
    private var extraNavigationItem:SActivityType.NavigationItem? = null

    override fun getLayout() = R.layout.screen_activity_bottom_navigation

    override fun onCreate() {

        vContainer = activity.findViewById(R.id.vScreenActivityBottomNavigationContainer)
        vLine = activity.findViewById(R.id.vScreenActivityBottomNavigationLine)

        updateNavigationVisible()
        setShadow(vLine!!)

        (activity.vActivityRoot as LayoutFrameMeasureCallback).onMeasure = { _, h ->
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

    override fun updateNavigationVisible() {
        if (screenNavigationAllowed && screenNavigationVisible) {
            if (screenHideBottomNavigationWhenKeyboard && isKeyboardShown()) {
                vContainer!!.visibility = View.GONE
                vLine!!.visibility = View.GONE
                skipNextNavigationAnimation  = true
            } else {
                ToolsView.fromAlpha(vContainer!!, if (screenNavigationAnimation && !skipNextNavigationAnimation) ToolsView.ANIMATION_TIME else 0)
                if (screenNavigationShadowAvailable) {
                    ToolsView.fromAlpha(vLine!!, if (screenNavigationAnimation && !skipNextNavigationAnimation) ToolsView.ANIMATION_TIME else 0)
                } else {
                    ToolsView.toAlpha(vLine!!, if (screenNavigationAnimation && !skipNextNavigationAnimation) ToolsView.ANIMATION_TIME else 0) {
                        vLine!!.visibility = if (screenNavigationAllowed) View.INVISIBLE else View.GONE
                    }
                }
            }
        } else {
            ToolsView.toAlpha(vContainer!!, if (screenNavigationAnimation) ToolsView.ANIMATION_TIME else 0) {
                vContainer!!.visibility = if (screenNavigationAllowed) View.INVISIBLE else View.GONE
            }
            ToolsView.toAlpha(vLine!!, if (screenNavigationAnimation) ToolsView.ANIMATION_TIME else 0) {
                vLine!!.visibility = if (screenNavigationAllowed) View.INVISIBLE else View.GONE
            }
        }
    }

    fun isKeyboardShown():Boolean{
        return if(ToolsAndroid.isScreenPortrait()) lastH_P < (maxH_P - ToolsView.dpToPx(50))
        else lastH_L < (maxH_L - ToolsView.dpToPx(50))
    }

    //
    //  Navigation Item
    //

    override fun addNavigationItem(icon: Int, text: String, hided: Boolean, useIconsFilters: Boolean, onClick: (View) -> Unit): SActivityType.NavigationItem {
        if(hided){
            val item = NavigationItem()
            if(widgetMenu == null) {
                widgetMenu = WidgetMenu()
                extraNavigationItem = addNavigationItem(R.drawable.ic_menu_white_24dp, "", false, useIconsFilters) { widgetMenu!!.asSheetShow() }
            }
            val menuItem = widgetMenu!!.add(text) { _, c -> onClick.invoke(c.getView()!!) }.icon(icon)
           // if(useIconsFilters) menuItem.iconFilter(ToolsResources.getColorAttr(R.attr.toolbar_content_color_secondary))
            widgetMenu!!.finishItemBuilding()
            item.menuIndex = widgetMenu!!.getItemsCount() - 1
            return item
        } else {

            val item = NavigationItem()

            item.view = ToolsView.inflate(activity, R.layout.layout_bottom_navigation_icon)
            item.vIcon = item.view?.findViewById(R.id.vNavigationItemIcon)
            item.vChip = item.view?.findViewById(R.id.vNavigationItemChip)
            item.vText = item.view?.findViewById(R.id.vNavigationItemText)

            item.vIcon?.setImageResource(icon)
            if(useIconsFilters)item.vIcon?.setFilter(ToolsResources.getColorAttr(R.attr.toolbar_content_color))
            item.view?.setOnClickListener(onClick)
            item.vChip?.visibility = View.GONE
            item.vText?.text = text

            vContainer?.addView(item.view)
            (item.view?.layoutParams as LinearLayout.LayoutParams).weight = 1f
            (item.view?.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.CENTER

            return item
        }
    }

    override fun getExtraNavigationItem() = extraNavigationItem

    inner class NavigationItem : SActivityType.NavigationItem() {

        var menuIndex:Int? = null

        override fun setVisible(visible: Boolean) {
            view?.visibility = if(visible) View.VISIBLE else View.GONE
            if(menuIndex != null && widgetMenu != null) widgetMenu!!.setItemVisible(menuIndex!!, visible)
        }

    }
}