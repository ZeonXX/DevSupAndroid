package com.sup.dev.android.views.cards;

import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.views.ViewIcon;
import com.sup.dev.java.classes.callbacks.simple.Callback3;

public class CardMenu extends Card {

    private Callback3<View, Integer, Integer> onClick;
    private boolean dividerVisible = false;
    private boolean enabled = true;
    private int background;

    private String text;
    private String description;
    private boolean customColor;
    private int textColor;
    private int icon;

    @Override
    public int getLayout() {
        return R.layout.card_menu;
    }

    @Override
    public void bindView(View view) {
        View vTouch = view.findViewById(R.id.touch);
        View vDivider = view.findViewById(R.id.divider);
        TextView vText = view.findViewById(R.id.text);
        TextView vDescription = view.findViewById(R.id.desc);
        ViewIcon vIcon = view.findViewById(R.id.icon);

        if (icon == 0) vIcon.setVisibility(View.GONE);
        else vIcon.setImageResource(icon);

        vDivider.setVisibility(dividerVisible ? View.VISIBLE : View.GONE);
        vTouch.setFocusable(onClick != null && enabled);
        vTouch.setClickable(onClick != null && enabled);
        vTouch.setEnabled(onClick != null && enabled);
        ToolsView.setOnClickCoordinates(vTouch, (v, x, y) -> {
            if (enabled && onClick != null) onClick.callback(v, x, y);
        });
        view.setBackgroundColor(background);

        vDescription.setText(description);
        vDescription.setEnabled(enabled);
        vDescription.setVisibility(description != null && !description.isEmpty() ? View.VISIBLE : View.GONE);
        vText.setText(text);
        vText.setEnabled(enabled);
        if (customColor) vText.setTextColor(textColor);
    }

    //
    //  Setters
    //

    public CardMenu setOnClick(Callback3<View, Integer, Integer> onClick) {
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

    public CardMenu setIcon(int icon) {
        this.icon = icon;
        update();
        return this;
    }

    public CardMenu setBackgroundRes(@ColorRes int background) {
        return setBackground(ToolsResources.getColor(background));
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

    public CardMenu setDescription(@StringRes int desc) {
        return setDescription(ToolsResources.getString(text));
    }

    public CardMenu setDescription(String desc) {
        this.description = desc;
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