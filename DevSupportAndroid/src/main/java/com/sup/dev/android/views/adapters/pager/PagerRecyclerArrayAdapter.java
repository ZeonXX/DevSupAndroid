package com.sup.dev.android.views.adapters.pager;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.sup.dev.android.views.adapters.recycler_view.NotifyItem;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.providers.Provider;

import java.util.ArrayList;
import java.util.List;

public class PagerRecyclerArrayAdapter<K, V extends View> extends PagerRecyclerAdapter<V> {

    protected final ArrayList<K> items = new ArrayList<>();
    private Callback2<V, K> binder;
    private int notifyCount = 1;

    public PagerRecyclerArrayAdapter() {
        this(0);
    }

    public PagerRecyclerArrayAdapter(@LayoutRes int layoutRes) {
        super(layoutRes);
    }

    public final void bind(V view, int position) {
        bind(view, get(position));

        for (int i = position; i < position + notifyCount && i < items.size(); i++)
            if (items.get(i) instanceof NotifyItem) ((NotifyItem) items.get(i)).notifyItem();
        for (int i = position - 1; i > position - notifyCount && i > -1; i--)
            if (items.get(i) instanceof NotifyItem) ((NotifyItem) items.get(i)).notifyItem();
    }

    public void bind(V view, K item) {
        if (binder != null) binder.callback(view, item);
    }

    public PagerRecyclerArrayAdapter<K, V> setBinder(Callback2<V, K> binder) {
        this.binder = binder;
        return this;
    }

    @Override
    public PagerRecyclerArrayAdapter<K, V> setProviderView(Provider<V> providerView) {
        return (PagerRecyclerArrayAdapter<K, V>) super.setProviderView(providerView);
    }

    //
    //  Items
    //

    @Override
    public int getCount() {
        return items.size();
    }

    public K get(int position) {
        return items.get(realPosition(position));
    }

    public void add(K item) {
        items.add(item);
        notifyDataSetChanged();
    }

    public void set(List<K> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void remove(K item) {
        if (items.remove(item))
            notifyDataSetChanged();
    }

    public void remove(int position) {
        items.remove(realPosition(position));
        notifyDataSetChanged();
    }

    public void clear() {
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int indexOf(K item) {
        return items.indexOf(item);
    }

    protected int realPosition(int position) {
        return position;
    }

    public int size() {
        return items.size();
    }

    public ArrayList<K> getItems() {
        return items;
    }

    public PagerRecyclerArrayAdapter<K,V> setNotifyCount(int notifyCount) {
        this.notifyCount = notifyCount;
        return this;
    }

    //
    //  Notify
    //

    public void notifyItemChanged(int position) {
        V view = getViewIfVisible(position);
        if (view != null)
            bind(view, get(realPosition(position)));

    }


}