package com.sup.dev.android.views.widgets.popup;

import android.support.annotation.StringRes;
import android.view.MenuItem;
import android.view.View;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.java.classes.callbacks.simple.Callback2;

import java.util.ArrayList;

public class ViewPopupMenu<K> implements android.support.v7.widget.PopupMenu.OnMenuItemClickListener {

    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();
    private final android.support.v7.widget.PopupMenu popupMenu;
    private final ArrayList<K> keys = new ArrayList<>();

    private Callback2<ViewPopupMenu, K> onSelected;

    public ViewPopupMenu(View anchorView) {
        popupMenu = new android.support.v7.widget.PopupMenu(anchorView.getContext(), anchorView);
        popupMenu.setOnMenuItemClickListener(this);
    }

    //
    //  Methods
    //

    public ViewPopupMenu<K> setOnSelected(Callback2<ViewPopupMenu, K> onSelected){
        this.onSelected = onSelected;
        return this;
    }

    public ViewPopupMenu<K> show() {
        popupMenu.show();
        return this;
    }

    public ViewPopupMenu<K> hide() {
        popupMenu.dismiss();
        return this;
    }

    public ViewPopupMenu<K> addItem(@StringRes int mask) {
        String string = utilsResources.getString(mask);
        return addItem((K) string, string);
    }

    public ViewPopupMenu<K> addItem(String mask) {
        return addItem((K) mask, mask);
    }

    public ViewPopupMenu<K> addItem(K key, @StringRes int mask) {
        return addItem(key, utilsResources.getString(mask));
    }

    public ViewPopupMenu<K> addItem(K key, String mask) {
        keys.add(key);
        popupMenu.getMenu().add(0, keys.size() - 1, 0, mask);
        return this;
    }

    //
    //  Events
    //

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (onSelected != null)
            onSelected.callback(this, keys.get(item.getItemId()));
        return true;
    }
}
