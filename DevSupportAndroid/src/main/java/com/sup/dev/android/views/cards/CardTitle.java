package com.sup.dev.android.views.cards;


import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsResources;

public class CardTitle extends Card {

    private boolean dividerVisible = false;
    private int background = 0;
    private boolean enabled = true;

    private String title;
    private boolean customColor;
    private int textColor;

    public CardTitle(){

    }

    public CardTitle(String title){
        setTitle(title);
    }

    @Override
    public int getLayout() {
        return R.layout.card_title;
    }

    @Override
    public void bindView(View view) {
        View vDivider = view.findViewById(R.id.divider);
        TextView textView = view.findViewById(R.id.text);

        vDivider.setVisibility(dividerVisible ? View.VISIBLE : View.GONE);
        if (background != 0) view.setBackgroundColor(background);
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

    public CardTitle setTitle(@StringRes  int title) {
        return setTitle(ToolsResources.getString(title));
    }

    public CardTitle setTitle(String title) {
        this.title = title;
        update();
        return this;
    }

    public CardTitle setTitleColor(int color) {
        customColor = true;
        textColor = color;
        update();
        return this;
    }


}
