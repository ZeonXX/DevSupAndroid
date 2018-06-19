package com.sup.dev.android.views.bricks;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsText;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback3;

public class BrickChooseTime extends Brick {

    private int h;
    private int m;
    private String enterText;
    private String cancelText;
    private Callback3<BrickChooseTime, Integer, Integer> onEnter;
    private Callback1<BrickChooseTime> onCancel;
    private boolean autoHideOnEnter = true;

    public BrickChooseTime() {

    }

    @Override
    public int getLayoutRes(Mode mode) {
        return R.layout.brick_choose_time;
    }


    @Override
    public void bindView(View view, Mode mode) {
        TimePicker vTimePicker = view.findViewById(R.id.time_picker);
        Button vCancel = view.findViewById(R.id.cancel);
        Button vEnter = view.findViewById(R.id.enter);

        vCancel.setText(cancelText);
        vEnter.setText(enterText);
        vTimePicker.setIs24HourView(true);
        vTimePicker.setCurrentHour(h);
        vTimePicker.setCurrentMinute(m);

        vCancel.setVisibility(ToolsText.empty(cancelText) ? View.GONE : View.VISIBLE);
        vEnter.setVisibility(ToolsText.empty(enterText) ? View.GONE : View.VISIBLE);

        vEnter.setOnClickListener(v -> {
            if (autoHideOnEnter) hide();
            else setEnabled(false);
            if (onEnter != null) onEnter.callback(this, vTimePicker.getCurrentHour(), vTimePicker.getCurrentMinute());
        });

        vCancel.setOnClickListener(v -> {
            hide();
            if (onCancel != null) onCancel.callback(this);
        });
    }

    public BrickChooseTime setTime(int h, int m) {
        this.h = h;
        this.m = m;
        update();
        return this;
    }

    public BrickChooseTime setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s));
    }

    public BrickChooseTime setOnEnter(String s) {
        return setOnEnter(s, null);
    }

    public BrickChooseTime setOnEnter(@StringRes int s, Callback3<BrickChooseTime, Integer, Integer> onEnter) {
        return setOnEnter(ToolsResources.getString(s), onEnter);
    }

    public BrickChooseTime setOnEnter(String s, Callback3<BrickChooseTime, Integer, Integer> onEnter) {
        this.enterText = s;
        this.onEnter = onEnter;
        update();
        return this;
    }

    public BrickChooseTime setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public BrickChooseTime setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s), null);
    }

    public BrickChooseTime setOnCancel(String s) {
        return setOnCancel(s, null);
    }

    public BrickChooseTime setOnCancel(Callback1<BrickChooseTime> onCancel) {
        return setOnCancel(null, onCancel);
    }

    public BrickChooseTime setOnCancel(@StringRes int s, Callback1<BrickChooseTime> onCancel) {
        return setOnCancel(ToolsResources.getString(s), onCancel);
    }

    public BrickChooseTime setOnCancel(String s, Callback1<BrickChooseTime> onCancel) {
        super.setOnHide(b -> {
            if (onCancel != null) onCancel.callback(this);
        });
        this.onCancel = onCancel;
        this.cancelText = s;
        update();
        return this;
    }

    public BrickChooseTime setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        return this;
    }

}
