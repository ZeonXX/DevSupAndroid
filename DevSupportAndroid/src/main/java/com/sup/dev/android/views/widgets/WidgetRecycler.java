package com.sup.dev.android.views.widgets;

import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.popup.PopupWidget;
import com.sup.dev.android.views.screens.SWidget;
import com.sup.dev.android.views.sheets.SheetWidget;

public class WidgetRecycler extends Widget {

    protected final RecyclerView vRecycler;
    protected final TextView vTitle;

    protected RecyclerCardAdapter adapter;

    public WidgetRecycler() {
        super(R.layout.widget_recycler);

        vRecycler = findViewById(R.id.recycler);
        vTitle = findViewById(R.id.title);

        vTitle.setText(null);
        vTitle.setVisibility(View.GONE);
    }

    @Override
    public void onShow() {
        super.onShow();

        ToolsView.setTextOrGone(vTitle, vTitle.getText());
        vRecycler.getLayoutParams().width = ViewGroup.LayoutParams.MATCH_PARENT;
        vRecycler.getLayoutParams().height = ViewGroup.LayoutParams.MATCH_PARENT;

        if (viewWrapper instanceof SheetWidget) {
            vRecycler.getLayoutParams().height = ViewGroup.LayoutParams.WRAP_CONTENT;
        } else if (viewWrapper instanceof PopupWidget) {
            vRecycler.getLayoutParams().width = ToolsView.dpToPx(200);
        } else if(viewWrapper instanceof SWidget){
            vTitle.setVisibility(View.GONE);
            ((SWidget)viewWrapper).setTitle(vTitle.getText().toString());
        }
    }

    //
    //  Setters
    //

    public <K extends WidgetRecycler> K setAdapter(RecyclerCardAdapter adapter) {
        vRecycler.setAdapter(adapter);
        return (K) this;
    }

    public <K extends WidgetRecycler> K setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public <K extends WidgetRecycler> K setTitle(String title) {
        ToolsView.setTextOrGone(vTitle, title);
        return (K) this;
    }


}
