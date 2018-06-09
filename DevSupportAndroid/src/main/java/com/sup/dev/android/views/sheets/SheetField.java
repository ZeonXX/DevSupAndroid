package com.sup.dev.android.views.sheets;

import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.watchers.TextWatcherChanged;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.items.Item2;
import com.sup.dev.java.classes.providers.Provider1;

import java.util.ArrayList;

public class SheetField extends BaseSheet {

    private ArrayList<Item2<String, Provider1<String, Boolean>>> checkers = new ArrayList<>();
    private int max;
    private int min;
    private int linesCount = 1;
    private int inputType = -1;
    private String text;
    private String hint;
    private String title;
    private String textCancel;
    private String textEnter;
    private boolean multiline;
    private boolean autoHideOnEnter = true;
    private boolean enabled = true;
    private Callback2<SheetField, String> onEnter;

    @Override
    public int getLayoutId() {
        return R.layout.sheet_field;
    }

    @Override
    public void bindView(View view) {
        EditText vField = view.findViewById(R.id.field);
        TextInputLayout vFieldLayout = view.findViewById(R.id.field_layout);
        Button vCancel = view.findViewById(R.id.cancel);
        Button vEnter = view.findViewById(R.id.enter);
        TextView vTitle = view.findViewById(R.id.title);

        vEnter.setVisibility(View.GONE);
        vCancel.setVisibility(View.GONE);
        vTitle.setVisibility(View.GONE);
        vFieldLayout.setCounterMaxLength(max);
        vFieldLayout.setHint(hint);

        vTitle.setText(title);
        vTitle.setVisibility(title != null && title.length() > 0 ? View.VISIBLE : View.GONE);

        if (inputType != -1) vField.setInputType(inputType);

        if (linesCount == 1) {
            vField.setSingleLine(true);
            vField.setGravity(Gravity.CENTER | Gravity.LEFT);
            vField.setLines(linesCount);
        } else {
            multiline = true;
            vField.setLines(linesCount);
        }

        if (multiline) {
            vField.setSingleLine(false);
            vField.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            vField.setGravity(Gravity.TOP);
        }

        if (textCancel != null && !textCancel.isEmpty()) {
            vCancel.setText(textCancel);
            vCancel.setVisibility(View.VISIBLE);
            vCancel.setOnClickListener(v -> hide());
        }

        vEnter.setText(textEnter);
        vEnter.setVisibility(textEnter != null && textEnter.length() > 0 ? View.VISIBLE : View.GONE);
        vEnter.setOnClickListener(v -> {
            if (autoHideOnEnter) hide();
            else setEnabled(false);
            if (onEnter != null) onEnter.callback(this, vField.getText().toString());
        });

        vFieldLayout.setHint(null);
        vField.addTextChangedListener(new TextWatcherChanged(text -> {

            this.text = text;
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
        }));

        vField.setText(text);
        if (text != null) vField.setSelection(text.length());


        if (vCancel != null) vCancel.setEnabled(enabled);
        if (vFieldLayout != null) vFieldLayout.setEnabled(enabled);
        if (vEnter != null) vEnter.setEnabled(enabled);
        if (vTitle != null) vTitle.setEnabled(enabled);
        if (vField != null) vField.setEnabled(enabled);
    }

    @Override
    protected void onExpanded(View view) {
        super.onExpanded(view);
        ToolsView.showKeyboard(view.findViewById(R.id.field));
    }

    @Override
    protected void onCollapsed(View view) {
        super.onCollapsed(view);
        ToolsView.hideKeyboard(view.findViewById(R.id.field));
    }

    //
    //  Setters
    //

    public SheetField setMax(int max) {
        this.max = max;
        update();
        return this;
    }

    public SheetField setMin(int min) {
        this.min = min;
        update();
        return this;
    }

    public SheetField setLinesCount(int linesCount) {
        this.linesCount = linesCount;
        update();
        return this;
    }

    public SheetField setMultiLine() {
        multiline = true;
        update();
        return this;
    }

    public SheetField addChecker(@StringRes int errorText, Provider1<String, Boolean> checker) {
        return addChecker(ToolsResources.getString(errorText), checker);
    }

    public SheetField addChecker(String errorText, Provider1<String, Boolean> checker) {
        checkers.add(new Item2<>(errorText, checker));
        update();
        return this;
    }

    public SheetField setHint(@StringRes int hint) {
        return setHint(ToolsResources.getString(hint));
    }

    public SheetField setHint(String hint) {
        this.hint = hint;
        update();
        return this;
    }

    public SheetField setInputType(int type) {
        this.inputType = type;
        update();
        return this;
    }

    public SheetField setText(String text) {
        this.text = text;
        update();
        return this;
    }

    public SheetField setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public SheetField setTitle(String title) {
        this.title = title;
        update();
        return this;
    }

    public SheetField setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public SheetField setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s));
    }

    public SheetField setOnCancel(String s) {
        this.textCancel = s;
        update();
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
        this.textEnter = s;
        this.onEnter = onEnter;
        update();
        return this;
    }

    public SheetField setEnabled(boolean enabled) {
        this.enabled = enabled;
        update();
        return this;
    }
}
