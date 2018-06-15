package com.sup.dev.android.views.bricks;

import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.android.views.cards.CardDivider;
import com.sup.dev.android.views.cards.CardDividerTitle;
import com.sup.dev.android.views.cards.CardMenu;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class BrickMenu extends BrickRecycler{

    private final RecyclerCardAdapter adapter;

    private int prefCount = 0;

    public BrickMenu() {
        adapter = new RecyclerCardAdapter();
        setAdapter(adapter);
    }

    @Override
    public void bindView(View view) {
        super.bindView(view);
        finishItemBuilding();

        RecyclerView vRecycler = view.findViewById(R.id.recycler);

        vRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    private void add(Item item) {

        item.card = new CardMenu();
        item.card.setText(item.text);
        item.card.setIcon(item.icon);
        item.card.setBackground(item.bg);
        item.card.setOnClick(() -> {
            hide();
            if (item.onClick != null) item.onClick.callback(this);
        });


        if (item.preferred) {
            if (prefCount == 0) adapter.add(0, new CardDivider());
            adapter.add(prefCount, item.card);
            prefCount++;
        } else {
            adapter.add(item.card);
        }

    }

    public BrickMenu group(@StringRes int title) {
        return group(ToolsResources.getString(title));
    }

    public BrickMenu group(String title) {
        finishItemBuilding();
        adapter.add(new CardDividerTitle().setText(title));
        return this;
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
        }
    }

    public BrickMenu add(@StringRes int text) {
        return add(ToolsResources.getString(text), null);
    }

    public BrickMenu add(String text) {
        return add(text, null);
    }

    public BrickMenu add(@StringRes int text, Callback1<BrickMenu> onClick) {
        return add(ToolsResources.getString(text), onClick);
    }

    public BrickMenu add(String text, Callback1<BrickMenu> onClick) {
        finishItemBuilding();
        buildItem = new Item();
        buildItem.text = text;
        buildItem.onClick = onClick;
        return this;
    }

    public BrickMenu text(@StringRes int text) {
        return text(ToolsResources.getString(text));
    }

    public BrickMenu text(String text) {
        buildItem.text = text;
        return this;
    }

    public BrickMenu icon(int icon) {
        buildItem.icon = icon;
        return this;
    }

    public BrickMenu backgroundRes(@ColorRes int color) {
        return background(ToolsResources.getColor(color));
    }

    public BrickMenu background(@ColorInt int color) {
        buildItem.bg = color;
        return this;
    }

    public BrickMenu preferred(boolean b) {
        buildItem.preferred = b;
        return this;
    }

    public BrickMenu onClick(Callback1<BrickMenu> onClick) {
        buildItem.onClick = onClick;
        return this;
    }

    public BrickMenu condition(boolean b) {
        skipThisItem = !b;
        return this;
    }

    public BrickMenu groupCondition(boolean b) {
        skipGroup = !b;
        return this;
    }

    public BrickMenu reverseGroupCondition() {
        skipGroup = !skipGroup;
        return this;
    }

    public BrickMenu clearGroupCondition() {
        skipGroup = false;
        return this;
    }

    //
    //  Item
    //

    private class Item {

        private CardMenu card;
        private Callback1<BrickMenu> onClick;
        private String text;
        private int icon;
        private int bg;
        private boolean preferred;

    }



}
