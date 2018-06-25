package com.sup.dev.android.views.widgets;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.android.views.popup.PopupWidget;
import com.sup.dev.android.views.screens.SWidget;
import com.sup.dev.android.views.sheets.SheetWidget;

public class WidgetRecycler extends Widget {

    protected final RecyclerView vRecycler;

    protected RecyclerCardAdapter adapter;

    public WidgetRecycler() {
        super(R.layout.widget_recycler);

        vRecycler = findViewById(R.id.recycler);
    }

    @Override
    public void onShow() {
        super.onShow();

        vRecycler.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        vRecycler.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        if (viewWrapper instanceof SheetWidget) {
            vRecycler.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else if (viewWrapper instanceof PopupWidget) {
            vRecycler.getLayoutParams().width = ToolsView.dpToPx(200);
        }
    }

    //
    //  Setters
    //

    public <K extends WidgetRecycler> K setAdapter(RecyclerCardAdapter adapter) {
        vRecycler.setAdapter(adapter);
        return (K) this;
    }

}
