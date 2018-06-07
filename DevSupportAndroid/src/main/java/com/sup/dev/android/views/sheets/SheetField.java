package com.sup.dev.android.views.sheets;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.TextInputLayout;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;

public class SheetField extends BaseSheet {

    private final EditText vText;
    private final TextInputLayout vTextLayout;
    private final Button vCancel;
    private final Button vEnter;
    private final TextView vTitle;

    private boolean autoHideOnCancel;
    private boolean autoHideOnEnter;
    private Callback1<SheetField> onCancel;

    public SheetField(Context viewContext, AttributeSet attrs) {
        super(viewContext, attrs, R.layout.sheet_field);
        vText = findViewById(R.id.field);
        vTextLayout = findViewById(R.id.field_layout);
        vCancel = findViewById(R.id.cancel);
        vEnter = findViewById(R.id.enter);
        vTitle = findViewById(R.id.title);

        vEnter.setVisibility(View.GONE);
        vCancel.setVisibility(View.GONE);
        vTitle.setVisibility(View.GONE);
    }

    @Override
    protected void onExpanded() {
        super.onExpanded();
        ToolsView.showKeyboard(vText);
    }

    @Override
    protected void onCollapsed() {
        super.onCollapsed();
        if(onCancel != null) onCancel.callback(this);
    }

    //
    //  Setters
    //

    public SheetField setHint(@StringRes int hint) {
        return setHint(ToolsResources.getString(hint));
    }

    public SheetField setHint(String hint) {
        vTextLayout.setHint(hint);
        return this;
    }

    public SheetField setText(String text) {
        vText.setText(text);
        return this;
    }

    public SheetField setTitle(@StringRes int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public SheetField setTitle(String title) {
        vTitle.setText(title);
        vTitle.setVisibility(title != null && title.length() > 0? View.VISIBLE : View.GONE);
        return this;
    }

    public SheetField setAutoHideOnCancel(boolean autoHideOnCancel) {
        this.autoHideOnCancel = autoHideOnCancel;
        if (!autoHideOnCancel) setCancelable(false);
        return this;
    }

    public SheetField setAutoHideOnEnter(boolean autoHideOnEnter) {
        this.autoHideOnEnter = autoHideOnEnter;
        return this;
    }

    public SheetField setCancelable(boolean cancelable) {
        return (SheetField) super.setCancelable(cancelable);
    }

    public SheetField setOnCancel(String s) {
        return setOnCancel(s, null);
    }

    public SheetField setOnCancel(Callback1<SheetField> onCancel) {
        return setOnCancel(null, onCancel);
    }

    public SheetField setOnCancel(@StringRes int s) {
        return setOnCancel(ToolsResources.getString(s), null);
    }

    public SheetField setOnCancel(@StringRes int s, Callback1<SheetField> onCancel) {
        return setOnCancel(ToolsResources.getString(s), onCancel);
    }

    public SheetField setOnCancel(String s, Callback1<SheetField> onCancel) {

        this.onCancel = onCancel;

        if (s != null && !s.isEmpty()) {
            vCancel.setText(s);
            vCancel.setVisibility(View.VISIBLE);
            vCancel.setOnClickListener(v -> {
                if (autoHideOnCancel) hide();
                else setEnabled(false);
                if (onCancel != null) onCancel.callback(this);
            });
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
            if (onEnter != null) onEnter.callback(this, vText.getText().toString());
        });
        return this;
    }

    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (vCancel != null) vCancel.setEnabled(enabled);
        if (vTextLayout != null) vTextLayout.setEnabled(enabled);
        if (vEnter != null) vEnter.setEnabled(enabled);
        if (vTitle != null) vTitle.setEnabled(enabled);
        if (vText != null) vText.setEnabled(enabled);
    }
}
