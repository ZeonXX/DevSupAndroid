package com.sup.dev.android.libs.mvp.presets.loading_recycler;

import com.sup.dev.android.libs.mvp.presets.loading.PLoading;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapterLoading;
import com.sup.dev.android.views.cards.Card;
import com.sup.dev.java.libs.api_simple.client.Request;
import com.sup.dev.java.tools.ToolsThreads;

public abstract class PLoadingRecycler<K extends FLoadingRecycler, C extends Card, V> extends PLoading<K> {

    protected RecyclerCardAdapterLoading<C, V> adapter;

    protected Request subscription;

    public PLoadingRecycler(Class<K> viewClass) {
        super(viewClass);

        ToolsThreads.main(true, () -> {

            Object textNetworkError = getTextNetworkError();
            if(textNetworkError instanceof Integer) textNetworkError = ToolsResources.getString((int)textNetworkError);

            Object textRetry = getTextRetry();
            if(textRetry instanceof Integer) textRetry = ToolsResources.getString((int)textRetry);

            adapter = instanceAdapter()
                    .setOnEmpty(() -> actionSingle("state", v -> v.setState(FLoadingRecycler.State.EMPTY)))
                    .setOnErrorAndEmpty(() -> actionSingle("state", v -> v.setState(FLoadingRecycler.State.ERROR)))
                    .setOnLoadingAndEmpty(() -> actionSingle("state", v -> v.setState(FLoadingRecycler.State.PROGRESS)))
                    .setOnLoadingAndNotEmpty(() -> actionSingle("state", v -> v.setState(FLoadingRecycler.State.NONE)))
                    .setOnLoadedNotEmpty(() -> actionSingle("state", v -> v.setState(FLoadingRecycler.State.NONE)))
                    .setRetryMessage(textNetworkError.toString(), textRetry.toString())
                    .setNotifyCount(5);

            actionAdd(v -> v.setAdapter(adapter));
        });



        reload();
    }


    private void reload() {
        if (subscription != null) subscription.unsubscribe();
        adapter.reloadBottom();
    }


    protected abstract RecyclerCardAdapterLoading<C, V> instanceAdapter();

    protected Object getTextNetworkError(){
        return ToolsResources.getString("app_retry");
    }

    protected Object getTextRetry(){
        return ToolsResources.getString("app_retry");
    }

    protected int getNotifyCount(){
        return 5;
    }

    //
    //  Fragment
    //

    @Override
    public void onReloadClicked() {
        reload();
    }










}
