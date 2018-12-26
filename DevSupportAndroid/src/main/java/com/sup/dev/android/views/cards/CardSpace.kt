package com.sup.dev.android.views.cards

import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsView

class CardSpace : Card {

    private var spaceHPx = ToolsView.dpToPx(2).toInt()
    private var spaceWPx = ToolsView.dpToPx(2).toInt()

    constructor(spaceHDp: Int, spaceWDp: Int = 0) {
        setSpace(spaceHDp, spaceWDp)
    }

    override fun getLayout(): Int {
        return R.layout.card_space
    }

    override fun bindView(view: View) {
        val space = view.findViewById<View>(R.id.vSpace)
        space.layoutParams.height = spaceHPx
        space.layoutParams.width = spaceWPx
    }

    //
    //  Setters
    //

    fun setSpace(spaceHDp: Int, spaceWDp: Int = 0): CardSpace {
        this.spaceHPx = ToolsView.dpToPx(spaceHDp).toInt()
        this.spaceWPx = ToolsView.dpToPx(spaceWDp).toInt()
        return this
    }
}
