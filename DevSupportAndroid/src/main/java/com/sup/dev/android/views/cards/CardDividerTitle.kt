package com.sup.dev.android.views.cards

import android.support.annotation.StringRes
import android.view.View
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources


class CardDividerTitle @JvmOverloads constructor(private var title: String? = null) : Card() {

    private var background: Int = 0
    private var divider = true
    private var enabled = true

    constructor(@StringRes title: Int) : this(ToolsResources.getString(title)) {}


    override fun getLayout(): Int {
        return R.layout.card_divider_title
    }

    override fun bindView(view: View) {
        val vText = view.findViewById<TextView>(R.id.vText)
        val vDivider1 = view.findViewById<View>(R.id.divider_d_1)
        val vDivider2 = view.findViewById<View>(R.id.divider_d_2)

        vDivider1.visibility = if (divider) View.VISIBLE else View.INVISIBLE
        vDivider2.visibility = if (divider) View.VISIBLE else View.INVISIBLE
        if (background != 0) view.setBackgroundColor(background)

        vText.text = title
        vText.isEnabled = isEnabled()
    }

    //
    //  Setters
    //

    fun setEnabled(enabled: Boolean): CardDividerTitle {
        this.enabled = enabled
        update()
        return this
    }

    fun setBackground(background: Int): CardDividerTitle {
        this.background = background
        update()
        return this
    }

    fun setText(@StringRes title: Int): CardDividerTitle {
        return setText(ToolsResources.getString(title))
    }

    fun setText(title: String?): CardDividerTitle {
        this.title = title
        update()
        return this
    }

    fun setDivider(divider: Boolean): CardDividerTitle {
        this.divider = divider
        update()
        return this
    }

    //
    //  Getters
    //

    fun isEnabled(): Boolean {
        return enabled
    }
}
