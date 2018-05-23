package com.sup.dev.android.views.elements.cards;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;

public class CardDividerTitle extends Card {

    private int background = 0x01FF0000;
    private boolean enabled = true;

    private String text;

    @Override
    public int getLayout() {
        return R.layout.card_divider_title;
    }

    @Override
    public void bindView(View view) {
        TextView vText =  view.findViewById(R.id.text);

        if (background != 0x01FF0000) view.setBackgroundColor(background);

        vText.setText(text);
        vText.setEnabled(isEnabled());
    }

    //
    //  Setters
    //

    public CardDividerTitle setEnabled(boolean enabled) {
        this.enabled = enabled;
        update();
        return this;
    }

    public CardDividerTitle setBackground(int background) {
        this.background = background;
        update();
        return this;
    }
    
    public CardDividerTitle setText(@StringRes int text) {
       return setText(ToolsResources.getString(text));
    }

    public CardDividerTitle setText(String text) {
        this.text = text;
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
