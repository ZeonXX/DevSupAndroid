package com.sup.dev.android.views.adapters.recycler_view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.FrameLayout;

import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.cards.Card;
import com.sup.dev.java.classes.collections.HashList;
import com.sup.dev.java.tools.ToolsClass;

import java.util.ArrayList;

public class RecyclerCardAdapter extends RecyclerArrayAdapter<Card> {

    private final HashList<Class<? extends Card>, View> viewCash = new HashList<>();

    public RecyclerCardAdapter() {
        super(0);
    }

    @Override
    protected View instanceView(Context context) {
        return new FrameLayout(context);
    }

    @Override
    protected void bind(View view, Card card) {

        FrameLayout frame = (FrameLayout) view;

        Class<? extends Card> tag = (Class<? extends Card>) frame.getTag();

        if(frame.getChildCount() != 0)
            viewCash.add(tag, frame.getChildAt(0));
        frame.removeAllViews();

        View cardView = viewCash.removeOne(card.getClass());
        if(cardView == null)
            cardView = card.instanceView(view.getContext());


        frame.addView(ToolsView.removeFromParent(cardView));
        frame.setTag(card.getClass());

        card.bindView(cardView);
    }

    @Override
    public void add(@NonNull Card o) {
        o.setAdapter(this);
        super.add(o);
    }

    @Override
    public void add(int p, @NonNull Card o) {
        o.setAdapter(this);
        super.add(p, o);
    }

    @Override
    public void remove(@NonNull Card o) {
        o.setAdapter(null);
        super.remove(o);
    }

    public void removeClass(Class<? extends Card> c) {
        for (int i = 0; i < getItemCount(); i++)
            if (ToolsClass.instanceOf(get(i).getClass(), c))
                removeIndex(i--);
    }


    public ArrayList<Card> getByTag(Object tag) {
        ArrayList<Card> list = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++)
            if ((get(i).tag == null && tag == null) || (tag != null && get(i).tag != null && get(i).tag.equals(tag)))
                list.add(get(i));
        return list;
    }

    public <K extends Card>ArrayList<K> getByClass(Class<K> c) {
        ArrayList<K> list = new ArrayList<>();
        for (int i = 0; i < getItemCount(); i++)
            if (ToolsClass.instanceOf(get(i).getClass(), c))
                list.add((K)get(i));
        return list;
    }

    public boolean containsClass(Class<? extends Card> c){
        for (int i = 0; i < getItemCount(); i++)
            if (ToolsClass.instanceOf(get(i).getClass(), c))
                return true;
        return false;
    }

    public int sizeClass(Class<? extends Card> c){
        int x = 0;
        for (int i = 0; i < getItemCount(); i++)
            if (ToolsClass.instanceOf(get(i).getClass(), c))
               x++;
        return x;
    }

    @Nullable
    public View getView(Card item) {
        View view = super.getView(item);

        if(view != null){
            FrameLayout frame = (FrameLayout) view;
            if(frame.getChildCount() == 1)
                return frame.getChildAt(0);
        }

        return null;
    }

}
