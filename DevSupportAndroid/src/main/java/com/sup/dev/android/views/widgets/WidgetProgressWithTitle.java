package com.sup.dev.android.views.widgets;

import android.support.annotation.StringRes;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;

public class WidgetProgressWithTitle extends Widget {

    private final TextView vTitle;

    public WidgetProgressWithTitle() {
        super(R.layout.widget_progress_with_title);
        vTitle = view.findViewById(R.id.title);
    }

    //
    //  Setters
    //

    public WidgetProgressWithTitle setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public WidgetProgressWithTitle setTitle(String title) {
        ToolsView.setTextOrGone(vTitle, title);
        return  this;
    }
}
