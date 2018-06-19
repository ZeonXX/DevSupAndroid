package com.sup.dev.android.views.popup_x;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.view.View;
import android.widget.PopupWindow;

import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsView;

import java.lang.ref.WeakReference;

public abstract class Popup {

    private WeakReference<PopupWindow> weakView;
    private boolean enabled;
    private boolean cancelable;

    public View instanceView(Context viewContext) {
        return getLayoutRes() != 0 ? ToolsView.inflate(viewContext, getLayoutRes()) : null;
    }

    public abstract int getLayoutRes();

    public abstract void bindView(View view);

    @CallSuper
    protected void onPreShow(View view) {

    }

    @CallSuper
    protected void onShow(View view) {

    }

    @CallSuper
    protected void onHide() {

    }

    public void hide() {
        if (weakView != null && weakView.get() != null)
            weakView.get().dismiss();
    }


    public <K extends Popup> K show(View anchor) {
        return show(anchor, -1, -1);
    }

    public <K extends Popup> K show(View anchor, int x, int y) {

        View view = instanceView(anchor.getContext());
        bindView(view);

        onPreShow(view);

        PopupWindow popupWindow = new PopupWindow(anchor.getContext());
        weakView = new WeakReference<>(popupWindow);
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setContentView(view);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);


        view.measure(ToolsAndroid.getScreenW(), ToolsAndroid.getScreenH());
        popupWindow.setWidth(view.getMeasuredWidth());
        popupWindow.setHeight(view.getMeasuredHeight());

        x -= popupWindow.getWidth();
        y -= anchor.getHeight();
        if (x > -1 && y > -1) {
            int[] p = new int[2];
            anchor.getLocationOnScreen(p);

            if (popupWindow.getHeight() + (anchor.getHeight() + y) + p[1] > ToolsAndroid.getScreenH())
                x += anchor.getHeight();


            popupWindow.showAsDropDown(anchor, x, y);
        } else {
            popupWindow.showAsDropDown(anchor);
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
