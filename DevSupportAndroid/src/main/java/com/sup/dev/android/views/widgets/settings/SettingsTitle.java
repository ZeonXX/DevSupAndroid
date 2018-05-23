package com.sup.dev.android.views.widgets.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;

public class SettingsTitle extends Settings {

    private final TextView vTitle;

    public SettingsTitle(@NonNull Context context) {
        this(context, null);
    }


    public SettingsTitle(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, R.layout.settings_title);

        vTitle = findViewById(R.id.dev_sup_title);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingsTitle, 0, 0);
        boolean lineVisible = a.getBoolean(R.styleable.SettingsTitle_SettingsTitle_lineVisible, true);
        String title = a.getString(R.styleable.SettingsTitle_SettingsTitle_title);
        a.recycle();

        setLineVisible(lineVisible);
        setTitle(title);
    }

    //
    //  Setters
    //

    public void setTitle(@StringRes int title) {
        setTitle(ToolsResources.getString(title));
    }

    public void setTitle(String title) {
        vTitle.setText(title);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        vTitle.setEnabled(enabled);
    }
}
