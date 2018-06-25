package com.sup.dev.android.views.dialogs;

import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.views.layouts.LayoutMaxSizes;

public abstract class Dialog extends AppCompatDialog{

    protected final View view;
    private boolean enabled;
    private boolean cancelable;

    public Dialog(int layoutRes) {
        this(ToolsView.inflate(layoutRes));
    }

    public Dialog(View view) {
        super(SupAndroid.activity);
        this.view = view;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setOnCancelListener(dialogInterface -> onHide());

        LayoutMaxSizes layoutMaxSizes = new LayoutMaxSizes(SupAndroid.activity) {
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                setMaxWidthParentPercent(ToolsAndroid.isScreenPortrait() ? 90 : 70);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        };
        layoutMaxSizes.setId(R.id.dialog_layout_max_sizes);
        layoutMaxSizes.setMaxWidth(600);
        layoutMaxSizes.setUseScreenWidthAsParent(true);
        layoutMaxSizes.setAlwaysMaxW(true);
        layoutMaxSizes.setChildAlwaysMaxW(true);
        layoutMaxSizes.addView(ToolsView.removeFromParent(view), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(layoutMaxSizes);
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT); //  Без этой строки диалог умирает при повороте экрана

    }

    @CallSuper
    protected void onShow() {

    }

    @CallSuper
    protected void onHide() {

    }

    @Override
    public void hide() {
        super.dismiss();
    }

    @Override
    public void show() {
        showDialog();
    }

    public <K extends Dialog> K showDialog() {
        onShow();
        super.show();
        return (K) this;
    }

    //
    //  Setters
    //


    @Override
    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        super.setCancelable(cancelable);
    }

    public <K extends Dialog> K setDialogCancelable(boolean cancelable) {
        setCancelable(cancelable);
        return (K) this;
    }

    public <K extends Dialog> K setEnabled(boolean enabled) {
        this.enabled = enabled;
        setCanceledOnTouchOutside(isCancelable() && isEnabled());
        return (K) this;
    }

    //
    //  Getters
    //

    public boolean isEnabled() {
        return enabled;
    }

    public boolean isCancelable() {
        return cancelable;
    }
}
