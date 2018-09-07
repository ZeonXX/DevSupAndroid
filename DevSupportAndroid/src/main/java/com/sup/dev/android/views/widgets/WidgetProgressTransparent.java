package com.sup.dev.android.views.widgets;

import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.dialogs.DialogWidget;
import com.sup.dev.java.tools.ToolsThreads;

public class WidgetProgressTransparent extends Widget {

    private FrameLayout frameLayout;
    private ProgressBar progressBar;

    public WidgetProgressTransparent() {
        super(0);
    }

    @Override
    public View instanceView() {
        frameLayout = new FrameLayout(SupAndroid.activity);
        progressBar = new ProgressBar(SupAndroid.activity);

        frameLayout.addView(progressBar);

        ((ViewGroup.MarginLayoutParams) progressBar.getLayoutParams()).setMargins(ToolsView.dpToPx(24), ToolsView.dpToPx(24), ToolsView.dpToPx(24), ToolsView.dpToPx(24));

        return frameLayout;
    }

    @Override
    public void onShow() {
        super.onShow();

        frameLayout.setVisibility(View.INVISIBLE);
        int invisibleTime = 0;

        if (viewWrapper instanceof DialogWidget) {
            invisibleTime = 1000;
            DialogWidget dialog = (DialogWidget) viewWrapper;
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
            dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        }

        ToolsThreads.INSTANCE.main(invisibleTime, () -> {
            ToolsView.fromAlpha(frameLayout);
            if (viewWrapper instanceof DialogWidget) ((DialogWidget) viewWrapper).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        });
    }


}
