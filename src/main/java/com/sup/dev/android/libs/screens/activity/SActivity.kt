package com.sup.dev.android.libs.screens.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.interpolator.view.animation.LinearOutSlowInInterpolator
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.*
import com.sup.dev.android.views.views.draw_animations.ViewDrawAnimations
import com.sup.dev.java.classes.Subscription
import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.tools.ToolsThreads
import java.util.*
import com.sup.dev.android.R
import com.sup.dev.android.views.splash.SplashView


abstract class SActivity : AppCompatActivity() {

    companion object {
        var onUrlClicked: ((String) -> Unit)? = null
    }

    var started = false

    var isFullScreen = false
    var screenStatusBarIsLight = 0
    var screenStatusBarColor = 0

    var vActivityRoot: View? = null
    var vActivityDrawAnimations: ViewDrawAnimations? = null
    var vActivityContainer: ViewGroup? = null
    var vSplashContainer: ViewGroup? = null
    var vActivityTouchLock: View? = null
    var parseNotifications = true
    var type = getDefaultType()

    override fun onCreate(bundle: Bundle?) {
        super.onCreate(bundle)
        SupAndroid.activity = this

        applyTheme()

        setContentView(type.getLayout())
        vActivityRoot = findViewById(R.id.vActivityRoot)
        vActivityDrawAnimations = findViewById(R.id.vActivityDrawAnimations)
        vActivityContainer = findViewById(R.id.vScreenActivityView)
        vActivityTouchLock = findViewById(R.id.vScreenActivityTouchLock)
        vSplashContainer = findViewById(R.id.vSplashContainer)

        vActivityTouchLock!!.visibility = View.GONE

        type.onCreate()

        ToolsThreads.main(true) {
            if (parseIntent(intent)) intent = Intent()
        }
    }

    protected open fun getDefaultType(): SActivityType = SActivityTypeSimple(this)

    override fun onStart() {
        super.onStart()

        SupAndroid.activityIsVisible = true

        if (!started) {
            started = true
            onFirstStart()
        } else {
            Navigator.resetCurrentView()
        }

        if (Navigator.getStackSize() == 0) toMainScreen()

    }

    override fun onStop() {
        super.onStop()
        SupAndroid.activityIsVisible = false
        Navigator.onActivityStop()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Navigator.onActivityConfigChanged()
    }

    protected open fun applyTheme() {
    }

    protected open fun onFirstStart() {

    }

    abstract fun toMainScreen()

    fun getViewRoot(): View? {
        return vActivityRoot
    }

    fun getViewContainer(): View? {
        return vActivityContainer
    }

    override fun startActivity(intent: Intent) {
        if (TextUtils.equals(intent.action, Intent.ACTION_VIEW) && onUrlClicked != null && intent.data != null) {
            onUrlClicked!!.invoke(intent.data!!.toString())
        } else {
            super.startActivity(intent)
        }
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        if (parseIntent(intent)) setIntent(Intent())
    }

    private fun parseIntent(intent: Intent?): Boolean {
        if (parseNotifications && intent != null) return ToolsNotifications.parseNotification(intent)
        return false
    }

    //
    //  Events
    //

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        ToolsIntent.onActivityResult(requestCode, resultCode, intent)
        super.onActivityResult(requestCode, resultCode, intent)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        ToolsPermission.onRequestPermissionsResult(requestCode, permissions, grantResults.toTypedArray())
    }

    open fun onViewBackPressed() {
        type.onViewBackPressed()
    }

    override fun onBackPressed() {
        if (!Navigator.onBackPressed() && !onLastBackPressed()) {
            started = false
            finish()
        }
    }

    open fun onLastBackPressed(): Boolean {
        return false
    }

    //
    //  Sheet
    //

    fun addSplash(splash: SplashView<out Any>) {
        splash.getView().tag = splash
        ToolsThreads.main {
            ToolsView.hideKeyboard()
            splash.getView().visibility = View.INVISIBLE
            vSplashContainer!!.addView(splash.getView())
            ToolsView.fromAlpha(splash.getView(), 200) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) window.navigationBarColor = splash.getNavigationBarColor()
            }
        }
    }

    fun removeSplash(splash: SplashView<out Any>) {
        ToolsThreads.main {
            ToolsView.hideKeyboard()
            ToolsView.toAlpha(splash.getView(), 200) {
                vSplashContainer!!.removeView(splash.getView())
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && vSplashContainer!!.childCount == 0 && Navigator.getCurrent() != null) window.navigationBarColor = Navigator.getCurrent()!!.navigationBarColor
            }
        }
    }

    fun isSplashShowed(splash: SplashView<out Any>) = vSplashContainer!!.indexOfChild(splash.getView()) > -1

    fun isTopSplash(splash: SplashView<out Any>) = vSplashContainer!!.childCount > 0 && vSplashContainer!!.indexOfChild(splash.getView()) == (vSplashContainer!!.childCount - 1)

    //
    //  Screens
    //

    private var subscriptionTouchLock: Subscription? = null

    open fun setScreen(screen: Screen?, a: Navigator.Animation, hideDialogs: Boolean) {
        var animation = a
        type.onSetScreen(screen)

        if (screen == null) {
            finish()
            return
        }

        if (hideDialogs && vSplashContainer != null)
            for (i in vSplashContainer!!.childCount - 1 downTo 0) {
                val splash = vSplashContainer!!.getChildAt(i).tag as SplashView<out Any>
                removeSplash(splash)
                if (splash.isDestroyScreenAnimation()) animation = Navigator.Animation.NONE
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            screenStatusBarColor = screen.statusBarColor
            if (window.statusBarColor != screenStatusBarColor) window.statusBarColor = screenStatusBarColor
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!isFullScreen) {
                screenStatusBarIsLight = if (screen.statusBarIsLight) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_VISIBLE
                if (window.decorView.systemUiVisibility != screenStatusBarIsLight) window.decorView.systemUiVisibility = screenStatusBarIsLight
            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.navigationBarColor = screen.navigationBarColor
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (screen.navigationBarIsLight) screen.systemUiVisibility = screen.systemUiVisibility or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
            else screen.systemUiVisibility = screen.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR.inv()
        }

        ToolsView.hideKeyboard()

        val oldViews = ArrayList<View>()
        for (i in 0 until vActivityContainer!!.childCount) {
            if (vActivityContainer!!.getChildAt(i) != screen) {
                oldViews.add(vActivityContainer!!.getChildAt(i))
            }
        }

        val old = if (vActivityContainer!!.childCount == 0) null else vActivityContainer!!.getChildAt(0)

        if (animation !== Navigator.Animation.IN) vActivityContainer!!.addView(ToolsView.removeFromParent(screen), 0)
        else vActivityContainer!!.addView(ToolsView.removeFromParent(screen))

        if (old != null && old !== screen) {

            vActivityTouchLock!!.visibility = View.VISIBLE
            ToolsView.clearAnimation(old)
            ToolsView.clearAnimation(screen)


            if (animation == Navigator.Animation.NONE) animateNone(oldViews)
            if (animation == Navigator.Animation.OUT) animateOut(screen, old, oldViews)
            if (animation == Navigator.Animation.IN) animateIn(screen, oldViews)
            if (animation == Navigator.Animation.ALPHA) animateAlpha(screen, old, oldViews)
        }

        if (subscriptionTouchLock != null) subscriptionTouchLock!!.unsubscribe()
        subscriptionTouchLock = ToolsThreads.main(ToolsView.ANIMATION_TIME.toLong()) {
            vActivityTouchLock!!.visibility = View.GONE
        }

        vActivityRoot?.setBackgroundColor(screen.activityRootBackground)

    }

    private fun animateNone(oldViews: ArrayList<View>) {
        for (v in oldViews) vActivityContainer!!.removeView(v)
    }

    private fun animateAlpha(screen: Screen, old: View, oldViews: ArrayList<View>) {
        screen.visibility = View.INVISIBLE
        ToolsView.toAlpha(old) {
            for (v in oldViews) vActivityContainer!!.removeView(v)
        }
        ToolsView.fromAlpha(screen)
    }

    private fun animateOut(screen: Screen, old: View, oldViews: ArrayList<View>) {
        screen.visibility = View.VISIBLE
        old.animate()
                .alpha(0f)
                .translationX((ToolsAndroid.getScreenW() / 3).toFloat())
                .setDuration(200)
                .setInterpolator(LinearOutSlowInInterpolator())
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        old.animate().setListener(null)
                        old.alpha = 1f
                        old.translationX = 0f
                        for (v in oldViews)
                            try {
                                vActivityContainer!!.removeView(v)
                            } catch (e: IndexOutOfBoundsException) {
                                err(e)
                            }
                    }
                })
    }

    private fun animateIn(screen: Screen, oldViews: ArrayList<View>) {
        screen.alpha = 0f
        screen.translationX = (ToolsAndroid.getScreenW() / 3).toFloat()
        screen.animate()
                .alpha(1f)
                .translationX(0f)
                .setDuration(200)
                .setInterpolator(LinearOutSlowInInterpolator())
                .setListener(object : AnimatorListenerAdapter() {

                    override fun onAnimationEnd(animation: Animator) {
                        screen.animate().setListener(null)
                        screen.alpha = 1f
                        screen.translationX = 0f
                        for (v in oldViews)
                            try {
                                vActivityContainer!!.removeView(v)
                            } catch (e: IndexOutOfBoundsException) {
                                err(e)
                            }
                    }
                })
    }

}
