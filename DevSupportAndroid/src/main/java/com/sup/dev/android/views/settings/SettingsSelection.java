package com.sup.dev.android.views.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.views.widgets.WidgetMenu;

public class SettingsSelection extends SettingsAction{

    private final WidgetMenu widgetMenu;

    public SettingsSelection(@NonNull Context context) {
        this(context, null);
    }

    public SettingsSelection(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        widgetMenu = new WidgetMenu();
        widgetMenu.showPopupWhenClick(view);
        widgetMenu.setOnGlobalSelected((w,t) -> setSubtitle(t));

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingsSelection, 0, 0);
        boolean lineVisible = a.getBoolean(R.styleable.SettingsSelection_SettingsSelection_lineVisible, true);
        String title = a.getString(R.styleable.SettingsSelection_SettingsSelection_title);
        String subtitle = a.getString(R.styleable.SettingsSelection_SettingsSelection_subtitle);
        int icon = a.getResourceId(R.styleable.SettingsSelection_SettingsSelection_icon, 0);
        int iconBackground = a.getColor(R.styleable.SettingsSelection_SettingsSelection_icon_background, 0);
        setIconBackground(iconBackground);
        a.recycle();

        setLineVisible(lineVisible);
        setTitle(title);
        setSubtitle(subtitle==null?"-":subtitle);
        setIcon(icon);
    }

    public WidgetMenu getMenu() {
        return widgetMenu;
    }

}
