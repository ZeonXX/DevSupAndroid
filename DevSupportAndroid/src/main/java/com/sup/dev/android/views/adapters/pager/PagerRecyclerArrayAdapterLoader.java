package com.sup.dev.android.views.adapters.pager;

import android.view.View;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.providers.Provider1;
import com.sup.dev.java.utils.interfaces.UtilsThreads;

import java.util.ArrayList;

public class PagerRecyclerArrayAdapterLoader<K, X, V extends View> extends PagerRecyclerArrayAdapter<K, V> {

    private final UtilsThreads utilsThreads = SupAndroid.di.utilsThreads();

    private final Callback2<Callback1<X[]>, ArrayList<K>> loader;
    private final Provider1<X, K> mapper;

    private int startLoadOffset = 0;
    private boolean lock;
    private boolean inProgress;
    private Callback onErrorAndEmpty;
    private Callback onEmpty;
    private Callback onLoadingAndEmpty;
    private Callback onLoadingAndNotEmpty;
    private Callback onLoadedNotEmpty;

    public PagerRecyclerArrayAdapterLoader(int layoutRes, Callback2<Callback1<X[]>, ArrayList<K>> loader, Provider1<X, K> mapper) {
        super(layoutRes);
        this.loader = loader;
        this.mapper = mapper;
    }

    @Override
    public PagerRecyclerArrayAdapterLoader<K, X, V> setBinder(Callback2<V, K> binder) {
        return (PagerRecyclerArrayAdapterLoader<K, X, V>) super.setBinder(binder);
    }

    @Override
    public void bind(V view, K item) {
        super.bind(view, item);
        int position = indexOf(item);

        if (lock || inProgress) return;

        if (position >= size() - 1 - startLoadOffset) {
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

        if (items.isEmpty()) {
            if (onLoadingAndEmpty != null) onLoadingAndEmpty.callback();
        } else {
            if (onLoadingAndNotEmpty != null) onLoadingAndNotEmpty.callback();
        }

        loader.callback(this::onLoaded, items);
    }

    private void onLoaded(X[] result) {

        inProgress = false;
        boolean isEmpty = items.isEmpty();

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


    public PagerRecyclerArrayAdapterLoader<K, X, V> setOnLoadedNotEmpty(Callback onLoadedNotEmpty) {
        this.onLoadedNotEmpty = onLoadedNotEmpty;
        return this;
    }

    public PagerRecyclerArrayAdapterLoader<K, X, V> setOnLoadingAndNotEmpty(Callback onLoadingAndNotEmpty) {
        this.onLoadingAndNotEmpty = onLoadingAndNotEmpty;
        return this;
    }

    public PagerRecyclerArrayAdapterLoader<K, X, V> setOnLoadingAndEmpty(Callback onLoadingAndEmpty) {
        this.onLoadingAndEmpty = onLoadingAndEmpty;
        return this;
    }

    public PagerRecyclerArrayAdapterLoader<K, X, V> setOnEmpty(Callback onEmpty) {
        this.onEmpty = onEmpty;
        return this;
    }

    public PagerRecyclerArrayAdapterLoader<K, X, V> setOnErrorAndEmpty(Callback onErrorAndEmpty) {
        this.onErrorAndEmpty = onErrorAndEmpty;
        return this;
    }

    public PagerRecyclerArrayAdapterLoader<K, X, V> setStartLoadOffset(int startLoadOffset) {
        this.startLoadOffset = startLoadOffset;
        return this;
    }

    @Override
    public PagerRecyclerArrayAdapterLoader<K, X, V> setNotifyCount(int notifyCount) {
        return (PagerRecyclerArrayAdapterLoader<K, X, V>) super.setNotifyCount(notifyCount);
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
