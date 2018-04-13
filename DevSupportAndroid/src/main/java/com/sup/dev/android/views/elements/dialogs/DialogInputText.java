package com.sup.dev.android.views.elements.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.text.InputType;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.android.utils.interfaces.UtilsView;
import com.sup.dev.android.views.watchers.TextWatcherChanged;
import com.sup.dev.java.classes.callbacks.simple.CallbackPair;
import com.sup.dev.java.classes.callbacks.simple.CallbackSource;
import com.sup.dev.java.classes.items.Pair;
import com.sup.dev.java.classes.providers.ProviderArg;

import java.util.ArrayList;

public class DialogInputText extends BaseDialog {

    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();
    private final UtilsView utilsView = SupAndroid.di.utilsView();
    private final EditText vField;
    private final TextInputLayout vFieldLayout;

    private ArrayList<Pair<String, ProviderArg<String, Boolean>>> checkers = new ArrayList<>();
    private int max;
    private int min;

    public DialogInputText(Context viewContext) {
        super(viewContext, SupAndroid.di.utilsView().inflate(viewContext, R.layout.dialog_input_text));

        vField = view.findViewById(R.id.field);
        vFieldLayout = view.findViewById(R.id.field_layout);

        vField.setText(null);
        vFieldLayout.setHint(null);
        vField.setHint(null);

        vField.addTextChangedListener(new TextWatcherChanged(text -> onTextChanged(text)));

        setLinesCount(1);
    }

    @Override
    public DialogInputText show() {
        super.show();
        vField.setSelection(vField.getText().length());
        utilsView.showKeyboard(vField);
        return this;
    }

    private void onTextChanged(String text) {

        String error = null;

        for (Pair<String, ProviderArg<String, Boolean>> pair : checkers)
            if (!pair.right.provide(text)) {
                error = pair.left;
                break;
            }

        if (error != null) {
            vFieldLayout.setError(error);
            vEnter.setEnabled(false);
        } else {
            vFieldLayout.setError(null);
            vEnter.setEnabled(text.length() >= min && (max == 0 || text.length() <= max));
        }
    }

    public DialogInputText setHint(@StringRes int s) {
        return setHint(utilsResources.getString(s));
    }

    public DialogInputText setHint(String s) {
        vFieldLayout.setHint(s);
        return this;
    }

    public DialogInputText setMax(int max) {
        this.max = max;
        vFieldLayout.setCounterMaxLength(max);
        return this;
    }

    public DialogInputText setMin(int min) {
        this.min = min;
        return this;
    }

    public DialogInputText setLinesCount(int linesCount) {
        if (linesCount == 1) {
            vField.setSingleLine(true);
            vField.setGravity(Gravity.CENTER | Gravity.LEFT);
            vField.setLines(linesCount);
        } else {
            setMultiLine();
            vField.setLines(linesCount);
        }
        return this;
    }

    public DialogInputText setMultiLine() {
        vField.setSingleLine(false);
        vField.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        vField.setGravity(Gravity.TOP);
        return this;
    }

    public DialogInputText setTextInput(String s) {
        vField.setText(s);
        vField.setSelection(s.length());
        return this;
    }

    public DialogInputText addChecker(@StringRes int errorText, ProviderArg<String, Boolean> checker) {
        return addChecker(utilsResources.getString(errorText), checker);
    }

    public DialogInputText addChecker(String errorText, ProviderArg<String, Boolean> checker) {
        checkers.add(new Pair<>(errorText, checker));
        onTextChanged(vField.getText().toString());
        return this;
    }

    public DialogInputText qeeInputType(int inputType) {
        vField.setInputType(vField.getInputType() | inputType);
        return this;
    }


    //
    //  Setters
    //


    public DialogInputText setTitle(@StringRes int title) {
        return (DialogInputText) super.setTitle(title);
    }

    public DialogInputText setTitle(String title) {
        return (DialogInputText) super.setTitle(title);
    }

    public DialogInputText setAutoHideOnCancel(boolean autoHideOnCancel) {
        return (DialogInputText) super.setAutoHideOnCancel(autoHideOnCancel);
    }

    public DialogInputText setAutoHideOnEnter(boolean autoHideOnEnter) {
        return (DialogInputText) super.setAutoHideOnEnter(autoHideOnEnter);
    }

    public DialogInputText setCancelable(boolean cancelable) {
        return (DialogInputText) super.setCancelable(cancelable);
    }

    public DialogInputText setOnCancel(String s) {
        return (DialogInputText) super.setOnCancel(s);
    }

    public DialogInputText setOnCancel(CallbackSource<BaseDialog> onCancel) {
        return (DialogInputText) super.setOnCancel(onCancel);
    }

    public DialogInputText setOnCancel(@StringRes int s) {
        return (DialogInputText) super.setOnCancel(s);
    }

    public DialogInputText setOnCancel(@StringRes int s, CallbackSource<BaseDialog> onCancel) {
        return (DialogInputText) super.setOnCancel(s, onCancel);
    }

    public DialogInputText setOnCancel(String s, CallbackSource<BaseDialog> onCancel) {
        return (DialogInputText) super.setOnCancel(s, onCancel);
    }

    public DialogInputText setOnEnter(@StringRes int s) {
        return setOnEnter(utilsResources.getString(s));
    }

    public DialogInputText setOnEnter(String s) {
        return setOnEnter(s, (CallbackPair<DialogInputText, String>) null);
    }

    public DialogInputText setOnEnter(@StringRes int s, CallbackPair<DialogInputText, String> onEnter) {
        return setOnEnter(utilsResources.getString(s), onEnter);
    }

    public DialogInputText setOnEnter(String s, CallbackPair<DialogInputText, String> onEnter) {
        super.setOnEnter(s, d -> {
            if (onEnter != null) onEnter.callback(this, vField.getText().toString());
        });
        return this;
    }

    public DialogInputText setEnabled(boolean enabled) {
        vField.setEnabled(enabled);
        vFieldLayout.setEnabled(enabled);
        super.setEnabled(enabled);
        return this;
    }


}
