package com.sup.dev.android.views.elements.cards;

import android.content.Context;
import android.support.annotation.CallSuper;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public abstract class Card {


    private final UtilsView utilsView = SupAndroid.di.utilsView();

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
        return utilsView.inflate(context, getLayout());
    }

    public void setAdapter(RecyclerCardAdapter adapter) {
        this.adapter = adapter;
    }


}
