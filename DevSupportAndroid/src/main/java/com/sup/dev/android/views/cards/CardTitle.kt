package com.sup.dev.android.views.cards

import android.support.annotation.StringRes
import android.view.View
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources


class CardTitle @JvmOverloads constructor(title: String? = null) : Card() {

    private var dividerVisible = false
    private var background = 0
    private var enabled = true

    private var title: String? = null
    private var customColor: Boolean = false
    private var textColor: Int = 0

    constructor(@StringRes title: Int) : this(ToolsResources.getString(title)) {}

    init {
        setTitle(title)
    }

    override fun getLayout(): Int {
        return R.layout.card_title
    }

    override fun bindView(view: View) {
        val vDivider = view.findViewById<View>(R.id.divider)
        val textView = view.findViewById<TextView>(R.id.text)

        vDivider.visibility = if (dividerVisible) View.VISIBLE else View.GONE
        if (background != 0) view.setBackgroundColor(background)
        textView.text = title
        textView.isEnabled = enabled

        if (customColor) textView.setTextColor(textColor)
    }

    //
    //  Setters
    //

    fun setDividerVisible(dividerVisible: Boolean): CardTitle {
        this.dividerVisible = dividerVisible
        update()
        return this
    }

    fun setEnabled(enabled: Boolean): CardTitle {
        this.enabled = enabled
        update()
        return this
    }

    fun setBackground(background: Int): CardTitle {
        this.background = background
        update()
        return this
    }

    fun setTitle(@StringRes title: Int): CardTitle {
        return setTitle(ToolsResources.getString(title))
    }

    fun setTitle(title: String?): CardTitle {
        this.title = title
        update()
        return this
    }

    fun setTitleColor(color: Int): CardTitle {
        customColor = true
        textColor = color
        update()
        return this
    }


}
