package com.sup.dev.android.views.elements.cards;


import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.java.classes.callbacks.simple.Callback;

public class CardTitle extends Card {

    private boolean dividerVisible = false;
    private int background = 0x01FF0000;
    private boolean enabled = true;

    private final UtilsResources utilsResources = SupAndroid.di.utilsResources();
    private String title;
    private boolean customColor;
    private int textColor;

    @Override
    public int getLayout() {
        return R.layout.card_title;
    }

    @Override
    public void bindView(View view) {
        View vDivider = view.findViewById(R.id.divider);
        TextView textView = view.findViewById(R.id.text);

        vDivider.setVisibility(dividerVisible ? View.VISIBLE : View.GONE);
        if (background != 0x01FF0000) view.setBackgroundColor(background);
        textView.setText(title);
        textView.setEnabled(enabled);

        if (customColor) textView.setTextColor(textColor);
    }

    //
    //  Setters
    //

    public CardTitle setDividerVisible(boolean dividerVisible) {
        this.dividerVisible = dividerVisible;
        update();
        return this;
    }

    public CardTitle setEnabled(boolean enabled) {
        this.enabled = enabled;
        update();
        return this;
    }

    public CardTitle setBackground(int background) {
        this.background = background;
        update();
        return this;
    }

    public CardTitle setText(@StringRes  int title) {
        return setText(utilsResources.getString(title));
    }

    public CardTitle setText(String title) {
        this.title = title;
        update();
        return this;
    }

    public CardTitle setTextColor(int color) {
        customColor = true;
        textColor = color;
        update();
        return this;
    }


}
