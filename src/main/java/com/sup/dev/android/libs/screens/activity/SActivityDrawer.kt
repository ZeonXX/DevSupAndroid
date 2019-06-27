package com.sup.dev.android.libs.screens.activity

import android.os.Bundle
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import android.view.View
import android.view.ViewGroup
import android.view.animation.TranslateAnimation
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsView


abstract class SActivityDrawer : SActivity(), androidx.drawerlayout.widget.DrawerLayout.DrawerListener {

    private var drawerLayout: androidx.drawerlayout.widget.DrawerLayout? = null
    private var drawerContainer: ViewGroup? = null
    private var vNavigationRowsContainer: ViewGroup? = null
    private var lastTranslate = 0.0f

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)

        drawerLayout = findViewById(R.id.vScreenDrawer)
        drawerContainer = findViewById(R.id.vScreenDrawerContainer)
        drawerLayout!!.setDrawerListener(this)
        drawerLayout!!.drawerElevation = 0f
        drawerLayout!!.setScrimColor(0)
        setNavigationLock(navigationLock)

        setDrawerView(ToolsView.inflate(this, R.layout.screen_activity_navigation_driver))
        vNavigationRowsContainer = findViewById(R.id.vNavigationRowsContainer)
    }

    override fun getLayout() = R.layout.screen_activity_navigation

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

    fun addView(view:View){
        vNavigationRowsContainer?.addView(view)
    }

    fun addItem(icon:Int, text:String, onClick:(View)->Unit):SNavigationRow{
        val view = SNavigationRow(this, icon, text, onClick)
        addView(view.view)
        return view
    }

    fun addDivider(){
        addView(ToolsView.inflate(R.layout.z_divider_16))
    }

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
        navigationLock = lock
        if (lock)
            drawerLayout!!.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        else
            drawerLayout!!.setDrawerLockMode(androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED)
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
        if (newState == androidx.drawerlayout.widget.DrawerLayout.STATE_DRAGGING)
            ToolsView.hideKeyboard()
    }

    companion object {

        private var navigationLock: Boolean = false
    }
}