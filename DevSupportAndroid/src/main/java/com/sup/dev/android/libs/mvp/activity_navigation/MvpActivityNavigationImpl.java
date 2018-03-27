package com.sup.dev.android.libs.mvp.activity_navigation;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.activity.MvpActivityImpl;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.utils.interfaces.UtilsIntent;
import com.sup.dev.android.utils.interfaces.UtilsView;


public class MvpActivityNavigationImpl extends MvpActivityImpl implements DrawerLayout.DrawerListener, MvpActivityNavigation {

    private static boolean navigationLock;

    private UtilsView utilsView = SupAndroid.di.utilsView();

    private DrawerLayout drawerLayout;
    private ViewGroup drawerContainer;
    private ViewGroup bottomDrawerContainer;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        drawerLayout = findViewById(R.id.drawer);
        drawerContainer = findViewById(R.id.drawer_content);
        bottomDrawerContainer = findViewById(R.id.bottom_drawer_container);
        drawerLayout.setDrawerListener(this);
        setNavigationLock(navigationLock);
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
        if(navigator.hasBackStack())
            super.onFragmentBackPressed();
        else
            showDrawer();
    }

    //
    //  Fragments
    //

    @Override
    public void addFragment(Fragment fragment, String key, boolean animate) {
        super.addFragment(fragment, key, animate);
        utilsView.hideKeyboard(this);
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

    public void clearDrawer(){
        drawerContainer.removeAllViews();
        bottomDrawerContainer.removeAllViews();
    }

    public void addDrawerView(View view) {
        drawerContainer.addView(view);
    }

    public void addDrawerMenu(DrawerMenuItem menuItem) {
        drawerContainer.addView(menuItem.getView());
    }

    public void addDrawerViewBottom(View view) {
        bottomDrawerContainer.addView(view);
    }

    public void addDrawerMenuBottom(DrawerMenuItem menuItem) {
        bottomDrawerContainer.addView(menuItem.getView());
    }


    @Override
    public void setNavigationLock(boolean lock) {
        MvpActivityNavigationImpl.navigationLock = lock;
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
            utilsView.hideKeyboard(this);
    }
}
