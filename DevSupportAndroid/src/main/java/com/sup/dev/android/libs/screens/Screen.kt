package com.sup.dev.android.libs.screens

import android.graphics.drawable.Drawable
import android.support.annotation.CallSuper
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.AppBarLayout
import android.support.v7.widget.Toolbar
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.activity.SActivityDrawer
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView


open class Screen(
        protected val viewScreen: View
) : FrameLayout(SupAndroid.activity!!) {

    private var onBackPressed: () -> Boolean = { false }

    var isBackStackAllowed = true
    var hasBackIcon = true
    var isBottomNavigationVisible = true
    var isBottomNavigationAllowed = true
    var isBottomNavigationAnimation = true
    var isBottomNavigationShadowAvliable = true
    var isSingleInstanceInBackstack = false

    protected var isAppbarExpanded: Boolean = false /* Обход разворачивания бара при повторном создании вью */

    private val navigationDrawable: Drawable?
        get() {
            if (!Navigator.hasBackStack() && SupAndroid.activity !is SActivityDrawer) return null
            return if (Navigator.hasBackStack()) ToolsResources.getDrawableFromAttr(R.attr.ic_arrow_back) else ToolsResources.getDrawableFromAttr(R.attr.ic_menu)
        }

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
        val toolbar = findViewById<Toolbar>(R.id.vToolbar)
        if (toolbar != null) {
            toolbar.setTitleTextColor(ToolsResources.getColorFromAttr(R.attr.toolbarTitleColor))
            if (hasBackIcon) {
                toolbar.navigationIcon = navigationDrawable
                toolbar.setNavigationOnClickListener { v -> SupAndroid.activity!!.onViewBackPressed() }
            } else {
                toolbar.navigationIcon = null
            }

        } else {
            val v = findViewById<View>(R.id.vBack)
            if (v != null && v is ImageView) {
                v.setImageDrawable(navigationDrawable)
                v.setOnClickListener { vv -> SupAndroid.activity!!.onViewBackPressed() }
            }
        }

        val appBarLayout = findViewById<AppBarLayout>(R.id.vAppBar)
        appBarLayout?.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout1, verticalOffset ->
            isAppbarExpanded = verticalOffset == 0
        })
    }

    @CallSuper
    open fun onPause() {

    }

    @CallSuper
    open fun onDestroy() {

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

    fun setOnBackPressed(onBackPressed:() -> Boolean){
        this.onBackPressed = onBackPressed
    }

    //
    //  Getters
    //

    fun equalsNView(view: Screen): Boolean {
        return this === view
    }
}
