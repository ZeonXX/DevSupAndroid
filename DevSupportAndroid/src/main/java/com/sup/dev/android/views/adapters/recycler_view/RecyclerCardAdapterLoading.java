package com.sup.dev.android.views.adapters.recycler_view;

import android.support.annotation.StringRes;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.android.views.elements.cards.Card;
import com.sup.dev.android.views.elements.cards.CardLoading;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.CallbackPair;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;
import com.sup.dev.java.classes.providers.ProviderArg;
import com.sup.dev.java.utils.interfaces.UtilsThreads;

import java.util.ArrayList;
import java.util.List;

public class RecyclerCardAdapterLoading<K extends Card, V> extends RecyclerCardAdapter {

    private final UtilsThreads utilsThreads = SupAndroid.di.utilsThreads();
    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();
    private final CallbackPair<CallbackSource<V[]>, ArrayList<K>> loader;
    private final ProviderArg<V, K> mapper;
    private final CardLoading cardLoading;
    private final Class<K> cardClass;

    private int addPositionOffset = 0;
    private int startLoadOffset = 0;
    private boolean lock;
    private boolean inProgress;
    private boolean retryEnabled;
    private boolean actionEnabled;
    private Callback onErrorAndEmpty;
    private Callback onEmpty;
    private Callback onLoadingAndEmpty;
    private Callback onLoadingAndNotEmpty;
    private Callback onLoadedNotEmpty;

    public RecyclerCardAdapterLoading(Class<K> cardClass, CallbackPair<CallbackSource<V[]>, ArrayList<K>> loader, ProviderArg<V, K> mapper) {
        this.loader = loader;
        this.cardClass = cardClass;
        this.mapper = mapper;
        cardLoading = new CardLoading();
    }

    @Override
    public void onBindViewHolder(Holder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (lock || inProgress) return;

        if (position >= getItemCount() - 1 - startLoadOffset) {
            inProgress = true;
            utilsThreads.main(true, this::loadNow);
        }
    }

    public void load() {
        if (inProgress) return;
        inProgress = true;
        loadNow();
    }

    private void loadNow() {
        lock = false;
        cardLoading.setState(CardLoading.State.LOADING);

        ArrayList<K> cards = getByClass(cardClass);

        if (cards.isEmpty()) {
            if (onLoadingAndEmpty != null) onLoadingAndEmpty.callback();
            else if (!contains(cardLoading)) add(cardLoading);
        } else {
            if (onLoadingAndNotEmpty != null) onLoadingAndNotEmpty.callback();
            else if (!contains(cardLoading)) add(cardLoading);
        }

        loader.callback(this::onLoaded, cards);
    }

    private void onLoaded(V[] result) {

        inProgress = false;
        boolean isEmpty = !containsClass(cardClass);

        if (result == null) {
            if (retryEnabled && (!isEmpty || onErrorAndEmpty == null)) cardLoading.setState(CardLoading.State.RETRY);
            else remove(cardLoading);
            if (isEmpty && onErrorAndEmpty != null) onErrorAndEmpty.callback();
            return;
        }


        if (isEmpty && result.length == 0) {
            lock();
            if (actionEnabled) cardLoading.setState(CardLoading.State.ACTION);
            else {
                remove(cardLoading);
                if (onEmpty != null) onEmpty.callback();
            }
        } else {
            if (result.length == 0) lock();
            remove(cardLoading);
        }

        for (V aResult : result) add(size() - addPositionOffset, mapper.provide(aResult));

        if (!isEmpty || result.length != 0)
            if (onLoadedNotEmpty != null) onLoadedNotEmpty.callback();

    }

    public void reload() {
        removeClass(cardClass);
        load();
    }


    public void lock() {
        lock = true;
    }

    public void unlock() {
        lock = false;
    }

    //
    //  Setters
    //


    public RecyclerCardAdapterLoading<K, V> setAddPositionOffset(int addPositionOffset) {
        this.addPositionOffset = addPositionOffset;
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

    public RecyclerCardAdapterLoading<K, V> setOnLoadingAndEmpty(Callback onLoadingAndEmpty) {
        this.onLoadingAndEmpty = onLoadingAndEmpty;
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
        return setRetryMessage(utilsResources.getString(message), utilsResources.getString(button));
    }

    public RecyclerCardAdapterLoading<K, V> setRetryMessage(String message, String button) {
        retryEnabled = true;
        cardLoading.setRetryMessage(message);
        cardLoading.setRetryButton(button, card -> load());
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setEmptyMessage(String message) {
        return setEmptyMessage(message, null, null);
    }

    public RecyclerCardAdapterLoading<K, V> setEmptyMessage(@StringRes int message, @StringRes int button, Callback onAction) {
        return setEmptyMessage(utilsResources.getString(message), utilsResources.getString(button), onAction);
    }

    public RecyclerCardAdapterLoading<K, V> setEmptyMessage(String message, String button, Callback onAction) {
        actionEnabled = true;
        cardLoading.setActionMessage(message);
        cardLoading.setActionButton(button, card -> onAction.callback());
        return this;
    }

    public RecyclerCardAdapterLoading<K, V> setStartLoadOffset(int startLoadOffset) {
        this.startLoadOffset = startLoadOffset;
        return this;
    }

    @Override
    public RecyclerCardAdapterLoading<K, V> setNotifyCount(int notifyCount) {
        return (RecyclerCardAdapterLoading) super.setNotifyCount(notifyCount);
    }

    //
    //  Getters
    //

    public boolean isLock() {
        return lock;
    }

    public boolean isInProgress() {
        return inProgress;
    }

}

