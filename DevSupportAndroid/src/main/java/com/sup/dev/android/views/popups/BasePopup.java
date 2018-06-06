package com.sup.dev.android.views.popups;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.design.widget.BottomSheetBehavior;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.providers.Provider;
import com.sup.dev.java.libs.debug.Debug;

public abstract class BasePopup extends PopupWindow {

    private final View anchor;

    public BasePopup(View anchor, @LayoutRes int res) {
        this(anchor, ToolsView.inflate(anchor.getContext(), res));
    }

    public BasePopup(Context viewContext, @LayoutRes int res) {
        this(null, ToolsView.inflate(viewContext, res));
    }

    public BasePopup(View anchor, View view) {
        super(view.getContext());
        this.anchor = anchor;
        setBackgroundDrawable(null);
        setContentView(view);
        setOutsideTouchable(true);
        setFocusable(true);

        init();
    }

    protected void init() {

    }

    public final <T extends View> T findViewById(@IdRes int id) {
        return getContentView().findViewById(id);
    }

    public void showAtLocation(View parent, int gravity) {
        showAtLocation(parent, gravity, 0, 0);
    }

    public void showAtLocation(View parent, int xoff, int yoff) {
        showAtLocation(parent, Gravity.TOP | Gravity.LEFT, xoff, yoff);
    }

    public void showAsDropDownOverlay(View anchor) {
        showAsDropDown(anchor, 0, -anchor.getHeight());
    }

    public void show() {
        showAsDropDown(anchor);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        calculateSize();
        onPreShow();
        super.showAsDropDown(anchor, xoff, yoff, gravity);
    }

    @Override
    public void showAtLocation(View anchor, int gravity, int x, int y) {
        calculateSize();
        onPreShow();
        super.showAtLocation(anchor, gravity, x, y);
    }

    public void hide() {
        dismiss();
    }

    private void calculateSize() {
        getContentView().measure(ToolsAndroid.getScreenW(), ToolsAndroid.getScreenH());
        setWidth(getContentView().getMeasuredWidth());
        setHeight(getContentView().getMeasuredHeight());
        Debug.log(getWidth(), getHeight());
    }

    @CallSuper
    protected void onPreShow() {

    }

    //
    //  Show on OnClick
    //

    private int clickScreenX;
    private int clickScreenY;

    private void setOnTouch(View v) {
        v.setOnTouchListener((v1, event) -> {
            clickScreenX = (int) event.getX();
            clickScreenY = (int) (event.getY());
            return false;
        });
    }

    public <K extends BasePopup> K showWhenClick(View v) {
        return showWhenClick(v, null);
    }

    public <K extends BasePopup> K showWhenClick(View v, Provider<Boolean> canShow) {
        setOnTouch(v);
        v.setOnClickListener(v1 -> {
            if (canShow != null && !canShow.provide()) return;
            showWhenClickNow(v);
        });
        return (K) this;
    }

    public <K extends BasePopup> K showWhenLongClick(View v) {
        setOnTouch(v);
        v.setOnLongClickListener(v1 -> {
            showWhenClickNow(v);
            return true;
        });
        return (K) this;
    }

    private void showWhenClickNow(View v) {

        int screenH = ToolsAndroid.getScreenH();

        getContentView().measure(ToolsAndroid.getScreenW(), screenH);
        setWidth(getContentView().getMeasuredWidth());
        setHeight(getContentView().getMeasuredHeight());

        clickScreenX -= getWidth();

        clickScreenY -= v.getHeight();

        int[] p = new int[2];
        v.getLocationOnScreen(p);
        if (getHeight() + (v.getHeight() + clickScreenY) + p[1] > screenH)
            clickScreenY += v.getHeight();

        onPreShow();
        super.showAsDropDown(v, clickScreenX, clickScreenY);
    }
}