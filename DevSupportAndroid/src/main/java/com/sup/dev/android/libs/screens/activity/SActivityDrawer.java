package com.sup.dev.android.libs.screens.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.libs.screens.SNavigator;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.tools.ToolsView;

public class SActivityDrawer extends SActivity implements DrawerLayout.DrawerListener{

    private static boolean navigationLock;

    private DrawerLayout drawerLayout;
    private ViewGroup drawerContainer;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        drawerLayout = findViewById(R.id.screen_drawer);
        drawerContainer = findViewById(R.id.screen_drawer_container);
        drawerLayout.setDrawerListener(this);
        setNavigationLock(navigationLock);

        setDrawerView(ToolsView.inflate(this, R.layout.screen_activity_navigation_driver));
    }

    @Override
    protected int getLayout(){
        return R.layout.screen_activity_navigation;
    }

    //
    //  Events
    //

    @Override
    public void onViewBackPressed() {
        if(SNavigator.hasBackStack())
            super.onBackPressed();
        else
            showDrawer();
    }

    //
    //  View
    //

    @Override
    public void setView(Screen view) {
        super.setView(view);
        ToolsView.hideKeyboard();
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
        SActivityDrawer.navigationLock = lock;
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
            ToolsView.hideKeyboard();
    }
}