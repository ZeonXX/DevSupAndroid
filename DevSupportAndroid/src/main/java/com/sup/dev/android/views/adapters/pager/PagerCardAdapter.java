package com.sup.dev.android.views.adapters.pager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.adapters.CardAdapter;
import com.sup.dev.android.views.adapters.NotifyItem;
import com.sup.dev.android.views.cards.Card;
import com.sup.dev.java.classes.collections.HashList;

public abstract class PagerCardAdapter extends PagerAdapter implements CardAdapter{

    private final ArrayList<Holder> holders = new ArrayList<>();
    private final ArrayList<Card> items = new ArrayList<>();
    private final HashList<Class<? extends Card>, View> viewCash = new HashList<>();

    private int notifyCount = 0;

    public PagerCardAdapter() {

    }

    //
    //  items
    //

    @Override
    public Object instantiateItem(ViewGroup parent, int position) {
        Holder holder = getFreeHolder(parent);
        parent.addView(holder.itemView);

        for(int i = position; i < position + notifyCount && i < items.size(); i++)
            if(items.get(i) instanceof NotifyItem)
                ((NotifyItem)items.get(i)).notifyItem();

        Card card = items.get(position);
        FrameLayout frame = holder.itemView;

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

        return holder;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return object instanceof PagerCardAdapter.Holder && ((Holder) object).itemView == view;
    }

    @Override
    public void destroyItem(ViewGroup parent, int position, Object object) {
        parent.removeView(((Holder) object).itemView);
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    public int getItemId(int position) {
        return position;
    }

    public ArrayList<Holder> getHolders() {

        ArrayList<Holder> list = new ArrayList<>();
        for (Holder h : holders)
            list.add(h);

        return list;
    }

    //
    //  Items
    //

    @Override
    public int getCount() {
        return items.size();
    }

    public Card get(int position) {
        return items.get(realPosition(position));
    }

    public void add(Card card) {
        items.add(card);
        notifyDataSetChanged();
    }

    @Override
    public void add(int i, Card card) {
        items.add(i, card);
        notifyDataSetChanged();
    }

    public void set(List<Card> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public void remove(Card item) {
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

    public int indexOf(Card item) {
        return items.indexOf(item);
    }

    @Override
    public boolean contains(Card card) {
        return indexOf(card) > -1;
    }

    protected int realPosition(int position) {
        return position;
    }

    public int size() {
        return items.size();
    }

    public ArrayList<Card> getItems() {
        return items;
    }

    @Override
    public View getView(Card card) {
        ArrayList<Holder> holders = getHolders();
        for (Holder h : holders) if (h.item == card) return h.itemView;
        return null;
    }

    public <K extends PagerCardAdapter>K setNotifyCount(int notifyCount) {
        this.notifyCount = notifyCount;
        return (K) this;
    }

    private void removeItemFromHolders(Card item){
        for (Holder h : holders)
            if (h.item == item)
                h.item = null;
    }

    //
    //  Getters
    //

    public ArrayList<View> getViews() {

        ArrayList<View> list = new ArrayList<>();
        for (Holder h : holders)
            list.add(h.itemView);

        return list;
    }

    public View getViewIfVisible(int position) {
        for (Holder h : holders)
            if (h.item == items.get(position))
                return h.itemView;
        return null;
    }

    //
    //  Holder
    //

    private Holder getFreeHolder(ViewGroup parent) {

        for (Holder holder : holders)
            if (holder.itemView.getParent() == null)
                return holder;

        Holder holder = new Holder(parent.getContext());
        holders.add(holder);

        return holder;
    }

    protected static class Holder {

        public Card item;
        public FrameLayout itemView;

        private Holder(Context context) {
            this.itemView = new FrameLayout(context);
        }
    }
}