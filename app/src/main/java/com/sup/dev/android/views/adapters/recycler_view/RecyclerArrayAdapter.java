package com.sup.dev.android.views.adapters.recycler_view;


import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsView;


public abstract class RecyclerArrayAdapter<K> extends RecyclerView.Adapter<RecyclerArrayAdapter.Holder> {

    private final UtilsView utilsView = SupAndroid.di.utilsView();
    private final List<K> items = new ArrayList<>();
    private final List<Holder<K>> holders = new ArrayList<>();

    private final int itemLayout;

    private int notifyCount = 0;

    public RecyclerArrayAdapter(@LayoutRes int itemLayout) {
        this.itemLayout = itemLayout;
    }

    protected View instanceView(Context context) {
        throw new RuntimeException("Override it or use itemLayout > 0");
    }

    //
    //  Bind
    //

    @Override
    public final void onBindViewHolder(final Holder holder, final int position) {
        for(int i = position; i < position + notifyCount && i < items.size(); i++)
            if(items.get(i) instanceof NotifyItem)
                ((NotifyItem)items.get(i)).notifyItem();
        removeItemFromHolders(items.get(position));
        holder.item = items.get(position);
        bind(holder.itemView, items.get(position));
    }

    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = itemLayout > 0 ? utilsView.inflate(parent, itemLayout) : instanceView(parent.getContext());
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Holder<K> holder = new Holder<>(view);
        holders.add(holder);
        return holder;
    }

    protected abstract void bind(View view, K item);

    //
    //  Items
    //

    @MainThread
    public void add(@NonNull List<K> list) {
        for (K o : list)
            add(o);
    }

    @MainThread
    public void add(@NonNull K o) {
        add(items.size(), o);
    }

    @MainThread
    public void add(int p, @NonNull K o) {
        items.add(p, o);
        notifyItemInserted(p);
    }


    @MainThread
    public void remove(@NonNull K o) {
        int position = items.indexOf(o);
        if (position == -1) return;
        removeIndex(position);
    }

    @MainThread
    public void removeIndex(int position) {
        K item = items.remove(position);
        notifyItemRemoved(position);
        removeItemFromHolders(item);
    }

    @MainThread
    public void remove(@NonNull List<K> list) {
        for (K item : list)
            remove(item);
    }

    @MainThread
    public void replace(int index, @NonNull K o) {
        removeItemFromHolders(items.get(index));
        items.set(index, o);
        notifyItemChanged(index);
    }

    @MainThread
    public void clear() {
        int count = items.size();
        items.clear();
        notifyItemRangeRemoved(0, count);
        for (Holder<K> h : holders)
            h.item = null;
    }

    public boolean isEmpty(){
        return getItemCount() == 0;
    }

    protected List<Holder<K>> getHolders() {
        return holders;
    }

    @Nullable
    public View getView(K item) {
        for (Holder<K> h : holders)
            if (h.item == item)
                return h.itemView;
        return null;
    }

    public K get(int index) {
        return items.get(index);
    }

    public int indexOf(@NonNull K o) {
        return items.indexOf(o);
    }

    public boolean contains(@NonNull K o){
        return items.contains(o);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public int size(){
        return getItemCount();
    }

    private void removeItemFromHolders(K item){
        for (Holder<K> h : holders)
            if (h.item == item)
                h.item = null;
    }

    //
    //  Notify
    //

    public void notifyAllChanged() {
        notifyItemRangeChanged(0, getItemCount());
    }

    //
    //  Setters
    //

    public RecyclerArrayAdapter setNotifyCount(int notifyCount) {
        this.notifyCount = notifyCount;
        return this;
    }

    //
    //  Holder
    //

    protected static class Holder<K> extends RecyclerView.ViewHolder {

        public K item;

        public Holder(View itemView) {
            super(itemView);
        }
    }

}
