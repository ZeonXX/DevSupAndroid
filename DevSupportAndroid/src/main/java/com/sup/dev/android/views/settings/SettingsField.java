package com.sup.dev.android.views.settings;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.views.watchers.TextWatcherChanged;
import com.sup.dev.android.views.widgets.ViewIcon;
import com.sup.dev.java.classes.providers.Provider1;

public class SettingsField extends Settings {

    private final ViewIcon vIcon;
    private final EditText vField;
    private final TextInputLayout vInputLayout;

    private boolean isError;
    private Provider1<String, Boolean> checker;

    public SettingsField(@NonNull Context context) {
        this(context, null);
    }

    public SettingsField(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, R.layout.settings_field);

        vIcon = findViewById(R.id.dev_sup_icon);
        vInputLayout = findViewById(R.id.dev_sup_input_layout);
        vField = findViewById(R.id.dev_sup_field);


        vField.setId(View.NO_ID); //   Чтоб система не востонавливала состояние

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SettingsField, 0, 0);
        String hint = a.getString(R.styleable.SettingsField_SettingsField_hint);
        String text = a.getString(R.styleable.SettingsField_SettingsField_text);
        int inputType = a.getInteger(R.styleable.SettingsField_android_inputType, vField.getInputType());
        int icon = a.getResourceId(R.styleable.SettingsField_SettingsField_icon, 0);
        int maxLength = a.getInteger(R.styleable.SettingsField_SettingsField_maxLength, 0);
        int iconBackground = a.getResourceId(R.styleable.SettingsField_SettingsField_icon_background, 0x01FF0000);
        setIconBackground(iconBackground);
        a.recycle();

        vField.addTextChangedListener(new TextWatcherChanged(s -> checkError()));

        setLineVisible(false);
        setIcon(icon);
        setText(text);
        setHint(hint);
        setInputType(inputType);
        setMaxLength(maxLength);
        vField.clearFocus();
    }

    //
    //  State
    //

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER_STATE", super.onSaveInstanceState());
        bundle.putString("text", vField.getText().toString());
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            setText(bundle.getString("text"));
            state = bundle.getParcelable("superState");
        }
        super.onRestoreInstanceState(state);
    }

    private void checkError() {
        if (checker == null) setError(false);
        if (checker != null) setError(!checker.provide(getText()));
    }

    //
    //  Setters
    //

    public void setError(boolean b) {
        isError = b;
        vField.setError(b ? "" : null);
    }

    public void setErrorChecker(Provider1<String, Boolean> checker) {
        this.checker = checker;
        checkError();
    }

    public void setIcon(@DrawableRes int icon) {
        if (icon == 0) vIcon.setImageBitmap(null);
        else vIcon.setImageResource(icon);
        vIcon.setVisibility(icon == 0 ? View.GONE : View.VISIBLE);
    }

    public void setIconBackground(int color) {
        vIcon.setIconBackgroundColor(color);
    }

    public void setText(String text) {
        vField.setText(text);
        if (text != null) vField.setSelection(text.length());
    }

    public void setHint(@StringRes int hintRes) {
        setHint(ToolsResources.getString(hintRes));
    }

    public void setHint(String hint) {
        vInputLayout.setHint(hint);
    }

    public void setInputType(int inputType) {
        vField.setInputType(inputType);
    }

    public void setMaxLength(int max) {
        vInputLayout.setCounterMaxLength(max);
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        vField.setEnabled(enabled);
        vInputLayout.setEnabled(enabled);
    }

    //
    //  Getters
    //


    public boolean isError() {
        checkError();
        return isError;
    }

    public String getText() {
        return vField.getText().toString();
    }

}