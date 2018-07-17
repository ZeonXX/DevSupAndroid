package com.sup.dev.android.views.views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.sup.dev.android.tools.ToolsView;

public class ViewSpace extends View{

    private int w;
    private int h;

    public ViewSpace(Context context, int wDP, int hDP) {
        super(context);
        w = ToolsView.dpToPx(wDP);
        h = ToolsView.dpToPx(hDP);
    }

    @Override
    public void setLayoutParams(ViewGroup.LayoutParams params) {
        params.width = w;
        params.height = h;
        super.setLayoutParams(params);
    }
}
