package com.sup.dev.android.views.dialogs;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.tools.ToolsThreads;

public class DialogProgressTransparent extends BaseDialog {

    public DialogProgressTransparent(Context viewContext) {
        this(viewContext, true);
    }

    public DialogProgressTransparent(Context viewContext, boolean cancelable) {
        super(viewContext, instanceView(viewContext));
        view.setVisibility(View.INVISIBLE);
        setCancelable(cancelable);
    }

    @Override
    protected void onPreShow() {
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

    private static View instanceView(Context viewContext) {
        FrameLayout frameLayout = new FrameLayout(viewContext);
        ProgressBar progressBar = new ProgressBar(viewContext);

        frameLayout.setBackgroundColor(0x00000000);
        frameLayout.addView(progressBar);


        return frameLayout;
    }

    public DialogProgressTransparent showNow() {
        view.setVisibility(View.VISIBLE);
        dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        return (DialogProgressTransparent) super.show();
    }

    public DialogProgressTransparent show() {
        ToolsThreads.main(1000, () -> {
            ToolsView.fromAlpha(view, 600);
            dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        });
        return (DialogProgressTransparent) super.show();
    }

    //
    //  Setters
    //

    public DialogRecycler setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogRecycler) super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogProgressTransparent setCancelable(boolean cancelable) {
        return (DialogProgressTransparent) super.setCancelable(cancelable);
    }

    public DialogProgressTransparent setOnCancel(Callback1<BaseDialog> onCancel) {
        return (DialogProgressTransparent) super.setOnCancel(onCancel);
    }

}
