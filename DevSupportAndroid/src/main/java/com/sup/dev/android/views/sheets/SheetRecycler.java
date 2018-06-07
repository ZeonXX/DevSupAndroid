package com.sup.dev.android.views.sheets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;

public class SheetRecycler extends BaseSheet {

    protected final RecyclerView vRecycler;
    protected final TextView vTitle;

    public SheetRecycler(Context viewContext, @Nullable AttributeSet attrs) {
        super(viewContext, attrs, R.layout.sheet_recycler);

        vRecycler = view.findViewById(R.id.recycler);
        vTitle = findViewById(R.id.title);

        vTitle.setVisibility(View.GONE);
    }

    //
    //  Setters
    //

    public SheetRecycler setAdapter(RecyclerView.Adapter adapter) {
        vRecycler.setAdapter(adapter);
        return this;
    }

    public <K extends SheetRecycler> K setTitle(@StringRes int title) {
       return setTitle(ToolsResources.getString(title));
    }

    public <K extends SheetRecycler> K setTitle(String title) {
        vTitle.setText(title);
        vTitle.setVisibility(title != null && title.length() > 0? View.VISIBLE : View.GONE);
        return (K) this;
    }

}
