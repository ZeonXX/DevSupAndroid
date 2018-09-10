package com.sup.dev.android.libs.screens.activity

import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.support.v4.widget.DrawerLayout
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsView


abstract class SActivityDrawer : SActivity(), DrawerLayout.DrawerListener {

    private var drawerLayout: DrawerLayout? = null
    private var drawerContainer: ViewGroup? = null
    private var lastTranslate = 0.0f

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        drawerLayout = findViewById(R.id.screen_drawer)
        drawerContainer = findViewById(R.id.screen_drawer_container)
        drawerLayout!!.setDrawerListener(this)
        drawerLayout!!.drawerElevation = 0f
        drawerLayout!!.setScrimColor(0)
        setNavigationLock(navigationLock)

        setDrawerView(ToolsView.inflate(this, R.layout.screen_activity_navigation_driver))
    }

    override fun getLayout(): Int {
        return R.layout.screen_activity_navigation
    }

    //
    //  Events
    //

    override fun onViewBackPressed() {
        if (Navigator.hasBackStack())
            super.onBackPressed()
        else
            showDrawer()
    }

    //
    //  View
    //

    override fun setScreen(screen: Screen?, animation: Navigator.Animation) {
        super.setScreen(screen, animation)
        hideDrawer()
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
        SActivityDrawer.navigationLock = lock
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
        vActivityContainer!!.startAnimation(anim)

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

    companion object {

        private var navigationLock: Boolean = false
    }
}