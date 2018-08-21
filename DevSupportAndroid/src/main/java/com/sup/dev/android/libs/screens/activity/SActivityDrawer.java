package com.sup.dev.android.libs.screens.activity;

import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;

import com.sup.dev.android.R;
import com.sup.dev.android.libs.screens.navigator.Navigator;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.tools.ToolsView;

public abstract class SActivityDrawer extends SActivity implements DrawerLayout.DrawerListener{

    private static boolean navigationLock;

    private DrawerLayout drawerLayout;
    private ViewGroup drawerContainer;
    private float lastTranslate = 0.0f;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        drawerLayout = findViewById(R.id.screen_drawer);
        drawerContainer = findViewById(R.id.screen_drawer_container);
        drawerLayout.setDrawerListener(this);
        drawerLayout.setDrawerElevation(0);
        drawerLayout.setScrimColor(0);
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
        if(Navigator.hasBackStack())
            super.onBackPressed();
        else
            showDrawer();
    }

    //
    //  View
    //

    @Override
    public void setScreen(Screen screen, Navigator.Animation animation) {
        super.setScreen(screen, animation);
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
        float moveFactor = (drawerContainer.getWidth() * slideOffset);

        TranslateAnimation anim = new TranslateAnimation(lastTranslate, moveFactor, 0.0f, 0.0f);
        anim.setDuration(0);
        anim.setFillAfter(true);
        vActivityContainer.startAnimation(anim);

        lastTranslate = moveFactor;
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