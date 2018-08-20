package com.sup.dev.android.views.widgets;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;

import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.android.views.cards.CardDivider;
import com.sup.dev.android.views.cards.CardDividerTitle;
import com.sup.dev.android.views.cards.CardMenu;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.providers.Provider;

public class WidgetMenu extends WidgetRecycler {

    private final RecyclerCardAdapter adapter;
    private Callback2<WidgetMenu, String> onGlobalSelected;

    private int prefCount = 0;

    public WidgetMenu() {
        vRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
        adapter = new RecyclerCardAdapter();
        setAdapter(adapter);
    }

    @Override
    public void onShow() {
        super.onShow();
        finishItemBuilding();
    }

    public void clear(){
        finishItemBuilding();
        adapter.clear();
        prefCount = 0;
    }

    private void add(Item item) {

        item.card = new CardMenu();
        item.card.setText(item.text);
        item.card.setIcon(item.icon);
        item.card.setBackground(item.bg);
        item.card.setOnClick((v, x, y) -> {
            hide();
            if (item.onClick != null) item.onClick.callback(this);
            if (onGlobalSelected != null) onGlobalSelected.callback(this, item.text);
        });


        if (item.preferred) {
            if (prefCount == 0) adapter.add(0, new CardDivider());
            adapter.add(prefCount, item.card);
            prefCount++;
        } else {
            adapter.add(item.card);
        }

    }

    public WidgetMenu group(@StringRes int title) {
        return group(ToolsResources.getString(title));
    }

    public WidgetMenu group(@StringRes int title, boolean divider) {
        return group(ToolsResources.getString(title), divider);
    }

    public WidgetMenu group(String title) {
        return group(title, true);
    }

    public WidgetMenu group(String title, boolean divider) {
        finishItemBuilding();
        adapter.add(new CardDividerTitle().setText(title).setDivider(divider));
        return this;
    }

    public void setOnGlobalSelected(Callback2<WidgetMenu, String> onGlobalSelected) {
        this.onGlobalSelected = onGlobalSelected;
    }

    @Override
    public WidgetMenu setTitle(@StringRes int title) {
        return super.setTitle(title);
    }

    @Override
    public WidgetMenu setTitle(String title) {
        return super.setTitle(title);
    }

    @Override
    public WidgetMenu setTitleBackgroundColor(int color) {
        return super.setTitleBackgroundColor(color);
    }

    @Override
    public WidgetMenu setTitleBackgroundColorRes(int color) {
        return super.setTitleBackgroundColorRes(color);
    }

    //
    //  Item
    //

    private Item buildItem;
    private boolean skipThisItem = false;
    private boolean skipGroup = false;

    private void finishItemBuilding() {
        if (buildItem != null) {
            Item i = buildItem;
            buildItem = null;
            if (!skipThisItem && !skipGroup) add(i);
            skipThisItem = false;
        }
    }

    public WidgetMenu add(@StringRes int text) {
        return add(ToolsResources.getString(text), null);
    }

    public WidgetMenu add(String text) {
        return add(text, null);
    }

    public WidgetMenu add(@StringRes int text, Callback1<WidgetMenu> onClick) {
        return add(ToolsResources.getString(text), onClick);
    }

    public WidgetMenu add(String text, Callback1<WidgetMenu> onClick) {
        finishItemBuilding();
        buildItem = new Item();
        buildItem.text = text;
        buildItem.onClick = onClick;
        return this;
    }

    public WidgetMenu text(@StringRes int text) {
        return text(ToolsResources.getString(text));
    }

    public WidgetMenu text(String text) {
        buildItem.text = text;
        return this;
    }

    public WidgetMenu icon(int icon) {
        buildItem.icon = icon;
        return this;
    }

    public WidgetMenu backgroundRes(@ColorRes int color) {
        return background(ToolsResources.getColor(color));
    }

    public WidgetMenu backgroundRes(@ColorRes int color, Provider<Boolean> condition) {
        if (condition.provide()) return background(ToolsResources.getColor(color));
        else return this;
    }

    public WidgetMenu background(@ColorInt int color) {
        buildItem.bg = color;
        return this;
    }

    public WidgetMenu preferred(boolean b) {
        buildItem.preferred = b;
        return this;
    }

    public WidgetMenu onClick(Callback1<WidgetMenu> onClick) {
        buildItem.onClick = onClick;
        return this;
    }

    public WidgetMenu condition(boolean b) {
        skipThisItem = !b;
        return this;
    }

    public WidgetMenu groupCondition(boolean b) {
        finishItemBuilding();
        skipGroup = !b;
        return this;
    }

    public WidgetMenu reverseGroupCondition() {
        finishItemBuilding();
        skipGroup = !skipGroup;
        return this;
    }

    public WidgetMenu clearGroupCondition() {
        finishItemBuilding();
        skipGroup = false;
        return this;
    }

    //
    //  Item
    //

    private class Item {

        private CardMenu card;
        private Callback1<WidgetMenu> onClick;
        private String text;
        private int icon;
        private int bg;
        private boolean preferred;

    }


}
