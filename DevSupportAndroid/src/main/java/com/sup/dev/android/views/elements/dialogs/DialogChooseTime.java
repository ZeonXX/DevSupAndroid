package com.sup.dev.android.views.elements.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.TimePicker;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback3;

public class DialogChooseTime extends BaseDialog {

    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();

    private final TimePicker vTimePicker;

    public DialogChooseTime(Context viewContext) {
        super(viewContext, R.layout.dialog_choose_time);

        vTimePicker = findViewById(R.id.time_picker);
        vTimePicker.setIs24HourView(true);
    }

    public DialogChooseTime setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogChooseTime) super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogChooseTime setCancelable(boolean cancelable) {
        return (DialogChooseTime) super.setCancelable(cancelable);
    }

    public DialogChooseTime setOnEnter(@StringRes int s) {
        return (DialogChooseTime) super.setOnEnter(s);
    }

    public DialogChooseTime setOnEnter(String s) {
        return (DialogChooseTime) super.setOnEnter(s);
    }

    public DialogChooseTime setOnEnter(@StringRes int s, Callback3<DialogChooseTime, Integer, Integer> onEnter) {
        return setOnEnter(utilsResources.getString(s), onEnter);
    }

    public DialogChooseTime setOnEnter(String s, Callback3<DialogChooseTime, Integer, Integer> onEnter) {
        return (DialogChooseTime) super.setOnEnter(s, d -> onEnter.callback(this, vTimePicker.getCurrentHour(), vTimePicker.getCurrentMinute()));
    }

    public DialogChooseTime setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogChooseTime) super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogChooseTime setOnCancel(String s) {
        return (DialogChooseTime) super.setOnCancel(s);
    }

    public DialogChooseTime setOnCancel(Callback1<BaseDialog> onCancel) {
        return (DialogChooseTime) super.setOnCancel(onCancel);
    }

    public DialogChooseTime setOnCancel(@StringRes int s) {
        return (DialogChooseTime) super.setOnCancel(s);
    }

    public DialogChooseTime setOnCancel(@StringRes int s, Callback1<BaseDialog> onCancel) {
        return (DialogChooseTime) super.setOnCancel(s, onCancel);
    }

    public DialogChooseTime setOnCancel(String s, Callback1<BaseDialog> onCancel) {
        return (DialogChooseTime) super.setOnCancel(s, onCancel);
    }

    public DialogChooseTime setEnabled(boolean enabled) {
        return (DialogChooseTime) super.setEnabled(enabled);
    }
}
