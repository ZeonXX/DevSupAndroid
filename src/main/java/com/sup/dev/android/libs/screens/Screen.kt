package com.sup.dev.android.libs.screens

import android.graphics.drawable.Drawable
import androidx.annotation.CallSuper
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.google.android.material.appbar.AppBarLayout
import androidx.appcompat.widget.Toolbar
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

    private var onHide: () -> Unit = {}
    var isBackStackAllowed = true
    var hasBackIcon = true
    var isBottomNavigationVisible = true
    var isBottomNavigationAllowed = true
    var isBottomNavigationAnimation = true
    var isBottomNavigationShadowAvailable = true
    var isSingleInstanceInBackStack = false
    var statusBarColor = ToolsResources.getPrimaryDarkColor(context)

    protected var isAppbarExpanded: Boolean = false /* Обход разворачивания бара при повторном создании вью */

    private val navigationDrawable: Drawable?
        get() {
            if (!Navigator.hasBackStack() && SupAndroid.activity !is SActivityDrawer) return null
            return if (Navigator.hasBackStack()) ToolsResources.getDrawableAttr(R.attr.ic_arrow_back_24dp) else ToolsResources.getDrawableAttr(R.attr.ic_menu_24dp)
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
            toolbar.setTitleTextColor(ToolsResources.getColorAttr(R.attr.revers_color))
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

    fun setOnBackPressed(onBackPressed:() -> Boolean){
        this.onBackPressed = onBackPressed
    }

    fun setOnHide(onHide: () -> Unit): Screen {
        this.onHide = onHide
        return this
    }

    //
    //  Getters
    //

    fun equalsNView(view: Screen): Boolean {
        return this === view
    }

}
