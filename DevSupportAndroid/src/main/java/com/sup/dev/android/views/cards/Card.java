package com.sup.dev.android.views.cards;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.View;

import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.adapters.CardAdapter;

public abstract class Card {

    public CardAdapter adapter;

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

    protected View instanceView(){
        return null;
    }

    //
    //  Adapter
    //

    public View instanceView(Context context) {
        int layout = getLayout();
        return (layout > 0)?ToolsView.inflate(context, getLayout()):instanceView();
    }

    public void setAdapter(CardAdapter adapter) {
        this.adapter = adapter;
    }


}
