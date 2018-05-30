package com.sup.dev.android.views.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;

import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.android.views.cards.CardDivider;
import com.sup.dev.android.views.cards.CardMenu;
import com.sup.dev.android.views.popups.PopupMenu;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;

public class DialogMenu extends DialogRecycler {

    private final RecyclerCardAdapter adapter;

    private int prefCount = 0;

    public DialogMenu(Context viewContext) {
        super(viewContext);
        adapter = new RecyclerCardAdapter();
        setAdapter(adapter);
    }

    @Override
    protected void onPreShow() {
        super.onPreShow();
        finishItemBuilding();
    }

    private void add(Item item) {

        item.card = new CardMenu();
        item.card.setText(item.text);
        item.card.setOnClick(() -> {
            if (isAutoHideOnEnter()) hide();
            else setEnabled(false);
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

    public DialogMenu add(@StringRes int text) {
        return add(ToolsResources.getString(text), null);
    }

    public DialogMenu add(String text) {
        return add(text, null);
    }

    public DialogMenu add(@StringRes int text, Callback1<DialogMenu> onClick) {
        return add(ToolsResources.getString(text), onClick);
    }

    public DialogMenu add(String text, Callback1<DialogMenu> onClick) {
        finishItemBuilding();
        buildItem = new Item();
        buildItem.text = text;
        buildItem.onClick = onClick;
        return this;
    }

    public DialogMenu text(@StringRes int text) {
        return text(ToolsResources.getString(text));
    }

    public DialogMenu text(String text) {
        buildItem.text = text;
        return this;
    }

    public DialogMenu prefered(boolean b) {
        buildItem.preferred = b;
        return this;
    }

    public DialogMenu condition(boolean b) {
        skipThisItem = !b;
        return this;
    }

    public DialogMenu groupCondition(boolean b) {
        skipGroup = !b;
        return this;
    }

    public DialogMenu reverseGroupCondition() {
        skipGroup = !skipGroup;
        return this;
    }

    public DialogMenu clearGroupCondition() {
        skipGroup = false;
        return this;
    }

    //
    //  Setters
    //

    public DialogMenu setTitle(@StringRes int title) {
        return (DialogMenu) super.setTitle(title);
    }

    public DialogMenu setTitle(String title) {
        return (DialogMenu) super.setTitle(title);
    }

    public DialogMenu setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogMenu) super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogMenu setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogMenu) super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogMenu setCancelable(boolean cancelable) {
        return (DialogMenu) super.setCancelable(cancelable);
    }

    public DialogMenu setOnCancel(String s) {
        return (DialogMenu) super.setOnCancel(s);
    }

    public DialogMenu setOnCancel(Callback1<BaseDialog> onCancel) {
        return (DialogMenu) super.setOnCancel(onCancel);
    }

    public DialogMenu setOnCancel(@StringRes int s) {
        return (DialogMenu) super.setOnCancel(s);
    }

    public DialogMenu setOnCancel(@StringRes int s, Callback1<BaseDialog> onCancel) {
        return (DialogMenu) super.setOnCancel(s, onCancel);
    }

    public DialogMenu setOnCancel(String s, Callback1<BaseDialog> onCancel) {
        return (DialogMenu) super.setOnCancel(s, onCancel);
    }

    public DialogMenu setEnabled(boolean enabled) {
        for (CardMenu card : adapter.getByClass(CardMenu.class))
            card.setEnabled(enabled);
        return (DialogMenu) super.setEnabled(enabled);
    }

    public DialogMenu setOnEnter(@StringRes int s) {
        return (DialogMenu) super.setOnEnter(s);
    }

    public DialogMenu setOnEnter(String s) {
        return (DialogMenu) super.setOnEnter(s);
    }

    public DialogMenu setOnEnter(@StringRes int s, Callback1<BaseDialog> onEnter) {
        return (DialogMenu) super.setOnEnter(s, onEnter);
    }

    public DialogMenu setOnEnter(String s, Callback1<BaseDialog> onEnter) {
        return (DialogMenu) super.setOnEnter(s, onEnter);
    }

    //
    //  Item
    //

    private class Item {

        private CardMenu card;
        private Callback1<DialogMenu> onClick;
        private String text;
        private boolean preferred;

    }


}
