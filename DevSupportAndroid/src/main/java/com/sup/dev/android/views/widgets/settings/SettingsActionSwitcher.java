package com.sup.dev.android.views.widgets.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.sup.dev.android.androiddevsup.R;

public class SettingsActionSwitcher extends SettingsAction {

    private final Switch vSwitcher;

    private OnClickListener onClickListener;
    private boolean salient;

    public SettingsActionSwitcher(@NonNull Context context) {
        this(context, null);
    }

    public SettingsActionSwitcher(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        vSwitcher = new Switch(context);
        vSwitcher.setFocusable(false);
        vSwitcher.setOnCheckedChangeListener((v, b) -> {
            setEnabledSubSettings(b);
            if (!salient) onClick();
        });

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingsActionSwitcher, 0, 0);
        boolean lineVisible = a.getBoolean(R.styleable.SettingsActionSwitcher_SettingsActionSwitcher_lineVisible, true);
        String title = a.getString(R.styleable.SettingsActionSwitcher_SettingsActionSwitcher_title);
        String subtitle = a.getString(R.styleable.SettingsActionSwitcher_SettingsActionSwitcher_subtitle);
        int icon = a.getResourceId(R.styleable.SettingsActionSwitcher_SettingsActionSwitcher_icon, 0);
        boolean checked = a.getBoolean(R.styleable.SettingsActionSwitcher_SettingsActionSwitcher_checked, false);
        int iconBackground = a.getColor(R.styleable.SettingsActionSwitcher_SettingsActionSwitcher_icon_background, 0x01FF0000);
        a.recycle();

        setLineVisible(lineVisible);
        setTitle(title);
        setSubtitle(subtitle);
        setIcon(icon);
        setChecked(checked);
        setSubView(vSwitcher);
        setIconBackground(iconBackground);

        super.setOnClickListener(v -> {
            salient = true;
            vSwitcher.setChecked(!vSwitcher.isChecked());
            salient = false;
            onClick();
        });
    }

    private void onClick() {
        if (onClickListener != null) onClickListener.onClick(this);
    }

    //
    //  State
    //

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER_STATE", super.onSaveInstanceState());
        bundle.putBoolean("checked", isChecked());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            salient = true;
            setChecked(bundle.getBoolean("checked"));
            salient = false;
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    //
    //  Setters
    //

    @Override
    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }


    public void setChecked(boolean checked) {
        salient = true;
        vSwitcher.setChecked(checked);
        salient = false;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        vSwitcher.setEnabled(enabled);
    }

    //
    //  Getters
    //

    public boolean isChecked() {
        return vSwitcher.isChecked();
    }


}
