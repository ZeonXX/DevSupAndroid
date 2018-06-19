package com.sup.dev.android.views.bricks;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

public class BrickProgressTransparent extends Brick{

    @Override
    public int getLayoutRes(Mode mode) {
        return 0;
    }

    @Override
    public View instanceView(Context viewContext, Mode mode) {
        FrameLayout frameLayout = new FrameLayout(viewContext);
        ProgressBar progressBar = new ProgressBar(viewContext);

        frameLayout.setBackgroundColor(0x00000000);
        frameLayout.addView(progressBar);
        frameLayout.setVisibility(View.INVISIBLE);

        return frameLayout;
    }

    @Override
    public void bindView(View view, Mode mode) {

    }

    protected void onPreShow() {
     //  dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));
     //  dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

}
