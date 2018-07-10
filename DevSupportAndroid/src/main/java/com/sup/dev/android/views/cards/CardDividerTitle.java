package com.sup.dev.android.views.cards;

import android.support.annotation.StringRes;
import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.R;
import com.sup.dev.android.tools.ToolsResources;

public class CardDividerTitle extends Card {

    private int background;
    private boolean divider = true;
    private boolean enabled = true;

    private String title;

    public CardDividerTitle(){
        this(null);
    }

    public CardDividerTitle(@StringRes int title){
       this(ToolsResources.getString(title));
    }

    public CardDividerTitle(String title){
        this.title = title;
    }


    @Override
    public int getLayout() {
        return R.layout.card_divider_title;
    }

    @Override
    public void bindView(View view) {
        TextView vText =  view.findViewById(R.id.text);
        View vDivider1 = view.findViewById(R.id.divider_d_1);
        View vDivider2 = view.findViewById(R.id.divider_d_2);

        vDivider1.setVisibility(divider?View.VISIBLE:View.INVISIBLE);
        vDivider2.setVisibility(divider?View.VISIBLE:View.INVISIBLE);
        if (background != 0) view.setBackgroundColor(background);

        vText.setText(title);
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
    
    public CardDividerTitle setText(@StringRes int title) {
       return setText(ToolsResources.getString(title));
    }

    public CardDividerTitle setText(String title) {
        this.title = title;
        update();
        return this;
    }

    public CardDividerTitle setDivider(boolean divider) {
        this.divider = divider;
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
