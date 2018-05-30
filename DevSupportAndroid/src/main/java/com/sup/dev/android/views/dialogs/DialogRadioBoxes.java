package com.sup.dev.android.views.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class DialogRadioBoxes extends BaseDialog {

    private final LinearLayout vOptionsContainer;
    private boolean multiSelection = true;

    public DialogRadioBoxes(Context viewContext) {
        super(viewContext, R.layout.dialog_container);
        vOptionsContainer = view.findViewById(R.id.content_container);
    }

    public DialogRadioBoxes setMultiSelection(boolean multiSelection) {
        this.multiSelection = multiSelection;

        if (!multiSelection) {
            boolean skip = false;
            for (int i = 0; i < vOptionsContainer.getChildCount(); i++) {
                CheckBox v = (CheckBox) vOptionsContainer.getChildAt(i);
                if (v.isChecked()) {
                    if (!skip) skip = true;
                    else v.setChecked(false);
                }
            }
        }

        return this;
    }

    @Override
    protected void onPreShow() {
        super.onPreShow();
        finishItemBuilding();
    }

    private void add(Item item) {
        item.checkBox = new CheckBox(viewContext);
        item.checkBox.setTag(item);
        item.checkBox.setText(item.text);
        item.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && !multiSelection)
                for (int i = 0; i < vOptionsContainer.getChildCount(); i++)
                    if (vOptionsContainer.getChildAt(i) != item.checkBox)
                        ((CheckBox) vOptionsContainer.getChildAt(i)).setChecked(false);
        });
        item.checkBox.setChecked(item.selected);
        vOptionsContainer.addView(item.checkBox);
        if (vOptionsContainer.getChildCount() > 1)
            ((ViewGroup.MarginLayoutParams) item.checkBox.getLayoutParams()).topMargin = ToolsView.dpToPx(16);

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

    public DialogRadioBoxes add(@StringRes int text) {
        return add(ToolsResources.getString(text));
    }

    public DialogRadioBoxes add(String text) {
        finishItemBuilding();
        buildItem = new Item();
        buildItem.text = text;
        return this;
    }

    public DialogRadioBoxes onChange(Callback2<DialogRadioBoxes, Boolean> onChange) {
        buildItem.onChange = onChange;
        return this;
    }

    public DialogRadioBoxes onSelected(Callback1<DialogRadioBoxes> onSelected) {
        buildItem.onSelected = onSelected;
        return this;
    }

    public DialogRadioBoxes onNotSelected(Callback1<DialogRadioBoxes> onNotSelected) {
        buildItem.onNotSelected = onNotSelected;
        return this;
    }

    public DialogRadioBoxes text(@StringRes int text) {
        return text(ToolsResources.getString(text));
    }

    public DialogRadioBoxes text(String text) {
        buildItem.text = text;
        return this;
    }

    public DialogRadioBoxes selected(boolean b) {
        buildItem.selected = b;
        return this;
    }

    public DialogRadioBoxes condition(boolean b) {
        skipThisItem = !b;
        return this;
    }

    public DialogRadioBoxes groupCondition(boolean b) {
        skipGroup = !b;
        return this;
    }

    public DialogRadioBoxes reverseGroupCondition() {
        skipGroup = !skipGroup;
        return this;
    }

    public DialogRadioBoxes clearGroupCondition() {
        skipGroup = false;
        return this;
    }

    //
    //  Setters
    //

    public DialogRadioBoxes setTitle(@StringRes int title) {
        return (DialogRadioBoxes) super.setTitle(title);
    }

    public DialogRadioBoxes setTitle(String title) {
        return (DialogRadioBoxes) super.setTitle(title);
    }

    public DialogRadioBoxes setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogRadioBoxes) super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogRadioBoxes setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogRadioBoxes) super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogRadioBoxes setCancelable(boolean cancelable) {
        return (DialogRadioBoxes) super.setCancelable(cancelable);
    }

    public DialogRadioBoxes setOnCancel(String s) {
        return (DialogRadioBoxes) super.setOnCancel(s);
    }

    public DialogRadioBoxes setOnCancel(Callback1<BaseDialog> onCancel) {
        return (DialogRadioBoxes) super.setOnCancel(onCancel);
    }

    public DialogRadioBoxes setOnCancel(@StringRes int s) {
        return (DialogRadioBoxes) super.setOnCancel(s);
    }

    public DialogRadioBoxes setOnCancel(@StringRes int s, Callback1<BaseDialog> onCancel) {
        return (DialogRadioBoxes) super.setOnCancel(s, onCancel);
    }

    public DialogRadioBoxes setOnCancel(String s, Callback1<BaseDialog> onCancel) {
        return (DialogRadioBoxes) super.setOnCancel(s, onCancel);
    }

    public DialogRadioBoxes setEnabled(boolean enabled) {
        for (int i = 0; i < vOptionsContainer.getChildCount(); i++)
            vOptionsContainer.getChildAt(i).setEnabled(enabled);
        return (DialogRadioBoxes) super.setEnabled(enabled);
    }

    public DialogRadioBoxes setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s));
    }

    public DialogRadioBoxes setOnEnter(String s) {
        return (DialogRadioBoxes) super.setOnEnter(s, baseDialog -> {
            if (isAutoHideOnEnter()) hide();
            else setEnabled(false);
            for (int i = 0; i < vOptionsContainer.getChildCount(); i++) {
                CheckBox v = (CheckBox) vOptionsContainer.getChildAt(i);
                Item item = (Item) v.getTag();
                if (item.onChange != null) item.onChange.callback(this, v.isChecked());
                if (v.isChecked() && item.onSelected != null) item.onSelected.callback(this);
                if (!v.isChecked() && item.onNotSelected != null) item.onNotSelected.callback(this);
            }
        });
    }

    //
    //  Item
    //

    private class Item {

        private CheckBox checkBox;
        private Callback2<DialogRadioBoxes, Boolean> onChange;
        private Callback1<DialogRadioBoxes> onSelected;
        private Callback1<DialogRadioBoxes> onNotSelected;
        private String text;
        private boolean selected;

    }


}