package com.sup.dev.android.views.adapters.pager;

import android.support.annotation.LayoutRes;
import android.view.View;

import com.sup.dev.android.views.adapters.recycler_view.RecyclerArrayAdapter;
import com.sup.dev.java.classes.callbacks.simple.CallbackPair;

import java.util.ArrayList;
import java.util.List;

public class PagerRecyclerArrayAdapter<K, V extends View> extends PagerRecyclerAdapter<V> {

    protected final ArrayList<K> items = new ArrayList<>();
    private final CallbackPair<V, K> binder;

    public PagerRecyclerArrayAdapter(@LayoutRes int layoutRes) {
        this(layoutRes, null);
    }

    public PagerRecyclerArrayAdapter(@LayoutRes int layoutRes, CallbackPair<V, K> binder) {
        super(layoutRes);
        this.binder = binder;
    }

    public final void bind(V view, int position) {
        bind(view, get(position));
    }

    public void bind(V view, K item){
        if(binder != null)binder.callback(view,item);
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

    public void clear(){
        items.clear();
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int indexOf(K item) {
        return items.indexOf(item);
    }

    protected int realPosition(int position){
        return position;
    }

    public int size(){
        return items.size();
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