package com.sup.dev.android.views.elements.dialogs;


import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

public class DialogAlert extends BaseDialog{

    public DialogAlert(Context viewContext) {
        super(viewContext, R.layout.dialog_alert);
    }

    //
    //  Setters
    //

    public DialogAlert addLine(String text) {
        CharSequence s = vText.getText();
        vText.setVisibility(View.VISIBLE);
        if (s.length() == 0)
            vText.setText(text);
        else
            vText.setText(s + "\n" + text);
        return this;
    }


    public DialogAlert setTitle(@StringRes int title) {
        return (DialogAlert)super.setTitle(title);
    }

    public DialogAlert setTitle(String title) {
        return (DialogAlert)super.setTitle(title);
    }

    public DialogAlert setText(@StringRes int text) {
        return (DialogAlert)super.setText(text);
    }

    public DialogAlert setText(CharSequence text) {
        return (DialogAlert)super.setText(text);
    }

    public DialogAlert setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogAlert)super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogAlert setCancelable(boolean cancelable) {
        return (DialogAlert)super.setCancelable(cancelable);
    }

    public DialogAlert setOnEnter(@StringRes int s) {
        return (DialogAlert)super.setOnEnter(s);
    }

    public DialogAlert setOnEnter(String s) {
        return (DialogAlert)super.setOnEnter(s);
    }

    public DialogAlert setOnEnter(@StringRes int s, CallbackSource<BaseDialog> onEnter) {
        return (DialogAlert)super.setOnEnter(s, onEnter);
    }

    public DialogAlert setOnEnter(String s, CallbackSource<BaseDialog> onEnter){
        return (DialogAlert)super.setOnEnter(s, onEnter);
    }

    public DialogAlert setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogAlert)super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogAlert setOnCancel(String s) {
        return (DialogAlert)super.setOnCancel(s);
    }

    public DialogAlert setOnCancel(CallbackSource<BaseDialog> onCancel) {
        return (DialogAlert)super.setOnCancel(onCancel);
    }

    public DialogAlert setOnCancel(@StringRes int s) {
        return (DialogAlert)super.setOnCancel(s);
    }

    public DialogAlert setOnCancel(@StringRes int s, CallbackSource<BaseDialog> onCancel) {
        return (DialogAlert)super.setOnCancel(s, onCancel);
    }

    public DialogAlert setOnCancel(String s, CallbackSource<BaseDialog> onCancel) {
        return (DialogAlert)super.setOnCancel(s, onCancel);
    }

    public DialogAlert setEnabled(boolean enabled) {
        return (DialogAlert)super.setEnabled(enabled);
    }



}
