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

public class DialogRadioButtons<K> extends BaseDialog {

    private final LinearLayout vOptionsContainer;

    private K selectedKey;

    public DialogRadioButtons(Context viewContext) {
        super(viewContext, R.layout.dialog_container);
        vOptionsContainer = view.findViewById(R.id.content_container);
    }

    public DialogRadioButtons<K> addItem(@StringRes int text) {
        return addItem(ToolsResources.getString(text));
    }

    public DialogRadioButtons<K> addItem(String text) {
        return addItem((K) text, text);
    }

    public DialogRadioButtons<K> addItem(K key, @StringRes int text) {
        return addItem(key, ToolsResources.getString(text));
    }

    public DialogRadioButtons<K> addItem(K key, String text) {
        RadioButton radioButton = new RadioButton(viewContext);
        radioButton.setText(text);
        radioButton.setChecked(selectedKey == key);
        radioButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) setSelectedKey(key);
        });
        radioButton.setTag(key);
        vOptionsContainer.addView(radioButton);
        if (vOptionsContainer.getChildCount() > 1)
            ((ViewGroup.MarginLayoutParams) radioButton.getLayoutParams()).topMargin = ToolsView.dpToPx(16);
        return this;
    }

    public DialogRadioButtons<K> setSelectedKey(K selectedKey) {
        this.selectedKey = selectedKey;
        if (vOptionsContainer != null)
            for (int i = 0; i < vOptionsContainer.getChildCount(); i++)
                ((RadioButton) vOptionsContainer.getChildAt(i)).setChecked(selectedKey == vOptionsContainer.getChildAt(i).getTag());
        return this;
    }

    //
    //  Setters
    //

    public DialogRadioButtons<K> setTitle(@StringRes int title) {
        return (DialogRadioButtons<K>) super.setTitle(title);
    }

    public DialogRadioButtons<K> setTitle(String title) {
        return (DialogRadioButtons<K>) super.setTitle(title);
    }

    public DialogRadioButtons<K> setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogRadioButtons<K>) super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogRadioButtons<K> setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogRadioButtons<K>) super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogRadioButtons<K> setCancelable(boolean cancelable) {
        return (DialogRadioButtons<K>) super.setCancelable(cancelable);
    }

    public DialogRadioButtons<K> setOnCancel(String s) {
        return (DialogRadioButtons<K>) super.setOnCancel(s);
    }

    public DialogRadioButtons<K> setOnCancel(Callback1<BaseDialog> onCancel) {
        return (DialogRadioButtons<K>) super.setOnCancel(onCancel);
    }

    public DialogRadioButtons<K> setOnCancel(@StringRes int s) {
        return (DialogRadioButtons<K>) super.setOnCancel(s);
    }

    public DialogRadioButtons<K> setOnCancel(@StringRes int s, Callback1<BaseDialog> onCancel) {
        return (DialogRadioButtons<K>) super.setOnCancel(s, onCancel);
    }

    public DialogRadioButtons<K> setOnCancel(String s, Callback1<BaseDialog> onCancel) {
        return (DialogRadioButtons<K>) super.setOnCancel(s, onCancel);
    }

    public DialogRadioButtons<K> setOnEnter(@StringRes int s) {
        return setOnEnter(s, (Callback2<DialogRadioButtons<K>, K>) null);
    }

    public DialogRadioButtons<K> setOnEnter(String s) {
        return setOnEnter(s, (Callback2<DialogRadioButtons<K>, K>) null);
    }

    public DialogRadioButtons<K> setOnEnter(@StringRes int s, Callback2<DialogRadioButtons<K> , K> onEnter) {
        return setOnEnter(ToolsResources.getString(s), onEnter);
    }

    public DialogRadioButtons<K> setOnEnter(String s, Callback2<DialogRadioButtons<K> , K> onEnter) {
        super.setOnEnter(s, d -> {
            if (onEnter != null) onEnter.callback(this, selectedKey);
        });
        return this;
    }

    public DialogRadioButtons<K> setEnabled(boolean enabled) {
        for (int i = 0; i < vOptionsContainer.getChildCount(); i++)
            vOptionsContainer.getChildAt(i).setEnabled(enabled);
        return (DialogRadioButtons<K>) super.setEnabled(enabled);
    }


}
