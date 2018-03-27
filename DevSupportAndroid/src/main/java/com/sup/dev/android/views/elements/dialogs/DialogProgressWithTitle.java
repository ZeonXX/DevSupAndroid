package com.sup.dev.android.views.elements.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

public class DialogProgressWithTitle extends BaseDialog{

    public DialogProgressWithTitle(Context viewContext) {
        super(viewContext, R.layout.dialog_progress_with_title);
    }

    //
    //  Setters
    //

    public DialogRecycler setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogRecycler)super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogProgressWithTitle setTitle(@StringRes int title) {
        return (DialogProgressWithTitle)super.setTitle(title);
    }

    public DialogProgressWithTitle setTitle(String title) {
        return (DialogProgressWithTitle)super.setTitle(title);
    }

    public DialogProgressWithTitle setCancelable(boolean cancelable) {
        return (DialogProgressWithTitle)super.setCancelable(cancelable);
    }

    public DialogProgressWithTitle setOnCancel(CallbackSource<BaseDialog> onCancel) {
        return (DialogProgressWithTitle)super.setOnCancel(onCancel);
    }

}
