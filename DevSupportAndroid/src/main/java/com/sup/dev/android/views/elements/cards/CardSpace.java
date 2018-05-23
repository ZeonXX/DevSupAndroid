package com.sup.dev.android.views.elements.cards;

import android.view.View;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.tools.ToolsView;

public class CardSpace extends Card {

    private int spacePx = ToolsView.dpToPx(2);

    public CardSpace(){

    }

    public CardSpace(int spaceDp){
        setSpace(spaceDp);
    }

    @Override
    public int getLayout() {
        return R.layout.card_space;
    }

    @Override
    public void bindView(View view) {
        View space = view.findViewById(R.id.space);
        space.getLayoutParams().height  = spacePx;
    }

    //
    //  Setters
    //

    public CardSpace setSpace(int dp) {
        this.spacePx = ToolsView.dpToPx(dp);
        return this;
    }
}
