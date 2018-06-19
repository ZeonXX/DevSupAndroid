package com.sup.dev.android.views.bricks;

import android.support.annotation.StringRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;

import java.util.ArrayList;

public class BrickRadioBoxes extends Brick{
    
    private final ArrayList<Item> items = new ArrayList<>();
    
    private boolean multiSelection = true;
    private String enterText;
    private String cancelText;
    private String title;
    private Callback1<BrickRadioBoxes> onCancel;
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

            CheckBox checkBox = new CheckBox(view.getContext());
            checkBox.setTag(item);
            checkBox.setText(item.text);
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                if (isChecked && !multiSelection)
                    for (int i = 0; i < vOptionsContainer.getChildCount(); i++)
                        if (vOptionsContainer.getChildAt(i) != checkBox)
                            ((CheckBox) vOptionsContainer.getChildAt(i)).setChecked(false);
            });
            checkBox.setChecked(item.selected);
            vOptionsContainer.addView(checkBox);
            if (vOptionsContainer.getChildCount() > 1)
                ((ViewGroup.MarginLayoutParams) checkBox.getLayoutParams()).topMargin = ToolsView.dpToPx(16);
            
        }
        
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


        vCancel.setOnClickListener(v -> {
            hide();
            if(onCancel != null) onCancel.callback(this);
        });

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


    }

    public BrickRadioBoxes setMultiSelection(boolean multiSelection) {
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

    public BrickRadioBoxes add(@StringRes int text) {
        return add(ToolsResources.getString(text));
    }

    public BrickRadioBoxes add(String text) {
        finishItemBuilding();
        buildItem = new Item();
        buildItem.text = text;
        return this;
    }

    public BrickRadioBoxes onChange(Callback2<BrickRadioBoxes, Boolean> onChange) {
        buildItem.onChange = onChange;
        return this;
    }

    public BrickRadioBoxes onSelected(Callback1<BrickRadioBoxes> onSelected) {
        buildItem.onSelected = onSelected;
        return this;
    }

    public BrickRadioBoxes onNotSelected(Callback1<BrickRadioBoxes> onNotSelected) {
        buildItem.onNotSelected = onNotSelected;
        return this;
    }

    public BrickRadioBoxes text(@StringRes int text) {
        return text(ToolsResources.getString(text));
    }

    public BrickRadioBoxes text(String text) {
        buildItem.text = text;
        return this;
    }

    public BrickRadioBoxes selected(boolean b) {
        buildItem.selected = b;
        return this;
    }

    public BrickRadioBoxes condition(boolean b) {
        skipThisItem = !b;
        return this;
    }

    public BrickRadioBoxes groupCondition(boolean b) {
        skipGroup = !b;
        return this;
    }

    public BrickRadioBoxes reverseGroupCondition() {
        skipGroup = !skipGroup;
        return this;
    }

    public BrickRadioBoxes clearGroupCondition() {
        skipGroup = false;
        return this;
    }

    //
    //  Setters
    //

    public BrickRadioBoxes setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public BrickRadioBoxes setTitle(String title) {
        this.title = title;
        update();
        return this;
    }


    public BrickRadioBoxes setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s));
    }

    public BrickRadioBoxes setOnEnter(String s) {
        this.enterText = s;
        return this;
    }

    public BrickRadioBoxes setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public BrickRadioBoxes setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s), null);
    }

    public BrickRadioBoxes setOnCancel(String s) {
        return setOnCancel(s, null);
    }

    public BrickRadioBoxes setOnCancel(Callback1<BrickRadioBoxes> onCancel) {
        return setOnCancel(null, onCancel);
    }

    public BrickRadioBoxes setOnCancel(@StringRes int s, Callback1<BrickRadioBoxes> onCancel) {
        return setOnCancel(ToolsResources.getString(s), onCancel);
    }

    public BrickRadioBoxes setOnCancel(String s, Callback1<BrickRadioBoxes> onCancel) {
        super.setOnHide(b -> {
            if (onCancel != null) onCancel.callback(this);
        });
        this.onCancel = onCancel;
        this.cancelText = s;
        return this;
    }

    public BrickRadioBoxes setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        return this;
    }

    //
    //  Item
    //

    private class Item {

        private Callback2<BrickRadioBoxes, Boolean> onChange;
        private Callback1<BrickRadioBoxes> onSelected;
        private Callback1<BrickRadioBoxes> onNotSelected;
        private String text;
        private boolean selected;

    }


}
