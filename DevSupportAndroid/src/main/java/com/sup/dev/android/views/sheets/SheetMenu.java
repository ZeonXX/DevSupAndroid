package com.sup.dev.android.views.sheets;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;

import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.android.views.cards.CardDivider;
import com.sup.dev.android.views.cards.CardDividerTitle;
import com.sup.dev.android.views.cards.CardMenu;
import com.sup.dev.android.views.popups.PopupMenu;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class SheetMenu extends SheetRecycler {

    private final RecyclerCardAdapter adapter;

    private int prefCount = 0;

    public SheetMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        vRecycler.setLayoutManager(new LinearLayoutManager(viewContext));
        adapter = new RecyclerCardAdapter();
        setAdapter(adapter);
    }


    @Override
    protected void onStateChanged(int newState) {
        super.onStateChanged(newState);
        finishItemBuilding();
    }

    private void add(Item item) {

        item.card = new CardMenu();
        item.card.setText(item.text);
        item.card.setIcon(item.icon);
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

    public SheetMenu group(@StringRes int title) {
        return group(ToolsResources.getString(title));
    }

    public SheetMenu group(String title) {
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

    public SheetMenu add(@StringRes int text) {
        return add(ToolsResources.getString(text), null);
    }

    public SheetMenu add(String text) {
        return add(text, null);
    }

    public SheetMenu add(@StringRes int text, Callback1<SheetMenu> onClick) {
        return add(ToolsResources.getString(text), onClick);
    }

    public SheetMenu add(String text, Callback1<SheetMenu> onClick) {
        finishItemBuilding();
        buildItem = new Item();
        buildItem.text = text;
        buildItem.onClick = onClick;
        return this;
    }

    public SheetMenu text(@StringRes int text) {
        return text(ToolsResources.getString(text));
    }

    public SheetMenu text(String text) {
        buildItem.text = text;
        return this;
    }

    public SheetMenu icon(int icon) {
        buildItem.icon = icon;
        return this;
    }

    public SheetMenu prefered(boolean b) {
        buildItem.preferred = b;
        return this;
    }

    public SheetMenu condition(boolean b) {
        skipThisItem = !b;
        return this;
    }

    public SheetMenu groupCondition(boolean b) {
        skipGroup = !b;
        return this;
    }

    public SheetMenu reverseGroupCondition() {
        skipGroup = !skipGroup;
        return this;
    }

    public SheetMenu clearGroupCondition() {
        skipGroup = false;
        return this;
    }

    //
    //  Item
    //

    private class Item {

        private CardMenu card;
        private Callback1<SheetMenu> onClick;
        private String text;
        private int icon;
        private boolean preferred;

    }


}
