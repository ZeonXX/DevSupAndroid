package com.sup.dev.android.views.elements.cards;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;

public abstract class Card {

    public RecyclerCardAdapter adapter;

    public Object tag;

    public void update() {
        if (adapter == null) return;

        View view = adapter.getView(this);

        if (view != null) bindView(view);
    }

    //
    //  Bind
    //

    @LayoutRes
    public abstract int getLayout();

    public abstract void bindView(View view);

    //
    //  Adapter
    //

    public View instanceView(Context context) {
        return ToolsView.inflate(context, getLayout());
    }

    public void setAdapter(RecyclerCardAdapter adapter) {
        this.adapter = adapter;
    }


}
