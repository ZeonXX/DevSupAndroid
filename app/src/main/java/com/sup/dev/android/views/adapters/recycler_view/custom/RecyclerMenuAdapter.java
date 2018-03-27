package com.sup.dev.android.views.adapters.recycler_view.custom;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerArrayAdapter;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;
import com.sup.dev.java.classes.items.Pair;

public class RecyclerMenuAdapter<K> extends RecyclerArrayAdapter<Pair<K, String>> {

    private final CallbackSource<K> onSelected;
    private CallbackSource<K> onLongClick;

    public RecyclerMenuAdapter(CallbackSource<K> onSelected) {
        this(R.layout.row_text, onSelected);
    }

    protected RecyclerMenuAdapter(@LayoutRes int layout, CallbackSource<K> onSelected) {
        super(layout);
        this.onSelected = onSelected;
    }


    @Override
    protected void bind(View view, Pair<K, String> item) {
        ((TextView) view.findViewById(R.id.text)).setText(item.right);

        view.findViewById(R.id.touch).setOnClickListener(v -> onSelected.callback(item.left));

        view.findViewById(R.id.touch).setOnLongClickListener(v -> {

            if (onLongClick != null)
                onLongClick.callback(item.left);

            return onLongClick != null;
        });
    }

    public K getKey(int index){
        return get(index).left;
    }

    public void addKey(K key) {
        addKey(key, key.toString());
    }


    public void addKey(K key, String mask) {
        add(new Pair<>(key, mask));
    }

    public void replaceKey(int index, K key) {
        replace(index, new Pair<>(key, key.toString()));
    }

    public void replaceKey(int index, K key, String mask) {
        replace(index, new Pair<>(key, mask));
    }

    public void removeKey(K key) {
        for (int i = 0; i < getItemCount(); i++)
            if (get(i).left == key)
                remove(get(i--));
    }

    public int indexOfKey(K key) {
        for (int i = 0; i < getItemCount(); i++)
            if (get(i).left == key)
                return i;
        return -1;
    }

    public void setOnLongClick(CallbackSource<K> onLongClick) {
        this.onLongClick = onLongClick;
    }
}