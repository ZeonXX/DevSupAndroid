package com.sup.dev.android.libs.mvp.activity_navigation;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.libs.mvp.activity.MvpActivity;
import com.sup.dev.android.libs.mvp.fragments.MvpFragment;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.tools.ToolsView;

public class MvpActivityNavigation extends MvpActivity implements DrawerLayout.DrawerListener{

    private static boolean navigationLock;

    private DrawerLayout drawerLayout;
    private ViewGroup drawerContainer;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        drawerLayout = findViewById(R.id.mvp_drawer);
        drawerContainer = findViewById(R.id.mvp_drawer_container);
        drawerLayout.setDrawerListener(this);
        setNavigationLock(navigationLock);

        setDrawerView(ToolsView.inflate(this, R.layout.mvp_activity_navigation_driver));
    }

    @Override
    protected int getLayout(){
        return R.layout.mvp_activity_navigation;
    }

    //
    //  Events
    //

    @Override
    public void onFragmentBackPressed() {
        if(MvpNavigator.hasBackStack())
            super.onFragmentBackPressed();
        else
            showDrawer();
    }

    //
    //  Fragments
    //


    @Override
    public void setFragment(MvpFragment fragment) {
        super.setFragment(fragment);
        ToolsView.hideKeyboard(this);
        hideDrawer();
    }

    //
    //  Navigation Drawer
    //


    public void hideDrawer() {
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void showDrawer() {
        drawerLayout.openDrawer(GravityCompat.START);
        drawerLayout.requestFocus();
    }

    public void setDrawerView(View v){
        drawerContainer.removeAllViews();
        drawerContainer.addView(v);
    }

    public void setNavigationLock(boolean lock) {
        MvpActivityNavigation.navigationLock = lock;
        if (lock)
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        else
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
    }

    @Override
    public void onDrawerSlide(View drawerView, float slideOffset) {

    }

    @Override
    public void onDrawerOpened(View drawerView) {

    }

    @Override
    public void onDrawerClosed(View drawerView) {

    }

    @Override
    public void onDrawerStateChanged(int newState) {
        if (newState == DrawerLayout.STATE_DRAGGING)
            ToolsView.hideKeyboard(this);
    }
}
