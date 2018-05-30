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
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.Callback2;

public class PopupMenu extends Popup {

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

    public PopupMenu setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public PopupMenu setTitle(String title) {
        finishItemBuilding();
        vTitle.setText(title);
        vTitle.setVisibility(title == null ? View.GONE : View.VISIBLE);
        return this;
    }

    private PopupMenu add(Item item) {

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
        vTouch.setOnClickListener(view -> {
            if (item.onClick != null) item.onClick.callback();
            hide();
        });

        vContainer.addView(item.view);
        setContentView(getContentView());

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

    public PopupMenu add(@StringRes int text) {
        return add(ToolsResources.getString(text), null);
    }

    public PopupMenu add(String text) {
        return add(text, null);
    }

    public PopupMenu add(@StringRes int text, Callback onClick) {
        return add(ToolsResources.getString(text), onClick);
    }

    public PopupMenu add(String text, Callback onClick) {
        finishItemBuilding();
        buildItem = new Item();
        buildItem.text = text;
        buildItem.onClick = onClick;
        return this;
    }

    public PopupMenu text(@StringRes int text) {
        return text(ToolsResources.getString(text));
    }

    public PopupMenu text(String text) {
        buildItem.text = text;
        return this;
    }

    public PopupMenu icon(int icon) {
        buildItem.icon = icon;
        return this;
    }

    public PopupMenu col(int textColor) {
        buildItem.textColor = textColor;
        return this;
    }

    public PopupMenu bg(int bgColor) {
        buildItem.bgColor = bgColor;
        return this;
    }

    public PopupMenu bgR(int res) {
        buildItem.bgColor = ToolsResources.getColor(res);
        return this;
    }

    public PopupMenu condition(boolean b) {
        skipThisItem = !b;
        return this;
    }

    public PopupMenu groupCondition(boolean b) {
        skipGroup = !b;
        return this;
    }

    public PopupMenu reverseGroupCondition() {
        skipGroup = !skipGroup;
        return this;
    }

    public PopupMenu clearGroupCondition() {
        skipGroup = false;
        return this;
    }

    //
    //  Class
    //

    private class Item {

        private View view;
        private Callback onClick;
        private String text;
        private int icon;
        private int textColor;
        private int bgColor;

    }

}