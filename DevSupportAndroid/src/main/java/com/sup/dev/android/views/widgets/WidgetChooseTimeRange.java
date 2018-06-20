package com.sup.dev.android.views.widgets;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.screens.SWidget;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback5;
import com.sup.dev.java.tools.ToolsDate;

public class WidgetChooseTimeRange extends Widget {

    private final Button vStart;
    private final Button vEnd;
    private final Button vCancel;
    private final Button vEnter;
    private final TextView vTitle;

    private int h1;
    private int m1;
    private int h2;
    private int m2;

    private boolean autoHideOnEnter = true;

    public WidgetChooseTimeRange() {
        super(R.layout.widget_choose_time_range);

        vStart = view.findViewById(R.id.start);
        vEnd = view.findViewById(R.id.end);
        vCancel = view.findViewById(R.id.cancel);
        vEnter = view.findViewById(R.id.enter);
        vTitle = view.findViewById(R.id.title);

        vCancel.setVisibility(View.GONE);
        vEnter.setVisibility(View.GONE);

        vStart.setOnClickListener(v -> new WidgetChooseTime()
                .setOnCancel(vCancel.getText().toString())
                .setOnEnter(vEnter.getText().toString(), (dialog, h, m) -> setTimeStart(h, m))
                .asDialogShow());

        vEnd.setOnClickListener(v -> new WidgetChooseTime()
                .setOnCancel(vCancel.getText().toString())
                .setOnEnter(vEnter.getText().toString(), (dialog, h, m) -> setTimeEnd(h, m))
                .asDialogShow());

        setTimeStart(12, 0);
        setTimeEnd(24, 0);
    }

    @Override
    public void onShow() {
        super.onShow();

        ToolsView.setTextOrGone(vTitle, vTitle.getText());
        if(viewWrapper instanceof SWidget){
            vTitle.setVisibility(View.GONE);
            ((SWidget)viewWrapper).setTitle(vTitle.getText().toString());
        }
    }

    //
    //  Setters
    //

    public WidgetChooseTimeRange setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public WidgetChooseTimeRange setTitle(String title) {
        ToolsView.setTextOrGone(vTitle, title);
        return this;
    }

    public WidgetChooseTimeRange setTimeStart(int h, int m) {
        h1 = h;
        m1 = m;
        vStart.setText(ToolsDate.timeToString(h, m));
        return this;
    }

    public WidgetChooseTimeRange setTimeEnd(int h, int m) {
        h2 = h;
        m2 = m;
        vEnd.setText(ToolsDate.timeToString(h, m));
        return this;
    }

    public WidgetChooseTimeRange setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s));
    }

    public WidgetChooseTimeRange setOnEnter(String s) {
        return setOnEnter(s, null);
    }

    public WidgetChooseTimeRange setOnEnter(@StringRes int s, Callback5<WidgetChooseTimeRange, Integer, Integer, Integer, Integer> onEnter) {
        return setOnEnter(ToolsResources.getString(s), onEnter);
    }

    public WidgetChooseTimeRange setOnEnter(String s, Callback5<WidgetChooseTimeRange, Integer, Integer, Integer, Integer> onEnter) {
        ToolsView.setTextOrGone(vEnter, s);
        vEnter.setOnClickListener(v -> {
            if (autoHideOnEnter) hide();
            else setEnabled(false);
            if (onEnter != null) onEnter.callback(this, h1, m1, h2, m2);
        });
        return this;
    }

    public WidgetChooseTimeRange setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public WidgetChooseTimeRange setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s), null);
    }

    public WidgetChooseTimeRange setOnCancel(String s) {
        return setOnCancel(s, null);
    }

    public WidgetChooseTimeRange setOnCancel(Callback1<WidgetChooseTimeRange> onCancel) {
        return setOnCancel(null, onCancel);
    }

    public WidgetChooseTimeRange setOnCancel(@StringRes int s, Callback1<WidgetChooseTimeRange> onCancel) {
        return setOnCancel(ToolsResources.getString(s), onCancel);
    }

    public WidgetChooseTimeRange setOnCancel(String s, Callback1<WidgetChooseTimeRange> onCancel) {
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

    public WidgetChooseTimeRange setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        return this;
    }


}
