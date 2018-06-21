package com.sup.dev.android.views.adapters.pager;

import android.view.View;
import android.view.ViewGroup;

import com.sup.dev.android.views.cards.Card;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.providers.Provider1;
import com.sup.dev.java.tools.ToolsThreads;

import java.util.ArrayList;

public class PagerCardAdapterLoader<X> extends PagerCardAdapter {

    private final Callback2<Callback1<X[]>, ArrayList<Card>> loader;
    private final Provider1<X, Card> mapper;

    private int startLoadOffset = 0;
    private boolean lock;
    private boolean inProgress;
    private Callback onErrorAndEmpty;
    private Callback onEmpty;
    private Callback onLoadingAndEmpty;
    private Callback onLoadingAndNotEmpty;
    private Callback onLoadedNotEmpty;

    public PagerCardAdapterLoader(Callback2<Callback1<X[]>, ArrayList<Card>> loader, Provider1<X, Card> mapper) {
        super();
        this.loader = loader;
        this.mapper = mapper;
    }

    @Override
    public Object instantiateItem(ViewGroup parent, int position) {
        Object o = super.instantiateItem(parent, position);

        if (lock || inProgress) return o;

        if (position >= size() - 1 - startLoadOffset) {
            inProgress = true;
            ToolsThreads.main(true, this::loadNow);
        }

        return o;
    }


    public void load() {
        if (inProgress) return;
        inProgress = true;
        loadNow();
    }

    private void loadNow() {
        lock = false;

        if (isEmpty()) {
            if (onLoadingAndEmpty != null) onLoadingAndEmpty.callback();
        } else {
            if (onLoadingAndNotEmpty != null) onLoadingAndNotEmpty.callback();
        }

        loader.callback(this::onLoaded, getItems());
    }

    private void onLoaded(X[] result) {

        inProgress = false;
        boolean isEmpty = isEmpty();

        if (result == null) {
            if (isEmpty && onErrorAndEmpty != null) onErrorAndEmpty.callback();
            return;
        }


        if (isEmpty && result.length == 0) {
            lock();
            if (onEmpty != null) onEmpty.callback();
        } else {
            if (result.length == 0) lock();
        }

        for (X aResult : result) add(mapper.provide(aResult));

        if (!isEmpty || result.length != 0)
            if (onLoadedNotEmpty != null) onLoadedNotEmpty.callback();

    }

    public void reload() {
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


    public PagerCardAdapterLoader<X> setOnLoadedNotEmpty(Callback onLoadedNotEmpty) {
        this.onLoadedNotEmpty = onLoadedNotEmpty;
        return this;
    }

    public PagerCardAdapterLoader<X> setOnLoadingAndNotEmpty(Callback onLoadingAndNotEmpty) {
        this.onLoadingAndNotEmpty = onLoadingAndNotEmpty;
        return this;
    }

    public PagerCardAdapterLoader<X> setOnLoadingAndEmpty(Callback onLoadingAndEmpty) {
        this.onLoadingAndEmpty = onLoadingAndEmpty;
        return this;
    }

    public PagerCardAdapterLoader<X> setOnEmpty(Callback onEmpty) {
        this.onEmpty = onEmpty;
        return this;
    }

    public PagerCardAdapterLoader<X> setOnErrorAndEmpty(Callback onErrorAndEmpty) {
        this.onErrorAndEmpty = onErrorAndEmpty;
        return this;
    }

    public PagerCardAdapterLoader<X> setStartLoadOffset(int startLoadOffset) {
        this.startLoadOffset = startLoadOffset;
        return this;
    }

    @Override
    public PagerCardAdapterLoader<X> setNotifyCount(int notifyCount) {
        return (PagerCardAdapterLoader<X>) super.setNotifyCount(notifyCount);
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
