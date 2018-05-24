package com.sup.dev.android.libs.mvp.presets.loading_recycler;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.libs.mvp.presets.loading.FLoading;

public abstract class FLoadingRecycler<K extends PLoadingRecycler> extends FLoading<K> {

    protected final RecyclerView vRecycler;
    protected final SwipeRefreshLayout vRefresh;

    public FLoadingRecycler(Context context, K presenter) {
        super(context, presenter);

        setContent(R.layout.fragment_loading_recycler);

        vRecycler = findViewById(R.id.recycler);
        vRefresh = findViewById(R.id.refresh);

        vRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        vRefresh.setOnRefreshListener(() -> {
            vRefresh.setRefreshing(false);
            presenter.onReloadClicked();
        });
    }

    //
    //  Presenter
    //

    public void setAdapter(RecyclerView.Adapter adapter) {
        vRecycler.setAdapter(adapter);
    }



}
