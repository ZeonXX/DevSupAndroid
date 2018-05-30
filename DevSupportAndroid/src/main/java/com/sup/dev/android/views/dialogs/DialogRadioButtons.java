package com.sup.dev.android.views.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class DialogRadioButtons extends BaseDialog {

    private final LinearLayout vOptionsContainer;
    private boolean multiSelection = true;

    public DialogRadioButtons(Context viewContext) {
        super(viewContext, R.layout.dialog_container);
        vOptionsContainer = view.findViewById(R.id.content_container);
    }

    public DialogRadioButtons setMultiSelection(boolean multiSelection) {
        this.multiSelection = multiSelection;

        if (!multiSelection) {
            boolean skip = false;
            for (int i = 0; i < vOptionsContainer.getChildCount(); i++) {
                RadioButton v = (RadioButton) vOptionsContainer.getChildAt(i);
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
        item.radioButton = new RadioButton(viewContext);
        item.radioButton.setTag(item);
        item.radioButton.setText(item.text);
        item.radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && !multiSelection)
                for (int i = 0; i < vOptionsContainer.getChildCount(); i++)
                    if (vOptionsContainer.getChildAt(i) != item.radioButton)
                        ((RadioButton) vOptionsContainer.getChildAt(i)).setChecked(false);
        });
        item.radioButton.setChecked(item.selected);
        vOptionsContainer.addView(item.radioButton);
        if (vOptionsContainer.getChildCount() > 1)
            ((ViewGroup.MarginLayoutParams) item.radioButton.getLayoutParams()).topMargin = ToolsView.dpToPx(16);

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

    public DialogRadioButtons add(@StringRes int text) {
        return add(ToolsResources.getString(text));
    }

    public DialogRadioButtons add(String text) {
        finishItemBuilding();
        buildItem = new Item();
        buildItem.text = text;
        return this;
    }

    public DialogRadioButtons onChange(Callback2<DialogRadioButtons, Boolean> onChange) {
        buildItem.onChange = onChange;
        return this;
    }

    public DialogRadioButtons onSelected(Callback1<DialogRadioButtons> onSelected) {
        buildItem.onSelected = onSelected;
        return this;
    }

    public DialogRadioButtons onNotSelected(Callback1<DialogRadioButtons> onNotSelected) {
        buildItem.onNotSelected = onNotSelected;
        return this;
    }

    public DialogRadioButtons text(@StringRes int text) {
        return text(ToolsResources.getString(text));
    }

    public DialogRadioButtons text(String text) {
        buildItem.text = text;
        return this;
    }

    public DialogRadioButtons selected(boolean b) {
        buildItem.selected = b;
        return this;
    }

    public DialogRadioButtons condition(boolean b) {
        skipThisItem = !b;
        return this;
    }

    public DialogRadioButtons groupCondition(boolean b) {
        skipGroup = !b;
        return this;
    }

    public DialogRadioButtons reverseGroupCondition() {
        skipGroup = !skipGroup;
        return this;
    }

    public DialogRadioButtons clearGroupCondition() {
        skipGroup = false;
        return this;
    }

    //
    //  Setters
    //

    public DialogRadioButtons setTitle(@StringRes int title) {
        return (DialogRadioButtons) super.setTitle(title);
    }

    public DialogRadioButtons setTitle(String title) {
        return (DialogRadioButtons) super.setTitle(title);
    }

    public DialogRadioButtons setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogRadioButtons) super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogRadioButtons setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogRadioButtons) super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogRadioButtons setCancelable(boolean cancelable) {
        return (DialogRadioButtons) super.setCancelable(cancelable);
    }

    public DialogRadioButtons setOnCancel(String s) {
        return (DialogRadioButtons) super.setOnCancel(s);
    }

    public DialogRadioButtons setOnCancel(Callback1<BaseDialog> onCancel) {
        return (DialogRadioButtons) super.setOnCancel(onCancel);
    }

    public DialogRadioButtons setOnCancel(@StringRes int s) {
        return (DialogRadioButtons) super.setOnCancel(s);
    }

    public DialogRadioButtons setOnCancel(@StringRes int s, Callback1<BaseDialog> onCancel) {
        return (DialogRadioButtons) super.setOnCancel(s, onCancel);
    }

    public DialogRadioButtons setOnCancel(String s, Callback1<BaseDialog> onCancel) {
        return (DialogRadioButtons) super.setOnCancel(s, onCancel);
    }

    public DialogRadioButtons setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s));
    }

    public DialogRadioButtons setOnEnter(String s) {
        return (DialogRadioButtons) super.setOnEnter(s, baseDialog -> {
            if (isAutoHideOnEnter()) hide();
            else setEnabled(false);
            for (int i = 0; i < vOptionsContainer.getChildCount(); i++) {
                RadioButton v = (RadioButton) vOptionsContainer.getChildAt(i);
                Item item = (Item) v.getTag();
                if (item.onChange != null) item.onChange.callback(this, v.isChecked());
                if (v.isChecked() && item.onSelected != null) item.onSelected.callback(this);
                if (!v.isChecked() && item.onNotSelected != null) item.onNotSelected.callback(this);
            }
        });
    }

    public DialogRadioButtons setEnabled(boolean enabled) {
        for (int i = 0; i < vOptionsContainer.getChildCount(); i++)
            vOptionsContainer.getChildAt(i).setEnabled(enabled);
        return (DialogRadioButtons) super.setEnabled(enabled);
    }

    //
    //  Item
    //

    private class Item {

        private RadioButton radioButton;
        private Callback2<DialogRadioButtons, Boolean> onChange;
        private Callback1<DialogRadioButtons> onSelected;
        private Callback1<DialogRadioButtons> onNotSelected;
        private String text;
        private boolean selected;

    }


}
