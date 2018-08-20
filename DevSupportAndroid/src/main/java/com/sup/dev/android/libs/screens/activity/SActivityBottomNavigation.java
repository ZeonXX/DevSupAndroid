package com.sup.dev.android.libs.screens.activity;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.sup.dev.android.R;
import com.sup.dev.android.libs.screens.Navigator;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.views.ViewIcon;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public abstract class SActivityBottomNavigation extends SActivityBottomNavigationAbstract {

    @Override
    protected int getLayout() {
        return R.layout.screen_activity_bottom_navigation;
    }

}