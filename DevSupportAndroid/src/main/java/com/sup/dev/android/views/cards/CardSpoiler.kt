package com.sup.dev.android.views.cards

import android.graphics.PorterDuff
import android.support.annotation.StringRes
import android.text.Html
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.support.adapters.CardAdapter
import java.util.ArrayList


open class CardSpoiler : Card() {

    //
    //  Getters
    //

    val cards = ArrayList<Card>()
    private var title: String? = null
    private var text: String? = null
    private var rightText: String? = null
    private var titleColor = 0
    private var rightTextColor = 0
    private var textColor = 0
    private var originalSeted = false
    private var dividerVisible = true
    private var titleColorOriginal: Int = 0
    private var rightTextColorOriginal: Int = 0
    private var textColorOriginal: Int = 0
    private var iconColor = 0

    internal var expanded: Boolean = false
    internal var enabled = true

    override fun getLayout(): Int {
        return R.layout.card_spoiler
    }

    override fun bindView(view: View) {
        val vIcon = view.findViewById<ImageView>(R.id.vIcon)
        val vTitle = view.findViewById<TextView>(R.id.vTitle)
        val vText = view.findViewById<TextView>(R.id.vText)
        val vRightText = view.findViewById<TextView>(R.id.vRightText)
        val vTouch = view.findViewById<View>(R.id.vTouch)
        val vDivider = view.findViewById<View>(R.id.vDivider)

        if (!originalSeted) {
            originalSeted = true
            titleColorOriginal = vTitle.currentTextColor
            textColorOriginal = vText.currentTextColor
            rightTextColorOriginal = vRightText.currentTextColor
        }

        vText.text = if (text == null) null else Html.fromHtml(text)
        vRightText.text = if (rightText == null) null else Html.fromHtml(rightText)
        vTitle.text = if (title == null) null else Html.fromHtml(title)

        vText.visibility = if (text == null) View.GONE else View.VISIBLE
        vRightText.visibility = if (rightText == null) View.GONE else View.VISIBLE
        vTitle.visibility = if (title == null) View.GONE else View.VISIBLE

        vText.isEnabled = enabled
        vRightText.isEnabled = enabled
        vTitle.isEnabled = enabled

        vDivider.visibility = if (dividerVisible) View.VISIBLE else View.GONE
        vText.setTextColor(if (textColor != 0) textColor else textColorOriginal)
        vRightText.setTextColor(if (rightTextColor != 0) rightTextColor else rightTextColorOriginal)
        vTitle.setTextColor(if (titleColor != 0) titleColor else titleColorOriginal)

        vIcon.setImageResource(if (expanded) ToolsResources.getDrawableId("ic_keyboard_arrow_up") else ToolsResources.getDrawableId("ic_keyboard_arrow_down"))
        vIcon.setAlpha(if (enabled) 255 else 106)
        if (iconColor != 0) vIcon.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP)
        if (enabled) vTouch.setOnClickListener{ v -> setExpanded(!expanded) }
        else vTouch.setOnClickListener(null)
        vTouch.isClickable = enabled
    }

    //
    //  Setters
    //

    fun add(card: Card): CardSpoiler {
        cards.add(card)
        setExpanded(expanded)
        return this
    }

    open fun setTitle(@StringRes title: Int): CardSpoiler {
        return setTitle(ToolsResources.getString(title))
    }

    fun setTitle(title: String?): CardSpoiler {
        this.title = title
        update()
        return this
    }

    override fun setCardAdapter(adapter: CardAdapter?) {
        super.adapter = adapter
        setExpanded(expanded)
    }

    fun setExpanded(expanded: Boolean): CardSpoiler {
        this.expanded = expanded
        update()

        if (adapter != null) {
            if (expanded) {

                var myIndex = adapter!!.indexOf(this)
                for (c in cards)
                    if (myIndex != -1)
                        if (!adapter!!.contains(c)) adapter!!.add(++myIndex, c)

            } else
                for (c in cards) adapter!!.remove(c)
        }

        return this
    }

    fun setEnabled(enabled: Boolean): CardSpoiler {
        this.enabled = enabled
        update()
        return this
    }

    fun setText(text: String): CardSpoiler {
        this.text = text
        update()
        return this
    }

    fun setRightText(rightText: String): CardSpoiler {
        this.rightText = rightText
        update()
        return this
    }

    fun setIconColor(iconColor: Int): CardSpoiler {
        this.iconColor = iconColor
        update()
        return this
    }

    fun setTextColor(textColor: Int): CardSpoiler {
        this.textColor = textColor
        update()
        return this
    }

    fun setTitleColor(titleColor: Int): CardSpoiler {
        this.titleColor = titleColor
        update()
        return this
    }

    fun setRightTextColor(rightTextColor: Int): CardSpoiler {
        this.rightTextColor = rightTextColor
        update()
        return this
    }

    fun setDividerVisible(dividerVisible: Boolean): CardSpoiler {
        this.dividerVisible = dividerVisible
        update()
        return this
    }
}
