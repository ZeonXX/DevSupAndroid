package com.sup.dev.android.libs.screens.activity;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.view.View;

import com.sup.dev.android.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.views.views.ViewIcon;
import com.sup.dev.android.views.widgets.WidgetMenu;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public abstract class SActivityBottomNavigationExtra extends SActivityBottomNavigationAbstract {

    private WidgetMenu widgetMenu;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);

        widgetMenu= new WidgetMenu();

        addIcon(0, ToolsResources.getDrawableFromAttrId(R.attr.ic_menu), v -> {
            widgetMenu.asSheetShow();
        });

    }

    public void clearNavigation(){
        widgetMenu.clear();
    }

    public void addNavigationView(View view){
        widgetMenu.addView(view);
    }

    public void hideNavigation(){
        widgetMenu.hide();
    }

    public void addNavigationItem(@StringRes int text, @DrawableRes int icon, Callback onClick){
        widgetMenu.add(text, w -> onClick.callback()).icon(icon);
    }

    @Override
    protected int getLayout() {
        return R.layout.screen_activity_bottom_navigation;
    }

    public ViewIcon addIcon(@DrawableRes int icon, Callback1<View> onClick) {
        return addIcon(vContainer.getChildCount() - 1, icon, onClick);
    }


}