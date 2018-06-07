package com.sup.dev.android.views.sheets;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.dialogs.DialogInputText;
import com.sup.dev.android.views.watchers.TextWatcherChanged;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.items.Item2;
import com.sup.dev.java.classes.providers.Provider1;

import java.util.ArrayList;

public class SheetField extends BaseSheet {

    private final EditText vField;
    private final TextInputLayout vFieldLayout;
    private final Button vCancel;
    private final Button vEnter;
    private final TextView vTitle;

    private ArrayList<Item2<String, Provider1<String, Boolean>>> checkers = new ArrayList<>();
    private int max;
    private int min;
    private boolean autoHideOnEnter;

    public SheetField(Context viewContext, AttributeSet attrs) {
        super(viewContext, attrs, R.layout.sheet_field);

        vField = findViewById(R.id.field);
        vFieldLayout = findViewById(R.id.field_layout);
        vCancel = findViewById(R.id.cancel);
        vEnter = findViewById(R.id.enter);
        vTitle = findViewById(R.id.title);

        vEnter.setVisibility(View.GONE);
        vCancel.setVisibility(View.GONE);
        vTitle.setVisibility(View.GONE);

        vFieldLayout.setHint(null);
        vField.addTextChangedListener(new TextWatcherChanged(text -> onTextChanged(text)));
    }

    @Override
    protected void onExpanded() {
        super.onExpanded();
        ToolsView.showKeyboard(vField);
    }

    @Override
    protected void onCollapsed() {
        super.onCollapsed();
        ToolsView.hideKeyboard(vField);
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

    //
    //  Setters
    //

    public SheetField setMax(int max) {
        this.max = max;
        vFieldLayout.setCounterMaxLength(max);
        return this;
    }

    public SheetField setMin(int min) {
        this.min = min;
        return this;
    }

    public SheetField setLinesCount(int linesCount) {
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

    public SheetField setMultiLine() {
        vField.setSingleLine(false);
        vField.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        vField.setGravity(Gravity.TOP);
        return this;
    }

    public SheetField setTextInput(String s) {
        vField.setText(s);
        vField.setSelection(s.length());
        return this;
    }

    public SheetField addChecker(@StringRes int errorText, Provider1<String, Boolean> checker) {
        return addChecker(ToolsResources.getString(errorText), checker);
    }

    public SheetField addChecker(String errorText, Provider1<String, Boolean> checker) {
        checkers.add(new Item2<>(errorText, checker));
        onTextChanged(vField.getText().toString());
        return this;
    }

    public SheetField setHint(@StringRes int hint) {
        return setHint(ToolsResources.getString(hint));
    }

    public SheetField setHint(String hint) {
        vFieldLayout.setHint(hint);
        return this;
    }

    public SheetField setInputType(int type) {
        vField.setInputType(type);
        return this;
    }

    public SheetField setText(String text) {
        vField.setText(text);
        return this;
    }

    public SheetField setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public SheetField setTitle(String title) {
        vTitle.setText(title);
        vTitle.setVisibility(title != null && title.length() > 0 ? View.VISIBLE : View.GONE);
        return this;
    }

    public SheetField setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public SheetField setCancelable(boolean cancelable) {
        return (SheetField) super.setCancelable(cancelable);
    }

    public SheetField setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s));
    }

    public SheetField setOnCancel(String s) {
        if (s != null && !s.isEmpty()) {
            vCancel.setText(s);
            vCancel.setVisibility(View.VISIBLE);
            vCancel.setOnClickListener(v -> hide());
        }
        return this;
    }

    public SheetField setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s), null);
    }

    public SheetField setOnEnter(String s) {
        return setOnEnter(s, null);
    }

    public SheetField setOnEnter(@StringRes int s, Callback2<SheetField, String> onEnter) {
        return setOnEnter(ToolsResources.getString(s), onEnter);
    }

    public SheetField setOnEnter(String s, Callback2<SheetField, String> onEnter) {
        vEnter.setText(s);
        vEnter.setVisibility(View.VISIBLE);
        vEnter.setOnClickListener(v -> {
            if (autoHideOnEnter) hide();
            else setEnabled(false);
            if (onEnter != null) onEnter.callback(this, vField.getText().toString());
        });
        return this;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (vCancel != null) vCancel.setEnabled(enabled);
        if (vFieldLayout != null) vFieldLayout.setEnabled(enabled);
        if (vEnter != null) vEnter.setEnabled(enabled);
        if (vTitle != null) vTitle.setEnabled(enabled);
        if (vField != null) vField.setEnabled(enabled);
    }
}
