package com.sup.dev.android.libs.screens.activity;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.design.widget.CoordinatorLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.sup.dev.android.R;
import com.sup.dev.android.libs.screens.Navigator;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.views.ViewIcon;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public abstract class SActivityBottomNavigationAbstract extends SActivity {

    protected LinearLayout vContainerRoot;
    protected LinearLayout vContainer;
    private boolean navigationVisible = true;
    private boolean screenBottomNavigationAllowed = true;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        vContainerRoot = findViewById(R.id.screen_activity_bottom_navigation_container_root);
        vContainer = findViewById(R.id.screen_activity_bottom_navigation_container);

        updateNavigationVisible();
    }

    public ViewIcon addIcon(@DrawableRes int icon, Callback1<View> onClick) {
        return addIcon(vContainer.getChildCount(), icon, onClick);
    }

    protected ViewIcon addIcon(int position, @DrawableRes int icon, Callback1<View> onClick) {
        ViewIcon viewIcon = ToolsView.inflate(this, R.layout.view_icon_toolbar);
        viewIcon.setImageResource(icon);
        viewIcon.setOnClickListener(onClick::callback);
        vContainer.addView(viewIcon, position);
        ((LinearLayout.LayoutParams) viewIcon.getLayoutParams()).weight = 1;
        ((LinearLayout.LayoutParams) viewIcon.getLayoutParams()).gravity = Gravity.CENTER;
        return viewIcon;
    }

    public void setNavigationVisible(boolean b) {
        navigationVisible = b;
        updateNavigationVisible();
    }

    @Override
    public void setScreen(Screen screen, Navigator.Animation animation) {
        super.setScreen(screen, animation);
        screenBottomNavigationAllowed = screen.isBottomNavigationAllowed();
        updateNavigationVisible();
    }

    private void updateNavigationVisible(){
        boolean b = navigationVisible && screenBottomNavigationAllowed;
        vContainerRoot.setVisibility(b ? View.VISIBLE : View.GONE);
        ((CoordinatorLayout.LayoutParams) vActivityContainer.getLayoutParams()).setMargins(0,0,0,b?vContainerRoot.getHeight():0);
    }

}