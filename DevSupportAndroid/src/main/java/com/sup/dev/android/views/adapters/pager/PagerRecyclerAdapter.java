package com.sup.dev.android.views.adapters.pager;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.java.classes.providers.Provider;

public abstract class PagerRecyclerAdapter<V extends View> extends PagerAdapter {

    private final UtilsView utilsView = SupAndroid.di.utilsView();
    private final List<Holder<V>> cache = new ArrayList<>();
    private final int layoutRes;

    private Provider<V> providerView;

    public PagerRecyclerAdapter(@LayoutRes int layoutRes) {
        this.layoutRes = layoutRes;
    }

    public PagerRecyclerAdapter<V> setProviderView(Provider<V> providerView) {
        this.providerView = providerView;
        return this;
    }

    //
    //  items
    //

    @Override
    public Object instantiateItem(ViewGroup parent, int position) {
        Holder<V> holder = getFreeHolder(parent);
        holder.position = position;
        parent.addView(holder.view);
        bind(holder.view, position);
        return holder;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object instanceof PagerRecyclerAdapter.Holder && ((Holder) object).view == view;
    }

    @Override
    public void destroyItem(ViewGroup parent, int position, Object object) {
        parent.removeView(((Holder) object).view);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public int getItemId(int position) {
        return position;
    }

    //  Можно переопределить и передавать собственный View, не используя layoutRes
    protected V instanceView(ViewGroup parent) {
        if (layoutRes == 0) return providerView.provide();
        return (V) utilsView.inflate(parent, layoutRes);
    }

    public abstract void bind(V view, int position);

    public ArrayList<Holder<V>> getHolders() {

        ArrayList<Holder<V>> list = new ArrayList<>();
        for (Holder<V> h : cache)
            list.add(h);

        return list;
    }

    //
    //  Getters
    //

    public ArrayList<V> getViews() {

        ArrayList<V> list = new ArrayList<>();
        for (Holder<V> h : cache)
            list.add(h.view);

        return list;
    }

    public V getViewIfVisible(int position) {

        for (Holder<V> h : cache)
            if (h.position == position)
                return h.view;

        return null;
    }

    //
    //  Holder
    //

    private Holder<V> getFreeHolder(ViewGroup parent) {

        for (Holder<V> holder : cache)
            if (holder.view.getParent() == null)
                return holder;

        Holder<V> holder = new Holder<>(instanceView(parent));
        cache.add(holder);

        return holder;
    }

    protected static class Holder<V extends View> {

        public final V view;
        public int position;

        private Holder(@NonNull V view) {
            this.view = view;
        }
    }
}