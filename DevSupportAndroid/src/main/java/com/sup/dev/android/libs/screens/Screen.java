package com.sup.dev.android.libs.screens;

import android.graphics.drawable.Drawable;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.screens.activity.SActivityDrawer;
import com.sup.dev.android.libs.screens.navigator.Navigator;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;

public class Screen extends FrameLayout {

    private final View view;
    protected boolean backStackAllowed = true;
    protected boolean hasBackIcon = true;
    protected boolean bottomNavigationAllowed = true;
    protected boolean bottomNavigationAnimation = true;
    protected boolean bottomNavigationLineAllowed = true;
    protected boolean singleInstanceInBackstack = true;
    protected boolean isAppbarExpanded; /* Обход разворачивания бара при повторном создании вью */

    public Screen(@LayoutRes int layoutRes) {
        this(ToolsView.inflate(SupAndroid.activity, layoutRes));
    }

    public Screen(View view) {
        super(SupAndroid.activity);
        this.view = view;
        addView(view);
    }

    protected void removeAppbar() {
        findViewById(R.id.app_bar).setVisibility(GONE);
    }

    protected void removeAppbarNavigation() {
        hasBackIcon = false;
        onResume();
    }

    //
    //  LifeCircle
    //

    @CallSuper
    public void onResume() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitleTextColor(ToolsResources.getColorFromAttr(R.attr.toolbarTitleColor));
            if (hasBackIcon) {
                toolbar.setNavigationIcon(getNavigationDrawable());
                toolbar.setNavigationOnClickListener(v -> SupAndroid.activity.onViewBackPressed());
            } else {
                toolbar.setNavigationIcon(null);
            }
        } else {
            View v = findViewById(R.id.back);
            if (v != null && v instanceof ImageView) {
                ImageView vBack = (ImageView) v;
                vBack.setImageDrawable(getNavigationDrawable());
                v.setOnClickListener(vv -> SupAndroid.activity.onViewBackPressed());
            }
        }

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        if (appBarLayout != null) {
            appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> isAppbarExpanded = verticalOffset == 0);
        }
    }

    private Drawable getNavigationDrawable() {
        if (!Navigator.hasBackStack() && !(SupAndroid.activity instanceof SActivityDrawer)) return null;
        return Navigator.hasBackStack() ? ToolsResources.getDrawableFromAttr(R.attr.ic_arrow_back) : ToolsResources.getDrawableFromAttr(R.attr.ic_menu);
    }

    @CallSuper
    public void onPause() {

    }

    @CallSuper
    public void onDestroy() {

    }

    @CallSuper
    public void onConfigChanged() {

    }

    public boolean onBackPressed() {
        return false;
    }

    //
    //  Getters
    //

    public boolean equalsNView(Screen view) {
        return this == view;
    }

    public boolean isBackStackAllowed() {
        return backStackAllowed;
    }

    public boolean isSingleInstanceInBackstack() {
        return singleInstanceInBackstack;
    }

    public boolean isBottomNavigationAllowed() {
        return bottomNavigationAllowed;
    }

    public boolean isBottomNavigationAnimation() {
        return bottomNavigationAnimation;
    }

    public boolean isBottomNavigationLineAllowed() {
        return bottomNavigationLineAllowed;
    }
}
