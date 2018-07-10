package com.sup.dev.android.views.widgets;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import com.sup.dev.android.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback3;

public class WidgetChooseTime extends Widget {

    private final TimePicker vTimePicker;
    private final Button vCancel;
    private final Button vEnter;

    private boolean autoHideOnEnter = true;

    public WidgetChooseTime() {
        super(R.layout.widget_choose_time);

        vTimePicker = view.findViewById(R.id.time_picker);
        vCancel = view.findViewById(R.id.cancel);
        vEnter = view.findViewById(R.id.enter);

        vCancel.setVisibility(View.GONE);
        vEnter.setVisibility(View.GONE);

        vTimePicker.setIs24HourView(true);
    }


    public WidgetChooseTime setTime(int h, int m) {
        vTimePicker.setCurrentHour(h);
        vTimePicker.setCurrentMinute(m);
        return this;
    }

    public WidgetChooseTime setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s));
    }

    public WidgetChooseTime setOnEnter(String s) {
        return setOnEnter(s, null);
    }

    public WidgetChooseTime setOnEnter(@StringRes int s, Callback3<WidgetChooseTime, Integer, Integer> onEnter) {
        return setOnEnter(ToolsResources.getString(s), onEnter);
    }

    public WidgetChooseTime setOnEnter(String s, Callback3<WidgetChooseTime, Integer, Integer> onEnter) {
        ToolsView.setTextOrGone(vEnter, s);
        vEnter.setOnClickListener(v -> {
            if (autoHideOnEnter) hide();
            else setEnabled(false);
            if (onEnter != null) onEnter.callback(this, vTimePicker.getCurrentHour(), vTimePicker.getCurrentMinute());
        });

        return this;
    }

    public WidgetChooseTime setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public WidgetChooseTime setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s), null);
    }

    public WidgetChooseTime setOnCancel(String s) {
        return setOnCancel(s, null);
    }

    public WidgetChooseTime setOnCancel(Callback1<WidgetChooseTime> onCancel) {
        return setOnCancel(null, onCancel);
    }

    public WidgetChooseTime setOnCancel(@StringRes int s, Callback1<WidgetChooseTime> onCancel) {
        return setOnCancel(ToolsResources.getString(s), onCancel);
    }

    public WidgetChooseTime setOnCancel(String s, Callback1<WidgetChooseTime> onCancel) {
        super.setOnHide(b -> {
            if (onCancel != null) onCancel.callback(this);
        });
        ToolsView.setTextOrGone(vCancel, s);
        vCancel.setOnClickListener(v -> {
            hide();
            if (onCancel != null) onCancel.callback(this);
        });
        return this;
    }

    public WidgetChooseTime setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        return this;
    }

}
