package com.sup.dev.android.views.settings;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sup.dev.android.R;
import com.sup.dev.android.views.views.ViewIcon;
import com.sup.dev.java.libs.debug.Debug;

public class SettingsAction extends Settings {

    private final ViewIcon vIcon;
    private final TextView vTitle;
    private final TextView vSubtitle;
    private final ViewGroup vSubViewContainer;

    public SettingsAction(@NonNull Context context) {
        this(context, null);
    }

    public SettingsAction(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, R.layout.settings_action);

        vIcon = findViewById(R.id.dev_sup_icon);
        vTitle = findViewById(R.id.dev_sup_title);
        vSubtitle = findViewById(R.id.dev_sup_subtitle);
        vSubViewContainer = findViewById(R.id.dev_sup_container);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingsAction, 0, 0);
        boolean lineVisible = a.getBoolean(R.styleable.SettingsAction_SettingsAction_lineVisible, true);
        String title = a.getString(R.styleable.SettingsAction_SettingsAction_title);
        String subtitle = a.getString(R.styleable.SettingsAction_SettingsAction_subtitle);
        int icon = a.getResourceId(R.styleable.SettingsAction_SettingsAction_icon, 0);
        int iconBackground = a.getColor(R.styleable.SettingsAction_SettingsAction_icon_background, 0);
        a.recycle();

        setLineVisible(lineVisible);
        setTitle(title);
        setSubtitle(subtitle);
        setIcon(icon);
        setIconBackground(iconBackground);
    }

    public void setSubView(View view) {
        vSubViewContainer.removeAllViews();
        if (view != null)
            vSubViewContainer.addView(view);
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        Debug.printStack();
        super.setOnTouchListener(l);
    }

    //
    //  Setters
    //

    public void setTitle(@StringRes int titleRes) {
        setTitle(getContext().getString(titleRes));
    }

    public void setTitle(String title) {
        vTitle.setText(title);
        vTitle.setVisibility(title != null && !title.isEmpty() ? View.VISIBLE : GONE);
    }

    public void setSubtitle(@StringRes int subtitleRes) {
        setSubtitle(getContext().getString(subtitleRes));
    }

    public void setSubtitle(String subtitle) {
        vSubtitle.setText(subtitle);
        vSubtitle.setVisibility(subtitle != null && !subtitle.isEmpty() ? View.VISIBLE : GONE);
    }

    public void setIcon(@DrawableRes int icon) {
        if (icon == 0) vIcon.setImageBitmap(null);
        else vIcon.setImageResource(icon);
        vIcon.setVisibility(icon == 0 ? View.GONE : View.VISIBLE);
    }

    public void setIconBackground(int color) {
        vIcon.setIconBackgroundColor(color);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        vTitle.setEnabled(enabled);
        vSubtitle.setEnabled(enabled);
    }

}
