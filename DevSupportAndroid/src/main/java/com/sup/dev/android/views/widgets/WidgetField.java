package com.sup.dev.android.views.widgets;

import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sup.dev.android.R;
import com.sup.dev.android.tools.ToolsAndroid;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.screens.SWidget;
import com.sup.dev.android.views.views.ViewEditTextMedia;
import com.sup.dev.android.views.views.ViewIcon;
import com.sup.dev.android.views.watchers.TextWatcherChanged;
import com.sup.dev.android.views.watchers.TextWatcherRemoveHTML;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.items.Item2;
import com.sup.dev.java.classes.providers.Provider1;
import com.sup.dev.java.tools.ToolsThreads;

import java.util.ArrayList;

public class WidgetField extends Widget {

    private final ViewIcon vCopy;
    private final ViewEditTextMedia vField;
    private final TextInputLayout vFieldLayout;
    private final Button vCancel;
    private final Button vEnter;

    private ArrayList<Item2<String, Provider1<String, Boolean>>> checkers = new ArrayList<>();
    private int max;
    private int min;
    private boolean autoHideOnEnter = true;
    private boolean autoHideOnCancel = true;
    private boolean fastCopy;

    public WidgetField() {
        super(R.layout.widget_field);

        vField = view.findViewById(R.id.field);
        vFieldLayout = view.findViewById(R.id.field_layout);
        vCopy = view.findViewById(R.id.copy);
        vCancel = view.findViewById(R.id.cancel);
        vEnter = view.findViewById(R.id.enter);

        vEnter.setVisibility(View.GONE);
        vCancel.setVisibility(View.GONE);
        vCopy.setVisibility(View.GONE);

        vField.addTextChangedListener(new TextWatcherRemoveHTML());
        vField.addTextChangedListener(new TextWatcherChanged(text -> check()));

        vField.setCallback(vField::setText);

        vCopy.setOnClickListener(v -> {
            if(fastCopy) vEnter.performClick();
            else setText(ToolsAndroid.getFromClipboard());
        });
    }

    private void check() {

        String text = getText();
        String error = null;

        for (Item2<String, Provider1<String, Boolean>> pair : checkers)
            if (!pair.getA2().provide(text)) {
                error = pair.getA1();
                break;
            }

        if (error != null) {
            vFieldLayout.setError(error.isEmpty()?null:error);
            vEnter.setEnabled(false);
        } else {
            vFieldLayout.setError(null);
            vEnter.setEnabled(text.length() >= min && (max == 0 || text.length() <= max));
        }
    }

    @Override
    public void onShow() {
        super.onShow();
        ToolsView.showKeyboard(view.findViewById(R.id.field));
    }

    @Override
    public void onHide() {
        super.onHide();
        ToolsThreads.main(500, () -> ToolsView.hideKeyboard()); //  Без задержки будет скрываться под клавиатуру и оставаться посреди экрана
    }

    public String getText() {
        return vField.getText().toString();
    }

    //
    //  Setters
    //

    public WidgetField enableCopy(){
        vCopy.setVisibility(View.VISIBLE);
        return this;
    }

    public WidgetField enableFastCopy(){
        vCopy.setVisibility(View.VISIBLE);
        fastCopy = true;
        return this;
    }

    public WidgetField setMediaCallback(Callback2<WidgetField, String> callback){
        vField.setCallback(s -> callback.callback(this, s));
        return this;
    }

    @Override
    public WidgetField setTitle(int title) {
        return super.setTitle(title);
    }

    @Override
    public WidgetField setTitle(String title) {
        return super.setTitle(title);
    }

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

    public WidgetField addChecker(Provider1<String, Boolean> checker) {
        return addChecker(null, checker);
    }

    public WidgetField addChecker(String errorText, Provider1<String, Boolean> checker) {
        checkers.add(new Item2<>(errorText == null ? "" : errorText, checker));
        check();
        return this;
    }

    public WidgetField setHint(@StringRes int hint) {
        return setHint(ToolsResources.getString(hint));
    }

    public WidgetField setHint(String hint) {
        vField.setHint(hint);
        return this;
    }

    public WidgetField setInputType(int type) {
        vField.setInputType(type);
        return this;
    }

    public WidgetField setText(String text) {
        vField.setText(text);
        vField.setSelection(vField.getText().length());
        return this;
    }

    public WidgetField setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public WidgetField setAutoHideOnCancel(boolean autoHideOnCancel) {
        this.autoHideOnCancel = autoHideOnCancel;
        return this;
    }

    public WidgetField setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s));
    }

    public WidgetField setOnCancel(String s) {
        return setOnCancel(s, null);
    }

    public WidgetField setOnCancel(@StringRes int s, Callback1<WidgetField> onCancel) {
        return setOnCancel(ToolsResources.getString(s), onCancel);
    }

    public WidgetField setOnCancel(String s, Callback1<WidgetField> onCancel) {
        ToolsView.setTextOrGone(vCancel, s);
        vCancel.setVisibility(View.VISIBLE);
        vCancel.setOnClickListener(v -> {
            if (autoHideOnCancel) hide();
            else setEnabled(false);
            if (onCancel != null) onCancel.callback(this);
        });
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
        vField.setEnabled(enabled);
        return this;
    }

}
