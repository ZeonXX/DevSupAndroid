package com.sup.dev.android.views.bricks;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.tools.ToolsDate;

public class BrickChooseTimeRange extends Brick {

    private int h1;
    private int m1;
    private int h2;
    private int m2;
    private String enterText;
    private String cancelText;
    private String title;
    private Callback1<BrickChooseTimeRange> onEnter;
    private Callback1<BrickChooseTimeRange> onCancel;
    private boolean autoHideOnEnter = true;

    public BrickChooseTimeRange() {
        setTimeStart(12, 0);
        setTimeEnd(24, 0);
    }

    @Override
    public int getLayoutRes(Mode mode) {
        return R.layout.brick_choose_time_range;
    }

    @Override
    public void bindView(View view, Mode mode) {
        Button vStart = view.findViewById(R.id.start);
        Button vEnd = view.findViewById(R.id.end);
        Button vCancel = view.findViewById(R.id.cancel);
        Button vEnter = view.findViewById(R.id.enter);
        TextView vTitle = view.findViewById(R.id.title);

        vCancel.setText(cancelText);
        vEnter.setText(enterText);
        vStart.setText(ToolsDate.timeToString(h1, m1));
        vEnd.setText(ToolsDate.timeToString(h2, m2));
        ToolsView.setTextOrGone(vTitle, title);

       vStart.setOnClickListener(v -> new BrickChooseTime()
               .setTime(h1, m1)
               .setOnCancel(vCancel.getText().toString())
               .setOnEnter(vEnter.getText().toString(), (dialog, h, m) -> setTimeStart(h, m))
               .asDialogShow());

       vEnd.setOnClickListener(v -> new BrickChooseTime()
               .setTime(h2, m2)
               .setOnCancel(vCancel.getText().toString())
               .setOnEnter(vEnter.getText().toString(), (dialog, h, m) -> setTimeEnd(h, m))
               .asDialogShow());

        vEnter.setOnClickListener(v -> {
            if (autoHideOnEnter) hide();
            else setEnabled(false);
            if (onEnter != null) onEnter.callback(this);
        });

        vCancel.setOnClickListener(v -> {
            hide();
            if (onCancel != null) onCancel.callback(this);
        });
    }


    //
    //  Setters
    //

    public BrickChooseTimeRange setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public BrickChooseTimeRange setTitle(String title) {
        this.title = title;
        update();
        return this;
    }

    public BrickChooseTimeRange setTimeStart(int h, int m) {
        h1 = h;
        m1 = m;
        update();
        return this;
    }

    public BrickChooseTimeRange setTimeEnd(int h, int m) {
        h2 = h;
        m2 = m;
        update();
        return this;
    }

    public BrickChooseTimeRange setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s));
    }

    public BrickChooseTimeRange setOnEnter(String s) {
        return setOnEnter(s, null);
    }

    public BrickChooseTimeRange setOnEnter(@StringRes int s, Callback1<BrickChooseTimeRange> onEnter) {
        return setOnEnter(ToolsResources.getString(s), onEnter);
    }

    public BrickChooseTimeRange setOnEnter(String s, Callback1<BrickChooseTimeRange> onEnter) {
        this.enterText = s;
        this.onEnter = onEnter;
        update();
        return this;
    }

    public BrickChooseTimeRange setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public BrickChooseTimeRange setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s), null);
    }

    public BrickChooseTimeRange setOnCancel(String s) {
        return setOnCancel(s, null);
    }

    public BrickChooseTimeRange setOnCancel(Callback1<BrickChooseTimeRange> onCancel) {
        return setOnCancel(null, onCancel);
    }

    public BrickChooseTimeRange setOnCancel(@StringRes int s, Callback1<BrickChooseTimeRange> onCancel) {
        return setOnCancel(ToolsResources.getString(s), onCancel);
    }

    public BrickChooseTimeRange setOnCancel(String s, Callback1<BrickChooseTimeRange> onCancel) {
        super.setOnHide(b -> {
            if (onCancel != null) onCancel.callback(this);
        });
        this.onCancel = onCancel;
        this.cancelText = s;
        update();
        return this;
    }

    public BrickChooseTimeRange setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        return this;
    }


}
