package com.sup.dev.android.views.popups;


import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.widgets.ViewIcon;
import com.sup.dev.java.classes.callbacks.simple.Callback2;

public class PopupMenu<K> extends Popup {

    private Callback2< K,PopupMenu> onSelected;
    private ViewGroup vContainer;
    private TextView vTitle;

    public PopupMenu(View anchor) {
        super(anchor, R.layout.popup_menu);
    }

    public PopupMenu(Context viewContext) {
        super(viewContext, R.layout.popup_menu);
    }

    @Override
    protected void init() {
        vContainer = findViewById(R.id.items_container);
        vTitle = findViewById(R.id.title);

        vTitle.setVisibility(View.GONE);
    }

    @Override
    protected void onPreShow() {
        super.onPreShow();
        finishItemBuilding();
    }

    private void onClick(Item item) {
        if (onSelected != null) onSelected.callback(item.key, this);
        hide();
    }

    public PopupMenu<K> setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public PopupMenu<K> setTitle(String title) {
        finishItemBuilding();
        vTitle.setText(title);
        vTitle.setVisibility(title == null ? View.GONE : View.VISIBLE);
        return this;
    }

    public PopupMenu<K> setOnSelected(Callback2< K, PopupMenu> onSelected) {
        finishItemBuilding();

        this.onSelected = onSelected;
        return this;
    }

    public PopupMenu<K> add(@StringRes int text) {
        return add(ToolsResources.getString(text));
    }

    public PopupMenu<K> add(String text) {
        return add((K) text, text);
    }

    public PopupMenu<K> add(K key, @StringRes int text) {
        return add(key, ToolsResources.getString(text));
    }

    public PopupMenu<K> add(K key, String text) {
        Item item = new Item(key);
        item.text = text;
        add(item);
        return this;
    }

    private PopupMenu<K> add(Item item) {

        finishItemBuilding();

        item.view = ToolsView.inflate(getContentView().getContext(), R.layout.popup_menu_row);
        View vTouch = item.view.findViewById(R.id.touch);
        ViewIcon vIcon = item.view.findViewById(R.id.icon);
        TextView vText = item.view.findViewById(R.id.text);

        if (item.icon == 0) vIcon.setVisibility(View.GONE);
        else vIcon.setImageResource(item.icon);

        if (item.textColor != 0) vText.setTextColor(item.textColor);
        if (item.bgColor != 0) vTouch.setBackgroundColor(item.bgColor);

        vText.setText(item.text);
        vTouch.setOnClickListener(view -> onClick(item));

        vContainer.addView(item.view);
        setContentView(getContentView());

        return this;
    }

    //
    //  Item
    //

    private Item buildItem;

    public PopupMenu<K> item(K key) {
        finishItemBuilding();
        buildItem = new Item(key);
        return this;
    }

    private void finishItemBuilding() {
        if (buildItem != null) {
            Item i = buildItem;
            buildItem = null;
            add(i);
        }
    }

    public PopupMenu<K> text(@StringRes int text) {
        return text(ToolsResources.getString(text));
    }

    public PopupMenu<K> text(String text) {
        buildItem.text = text;
        return this;
    }

    public PopupMenu<K> icon(int icon) {
        buildItem.icon = icon;
        return this;
    }

    public PopupMenu<K> col(int textColor) {
        buildItem.textColor = textColor;
        return this;
    }

    public PopupMenu<K> bg(int bgColor) {
        buildItem.bgColor = bgColor;
        return this;
    }

    public PopupMenu<K> bgR(int res) {
        buildItem.bgColor = ToolsResources.getColor(res);
        return this;
    }


    private class Item {

        private View view;
        private K key;
        private String text;
        private int icon;
        private int textColor;
        private int bgColor;

        private Item(K key) {
            this.key = key;
        }
    }

}