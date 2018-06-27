package com.sup.dev.android.views.adapters.recycler_view;


import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.adapters.CardAdapter;
import com.sup.dev.android.views.adapters.NotifyItem;
import com.sup.dev.android.views.cards.Card;
import com.sup.dev.java.classes.callbacks.list.CallbacksList;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.collections.HashList;
import com.sup.dev.java.tools.ToolsClass;

import java.util.ArrayList;
import java.util.List;


public class RecyclerCardAdapter extends RecyclerView.Adapter<RecyclerCardAdapter.Holder> implements CardAdapter {

    private final HashList<Class<? extends Card>, View> viewCash = new HashList<>();
    private final ArrayList<Card> items = new ArrayList<>();
    private final ArrayList<Holder> holders = new ArrayList<>();
    private final CallbacksList onItemsChangeListeners = new CallbacksList();

    private int notifyCount = 0;

    //
    //  Bind
    //

    @Override
    public final void onBindViewHolder(final Holder holder, final int position) {
        for (int i = position; i < position + notifyCount && i < items.size(); i++)
            if (items.get(i) instanceof NotifyItem)
                ((NotifyItem) items.get(i)).notifyItem();
        removeItemFromHolders(items.get(position));
        holder.item = items.get(position);

        Card card = items.get(position);
        FrameLayout frame = (FrameLayout) holder.itemView;

        Class<? extends Card> tag = (Class<? extends Card>) frame.getTag();

        if (frame.getChildCount() != 0)
            viewCash.add(tag, frame.getChildAt(0));
        frame.removeAllViews();

        View cardView = viewCash.removeOne(card.getClass());
        if (cardView == null)
            cardView = card.instanceView(frame.getContext());


        frame.addView(ToolsView.removeFromParent(cardView));
        frame.setTag(card.getClass());

        card.bindView(cardView);
    }

    @Override
    public Holder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View view = new FrameLayout(parent.getContext());
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Holder holder = new Holder(view);
        holders.add(holder);
        return holder;
    }

    private void removeItemFromHolders(Card item) {
        for (Holder h : holders)
            if (h.item == item)
                h.item = null;
    }

    //
    //  Items
    //

    public void add(@NonNull List<Card> list) {
        for (Card card : list)
            add(card);
    }

    public void add(@NonNull Card card) {
        add(items.size(), card);
    }

    public void add(int p, @NonNull Card card) {
        card.setAdapter(this);
        items.add(p, card);
        notifyItemInserted(p);
        onItemsChangeListeners.callback();
    }

    public void remove(@NonNull Card card) {
        card.setAdapter(null);
        int position = indexOf(card);
        if (position == -1) return;
        remove(position);
    }

    public void remove(Class<? extends Card> c) {
        for (int i = 0; i < getItemCount(); i++)
            if (ToolsClass.instanceOf(get(i).getClass(), c))
                remove(i--);
    }

    public void remove(@NonNull List<Card> list) {
        for (Card card : list) remove(card);
    }

    public void remove(int position) {
        Card card = items.remove(position);
        notifyItemRemoved(position);
        removeItemFromHolders(card);
        onItemsChangeListeners.callback();
    }

    public void replace(int index, @NonNull Card o) {
        removeItemFromHolders(items.get(index));
        items.set(index, o);
        notifyItemChanged(index);
    }

    public void clear() {
        int count = items.size();
        items.clear();
        notifyItemRangeRemoved(0, count);
        for (Holder h : holders)
            h.item = null;
    }


    //
    //  Listeners
    //

    public void addItemsChangeListener(Callback callback){
        onItemsChangeListeners.add(callback);
    }
    public void removeItemsChangeListener(Callback callback){
        onItemsChangeListeners.remove(callback);
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

    public RecyclerCardAdapter setNotifyCount(int notifyCount) {
        this.notifyCount = notifyCount;
        return this;
    }

    //
    //  Getters
    //

    public boolean containsClass(Class<? extends Card> c) {
        for (int i = 0; i < getItemCount(); i++)
            if (ToolsClass.instanceOf(get(i).getClass(), c))
                return true;
        return false;
    }

    public int size(Class<? extends Card> c) {
        int x = 0;
        for (int i = 0; i < getItemCount(); i++)
            if (ToolsClass.instanceOf(get(i).getClass(), c))
                x++;
        return x;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public int size() {
        return getItemCount();
    }

    protected ArrayList<Holder> getHolders() {
        return holders;
    }


    public Card get(int index) {
        return items.get(index);
    }

    public int indexOf(@NonNull Card o) {
        return items.indexOf(o);
    }

    public boolean contains(@NonNull Card o) {
        return items.contains(o);
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public ArrayList<Card> getByTag(Object tag) {
        ArrayList<Card> list = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++)
            if ((get(i).tag == null && tag == null) || (tag != null && get(i).tag != null && get(i).tag.equals(tag)))
                list.add(get(i));
        return list;
    }

    public <K extends Card> ArrayList<K> getByClass(Class<K> c) {
        ArrayList<K> list = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++)
            if (ToolsClass.instanceOf(get(i).getClass(), c))
                list.add((K) get(i));
        return list;
    }

    public View getView(Card item) {
        View view = null;

        for (Holder h : holders)
            if (h.item == item)
                view = h.itemView;

        if (view != null) {
            FrameLayout frame = (FrameLayout) view;
            if (frame.getChildCount() == 1)
                return frame.getChildAt(0);
        }

        return null;
    }

    //
    //  Holder
    //

    protected static class Holder extends RecyclerView.ViewHolder {

        public Card item;

        public Holder(View itemView) {
            super(itemView);
        }
    }

}
