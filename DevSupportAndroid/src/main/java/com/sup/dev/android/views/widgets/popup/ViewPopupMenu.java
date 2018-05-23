package com.sup.dev.android.views.widgets.popup;

import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.v7.view.menu.MenuBuilder;
import android.support.v7.view.menu.MenuPopupHelper;
import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import java.util.ArrayList;

public class ViewPopupMenu<K> implements PopupMenu.OnMenuItemClickListener {

    private final PopupMenu popupMenu;
    private final ArrayList<K> keys = new ArrayList<>();
    private final View anchorView;

    private boolean hasIcon;
    private Callback2<ViewPopupMenu, K> onSelected;

    public ViewPopupMenu(View anchorView) {
        this(anchorView, Gravity.LEFT);
    }

    public ViewPopupMenu(View anchorView, int gravity) {
        this.anchorView = anchorView;
        popupMenu = new PopupMenu(anchorView.getContext(), anchorView, gravity);
        popupMenu.setOnMenuItemClickListener(this);
    }

    //
    //  Methods
    //

    public ViewPopupMenu<K> setOnSelected(Callback2<ViewPopupMenu, K> onSelected) {
        this.onSelected = onSelected;
        return this;
    }

    public ViewPopupMenu<K> show() {
        if(hasIcon) {
            MenuPopupHelper menuHelper = new MenuPopupHelper(anchorView.getContext(), (MenuBuilder) popupMenu.getMenu(), anchorView);
            menuHelper.setForceShowIcon(true);
            menuHelper.show();
        }else{
            popupMenu.show();
        }
        return this;
    }

    public ViewPopupMenu<K> hide() {
        popupMenu.dismiss();
        return this;
    }

    public ViewPopupMenu<K> addItem(@StringRes int mask) {
        String string = ToolsResources.getString(mask);
        return addItem((K) string, string);
    }

    public ViewPopupMenu<K> addItem(@StringRes int mask, @DrawableRes int icon) {
        String string = ToolsResources.getString(mask);
        return addItem((K) string, string, icon);
    }

    public ViewPopupMenu<K> addItem(String mask) {
        return addItem((K) mask, mask);
    }

    public ViewPopupMenu<K> addItem(String mask, @DrawableRes int icon) {
        return addItem((K) mask, mask, icon);
    }

    public ViewPopupMenu<K> addItem(K key, @StringRes int mask) {
        return addItem(key, ToolsResources.getString(mask));
    }

    public ViewPopupMenu<K> addItemK(K key, @StringRes int mask) {
        return addItem(key, ToolsResources.getString(mask));
    }

    public ViewPopupMenu<K> addItem(K key, String mask) {
        return addItem(key, mask, 0);
    }

    public ViewPopupMenu<K> addItem(K key, @StringRes int mask, @DrawableRes int icon) {
        return addItem(key, ToolsResources.getString(mask), icon);
    }

    public ViewPopupMenu<K> addItem(K key, String mask, @DrawableRes int icon) {
        keys.add(key);
        MenuItem item = popupMenu.getMenu().add(Menu.NONE, keys.size() - 1, 0, mask);
        if (icon != 0) {
            hasIcon = true;
            item.setIcon(ToolsResources.getDrawable(icon));
        }
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
