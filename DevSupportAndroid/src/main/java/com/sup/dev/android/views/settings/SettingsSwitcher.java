package com.sup.dev.android.views.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Switch;

import com.sup.dev.android.R;

public class SettingsSwitcher extends SettingsAction {

    private final Switch vSwitcher;

    private OnClickListener onClickListener;
    private boolean salient;

    public SettingsSwitcher(@NonNull Context context) {
        this(context, null);
    }

    public SettingsSwitcher(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        vSwitcher = new Switch(context);
        vSwitcher.setFocusable(false);
        vSwitcher.setOnCheckedChangeListener((v, b) -> {
            setEnabledSubSettings(b);
            if (!salient) onClick();
        });

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingsSwitcher, 0, 0);
        boolean lineVisible = a.getBoolean(R.styleable.SettingsSwitcher_SettingsSwitcher_lineVisible, true);
        String title = a.getString(R.styleable.SettingsSwitcher_SettingsSwitcher_title);
        String subtitle = a.getString(R.styleable.SettingsSwitcher_SettingsSwitcher_subtitle);
        int icon = a.getResourceId(R.styleable.SettingsSwitcher_SettingsSwitcher_icon, 0);
        boolean checked = a.getBoolean(R.styleable.SettingsSwitcher_SettingsSwitcher_checked, false);
        int iconBackground = a.getColor(R.styleable.SettingsSwitcher_SettingsSwitcher_icon_background, 0);
        a.recycle();

        setLineVisible(lineVisible);
        setTitle(title);
        setSubtitle(subtitle);
        setIcon(icon);
        setChecked(checked);
        setSubView(vSwitcher);
        setIconBackground(iconBackground);

        super.setOnClickListener(v -> {
            setChecked(!vSwitcher.isChecked());
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
            state = bundle.getParcelable("SUPER_STATE");
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
        setEnabledSubSettings(checked);
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
