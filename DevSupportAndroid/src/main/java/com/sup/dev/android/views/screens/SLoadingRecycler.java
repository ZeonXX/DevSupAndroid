package com.sup.dev.android.views.screens;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapterLoading;
import com.sup.dev.android.views.cards.Card;
import com.sup.dev.java.libs.api_simple.client.Request;
import com.sup.dev.java.tools.ToolsThreads;

public abstract class SLoadingRecycler<C extends Card, V> extends SLoading {


    protected final RecyclerView vRecycler;
    protected final SwipeRefreshLayout vRefresh;

    protected RecyclerCardAdapterLoading<C, V> adapter;
    protected Request subscription;

    public SLoadingRecycler() {
        super(R.layout.screen_loading_recycler);

        vRecycler = findViewById(R.id.recycler);
        vRefresh = findViewById(R.id.refresh);

        vRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        vRefresh.setOnRefreshListener(() -> {
            vRefresh.setRefreshing(false);
            onReloadClicked();
        });

        ToolsThreads.main(true, () -> {

            Object textNetworkError = getTextErrorNetwork();
            if(textNetworkError instanceof Integer) textNetworkError = ToolsResources.getString((int)textNetworkError);

            Object textRetry = getTextRetry();
            if(textRetry instanceof Integer) textRetry = ToolsResources.getString((int)textRetry);

            adapter = instanceAdapter()
                    .setOnEmpty(()->setState(State.EMPTY))
                    .setOnErrorAndEmpty(()->setState(State.ERROR))
                    .setOnStartLoadingAndEmpty(()->setState(State.PROGRESS))
                    .setOnLoadingAndNotEmpty(()->setState(State.NONE))
                    .setOnLoadedNotEmpty(()->setState(State.NONE))
                    .setRetryMessage(textNetworkError.toString(), textRetry.toString())
                    .setNotifyCount(5);

            setAdapter(adapter);
            reload();
        });
    }

    @Override
    public void onReloadClicked() {
        reload();
    }

    private void reload() {
        if (subscription != null) subscription.unsubscribe();
        adapter.reloadBottom();
    }


    protected abstract RecyclerCardAdapterLoading<C, V> instanceAdapter();

    public Object getTextErrorNetwork(){
        return SupAndroid.TEXT_APP_RETRY;
    }

    public Object getTextRetry(){
        return SupAndroid.TEXT_APP_RETRY;
    }

    protected int getNotifyCount(){
        return 5;
    }

    //
    //  Setters
    //

    public void setAdapter(RecyclerView.Adapter adapter) {
        vRecycler.setAdapter(adapter);
    }










}
