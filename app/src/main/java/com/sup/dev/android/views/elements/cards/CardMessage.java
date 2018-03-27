package com.sup.dev.android.views.elements.cards;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public class CardMessage extends Card {

    private boolean dividerVisible = false;
    private boolean enabled = true;
    private int background = 0x01FF0000;

    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();
    private String text;
    private boolean customColor;
    private int textColor;
    private String actionText;
    private Callback onActionClicked;

    @Override
    public int getLayout() {
        return R.layout.card_message;
    }

    @Override
    public void bindView(View view) {
        View vDivider = view.findViewById(R.id.divider);
        TextView vText = view.findViewById(R.id.text);
        Button vAction = view.findViewById(R.id.action);

        vDivider.setVisibility(dividerVisible ? View.VISIBLE : View.GONE);
        if (background != 0x01FF0000) view.setBackgroundColor(background);

        vText.setVisibility(text == null ? View.GONE : View.VISIBLE);
        vText.setText(text);
        vText.setEnabled(isEnabled());

        vAction.setEnabled(isEnabled());
        vAction.setVisibility(actionText == null ? View.GONE : View.VISIBLE);
        vAction.setText(actionText);
        vAction.setOnClickListener(v -> {
            if (onActionClicked != null) onActionClicked.callback();
        });

        if (customColor)
            vText.setTextColor(textColor);
    }

    //
    //  Setters
    //
    

    public CardMessage setDividerVisible(boolean dividerVisible) {
        this.dividerVisible = dividerVisible;
        update();
        return this;
    }

    public CardMessage setEnabled(boolean enabled) {
        this.enabled = enabled;
        update();
        return this;
    }

    public CardMessage setBackground(int background) {
        this.background = background;
        update();
        return this;
    }

    public CardMessage setText(@StringRes int text) {
        return setText(utilsResources.getString(text));
    }

    public CardMessage setText(String text) {
        this.text = text;
        update();
        return this;
    }

    public CardMessage setAction(@StringRes int text, Callback onActionClicked) {
        return setAction(utilsResources.getString(text), onActionClicked);
    }

    public CardMessage setAction(String actionText, Callback onActionClicked) {
        this.actionText = actionText;
        this.onActionClicked = onActionClicked;
        return this;
    }

    public CardMessage setTextColor(int color) {
        customColor = true;
        textColor = color;
        update();
        return this;
    }

    //
    //  Getters
    //

    public boolean isEnabled() {
        return enabled;
    }
}
