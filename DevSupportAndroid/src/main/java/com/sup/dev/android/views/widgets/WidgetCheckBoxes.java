package com.sup.dev.android.views.widgets;

import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.screens.SWidget;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;

import java.util.ArrayList;

public class WidgetCheckBoxes extends Widget {

    private final ArrayList<Item> items = new ArrayList<>();
    private final LinearLayout vOptionsContainer;
    private final Button vCancel;
    private final Button vEnter;

    private boolean autoHideOnEnter = true;

    public WidgetCheckBoxes() {
        super(R.layout.widget_container);

        vOptionsContainer = view.findViewById(R.id.content_container);
        vCancel = view.findViewById(R.id.cancel);
        vEnter = view.findViewById(R.id.enter);

        vCancel.setVisibility(View.GONE);
        vEnter.setVisibility(View.GONE);
    }

    @Override
    public void onShow() {
        super.onShow();
        finishItemBuilding();
    }

    private void add(Item item) {
        items.add(item);
    }

    @Override
    public WidgetCheckBoxes setTitle(int title) {
        return super.setTitle(title);
    }

    @Override
    public WidgetCheckBoxes setTitle(String title) {
        return super.setTitle(title);
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

    public WidgetCheckBoxes add(@StringRes int text) {
        return add(ToolsResources.getString(text));
    }

    public WidgetCheckBoxes add(String text) {
        finishItemBuilding();
        buildItem = new Item();
        buildItem.v.setText(text);
        return this;
    }

    public WidgetCheckBoxes onChange(Callback2<WidgetCheckBoxes, Boolean> onChange) {
        buildItem.onChange = onChange;
        return this;
    }

    public WidgetCheckBoxes onSelected(Callback1<WidgetCheckBoxes> onSelected) {
        buildItem.onSelected = onSelected;
        return this;
    }

    public WidgetCheckBoxes onNotSelected(Callback1<WidgetCheckBoxes> onNotSelected) {
        buildItem.onNotSelected = onNotSelected;
        return this;
    }

    public WidgetCheckBoxes text(@StringRes int text) {
        return text(ToolsResources.getString(text));
    }

    public WidgetCheckBoxes text(String text) {

        buildItem.v.setText(text);
        return this;
    }

    public WidgetCheckBoxes checked(boolean b) {
        buildItem.v.setChecked(b);
        return this;
    }

    public WidgetCheckBoxes condition(boolean b) {
        skipThisItem = !b;
        return this;
    }

    public WidgetCheckBoxes groupCondition(boolean b) {
        skipGroup = !b;
        return this;
    }

    public WidgetCheckBoxes reverseGroupCondition() {
        skipGroup = !skipGroup;
        return this;
    }

    public WidgetCheckBoxes clearGroupCondition() {
        skipGroup = false;
        return this;
    }

    //
    //  Setters
    //

    public WidgetCheckBoxes setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s));
    }

    public WidgetCheckBoxes setOnEnter(String s) {
        ToolsView.setTextOrGone(vEnter, s);
        vEnter.setOnClickListener(vi -> {
            if (autoHideOnEnter) hide();
            else setEnabled(false);
            for (int i = 0; i < vOptionsContainer.getChildCount(); i++) {
                CheckBox v = (CheckBox) vOptionsContainer.getChildAt(i);
                Item item = (Item) v.getTag();
                if (item.onChange != null) item.onChange.callback(this, v.isChecked());
                if (v.isChecked() && item.onSelected != null) item.onSelected.callback(this);
                if (!v.isChecked() && item.onNotSelected != null) item.onNotSelected.callback(this);
            }
        });
        return this;
    }

    public WidgetCheckBoxes setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public WidgetCheckBoxes setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s), null);
    }

    public WidgetCheckBoxes setOnCancel(String s) {
        return setOnCancel(s, null);
    }

    public WidgetCheckBoxes setOnCancel(Callback1<WidgetCheckBoxes> onCancel) {
        return setOnCancel(null, onCancel);
    }

    public WidgetCheckBoxes setOnCancel(@StringRes int s, Callback1<WidgetCheckBoxes> onCancel) {
        return setOnCancel(ToolsResources.getString(s), onCancel);
    }

    public WidgetCheckBoxes setOnCancel(String s, Callback1<WidgetCheckBoxes> onCancel) {
        super.setOnHide(b -> {
            if (onCancel != null) onCancel.callback(this);
        });
        ToolsView.setTextOrGone(vCancel, s);
        vCancel.setOnClickListener(v -> {
            hide();
            if (onCancel != null) onCancel.callback(this);
        });

        return this;
    }

    public WidgetCheckBoxes setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        return this;
    }

    //
    //  Item
    //

    private class Item {

        private final CheckBox v;

        private Callback2<WidgetCheckBoxes, Boolean> onChange;
        private Callback1<WidgetCheckBoxes> onSelected;
        private Callback1<WidgetCheckBoxes> onNotSelected;

        public Item() {
            v = new CheckBox(SupAndroid.activity);
            v.setTag(this);
            vOptionsContainer.addView(v);
            if (vOptionsContainer.getChildCount() > 1)
                ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).topMargin = ToolsView.dpToPx(16);

        }

    }


}
