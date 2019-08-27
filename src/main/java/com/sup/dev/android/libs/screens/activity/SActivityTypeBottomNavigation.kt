package com.sup.dev.android.libs.screens.activity

import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutFrameMeasureCallback
import com.sup.dev.android.views.widgets.WidgetMenu
import com.sup.dev.java.tools.ToolsThreads

class SActivityTypeBottomNavigation(
        activity:SActivity
) : SActivityType(activity) {

    companion object {

        fun setShadow(view: View) {
            view.setBackgroundDrawable(GradientDrawable(GradientDrawable.Orientation.BOTTOM_TOP, intArrayOf(0x30000000, 0x00000000)))
        }

    }

    private var widgetMenu: WidgetMenu? = null

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

    private var vContainer: LinearLayout? = null
    private var vLine: View? = null
    private var extraNavigationItem:NavigationItem? = null

    override fun getLayout() = R.layout.screen_activity_bottom_navigation

    override fun onCreate() {

        vContainer = activity.findViewById(R.id.vScreenActivityBottomNavigationContainer)
        vLine = activity.findViewById(R.id.vScreenActivityBottomNavigationLine)

        updateNavigationVisible()
        setShadow(vLine!!)

        (activity.vActivityRoot as LayoutFrameMeasureCallback).onMeasure = { w, h ->
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

    override fun onSetScreen(screen: Screen?) {
        if (screen != null) {
            screenBottomNavigationVisible = screen.isBottomNavigationVisible
            screenBottomNavigationAllowed = screen.isBottomNavigationAllowed
            screenBottomNavigationAnimation = screen.isBottomNavigationAnimation
            screenBottomNavigationShadowAvailable = screen.isBottomNavigationShadowAvailable
            screenHideBottomNavigationWhenKeyboard = screen.isHideBottomNavigationWhenKeyboard
            updateNavigationVisible()
        }
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

    fun isKeyboardShown():Boolean{
        return if(ToolsAndroid.isScreenPortrait()) lastH_P < (maxH_P - ToolsView.dpToPx(50))
        else lastH_L < (maxH_L - ToolsView.dpToPx(50))
    }

    //fun setNavigationVisible(b: Boolean) {
    //    navigationVisible = b
    //    updateNavigationVisible()
    //}

    override fun addNavigationItem(icon: Int, text: String, hided: Boolean, onClick: (View) -> Unit): NavigationItem? {
        if(hided){
            if(widgetMenu == null) {
                widgetMenu = WidgetMenu()
                extraNavigationItem = addNavigationItem(ToolsResources.getDrawableAttrId(R.attr.ic_menu_24dp), "", false) { v -> widgetMenu!!.asSheetShow() }
            }
            widgetMenu!!.add(text) { w, c -> onClick.invoke(c.getView()!!) }.icon(icon)
            return null
        } else {
            return super.addNavigationItem(icon, text, hided, onClick)
        }
    }

    override fun addItemNavigationView(view:View){
        vContainer?.addView(view)
        (view.layoutParams as LinearLayout.LayoutParams).weight = 1f
        (view.layoutParams as LinearLayout.LayoutParams).gravity = Gravity.CENTER
    }

    override fun getExtraNavigationItem() = extraNavigationItem

    fun clearNavigation() {
        widgetMenu!!.clear()
    }

    fun hideNavigation() {
        widgetMenu!!.hide()
    }

    override fun getNavigationItemLayout() =  R.layout.screen_activity_bottom_navigation_item

}