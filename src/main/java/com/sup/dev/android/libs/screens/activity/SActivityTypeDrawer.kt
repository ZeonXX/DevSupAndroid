package com.sup.dev.android.libs.screens.activity

import android.graphics.drawable.Drawable
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView

class SActivityTypeDrawer(
        activity: SActivity
) : SActivityType(activity), DrawerLayout.DrawerListener {

    companion object {
        private var navigationLock: Boolean = false
    }

    private var drawerLayout: DrawerLayout? = null
    private var drawerContainer: ViewGroup? = null
    private var vNavigationRowsContainer: ViewGroup? = null
    private var lastTranslate = 0.0f

    override fun getLayout() = R.layout.screen_activity_navigation

    override fun onCreate() {
        drawerLayout = activity.findViewById(R.id.vScreenDrawer)
        drawerContainer = activity.findViewById(R.id.vScreenDrawerContainer)
        drawerLayout!!.addDrawerListener(this)
        drawerLayout!!.drawerElevation = 0f
        drawerLayout!!.setScrimColor(0)
        setNavigationLock(navigationLock)

        setDrawerView(ToolsView.inflate(activity, R.layout.screen_activity_navigation_driver))
        vNavigationRowsContainer = activity.findViewById(R.id.vNavigationRowsContainer)
    }

    override fun onSetScreen(screen: Screen?) {
        super.onSetScreen(screen)
        hideDrawer()
    }

    override fun onViewBackPressed() {
        if (Navigator.hasBackStack())
            activity.onBackPressed()
        else
            showDrawer()
    }

    override fun getNavigationDrawable(screen: Screen): Drawable? {
        return if (Navigator.hasBackStack()) ToolsResources.getDrawableAttr(R.attr.ic_arrow_back_24dp) else ToolsResources.getDrawableAttr(R.attr.ic_menu_24dp)
    }

    override fun updateNavigationVisible() {
        if (screenNavigationAllowed && screenNavigationVisible) {
            setNavigationLock(false)
        } else {
            hideDrawer()
            setNavigationLock(true)
        }
    }


    //
    //  Navigation Drawer
    //

    fun hideDrawer() {
        drawerLayout!!.closeDrawer(GravityCompat.START)
    }

    fun showDrawer() {
        drawerLayout!!.openDrawer(GravityCompat.START)
        drawerLayout!!.requestFocus()
    }

    fun setDrawerView(v: View) {
        drawerContainer!!.removeAllViews()
        drawerContainer!!.addView(v)
    }

    fun setNavigationLock(lock: Boolean) {
        navigationLock = lock
        if (lock)
            drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        else
            drawerLayout!!.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
        val moveFactor = drawerContainer!!.width * slideOffset

        val anim = TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f)
        anim.duration = 0
        anim.fillAfter = true
        activity.vActivityContainer!!.startAnimation(anim)

        lastTranslate = moveFactor
    }

    override fun onDrawerOpened(drawerView: View) {

    }

    override fun onDrawerClosed(drawerView: View) {

    }

    override fun onDrawerStateChanged(newState: Int) {
        if (newState == DrawerLayout.STATE_DRAGGING)
            ToolsView.hideKeyboard()
    }

    //
    //  Navigation Item
    //

    override fun addNavigationView(view: View, useIconsFilters: Boolean) {
        vNavigationRowsContainer?.addView(view)
    }

    override fun addNavigationDivider() {
        vNavigationRowsContainer?.addView(ToolsView.inflate(R.layout.z_divider))
    }

    override fun addNavigationItem(icon: Int, text: String, hided: Boolean, useIconsFilters: Boolean, onClick: (View) -> Unit, onLongClick: ((View) -> Unit)?): SActivityType.NavigationItem {
        val item = NavigationItem()

        item.view = ToolsView.inflate(activity, R.layout.screen_activity_navigation_driver_row)
        item.vIcon = item.view?.findViewById(R.id.vNavigationItemIcon)
        item.vChip = item.view?.findViewById(R.id.vNavigationItemChip)
        item.vText = item.view?.findViewById(R.id.vNavigationItemText)

        item.vIcon?.setImageResource(icon)
        if (useIconsFilters) item.vIcon?.setColorFilter(ToolsResources.getColorAttr(R.attr.toolbar_content_color))
        item.view?.setOnClickListener(onClick)
        if (onLongClick != null) item.view?.setOnLongClickListener { onLongClick.invoke(it); return@setOnLongClickListener true }
        item.vChip?.visibility = View.GONE
        item.vText?.text = text

        vNavigationRowsContainer?.addView(item.view)

        return item
    }

    inner class NavigationItem : SActivityType.NavigationItem() {

        override fun setVisible(visible: Boolean) {
            view?.visibility = if (visible) View.VISIBLE else View.GONE
        }

    }
}