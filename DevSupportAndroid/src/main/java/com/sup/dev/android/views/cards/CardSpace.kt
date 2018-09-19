package com.sup.dev.android.views.cards

import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsView

class CardSpace : Card {

    private var spacePx = ToolsView.dpToPx(2)

    constructor(spaceDp: Int) {
        setSpace(spaceDp)
    }

    override fun getLayout(): Int {
        return R.layout.card_space
    }

    override fun bindView(view: View) {
        val space = view.findViewById<View>(R.id.space)
        space.layoutParams.height = spacePx
    }

    //
    //  Setters
    //

    fun setSpace(dp: Int): CardSpace {
        this.spacePx = ToolsView.dpToPx(dp)
        return this
    }
}
