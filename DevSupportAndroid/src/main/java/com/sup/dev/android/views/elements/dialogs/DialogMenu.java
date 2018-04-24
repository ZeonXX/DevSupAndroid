package com.sup.dev.android.views.elements.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.android.views.elements.cards.CardDivider;
import com.sup.dev.android.views.elements.cards.CardMenu;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class DialogMenu<K> extends DialogRecycler {

    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();
    private final RecyclerCardAdapter adapter;

    private int prefCount = 0;
    private Callback1<K> onSelected;

    public DialogMenu(Context viewContext) {
        super(viewContext);
        adapter = new RecyclerCardAdapter();
        setAdapter(adapter);
    }

    public DialogMenu<K> addPreferred(K v) {
        return addPreferred(v, v.toString());
    }


    public DialogMenu<K> addPreferred(K key, String text) {
        if (prefCount == 0)
            adapter.add(0, new CardDivider());
        adapter.add(prefCount, instanceCard(key, text));
        prefCount++;
        return this;
    }


    public DialogMenu<K> add(K v) {
        return add(v, v.toString());
    }

    public DialogMenu<K> add(K key, @StringRes int text) {
        adapter.add(instanceCard(key, utilsResources.getString(text)));
        return this;
    }

    public DialogMenu<K> add(K key, String text) {
        adapter.add(instanceCard(key, text));
        return this;
    }

    public DialogMenu setPreferred(K key) {
        for (CardMenu card : adapter.getByClass(CardMenu.class))
            if (card.tag.equals(key)) {
                adapter.remove(card);
                addPreferred(key, card.getText());
            }
        return this;
    }

    private CardMenu instanceCard(K key, String text) {
        CardMenu cardMenu = new CardMenu();
        cardMenu.tag = key;
        cardMenu.setText(text);
        cardMenu.setOnClick(() -> onSelected(key));
        return cardMenu;
    }

    public DialogMenu<K> onSelected(K key) {
        if (isAutoHideOnEnter()) hide();
        else setEnabled(false);
        if (onSelected != null) onSelected.callback(key);
        return this;
    }

    public DialogMenu<K> setOnSelected(Callback1<K> onSelected) {
        this.onSelected = onSelected;
        return this;
    }


    //
    //  Setters
    //


    public DialogMenu<K> setTitle(@StringRes int title) {
        return (DialogMenu<K>)super.setTitle(title);
    }

    public DialogMenu<K> setTitle(String title) {
        return (DialogMenu<K>)super.setTitle(title);
    }

    public DialogMenu<K> setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogMenu<K>)super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogMenu<K> setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogMenu<K>)super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogMenu<K> setCancelable(boolean cancelable) {
        return (DialogMenu<K>)super.setCancelable(cancelable);
    }

    public DialogMenu<K> setOnCancel(String s) {
        return (DialogMenu<K>)super.setOnCancel(s);
    }

    public DialogMenu<K> setOnCancel(Callback1<BaseDialog> onCancel) {
        return (DialogMenu<K>)super.setOnCancel(onCancel);
    }

    public DialogMenu<K> setOnCancel(@StringRes int s) {
        return (DialogMenu<K>)super.setOnCancel(s);
    }

    public DialogMenu<K> setOnCancel(@StringRes int s, Callback1<BaseDialog> onCancel) {
        return (DialogMenu<K>)super.setOnCancel(s, onCancel);
    }

    public DialogMenu<K> setOnCancel(String s, Callback1<BaseDialog> onCancel) {
        return (DialogMenu<K>)super.setOnCancel(s, onCancel);
    }

    public DialogMenu<K> setEnabled(boolean enabled) {
        for (CardMenu card : adapter.getByClass(CardMenu.class))
            card.setEnabled(enabled);
        return (DialogMenu<K>) super.setEnabled(enabled);
    }

    public DialogMenu<K> setOnEnter(@StringRes int s) {
        return (DialogMenu<K>)super.setOnEnter(s);
    }

    public DialogMenu<K> setOnEnter(String s) {
        return (DialogMenu<K>)super.setOnEnter(s);
    }

    public DialogMenu<K> setOnEnter(@StringRes int s, Callback1<BaseDialog> onEnter) {
        return (DialogMenu<K>)super.setOnEnter(s, onEnter);
    }

    public DialogMenu<K> setOnEnter(String s, Callback1<BaseDialog> onEnter) {
        return (DialogMenu<K>)super.setOnEnter(s, onEnter);
    }



}
