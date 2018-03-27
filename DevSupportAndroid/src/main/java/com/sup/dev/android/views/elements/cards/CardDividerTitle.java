package com.sup.dev.android.views.elements.cards;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public class CardDividerTitle extends Card {

    private int background = 0x01FF0000;
    private boolean enabled = true;

    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();
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
       return setText(utilsResources.getString(text));
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
