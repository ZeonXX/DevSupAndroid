package com.sup.dev.android.views.dialogs;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.watchers.TextWatcherChanged;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.items.Item2;
import com.sup.dev.java.classes.providers.Provider1;

import java.util.ArrayList;

public class DialogInputText extends BaseDialog {

    private final EditText vField;
    private final TextInputLayout vFieldLayout;

    private ArrayList<Item2<String, Provider1<String, Boolean>>> checkers = new ArrayList<>();
    private int max;
    private int min;

    public DialogInputText(Context viewContext) {
        super(viewContext, ToolsView.inflate(viewContext, R.layout.dialog_input_text));

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
        ToolsView.showKeyboard(vField);
        return this;
    }

    private void onTextChanged(String text) {

        String error = null;

        for (Item2<String, Provider1<String, Boolean>> pair : checkers)
            if (!pair.a2.provide(text)) {
                error = pair.a1;
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
        return setHint(ToolsResources.getString(s));
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

    public DialogInputText addChecker(@StringRes int errorText, Provider1<String, Boolean> checker) {
        return addChecker(ToolsResources.getString(errorText), checker);
    }

    public DialogInputText addChecker(String errorText, Provider1<String, Boolean> checker) {
        checkers.add(new Item2<>(errorText, checker));
        onTextChanged(vField.getText().toString());
        return this;
    }

    //
    //  Setters
    //

    public DialogInputText setInputType(int type){
        vField.setInputType(type);
        return this;
    }

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

    public DialogInputText setOnCancel(Callback1<BaseDialog> onCancel) {
        return (DialogInputText) super.setOnCancel(onCancel);
    }

    public DialogInputText setOnCancel(@StringRes int s) {
        return (DialogInputText) super.setOnCancel(s);
    }

    public DialogInputText setOnCancel(@StringRes int s, Callback1<BaseDialog> onCancel) {
        return (DialogInputText) super.setOnCancel(s, onCancel);
    }

    public DialogInputText setOnCancel(String s, Callback1<BaseDialog> onCancel) {
        return (DialogInputText) super.setOnCancel(s, onCancel);
    }

    public DialogInputText setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s));
    }

    public DialogInputText setOnEnter(String s) {
        return setOnEnter(s, (Callback2<DialogInputText, String>) null);
    }

    public DialogInputText setOnEnter(@StringRes int s, Callback2<DialogInputText, String> onEnter) {
        return setOnEnter(ToolsResources.getString(s), onEnter);
    }

    public DialogInputText setOnEnter(String s, Callback2<DialogInputText, String> onEnter) {
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
