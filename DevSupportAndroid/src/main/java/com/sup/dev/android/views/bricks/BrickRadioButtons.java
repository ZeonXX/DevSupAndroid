package com.sup.dev.android.views.bricks;

import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;

import java.util.ArrayList;

public class BrickRadioButtons extends Brick{

    private final ArrayList<Item> items = new ArrayList<>();

    private boolean multiSelection = true;
    private String enterText;
    private String cancelText;
    private String title;
    private Callback1<BrickRadioButtons> onCancel;
    private boolean autoHideOnEnter = true;

    @Override
    public int getLayoutRes(Mode mode) {
        return R.layout.brick_container;
    }

    @Override
    public void bindView(View view, Mode mode) {
        finishItemBuilding();

        LinearLayout vOptionsContainer = view.findViewById(R.id.content_container);
        TextView vTitle = view.findViewById(R.id.title);
        Button vCancel = view.findViewById(R.id.cancel);
        Button vEnter = view.findViewById(R.id.enter);

        ToolsView.setTextOrGone(vTitle, title);
        ToolsView.setTextOrGone(vCancel, cancelText);
        ToolsView.setTextOrGone(vEnter, enterText);

        for (Item item : items){

            RadioButton radioButton = new RadioButton(view.getContext());
            radioButton.setTag(item);
            radioButton.setText(item.text);
            radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked && !multiSelection)
                    for (int i = 0; i < vOptionsContainer.getChildCount(); i++)
                        if (vOptionsContainer.getChildAt(i) != radioButton)
                            ((RadioButton) vOptionsContainer.getChildAt(i)).setChecked(false);
            });
            radioButton.setChecked(item.selected);
            vOptionsContainer.addView(radioButton);
            if (vOptionsContainer.getChildCount() > 1)
                ((ViewGroup.MarginLayoutParams) radioButton.getLayoutParams()).topMargin = ToolsView.dpToPx(16);

        }

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


        vCancel.setOnClickListener(v -> {
            hide();
            if(onCancel != null) onCancel.callback(this);
        });

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


    }

    public BrickRadioButtons setMultiSelection(boolean multiSelection) {
        this.multiSelection = multiSelection;
        update();
        return this;
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

    public BrickRadioButtons add(@StringRes int text) {
        return add(ToolsResources.getString(text));
    }

    public BrickRadioButtons add(String text) {
        finishItemBuilding();
        buildItem = new Item();
        buildItem.text = text;
        return this;
    }

    public BrickRadioButtons onChange(Callback2<BrickRadioButtons, Boolean> onChange) {
        buildItem.onChange = onChange;
        return this;
    }

    public BrickRadioButtons onSelected(Callback1<BrickRadioButtons> onSelected) {
        buildItem.onSelected = onSelected;
        return this;
    }

    public BrickRadioButtons onNotSelected(Callback1<BrickRadioButtons> onNotSelected) {
        buildItem.onNotSelected = onNotSelected;
        return this;
    }

    public BrickRadioButtons text(@StringRes int text) {
        return text(ToolsResources.getString(text));
    }

    public BrickRadioButtons text(String text) {
        buildItem.text = text;
        return this;
    }

    public BrickRadioButtons selected(boolean b) {
        buildItem.selected = b;
        return this;
    }

    public BrickRadioButtons condition(boolean b) {
        skipThisItem = !b;
        return this;
    }

    public BrickRadioButtons groupCondition(boolean b) {
        skipGroup = !b;
        return this;
    }

    public BrickRadioButtons reverseGroupCondition() {
        skipGroup = !skipGroup;
        return this;
    }

    public BrickRadioButtons clearGroupCondition() {
        skipGroup = false;
        return this;
    }

    //
    //  Setters
    //

    public BrickRadioButtons setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public BrickRadioButtons setTitle(String title) {
        this.title = title;
        update();
        return this;
    }


    public BrickRadioButtons setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s));
    }

    public BrickRadioButtons setOnEnter(String s) {
        this.enterText = s;
        return this;
    }

    public BrickRadioButtons setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public BrickRadioButtons setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s), null);
    }

    public BrickRadioButtons setOnCancel(String s) {
        return setOnCancel(s, null);
    }

    public BrickRadioButtons setOnCancel(Callback1<BrickRadioButtons> onCancel) {
        return setOnCancel(null, onCancel);
    }

    public BrickRadioButtons setOnCancel(@StringRes int s, Callback1<BrickRadioButtons> onCancel) {
        return setOnCancel(ToolsResources.getString(s), onCancel);
    }

    public BrickRadioButtons setOnCancel(String s, Callback1<BrickRadioButtons> onCancel) {
        super.setOnHide(b -> {
            if (onCancel != null) onCancel.callback(this);
        });
        this.onCancel = onCancel;
        this.cancelText = s;
        return this;
    }

    public BrickRadioButtons setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        return this;
    }

    //
    //  Item
    //

    private class Item {

        private Callback2<BrickRadioButtons, Boolean> onChange;
        private Callback1<BrickRadioButtons> onSelected;
        private Callback1<BrickRadioButtons> onNotSelected;
        private String text;
        private boolean selected;

    }


}
