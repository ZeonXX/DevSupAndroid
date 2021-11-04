package com.sup.dev.android.views.cards

import android.support.annotation.StringRes
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources


class CardDividerTitle constructor(private var title: String? = null) : Card() {

    private var background: Int = 0
    private var enabled = true
    private var dividerBottom = true
    private var dividerTop = false
    private var gravity = Gravity.LEFT

    constructor(@StringRes title: Int) : this(ToolsResources.getString(title)) {}

    override fun getLayout() = R.layout.card_divider_title

    override fun bindView(view: View) {
        val vText = view.findViewById<TextView>(R.id.vText)
        val vDividerTop = view.findViewById<View>(R.id.vDividerTop)
        val vDividerBottom = view.findViewById<View>(R.id.vDividerBottom)

        vDividerTop.visibility = if (dividerTop) View.VISIBLE else View.INVISIBLE
        vDividerBottom.visibility = if (dividerBottom) View.VISIBLE else View.INVISIBLE
        if (background != 0) view.setBackgroundColor(background)

        vText.text = title
        vText.isEnabled = isEnabled()
        (vText.layoutParams as FrameLayout.LayoutParams).gravity = gravity
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

    fun setDividerBottom(divider: Boolean): CardDividerTitle {
        this.dividerBottom = divider
        update()
        return this
    }

    fun setDividerTop(divider: Boolean): CardDividerTitle {
        this.dividerTop = divider
        update()
        return this
    }

    fun toCenter(): CardDividerTitle {
        this.gravity = Gravity.CENTER
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
