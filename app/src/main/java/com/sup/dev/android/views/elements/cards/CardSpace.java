package com.sup.dev.android.views.elements.cards;

import android.view.View;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsView;

public class CardSpace extends Card {

    private final UtilsView utilsView = SupAndroid.di.utilsView();
    private int spacePx = utilsView.dpToPx(2);

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
        this.spacePx = utilsView.dpToPx(dp);
        return this;
    }
}
