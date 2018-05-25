package com.sup.dev.android.views.cards;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public class CardMenu extends Card {

    private Callback onClick;
    private boolean dividerVisible = false;
    private boolean enabled = true;
    private int background = 0x01FF0000;

    public Object tag;
    private String text;
    private boolean customColor;
    private int textColor;

    @Override
    public int getLayout() {
        return R.layout.card_menu;
    }

    @Override
    public void bindView(View view) {
        View vTouch = view.findViewById(R.id.touch);
        View vDivider = view.findViewById(R.id.divider);
        TextView vText =  view.findViewById(R.id.text);

        vDivider.setVisibility(dividerVisible ? View.VISIBLE : View.GONE);
        vTouch.setFocusable(onClick != null && enabled);
        vTouch.setClickable(onClick != null && enabled);
        vTouch.setEnabled(onClick != null && enabled);
        vTouch.setOnClickListener((onClick != null && enabled)?v -> onClick.callback():null);
        if (background != 0x01FF0000) view.setBackgroundColor(background);

        vText.setText(text);
        vText.setEnabled(enabled);
        if (customColor) vText.setTextColor(textColor);
    }

    //
    //  Setters
    //

    public CardMenu setOnClick(Callback onClick) {
        this.onClick = onClick;
        update();
        return this;
    }

    public CardMenu setDividerVisible(boolean dividerVisible) {
        this.dividerVisible = dividerVisible;
        update();
        return this;
    }

    public CardMenu setEnabled(boolean enabled) {
        this.enabled = enabled;
        update();
        return this;
    }

    public CardMenu setBackground(int background) {
        this.background = background;
        update();
        return this;
    }

    public CardMenu setText(@StringRes int text) {
        return setText(ToolsResources.getString(text));
    }

    public CardMenu setText(String text) {
        this.text = text;
        update();
        return this;
    }

    public CardMenu setTextColor(int color) {
        customColor = true;
        textColor = color;
        update();
        return this;
    }

    //
    //  Getters
    //

    public String getText() {
        return text;
    }

    public boolean isEnabled() {
        return enabled;
    }
}