package com.sup.dev.android.views.widgets;

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
import com.sup.dev.android.views.screens.SWidget;
import com.sup.dev.android.views.watchers.TextWatcherChanged;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.items.Item2;
import com.sup.dev.java.classes.providers.Provider1;

import java.util.ArrayList;

public class WidgetField extends Widget {

    private final EditText vField;
    private final TextInputLayout vFieldLayout;
    private final Button vCancel;
    private final Button vEnter;
    private final TextView vTitle;

    private ArrayList<Item2<String, Provider1<String, Boolean>>> checkers = new ArrayList<>();
    private int max;
    private int min;
    private boolean autoHideOnEnter = true;

    public WidgetField(){
        super(R.layout.widget_field);

        vField = view.findViewById(R.id.field);
        vFieldLayout = view.findViewById(R.id.field_layout);
        vCancel = view.findViewById(R.id.cancel);
        vEnter = view.findViewById(R.id.enter);
        vTitle = view.findViewById(R.id.title);

        vTitle.setText(null);

        vEnter.setVisibility(View.GONE);
        vCancel.setVisibility(View.GONE);
        vTitle.setVisibility(View.GONE);

        vFieldLayout.setHint(null);
        vField.addTextChangedListener(new TextWatcherChanged(text -> {

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
    }

    @Override
    public void onShow() {
        super.onShow();
        ToolsView.showKeyboard(view.findViewById(R.id.field));

        ToolsView.setTextOrGone(vTitle, vTitle.getText());
        if(viewWrapper instanceof SWidget){
            vTitle.setVisibility(View.GONE);
            ((SWidget)viewWrapper).setTitle(vTitle.getText().toString());
        }
    }

    @Override
    public void onHide() {
        super.onHide();
        ToolsView.hideKeyboard();
    }

    //
    //  Setters
    //

    public WidgetField setMax(int max) {
        this.max = max;
        vFieldLayout.setCounterMaxLength(max);
        return this;
    }

    public WidgetField setMin(int min) {
        this.min = min;
        return this;
    }

    public WidgetField setLinesCount(int linesCount) {
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

    public WidgetField setMultiLine() {
        vField.setSingleLine(false);
        vField.setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        vField.setGravity(Gravity.TOP);
        return this;
    }

    public WidgetField addChecker(@StringRes int errorText, Provider1<String, Boolean> checker) {
        return addChecker(ToolsResources.getString(errorText), checker);
    }

    public WidgetField addChecker(String errorText, Provider1<String, Boolean> checker) {
        checkers.add(new Item2<>(errorText, checker));
        return this;
    }

    public WidgetField setHint(@StringRes int hint) {
        return setHint(ToolsResources.getString(hint));
    }

    public WidgetField setHint(String hint) {
        vFieldLayout.setHint(hint);
        return this;
    }

    public WidgetField setInputType(int type) {
        vField.setInputType(type);
        return this;
    }

    public WidgetField setText(String text) {
        vField.setText(text);
        if (text != null) vField.setSelection(text.length());
        return this;
    }

    public WidgetField setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public WidgetField setTitle(String title) {
        ToolsView.setTextOrGone(vTitle, title);
        return this;
    }

    public WidgetField setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public WidgetField setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s));
    }

    public WidgetField setOnCancel(String s) {
        ToolsView.setTextOrGone(vCancel, s);
        vCancel.setVisibility(View.VISIBLE);
        vCancel.setOnClickListener(v -> hide());
        return this;
    }

    public WidgetField setOnEnter(@StringRes int s) {
        return setOnEnter(ToolsResources.getString(s), null);
    }

    public WidgetField setOnEnter(String s) {
        return setOnEnter(s, null);
    }

    public WidgetField setOnEnter(@StringRes int s, Callback2<WidgetField, String> onEnter) {
        return setOnEnter(ToolsResources.getString(s), onEnter);
    }

    public WidgetField setOnEnter(String s, Callback2<WidgetField, String> onEnter) {
        ToolsView.setTextOrGone(vEnter, s);
        vEnter.setOnClickListener(v -> {
            if (autoHideOnEnter) hide();
            else setEnabled(false);
            if (onEnter != null) onEnter.callback(this, vField.getText().toString());
        });

        return this;
    }

    @Override
    public WidgetField setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        vCancel.setEnabled(enabled);
        vFieldLayout.setEnabled(enabled);
        vEnter.setEnabled(enabled);
        vTitle.setEnabled(enabled);
        vField.setEnabled(enabled);
        return this;
    }

}
