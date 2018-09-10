package com.sup.dev.android.views.adapters

import android.view.View
import com.sup.dev.android.views.cards.Card


interface CardAdapter {

    fun getView(card: Card): View?

    fun remove(card: Card)

    fun indexOf(card: Card): Int

    fun size(): Int

    operator fun get(i: Int): Card

    operator fun contains(card: Card): Boolean

    fun add(i: Int, card: Card)

    fun isVisible(card: Card): Boolean
}
