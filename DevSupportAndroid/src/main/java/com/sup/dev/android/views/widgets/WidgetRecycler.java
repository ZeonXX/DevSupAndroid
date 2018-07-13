package com.sup.dev.android.views.widgets;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.sup.dev.android.R;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.android.views.dialogs.DialogSheetWidget;
import com.sup.dev.android.views.popup.PopupWidget;

public class WidgetRecycler extends Widget {

    protected final RecyclerView vRecycler;
    protected final ViewGroup vContainer;

    protected RecyclerCardAdapter adapter;

    public WidgetRecycler() {
        super(R.layout.widget_recycler);

        vRecycler = findViewById(R.id.recycler);
        vContainer = findViewById(R.id.container);
    }

    @Override
    public void onShow() {
        super.onShow();

        vRecycler.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        vRecycler.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        if (viewWrapper instanceof DialogSheetWidget) {
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
