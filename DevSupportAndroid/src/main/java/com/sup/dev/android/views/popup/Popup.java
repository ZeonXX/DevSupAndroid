package com.sup.dev.android.views.popup;

import android.support.annotation.CallSuper;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.PopupWindow;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsView;

import java.lang.ref.WeakReference;

public abstract class Popup extends PopupWindow {

    private final View view;
    private boolean enabled;
    private boolean cancelable;

    public Popup(int layoutRes) {
        this(ToolsView.INSTANCE.inflate(layoutRes));
    }

    public Popup(View view) {
        super(SupAndroid.activity);
        this.view = view;

        CardView vCard = ToolsView.INSTANCE.inflate(R.layout.view_card_6dp);
        ToolsView.INSTANCE.removeFromParent(view);
        vCard.addView(view);
        setBackgroundDrawable(null);
        setContentView(vCard);
        setOutsideTouchable(true);
        setFocusable(true);

    }

    @CallSuper
    protected void onShow() {

    }

    @CallSuper
    protected void onHide() {

    }

    public <K extends Popup> K hide() {
        dismiss();
        return (K) this;
    }


    public <K extends Popup> K show(View anchor) {
        return show(anchor, -1, -1);
    }

    public <K extends Popup> K show(View anchor, int x, int y) {

        onShow();

        view.measure(View.MeasureSpec.makeMeasureSpec(ToolsAndroid.INSTANCE.getScreenW(), View.MeasureSpec.AT_MOST),
                View.MeasureSpec.makeMeasureSpec(ToolsAndroid.INSTANCE.getScreenH(), View.MeasureSpec.AT_MOST));
        setWidth(view.getMeasuredWidth());
        if (view.getMeasuredHeight() < ToolsView.INSTANCE.dpToPx(240/*Запас, чтоб не обрезать 2 пикселя*/))
            setHeight(Math.min(view.getMeasuredHeight(), view.getMeasuredHeight()));
        else
            setHeight(Math.min(view.getMeasuredHeight(), ToolsView.INSTANCE.dpToPx(200)));

        if (x > -1 && y > -1) {
            x -= getWidth() / 2;
            y -= anchor.getHeight();

            int[] p = new int[2];
            anchor.getLocationOnScreen(p);
            if (getHeight() + (anchor.getHeight() + y) + p[1] > ToolsAndroid.INSTANCE.getScreenH())
                y += anchor.getHeight();

            showAsDropDown(anchor, x, y);
        } else {
            showAsDropDown(anchor);
        }

        return (K) this;
    }

    //
    //  Setters
    //

    public <K extends Popup> K setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return (K) this;
    }

    public <K extends Popup> K setEnabled(boolean enabled) {
        this.enabled = enabled;
        return (K) this;
    }

    //
    //  Getters
    //


    public boolean isCancelable() {
        return cancelable;
    }

    public boolean isEnabled() {
        return enabled;
    }

}
