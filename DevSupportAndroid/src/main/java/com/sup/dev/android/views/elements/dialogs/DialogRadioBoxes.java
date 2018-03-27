package com.sup.dev.android.views.elements.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.CallbackPair;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

public class DialogRadioBoxes<K> extends BaseDialog{

    private final UtilsView utilsView = SupAndroid.di.utilsView();
    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();

    private final LinearLayout vOptionsContainer;

    private K selectedKey;

    public DialogRadioBoxes(Context viewContext) {
        super(viewContext, R.layout.dialog_container);
        vOptionsContainer = view.findViewById(R.id.content_container);
    }

    public DialogRadioBoxes<K> addItem(@StringRes int text) {
        return addItem(utilsResources.getString(text));
    }

    public DialogRadioBoxes<K> addItem(String text) {
        return addItem((K)text, text);
    }

    public DialogRadioBoxes<K> addItem(K key, @StringRes int text) {
        return addItem(key, utilsResources.getString(text));
    }

    public DialogRadioBoxes<K> addItem(K key, String text) {
        CheckBox checkBox = new CheckBox(viewContext);
        checkBox.setText(text);
        checkBox.setChecked(selectedKey == key);
        checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) setSelectedKey(key);
        });
        checkBox.setTag(key);
        vOptionsContainer.addView(checkBox);
        if (vOptionsContainer.getChildCount() > 1)
            ((ViewGroup.MarginLayoutParams) checkBox.getLayoutParams()).topMargin = utilsView.dpToPx(16);
        return this;
    }

    public DialogRadioBoxes<K> setSelectedKey(K selectedKey) {
        this.selectedKey = selectedKey;
        if (vOptionsContainer != null)
            for (int i = 0; i < vOptionsContainer.getChildCount(); i++)
                ((CheckBox) vOptionsContainer.getChildAt(i)).setChecked(selectedKey == vOptionsContainer.getChildAt(i).getTag());
        return this;
    }

    //
    //  Setters
    //

    public DialogRadioBoxes<K> setTitle(@StringRes int title) {
        return (DialogRadioBoxes<K>)super.setTitle(title);
    }

    public DialogRadioBoxes<K> setTitle(String title) {
        return (DialogRadioBoxes<K>)super.setTitle(title);
    }

    public DialogRadioBoxes<K> setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogRadioBoxes<K>)super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogRadioBoxes<K> setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogRadioBoxes<K>)super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogRadioBoxes<K> setCancelable(boolean cancelable) {
        return (DialogRadioBoxes<K>)super.setCancelable(cancelable);
    }

    public DialogRadioBoxes<K> setOnCancel(String s) {
        return (DialogRadioBoxes<K>)super.setOnCancel(s);
    }

    public DialogRadioBoxes<K> setOnCancel(CallbackSource<BaseDialog> onCancel) {
        return (DialogRadioBoxes<K>)super.setOnCancel(onCancel);
    }

    public DialogRadioBoxes<K> setOnCancel(@StringRes int s) {
        return (DialogRadioBoxes<K>)super.setOnCancel(s);
    }

    public DialogRadioBoxes<K> setOnCancel(@StringRes int s, CallbackSource<BaseDialog> onCancel) {
        return (DialogRadioBoxes<K>)super.setOnCancel(s, onCancel);
    }

    public DialogRadioBoxes<K> setOnCancel(String s, CallbackSource<BaseDialog> onCancel) {
        return (DialogRadioBoxes<K>)super.setOnCancel(s, onCancel);
    }

    public DialogRadioBoxes<K> setEnabled(boolean enabled) {
        for (int i = 0; i < vOptionsContainer.getChildCount(); i++)
            vOptionsContainer.getChildAt(i).setEnabled(enabled);
        return (DialogRadioBoxes<K>)super.setEnabled(enabled);
    }

    public DialogRadioBoxes<K> setOnEnter(@StringRes int s) {
        return setOnEnter(s, (CallbackPair<DialogRadioBoxes<K>, K>)null);
    }

    public DialogRadioBoxes<K> setOnEnter(String s) {
        return setOnEnter(s, (CallbackPair<DialogRadioBoxes<K>, K>)null);
    }

    public DialogRadioBoxes<K> setOnEnter(@StringRes int s, CallbackPair<DialogRadioBoxes<K>, K> onEnter) {
        return setOnEnter(utilsResources.getString(s), onEnter);
    }

    public DialogRadioBoxes<K> setOnEnter(String s, CallbackPair<DialogRadioBoxes<K>, K> onEnter) {
        super.setOnEnter(s, d->{
            if (onEnter != null) onEnter.callback(this, selectedKey);
        });
        return this;
    }





}