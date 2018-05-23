package com.sup.dev.android.views.elements.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.CheckBox;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsStorage;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class DialogAlertCheck extends BaseDialog{

    private final String key;
    private final CheckBox vCheck;

    private boolean lockUntilAccept;

    public static boolean check(String key) {
        return ToolsStorage.getBoolean(key, false);
    }

    public static void clear(String key) {
        ToolsStorage.remove(key);
    }

    public DialogAlertCheck(Context viewContext, String key) {
        super(viewContext, R.layout.dialog_alert_check);
        this.key = key;

        vCheck = view.findViewById(R.id.check_box);

        vCheck.setText(null);
        vCheck.setOnCheckedChangeListener((compoundButton, b) -> updateLock());

    }

    public DialogAlertCheck setCheckText(@StringRes int text) {
        return setCheckText(ToolsResources.getString(text));
    }

    public DialogAlertCheck setCheckText(String text) {
        vCheck.setText(text);
        return this;
    }

    private void updateLock(){
        if(lockUntilAccept)
            vEnter.setEnabled(vCheck.isChecked());
    }

    //
    //  Setters
    //


    public DialogAlertCheck setLockUntilAccept(boolean lockUntilAccept) {
        this.lockUntilAccept = lockUntilAccept;
        updateLock();
        return this;
    }

    public DialogAlertCheck setTitle(@StringRes int title) {
        return (DialogAlertCheck)super.setTitle(title);
    }

    public DialogAlertCheck setTitle(String title) {
        return (DialogAlertCheck)super.setTitle(title);
    }

    public DialogAlertCheck setText(@StringRes int text) {
        return (DialogAlertCheck)super.setText(text);
    }

    public DialogAlertCheck setText(CharSequence text) {
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

    public DialogAlertCheck setOnCancel(Callback1<BaseDialog> onCancel) {
        return (DialogAlertCheck)super.setOnCancel(onCancel);
    }

    public DialogAlertCheck setOnCancel(@StringRes int s) {
        return (DialogAlertCheck)super.setOnCancel(s);
    }

    public DialogAlertCheck setOnCancel(@StringRes int s, Callback1<BaseDialog> onCancel) {
        return (DialogAlertCheck)super.setOnCancel(s, onCancel);
    }

    public DialogAlertCheck setOnCancel(String s, Callback1<BaseDialog> onCancel) {
        return (DialogAlertCheck)super.setOnCancel(s, onCancel);
    }

    public DialogAlertCheck setOnEnter(@StringRes int s) {
        return (DialogAlertCheck)super.setOnEnter(s);
    }

    public DialogAlertCheck setOnEnter(String s) {
        return (DialogAlertCheck)super.setOnEnter(s);
    }

    public DialogAlertCheck setOnEnter(@StringRes int s, Callback1<BaseDialog> onEnter) {
        return (DialogAlertCheck)super.setOnEnter(s, onEnter);
    }

    public DialogAlertCheck setOnEnter(String s, Callback1<BaseDialog> onEnter) {
        super.setOnEnter(s, d->{
            ToolsStorage.put(key, vCheck.isChecked());
            if (onEnter != null) onEnter.callback(this);
        });
        return this;
    }

    public DialogAlertCheck setEnabled(boolean enabled) {
        vCheck.setEnabled(enabled);
        return (DialogAlertCheck)super.setEnabled(enabled);
    }


}

