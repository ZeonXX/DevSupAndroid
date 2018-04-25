package com.sup.dev.android.views.elements.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Button;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback5;
import com.sup.dev.java.tools.ToolsDate;

public class DialogChooseTimeRange extends BaseDialog {

    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();

    private final Button vStart;
    private final Button vEnd;

    private int h1;
    private int m1;
    private int h2;
    private int m2;

    public DialogChooseTimeRange(Context viewContext) {
        super(viewContext, R.layout.dialog_choose_time_range);

        vStart = findViewById(R.id.start);
        vEnd = findViewById(R.id.end);

        vStart.setOnClickListener(v -> new DialogChooseTime(getContext())
                .setTime(h1, m1)
                .setOnCancel(vCancel.getText().toString())
                .setOnEnter(vEnter.getText().toString(), (dialog, h, m) -> setTimeStart(h, m))
                .show());

        vEnd.setOnClickListener(v -> new DialogChooseTime(getContext())
                .setTime(h2, m2)
                .setOnCancel(vCancel.getText().toString())
                .setOnEnter(vEnter.getText().toString(), (dialog, h, m) -> setTimeEnd(h, m))
                .show());

        setTimeStart(12, 0);
        setTimeEnd(24, 0);
    }

    public DialogChooseTimeRange setTimeStart(int h, int m) {
        h1 = h;
        m1 = m;
        vStart.setText(ToolsDate.timeToString(h1, m1));
        return this;
    }

    public DialogChooseTimeRange setTimeEnd(int h, int m) {
        h2 = h;
        m2 = m;
        vEnd.setText(ToolsDate.timeToString(h2, m2));
        return this;
    }

    public DialogChooseTimeRange setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogChooseTimeRange) super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogChooseTimeRange setCancelable(boolean cancelable) {
        return (DialogChooseTimeRange) super.setCancelable(cancelable);
    }

    public DialogChooseTimeRange setOnEnter(@StringRes int s) {
        return (DialogChooseTimeRange) super.setOnEnter(s);
    }

    public DialogChooseTimeRange setOnEnter(String s) {
        return (DialogChooseTimeRange) super.setOnEnter(s);
    }

    public DialogChooseTimeRange setOnEnter(@StringRes int s, Callback5<DialogChooseTimeRange, Integer, Integer, Integer, Integer> onEnter) {
        return setOnEnter(utilsResources.getString(s), onEnter);
    }

    public DialogChooseTimeRange setOnEnter(String s, Callback5<DialogChooseTimeRange, Integer, Integer, Integer, Integer> onEnter) {
        return (DialogChooseTimeRange) super.setOnEnter(s, d -> onEnter.callback(this, h1, m1, h2, m2));
    }

    public DialogChooseTimeRange setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogChooseTimeRange) super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogChooseTimeRange setOnCancel(String s) {
        return (DialogChooseTimeRange) super.setOnCancel(s);
    }

    public DialogChooseTimeRange setOnCancel(Callback1<BaseDialog> onCancel) {
        return (DialogChooseTimeRange) super.setOnCancel(onCancel);
    }

    public DialogChooseTimeRange setOnCancel(@StringRes int s) {
        return (DialogChooseTimeRange) super.setOnCancel(s);
    }

    public DialogChooseTimeRange setOnCancel(@StringRes int s, Callback1<BaseDialog> onCancel) {
        return (DialogChooseTimeRange) super.setOnCancel(s, onCancel);
    }

    public DialogChooseTimeRange setOnCancel(String s, Callback1<BaseDialog> onCancel) {
        return (DialogChooseTimeRange) super.setOnCancel(s, onCancel);
    }

    public DialogChooseTimeRange setEnabled(boolean enabled) {
        return (DialogChooseTimeRange) super.setEnabled(enabled);
    }

}
