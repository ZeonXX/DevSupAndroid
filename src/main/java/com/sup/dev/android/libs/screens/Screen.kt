package com.sup.dev.android.libs.screens

import android.graphics.PorterDuff
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.google.android.material.appbar.AppBarLayout
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.activity.SActivity
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView


open class Screen(
        protected val viewScreen: View
) : FrameLayout(SupAndroid.activity!!) {

    companion object {
        var GLOBAL_STATUS_BAR_IS_LIGHT = false
    }

    private var onBackPressed: () -> Boolean = { false }
    private var onHide: () -> Unit = {}

    //  Params
    var toolbarNavigationIcon = R.attr.ic_arrow_back_24dp
    var toolbarContentColor = ToolsResources.getColorAttr(R.attr.toolbar_content_color)
    var isBackStackAllowed = true
    var hasBackIcon = true
    var isSingleInstanceInBackStack = false
    var statusBarColor = ToolsResources.getPrimaryDarkColor(context)
    var statusBarIsLight = GLOBAL_STATUS_BAR_IS_LIGHT
    //  All activity navigation types
    var activityRootBackground = ToolsResources.getColorAttr(R.attr.window_background)
    var isNavigationAllowed = true
    var isNavigationVisible = true
    var isNavigationAnimation = true
    var isNavigationShadowAvailable = true
    //  Bottom navigation
    var isHideBottomNavigationWhenKeyboard = true

    protected var isAppbarExpanded: Boolean = false /* Обход разворачивания бара при повторном создании вью */

    constructor(@LayoutRes layoutRes: Int) : this(ToolsView.inflate<View>(SupAndroid.activity!!, layoutRes))

    init {
        addView(viewScreen)
    }

    protected fun removeAppbar() {
        findViewById<View>(R.id.vAppBar).visibility = View.GONE
    }

    protected fun removeAppbarNavigation() {
        hasBackIcon = false
        onResume()
    }

    //
    //  LifeCircle
    //

    @CallSuper
    open fun onResume() {
        val toolbar: Toolbar? = findViewById(R.id.vToolbar)
        if (toolbar != null) {
            toolbar.setTitleTextColor(toolbarContentColor)
            if (hasBackIcon) {
                toolbar.navigationIcon = getActivity().type.getNavigationDrawable(this)
                toolbar.navigationIcon?.setColorFilter(toolbarContentColor, PorterDuff.Mode.SRC_ATOP)
                toolbar.setNavigationOnClickListener { SupAndroid.activity!!.onViewBackPressed() }
            } else {
                toolbar.navigationIcon = null
            }
        } else {
            val v = findViewById<View>(R.id.vBack)
            if (v != null && v is ImageView) {
                v.setImageDrawable(getActivity().type.getNavigationDrawable(this))
                v.setOnClickListener { SupAndroid.activity!!.onViewBackPressed() }
            }
        }

        val appBarLayout:AppBarLayout? = findViewById(R.id.vAppBar)
        appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset -> isAppbarExpanded = verticalOffset == 0 })
    }

    @CallSuper
    open fun onPause() {

    }

    @CallSuper
    open fun onStackChanged() {

    }

    @CallSuper
    open fun onDestroy() {
        onHide.invoke()
    }

    @CallSuper
    fun onConfigChanged() {

    }

    open fun onBackPressed(): Boolean {
        return onBackPressed.invoke()
    }

    //
    //  Setters
    //

    open fun setTitle(@StringRes title: Int) {
        setTitle(ToolsResources.s(title))
    }

    open fun setTitle(title: String?) {
        (findViewById<View>(R.id.vToolbar) as Toolbar).title = title
    }

    fun setOnBackPressed(onBackPressed: () -> Boolean) {
        this.onBackPressed = onBackPressed
    }

    fun setOnHide(onHide: () -> Unit): Screen {
        this.onHide = onHide
        return this
    }

    fun setScreenColor(color: Int) {
        findViewById<View>(R.id.vScreenRoot).setBackgroundColor(color)
    }

    fun setScreenColorBackground() {
        setScreenColor(ToolsResources.getColorAttr(R.attr.window_background))
    }

    //
    //  Getters
    //

    fun equalsNView(view: Screen): Boolean {
        return this === view
    }

    fun getActivity() = context as SActivity

}
