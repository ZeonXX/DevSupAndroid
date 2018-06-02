package com.sup.dev.android.views.adapters.recycler_view;
import android.support.annotation.StringRes;

import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.views.cards.Card;
import com.sup.dev.android.views.cards.CardLoading;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.providers.Provider1;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

import java.util.ArrayList;
import java.util.List;

public class RecyclerCardAdapterLoading<K extends Card, V> extends RecyclerCardAdapter {

    private Callback2<Callback1<V[]>, ArrayList<K>> bottomLoader;
    private Callback2<Callback1<V[]>, ArrayList<K>> topLoader;
    private Provider1<V, K> mapper;
    private final CardLoading cardLoading;
    private final Class<K> cardClass;

    private int addBottomPositionOffset = 0;
    private int addTopPositionOffset = 0;
    private int startBottomLoadOffset = 0;
    private int startTopLoadOffset = 0;
    private boolean lockTop;
    private boolean lockBottom;
    private boolean inProgress;
    private boolean retryEnabled;
    private boolean actionEnabled;
    private Callback onErrorAndEmpty;
    private Callback onEmpty;
    private Callback onStartLoadingAndEmpty;
    private Callback onLoadingAndNotEmpty;
    private Callback onLoadedNotEmpty;

    public RecyclerCardAdapterLoading(Class<K> cardClass, Provider1<V, K> mapper) {
        this.cardClass = cardClass;
        this.mapper = mapper;
        cardLoading = new CardLoading();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (inProgress) return;

        if (!lockBottom && position >= getItemCount() - 1 - startBottomLoadOffset) {
            inProgress = true;
            ToolsThreads.main(true, () -> loadNow(true));
        } else if (!lockTop && topLoader != null && position <= startTopLoadOffset) {
            inProgress = true;
            ToolsThreads.main(true, () -> loadNow(false));
        }
    }

    public void loadTop() {
        load(false);
    }

    public void loadBottom() {
        load(true);
    }

    public void load(boolean bottom) {
        if (inProgress) return;
        inProgress = true;
        loadNow(bottom);
    }

    private void loadNow(boolean bottom) {
        if (bottom) lockBottom = false;
        else lockTop = false;
        cardLoading.setOnRetry(source -> load(bottom));
        cardLoading.setState(CardLoading.State.LOADING);

        ArrayList<K> cards = getByClass(cardClass);

        if (isEmpty()) {
            if (onStartLoadingAndEmpty != null) onStartLoadingAndEmpty.callback();
            else if (!contains(cardLoading)) add(bottom ? size() - addBottomPositionOffset : addTopPositionOffset, cardLoading);
        } else {
            if (onLoadingAndNotEmpty != null) onLoadingAndNotEmpty.callback();
            if (!contains(cardLoading)) add(bottom ? size() - addBottomPositionOffset : addTopPositionOffset, cardLoading);
        }

        if (bottom) bottomLoader.callback(result -> onLoaded(result, bottom), cards);
        else topLoader.callback(result -> onLoaded(result, bottom), cards);
    }

    private void onLoaded(V[] result, boolean bottom) {

        inProgress = false;

        if (result == null) {
            if (retryEnabled && (!isEmpty() || onErrorAndEmpty == null)) cardLoading.setState(CardLoading.State.RETRY);
            else remove(cardLoading);
            if (isEmpty() && onErrorAndEmpty != null) onErrorAndEmpty.callback();
            return;
        }


        if (!containsClass(cardClass) && result.length == 0) {
            if (bottom) lockBottom();
            else lockTop();
            if (actionEnabled) cardLoading.setState(CardLoading.State.ACTION);
            else {
                remove(cardLoading);
                if (onEmpty != null) onEmpty.callback();
            }
        } else {
            if (result.length == 0) {
                if (bottom) lockBottom();
                else lockTop();
            }
            remove(cardLoading);
        }

        for (int i = 0; i < result.length; i++)
            if (bottom) add(size() - addBottomPositionOffset, mapper.provide(result[i]));
            else add(addTopPositionOffset + i, mapper.provide(result[i]));

        if (!isEmpty() || result.length != 0)
            if (onLoadedNotEmpty != null) onLoadedNotEmpty.callback();

    }

    public void reloadTop() {
        reload(false);
    }

    public void reloadBottom() {
        reload(true);
    }

    public void reload(boolean bottom) {
        removeClass(cardClass);
        load(bottom);
    }


    public void lockBottom() {
        lockBottom = true;
    }

    public void lockTop() {
        lockTop = true;
    }

    public void unlockBottom() {
        lockBottom = false;
    }

    public void unlockTop() {
        lockTop = false;
    }

    @Override
    public void removeIndex(int position) {
        super.removeIndex(position);
        if (onEmpty != null && isEmpty()) onEmpty.callback();
    }

    //
    //  Setters
    //

    public RecyclerCardAdapterLoading<K, V> setAddBottomPositionOffset(int i) {
        this.addBottomPositionOffset = i;
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setAddTopPositionOffset(int i) {
        this.addTopPositionOffset = i;
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setOnLoadedNotEmpty(Callback onLoadedNotEmpty) {
        this.onLoadedNotEmpty = onLoadedNotEmpty;
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setOnLoadingAndNotEmpty(Callback onLoadingAndNotEmpty) {
        this.onLoadingAndNotEmpty = onLoadingAndNotEmpty;
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setOnStartLoadingAndEmpty(Callback onStartLoadingAndEmpty) {
        this.onStartLoadingAndEmpty = onStartLoadingAndEmpty;
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setActionEnabled(boolean actionEnabled) {
        this.actionEnabled = actionEnabled;
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setOnEmpty(Callback onEmpty) {
        this.onEmpty = onEmpty;
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setOnErrorAndEmpty(Callback onErrorAndEmpty) {
        this.onErrorAndEmpty = onErrorAndEmpty;
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setRetryMessage(@StringRes int message, @StringRes int button) {
        return setRetryMessage(ToolsResources.getString(message), ToolsResources.getString(button));
    }

    public RecyclerCardAdapterLoading<K, V> setRetryMessage(String message, String button) {
        retryEnabled = true;
        cardLoading.setRetryMessage(message);
        cardLoading.setRetryButton(button, null);
        return this;
    }


    public RecyclerCardAdapterLoading<K, V> setEmptyMessage(@StringRes int message) {
        return setEmptyMessage(ToolsResources.getString(message));
    }

    public RecyclerCardAdapterLoading<K, V> setEmptyMessage(String message) {
        return setEmptyMessage(message, null, null);
    }

    public RecyclerCardAdapterLoading<K, V> setEmptyMessage(@StringRes int message, @StringRes int button, Callback onAction) {
        return setEmptyMessage(ToolsResources.getString(message), ToolsResources.getString(button), onAction);
    }

    public RecyclerCardAdapterLoading<K, V> setEmptyMessage(String message, String button, Callback onAction) {
        actionEnabled = true;
        cardLoading.setActionMessage(message);
        cardLoading.setActionButton(button, card -> onAction.callback());
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setTopLoader(Callback2<Callback1<V[]>, ArrayList<K>> topLoader) {
        this.topLoader = topLoader;
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setBottomLoader(Callback2<Callback1<V[]>, ArrayList<K>> bottomLoader) {
        this.bottomLoader = bottomLoader;
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setStartBottomLoadOffset(int i) {
        this.startBottomLoadOffset = i;
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setTopBottomLoadOffset(int i) {
        this.startTopLoadOffset = i;
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setMapper(Provider1<V, K> mapper) {
        this.mapper = mapper;
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setCardLoadingType(CardLoading.Type type) {
        cardLoading.setType(type);
        return this;
    }

    @Override
    public RecyclerCardAdapterLoading<K, V> setNotifyCount(int notifyCount) {
        return (RecyclerCardAdapterLoading) super.setNotifyCount(notifyCount);
    }

    //
    //  Getters
    //

    public boolean isLockBottom() {
        return lockBottom;
    }

    public boolean isLockTop() {
        return lockTop;
    }

    public boolean isInProgress() {
        return inProgress;
    }

}