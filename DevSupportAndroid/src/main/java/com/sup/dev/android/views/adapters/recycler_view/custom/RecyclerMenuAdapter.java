package com.sup.dev.android.views.adapters.recycler_view.custom;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerArrayAdapter;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.items.Item2;

public class RecyclerMenuAdapter<K> extends RecyclerArrayAdapter<Item2<K, String>> {

    private final Callback1<K> onSelected;
    private Callback1<K> onLongClick;

    public RecyclerMenuAdapter(Callback1<K> onSelected) {
        this(R.layout.row_text, onSelected);
    }

    protected RecyclerMenuAdapter(@LayoutRes int layout, Callback1<K> onSelected) {
        super(layout);
        this.onSelected = onSelected;
    }


    @Override
    protected void bind(View view, Item2<K, String> item) {
        ((TextView) view.findViewById(R.id.text)).setText(item.a2);

        view.findViewById(R.id.touch).setOnClickListener(v -> onSelected.callback(item.a1));

        view.findViewById(R.id.touch).setOnLongClickListener(v -> {

            if (onLongClick != null)
                onLongClick.callback(item.a1);

            return onLongClick != null;
        });
    }

    public K getKey(int index){
        return get(index).a1;
    }

    public void addKey(K key) {
        addKey(key, key.toString());
    }


    public void addKey(K key, String mask) {
        add(new Item2<>(key, mask));
    }

    public void replaceKey(int index, K key) {
        replace(index, new Item2<>(key, key.toString()));
    }

    public void replaceKey(int index, K key, String mask) {
        replace(index, new Item2<>(key, mask));
    }

    public void removeKey(K key) {
        for (int i = 0; i < getItemCount(); i++)
            if (get(i).a1 == key)
                remove(get(i--));
    }

    public int indexOfKey(K key) {
        for (int i = 0; i < getItemCount(); i++)
            if (get(i).a1 == key)
                return i;
        return -1;
    }

    public void setOnLongClick(Callback1<K> onLongClick) {
        this.onLongClick = onLongClick;
    }
}