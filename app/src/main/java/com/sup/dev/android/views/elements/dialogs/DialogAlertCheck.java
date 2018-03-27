package com.sup.dev.android.views.elements.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.android.utils.interfaces.UtilsStorage;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

public class DialogAlertCheck extends BaseDialog{

    private final UtilsStorage utilsStorage = SupAndroid.di.utilsStorage();
    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();

    private final String key;

    private final CheckBox vCheck;

    public static boolean check(String key) {
        return SupAndroid.di.utilsStorage().getBoolean(key, false);
    }

    public static void clear(String key) {
        SupAndroid.di.utilsStorage().remove(key);
    }

    public DialogAlertCheck(Context viewContext, String key) {
        super(viewContext, R.layout.dialog_alert_check);
        this.key = key;

        vCheck = view.findViewById(R.id.check_box);

        vCheck.setText(null);

    }

    public DialogAlertCheck setCheckText(@StringRes int text) {
        return setCheckText(utilsResources.getString(text));
    }

    public DialogAlertCheck setCheckText(String text) {
        vCheck.setText(text);
        return this;
    }

    //
    //  Setters
    //

    public DialogAlertCheck setTitle(@StringRes int title) {
        return (DialogAlertCheck)super.setTitle(title);
    }

    public DialogAlertCheck setTitle(String title) {
        return (DialogAlertCheck)super.setTitle(title);
    }

    public DialogAlertCheck setText(@StringRes int text) {
        return (DialogAlertCheck)super.setText(text);
    }

    public DialogAlertCheck setText(String text) {
        return (DialogAlertCheck)super.setText(text);
    }

    public DialogAlertCheck setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogAlertCheck)super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogAlertCheck setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogAlertCheck)super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogAlertCheck setCancelable(boolean cancelable) {
        return (DialogAlertCheck)super.setCancelable(cancelable);
    }

    public DialogAlertCheck setOnCancel(String s) {
        return (DialogAlertCheck)super.setOnCancel(s);
    }

    public DialogAlertCheck setOnCancel(CallbackSource<BaseDialog> onCancel) {
        return (DialogAlertCheck)super.setOnCancel(onCancel);
    }

    public DialogAlertCheck setOnCancel(@StringRes int s) {
        return (DialogAlertCheck)super.setOnCancel(s);
    }

    public DialogAlertCheck setOnCancel(@StringRes int s, CallbackSource<BaseDialog> onCancel) {
        return (DialogAlertCheck)super.setOnCancel(s, onCancel);
    }

    public DialogAlertCheck setOnCancel(String s, CallbackSource<BaseDialog> onCancel) {
        return (DialogAlertCheck)super.setOnCancel(s, onCancel);
    }

    public DialogAlertCheck setOnEnter(@StringRes int s) {
        return (DialogAlertCheck)super.setOnEnter(s);
    }

    public DialogAlertCheck setOnEnter(String s) {
        return (DialogAlertCheck)super.setOnEnter(s);
    }

    public DialogAlertCheck setOnEnter(@StringRes int s, CallbackSource<BaseDialog> onEnter) {
        return (DialogAlertCheck)super.setOnEnter(s, onEnter);
    }

    public DialogAlertCheck setOnEnter(String s, CallbackSource<BaseDialog> onEnter) {
        super.setOnEnter(s, d->{
            utilsStorage.put(key, vCheck.isChecked());
            if (onEnter != null) onEnter.callback(this);
        });
        return this;
    }

    public DialogAlertCheck setEnabled(boolean enabled) {
        vCheck.setEnabled(enabled);
        return (DialogAlertCheck)super.setEnabled(enabled);
    }


}

