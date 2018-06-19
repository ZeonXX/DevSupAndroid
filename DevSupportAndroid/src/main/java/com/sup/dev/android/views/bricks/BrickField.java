package com.sup.dev.android.views.bricks;

import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.watchers.TextWatcherChanged;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.items.Item2;
import com.sup.dev.java.classes.providers.Provider1;

import java.util.ArrayList;

public class BrickField extends Brick {

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
    private Callback2<BrickField, String> onEnter;

    @Override
    public int getLayoutRes(Mode mode) {
        return R.layout.brick_field;
    }

    @Override
    public void bindView(View view, Mode mode) {
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


        vCancel.setEnabled(isEnabled());
        vFieldLayout.setEnabled(isEnabled());
        vEnter.setEnabled(isEnabled());
        vTitle.setEnabled(isEnabled());
        vField.setEnabled(isEnabled());
    }

    @Override
    public void onShow(View view) {
        super.onShow(view);
        ToolsView.showKeyboard(view.findViewById(R.id.field));
    }

    @Override
    public void onHide() {
        super.onHide();
        SupAndroid.mvpActivity(activity -> ToolsView.hideKeyboard(activity));
    }

    //
    //  Setters
    //

    public BrickField setMax(int max) {
        this.max = max;
        update();
        return this;
    }

    public BrickField setMin(int min) {
        this.min = min;
        update();
        return this;
    }

    public BrickField setLinesCount(int linesCount) {
        this.linesCount = linesCount;
        update();
        return this;
    }

    public BrickField setMultiLine() {
        multiline = true;
        update();
        return this;
    }

    public BrickField addChecker(@StringRes int errorText, Provider1<String, Boolean> checker) {
        return addChecker(ToolsResources.getString(errorText), checker);
    }

    public BrickField addChecker(String errorText, Provider1<String, Boolean> checker) {
        checkers.add(new Item2<>(errorText, checker));
        update();
        return this;
    }

    public BrickField setHint(@StringRes int hint) {
        return setHint(ToolsResources.getString(hint));
    }

    public BrickField setHint(String hint) {
        this.hint = hint;
        update();
        return this;
    }

    public BrickField setInputType(int type) {
        this.inputType = type;
        update();
        return this;
    }

    public BrickField setText(String text) {
        this.text = text;
        update();
        return this;
    }

    public BrickField setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public BrickField setTitle(String title) {
        this.title = title;
        update();
        return this;
    }

    public BrickField setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public BrickField setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s));
    }

    public BrickField setOnCancel(String s) {
        this.textCancel = s;
        update();
        return this;
    }

    public BrickField setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s), null);
    }

    public BrickField setOnEnter(String s) {
        return setOnEnter(s, null);
    }

    public BrickField setOnEnter(@StringRes int s, Callback2<BrickField, String> onEnter) {
        return setOnEnter(ToolsResources.getString(s), onEnter);
    }

    public BrickField setOnEnter(String s, Callback2<BrickField, String> onEnter) {
        this.textEnter = s;
        this.onEnter = onEnter;
        update();
        return this;
    }

    @Override
    public BrickField setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        return this;
    }

}
