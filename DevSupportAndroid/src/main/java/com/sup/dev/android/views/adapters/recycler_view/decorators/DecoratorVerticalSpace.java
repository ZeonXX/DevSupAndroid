package com.sup.dev.android.views.adapters.recycler_view.decorators;


import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sup.dev.android.tools.ToolsView;

public class DecoratorVerticalSpace extends RecyclerView.ItemDecoration {

    private final int space;

    public DecoratorVerticalSpace() {
        this(4);
    }

    public DecoratorVerticalSpace(int dp) {
        this.space = ToolsView.dpToPx(dp);
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.bottom = space;
    }
}