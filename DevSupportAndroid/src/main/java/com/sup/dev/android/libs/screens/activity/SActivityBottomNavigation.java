package com.sup.dev.android.libs.screens.activity;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;

import com.sup.dev.android.R;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.views.ViewIcon;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class SActivityBottomNavigation extends SActivity {

    private LinearLayout vContainer;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        vContainer = findViewById(R.id.screen_activity_bottom_navigation_container);
    }

    @Override
    protected int getLayout() {
        return R.layout.screen_activity_bottom_navigation;
    }

    public ViewIcon addIcon(@DrawableRes int icon, Callback1<View> onClick) {
        ViewIcon viewIcon = ToolsView.inflate(this, R.layout.view_icon_toolbar);
        viewIcon.setImageResource(icon);
        viewIcon.setOnClickListener(onClick::callback);
        vContainer.addView(viewIcon);
        ((LinearLayout.LayoutParams)viewIcon.getLayoutParams()).weight = 1;
        ((LinearLayout.LayoutParams)viewIcon.getLayoutParams()).gravity = Gravity.CENTER;
        return viewIcon;
    }

}