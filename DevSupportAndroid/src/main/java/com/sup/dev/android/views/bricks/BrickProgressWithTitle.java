package com.sup.dev.android.views.bricks;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;

public class BrickProgressWithTitle extends Brick{

    private String title;

    @Override
    public int getLayoutRes(Mode mode) {
        return R.layout.brick_progress_with_title;
    }

    @Override
    public void bindView(View view, Mode mode) {
        TextView vTitle = view.findViewById(R.id.title);

        ToolsView.setTextOrGone(vTitle, title);
    }

    //
    //  Setters
    //

    public BrickProgressWithTitle setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public BrickProgressWithTitle setTitle(String title) {
        this.title = title;
        return  this;
    }
}
