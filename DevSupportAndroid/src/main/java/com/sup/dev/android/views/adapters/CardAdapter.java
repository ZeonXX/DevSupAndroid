package com.sup.dev.android.views.adapters;

import android.view.View;

import com.sup.dev.android.views.cards.Card;
import com.sup.dev.android.views.cards.CardSpoiler;
import com.sup.dev.android.views.cards.CardWidget;

public interface CardAdapter {

    View getView(Card card);

    void remove(Card card);

    int indexOf(Card card);

    boolean contains(Card card);

    void add(int i, Card card);
}
