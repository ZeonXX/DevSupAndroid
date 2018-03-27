package com.sup.dev.android.views.elements.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.callbacks.simple.CallbackPair;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

public class DialogRadioButtons<K> extends BaseDialog {

    private final UtilsView utilsView = SupAndroid.di.utilsView();
    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();

    private final LinearLayout vOptionsContainer;

    private K selectedKey;

    public DialogRadioButtons(Context viewContext) {
        super(viewContext, R.layout.dialog_container);
        vOptionsContainer = view.findViewById(R.id.content_container);
    }

    public DialogRadioButtons<K> addItem(@StringRes int text) {
        return addItem(utilsResources.getString(text));
    }

    public DialogRadioButtons<K> addItem(String text) {
        return addItem((K) text, text);
    }

    public DialogRadioButtons<K> addItem(K key, @StringRes int text) {
        return addItem(key, utilsResources.getString(text));
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
            ((ViewGroup.MarginLayoutParams) radioButton.getLayoutParams()).topMargin = utilsView.dpToPx(16);
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

    public DialogRadioButtons<K> setOnCancel(CallbackSource<BaseDialog> onCancel) {
        return (DialogRadioButtons<K>) super.setOnCancel(onCancel);
    }

    public DialogRadioButtons<K> setOnCancel(@StringRes int s) {
        return (DialogRadioButtons<K>) super.setOnCancel(s);
    }

    public DialogRadioButtons<K> setOnCancel(@StringRes int s, CallbackSource<BaseDialog> onCancel) {
        return (DialogRadioButtons<K>) super.setOnCancel(s, onCancel);
    }

    public DialogRadioButtons<K> setOnCancel(String s, CallbackSource<BaseDialog> onCancel) {
        return (DialogRadioButtons<K>) super.setOnCancel(s, onCancel);
    }

    public DialogRadioButtons<K> setOnEnter(@StringRes int s) {
        return setOnEnter(s, (CallbackPair<DialogRadioButtons<K>, K>) null);
    }

    public DialogRadioButtons<K> setOnEnter(String s) {
        return setOnEnter(s, (CallbackPair<DialogRadioButtons<K>, K>) null);
    }

    public DialogRadioButtons<K> setOnEnter(@StringRes int s, CallbackPair<DialogRadioButtons<K> , K> onEnter) {
        return setOnEnter(utilsResources.getString(s), onEnter);
    }

    public DialogRadioButtons<K> setOnEnter(String s, CallbackPair<DialogRadioButtons<K> , K> onEnter) {
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
