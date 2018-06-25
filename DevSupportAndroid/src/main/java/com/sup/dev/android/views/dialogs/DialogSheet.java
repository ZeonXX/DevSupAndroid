package com.sup.dev.android.views.dialogs;

import android.os.Build;
import android.support.annotation.CallSuper;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;

public class DialogSheet  extends AppCompatDialog {

    protected final View view;
    private boolean enabled;
    private boolean cancelable = true;

    public DialogSheet(int layoutRes) {
        this(ToolsView.inflate(layoutRes));
    }

    public DialogSheet(View view) {
        super(SupAndroid.activity);
        this.view = view;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setOnCancelListener(dialogInterface -> onHide());

        FrameLayout vRoot = new FrameLayout(view.getContext());
        FrameLayout vContainer = new FrameLayout(view.getContext());
        vContainer.addView(ToolsView.removeFromParent(view));
        vRoot.addView(vContainer);
        vContainer.setBackgroundColor(ToolsResources.getPrimaryColor(view.getContext()));
        vContainer.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        vContainer.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        ((FrameLayout.LayoutParams)vContainer.getLayoutParams()).gravity = Gravity.BOTTOM;
        setContentView(vRoot);

        getWindow().setBackgroundDrawable(null);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE );
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(0x00000000);
        }

        vRoot.setOnClickListener(v -> {
            if(cancelable && isEnabled()) hide();
        });

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

    public <K extends DialogSheet> K showDialog() {
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

    public <K extends DialogSheet> K setDialogCancelable(boolean cancelable) {
        setCancelable(cancelable);
        return (K) this;
    }

    public <K extends DialogSheet> K setEnabled(boolean enabled) {
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
