package com.sup.dev.android.views.widgets.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsView;

public class SettingsActionCheckBox extends SettingsAction {

    private final UtilsView utilsView = SupAndroid.di.utilsView();

    private final CheckBox vCheckBox;

    private OnClickListener onClickListener;
    private boolean salient;

    public SettingsActionCheckBox(@NonNull Context context) {
        this(context, null);
    }

    public SettingsActionCheckBox(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        vCheckBox = utilsView.inflate(context, R.layout.w_check_box);
        vCheckBox.setFocusable(false);
        vCheckBox.setOnCheckedChangeListener((v, b) -> {
            if (!salient) onClick();
        });

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingsActionCheckBox, 0, 0);
        boolean lineVisible = a.getBoolean(R.styleable.SettingsActionCheckBox_SettingsActionCheckBox_lineVisible, true);
        String title = a.getString(R.styleable.SettingsActionCheckBox_SettingsActionCheckBox_title);
        String subtitle = a.getString(R.styleable.SettingsActionCheckBox_SettingsActionCheckBox_subtitle);
        int icon = a.getResourceId(R.styleable.SettingsActionCheckBox_SettingsActionCheckBox_icon, 0);
        boolean checked = a.getBoolean(R.styleable.SettingsActionCheckBox_SettingsActionCheckBox_checked, false);
        int iconBackground = a.getResourceId(R.styleable.SettingsActionCheckBox_SettingsActionCheckBox_icon_background, 0x01FF0000);
        a.recycle();

        setLineVisible(lineVisible);
        setTitle(title);
        setSubtitle(subtitle);
        setIcon(icon);
        setChecked(checked);
        setSubView(vCheckBox);
        setIconBackground(iconBackground);

        super.setOnClickListener(v -> {
            salient = true;
            vCheckBox.setChecked(!vCheckBox.isChecked());
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
        vCheckBox.setChecked(checked);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        vCheckBox.setEnabled(enabled);
    }

    //
    //  Getters
    //

    public boolean isChecked() {
        return vCheckBox.isChecked();
    }

}