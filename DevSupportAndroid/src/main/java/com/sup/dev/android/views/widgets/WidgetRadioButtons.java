package com.sup.dev.android.views.widgets;

import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;

import java.util.ArrayList;

public class WidgetRadioButtons  extends Widget {

    private final ArrayList<Item> items = new ArrayList<>();
    private final LinearLayout vOptionsContainer;
    private final Button vCancel;
    private final Button vEnter;

    private boolean autoHideOnEnter = true;

    public WidgetRadioButtons(){
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

    public WidgetRadioButtons add(@StringRes int text) {
        return add(ToolsResources.getString(text));
    }

    public WidgetRadioButtons add(String text) {
        finishItemBuilding();
        buildItem = new Item();
        buildItem.v.setText(text);
        return this;
    }

    public WidgetRadioButtons onChange(Callback2<WidgetRadioButtons, Boolean> onChange) {
        buildItem.onChange = onChange;
        return this;
    }

    public WidgetRadioButtons onSelected(Callback1<WidgetRadioButtons> onSelected) {
        buildItem.onSelected = onSelected;
        return this;
    }

    public WidgetRadioButtons onNotSelected(Callback1<WidgetRadioButtons> onNotSelected) {
        buildItem.onNotSelected = onNotSelected;
        return this;
    }

    public WidgetRadioButtons text(@StringRes int text) {
        return text(ToolsResources.getString(text));
    }

    public WidgetRadioButtons text(String text) {

        buildItem.v.setText(text);
        return this;
    }

    public WidgetRadioButtons checked(boolean b) {
        buildItem.v.setChecked(b);
        return this;
    }

    public WidgetRadioButtons condition(boolean b) {
        skipThisItem = !b;
        return this;
    }

    public WidgetRadioButtons groupCondition(boolean b) {
        skipGroup = !b;
        return this;
    }

    public WidgetRadioButtons reverseGroupCondition() {
        skipGroup = !skipGroup;
        return this;
    }

    public WidgetRadioButtons clearGroupCondition() {
        skipGroup = false;
        return this;
    }

    //
    //  Setters
    //

    public WidgetRadioButtons setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public WidgetRadioButtons setTitle(String title) {
        ToolsView.setTextOrGone(vTitle, title);
        return this;
    }

    public WidgetRadioButtons setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s));
    }

    public WidgetRadioButtons setOnEnter(String s) {
        ToolsView.setTextOrGone(vEnter, s);
        vEnter.setOnClickListener(vi -> {
            if (autoHideOnEnter) hide();
            else setEnabled(false);
            for (int i = 0; i < vOptionsContainer.getChildCount(); i++) {
                RadioButton v = (RadioButton) vOptionsContainer.getChildAt(i);
                Item item = (Item) v.getTag();
                if (item.onChange != null) item.onChange.callback(this, v.isChecked());
                if (v.isChecked() && item.onSelected != null) item.onSelected.callback(this);
                if (!v.isChecked() && item.onNotSelected != null) item.onNotSelected.callback(this);
            }
        });
        return this;
    }

    public WidgetRadioButtons setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public WidgetRadioButtons setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s), null);
    }

    public WidgetRadioButtons setOnCancel(String s) {
        return setOnCancel(s, null);
    }

    public WidgetRadioButtons setOnCancel(Callback1<WidgetRadioButtons> onCancel) {
        return setOnCancel(null, onCancel);
    }

    public WidgetRadioButtons setOnCancel(@StringRes int s, Callback1<WidgetRadioButtons> onCancel) {
        return setOnCancel(ToolsResources.getString(s), onCancel);
    }

    public WidgetRadioButtons setOnCancel(String s, Callback1<WidgetRadioButtons> onCancel) {
        super.setOnHide(b -> {
            if (onCancel != null) onCancel.callback(this);
        });
        ToolsView.setTextOrGone(vCancel, s);
        vCancel.setOnClickListener(v -> {
            hide();
            if(onCancel != null) onCancel.callback(this);
        });

        return this;
    }

    public WidgetRadioButtons setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        return this;
    }

    //
    //  Item
    //

    private class Item {

        private final RadioButton v;

        private Callback2<WidgetRadioButtons, Boolean> onChange;
        private Callback1<WidgetRadioButtons> onSelected;
        private Callback1<WidgetRadioButtons> onNotSelected;

        public Item(){
            v = new RadioButton(SupAndroid.activity);
            v.setTag(this);
            v.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked)
                    for (int i = 0; i < vOptionsContainer.getChildCount(); i++)
                        if (vOptionsContainer.getChildAt(i) != v)
                            ((RadioButton) vOptionsContainer.getChildAt(i)).setChecked(false);
            });
            vOptionsContainer.addView(v);
            if (vOptionsContainer.getChildCount() > 1)
                ((ViewGroup.MarginLayoutParams) v.getLayoutParams()).topMargin = ToolsView.dpToPx(16);

        }

    }


}
