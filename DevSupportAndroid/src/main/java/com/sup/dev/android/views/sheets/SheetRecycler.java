package com.sup.dev.android.views.sheets;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.sup.dev.android.androiddevsup.R;

public class SheetRecycler extends BaseSheet {

    protected final RecyclerView vRecycler;

    public SheetRecycler(Context viewContext, @Nullable AttributeSet attrs) {
        super(viewContext, attrs, R.layout.sheet_recycler);

        vRecycler = view.findViewById(R.id.recycler);
    }

    //
    //  Setters
    //

    public SheetRecycler setAdapter(RecyclerView.Adapter adapter) {
        vRecycler.setAdapter(adapter);
        return this;
    }


}
