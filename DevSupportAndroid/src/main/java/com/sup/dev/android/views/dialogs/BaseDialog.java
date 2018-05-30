package com.sup.dev.android.views.dialogs;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatDialog;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.widgets.layouts.LayoutMaxSizes;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class BaseDialog{

    protected final Context viewContext;
    protected final View view;
    protected final TextView vTitle;
    protected final TextView vText;
    protected final Button vCancel;
    protected final Button vEnter;

    private Callback1<BaseDialog> onCancel;
    private boolean cancelable = true;
    private boolean enabled = true;
    private boolean autoHideOnCancel = true;
    private boolean autoHideOnEnter = true;
    private boolean isDismissCalled;

    protected AppCompatDialog dialog;

    public BaseDialog(Context viewContext, @LayoutRes int res) {
        this(viewContext, ToolsView.inflate(viewContext, res));
    }

    public BaseDialog(Context viewContext, View view) {
        this.viewContext = viewContext;
        this.view = view;

        vCancel = view.findViewById(R.id.cancel);
        vTitle = view.findViewById(R.id.title);
        vText = view.findViewById(R.id.text);
        vEnter = view.findViewById(R.id.enter);

        if (vEnter != null) {
            vEnter.setText(null);
            vEnter.setVisibility(View.GONE);
        }
        if (vCancel != null) {
            vCancel.setText(null);
            vCancel.setVisibility(View.GONE);
        }
        if (vTitle != null) {
            vTitle.setText("");
            vTitle.setVisibility(View.GONE);
        }
        if (vText != null) {
            vText.setText("");
            vText.setVisibility(View.GONE);
        }
    }


    public BaseDialog show() {
        dialog = new AppCompatDialog(viewContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        onPreShow();
        dialog.setCancelable(cancelable && enabled);
        dialog.setCanceledOnTouchOutside(cancelable && enabled);
        dialog.setOnCancelListener(dialogInterface -> {
            if (onCancel != null) onCancel.callback(this);
        });

        LayoutMaxSizes layoutMaxSizes = new LayoutMaxSizes(viewContext) {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                setMaxWidthParentPercent(ToolsAndroid.isScreenPortrait() ? 90 : 70);
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        };
        layoutMaxSizes.setMaxWidth(600);
        layoutMaxSizes.setUseScreenWidthAsParent(true);
        layoutMaxSizes.setAlwaysMaxW(true);
        layoutMaxSizes.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.setContentView(layoutMaxSizes);
        dialog.show();

        //  Без этой строки диалог умирает при повороте экрана
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


        return this;
    }

    protected <V extends View> V findViewById(int id) {
        return view.findViewById(id);
    }

    public void hide() {
        isDismissCalled = true;
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
    }

    protected void onPreShow() {

    }

    //
    //  Setters (For Override)
    //

    protected BaseDialog setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    protected BaseDialog setTitle(String title) {
        vTitle.setText(title);
        vTitle.setVisibility(title != null ? View.VISIBLE : View.GONE);
        return this;
    }

    protected BaseDialog setText(@StringRes int text) {
        return setText(ToolsResources.getString(text));
    }

    protected BaseDialog setText(CharSequence text) {
        vText.setText(text);
        vText.setVisibility(text != null ? View.VISIBLE : View.GONE);
        if (text instanceof Spannable) vText.setMovementMethod(LinkMovementMethod.getInstance());
        else vText.setMovementMethod(null);
        return this;
    }

    protected BaseDialog setAutoHideOnCancel(boolean autoHideOnCancel) {
        this.autoHideOnCancel = autoHideOnCancel;
        if (!autoHideOnCancel) setCancelable(false);
        return this;
    }

    protected BaseDialog setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    protected BaseDialog setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
        return this;
    }

    protected BaseDialog setOnCancel(String s) {
        return setOnCancel(s, null);
    }

    protected BaseDialog setOnCancel(Callback1<BaseDialog> onCancel) {
        return setOnCancel(null, onCancel);
    }

    protected BaseDialog setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s), null);
    }

    protected BaseDialog setOnCancel(@StringRes int s, Callback1<BaseDialog> onCancel) {
        return setOnCancel(ToolsResources.getString(s), onCancel);
    }


    protected BaseDialog setOnCancel(String s, Callback1<BaseDialog> onCancel) {

        this.onCancel = onCancel;

        if (s != null && !s.isEmpty()) {
            vCancel.setText(s);
            vCancel.setVisibility(View.VISIBLE);
            vCancel.setOnClickListener(v -> {
                if (autoHideOnCancel) hide();
                else setEnabled(false);
                if (onCancel != null) onCancel.callback(this);
            });
        }
        return this;
    }

    protected BaseDialog setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s), null);
    }

    protected BaseDialog setOnEnter(String s) {
        return setOnEnter(s, null);
    }

    protected BaseDialog setOnEnter(@StringRes int s, Callback1<BaseDialog> onEnter) {
        return setOnEnter(ToolsResources.getString(s), onEnter);
    }

    protected BaseDialog setOnEnter(String s, Callback1<BaseDialog> onEnter) {
        vEnter.setText(s);
        vEnter.setVisibility(View.VISIBLE);
        vEnter.setOnClickListener(v -> {
            if (autoHideOnEnter) hide();
            else setEnabled(false);
            if (onEnter != null) onEnter.callback(this);
        });
        return this;
    }

    @CallSuper
    public BaseDialog setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (vCancel != null) vCancel.setEnabled(enabled);
        if (vEnter != null) vEnter.setEnabled(enabled);
        if (vTitle != null) vTitle.setEnabled(enabled);
        if (vText != null) vText.setEnabled(enabled);

        if (dialog != null) {
            dialog.setCancelable(cancelable && enabled);
            dialog.setCanceledOnTouchOutside(cancelable && enabled);
        }
        return this;
    }

    //
    //  Getters
    //


    public Context getContext() {
        return viewContext;
    }

    public boolean isShoved() {
        return !isDismissCalled && dialog != null && dialog.isShowing();
    }

    public boolean isDismissCalled() {
        return isDismissCalled;
    }

    public boolean isAutoHideOnCancel() {
        return autoHideOnCancel;
    }

    public boolean isAutoHideOnEnter() {
        return autoHideOnEnter;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
