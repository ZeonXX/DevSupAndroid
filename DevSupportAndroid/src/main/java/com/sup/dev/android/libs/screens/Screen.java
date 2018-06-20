package com.sup.dev.android.libs.screens;

import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.design.widget.AppBarLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsView;

public class Screen extends FrameLayout{

    private final View view;
    protected boolean backStackAllowed = true;
    protected boolean isAppbarExpanded; /* Обход разворачивания бара при повторном создании вью */

    public Screen(@LayoutRes int layoutRes) {
        this( ToolsView.inflate(SupAndroid.activity, layoutRes));
    }

    public Screen(View view) {
        super(SupAndroid.activity);
        this.view = view;
        addView(view);
    }

    //
    //  LifeCircle
    //

    @CallSuper
    public void onResume() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        if (toolbar != null) {
            toolbar.setNavigationIcon(SNavigator.hasBackStack() ? R.drawable.ic_arrow_back_white_24dp : R.drawable.ic_menu_white_24dp);
            toolbar.setNavigationOnClickListener(v -> SupAndroid.activity.onViewBackPressed());
        } else {
            View v = findViewById(R.id.back);
            if (v != null && v instanceof ImageView) {
                ImageView vBack = (ImageView) v;
                vBack.setImageResource(SNavigator.hasBackStack() ? R.drawable.ic_arrow_back_white_24dp : R.drawable.ic_menu_white_24dp);
                v.setOnClickListener(vv -> SupAndroid.activity.onViewBackPressed());
            }
        }

        AppBarLayout appBarLayout = findViewById(R.id.app_bar);
        if (appBarLayout != null) {
            appBarLayout.addOnOffsetChangedListener((appBarLayout1, verticalOffset) -> isAppbarExpanded = verticalOffset == 0);
        }
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

    public boolean equalsNView(Screen view){
        return this == view;
    }

    public boolean isBackStackAllowed() {
        return true;
    }

}
