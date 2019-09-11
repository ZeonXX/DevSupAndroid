package com.sup.dev.android.views.cards

import android.graphics.PorterDuff
import androidx.annotation.StringRes
import android.text.Html
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.support.adapters.CardAdapter
import java.util.ArrayList


open class CardSpoiler : Card(R.layout.card_spoiler) {

    //
    //  Getters
    //

    val cards = ArrayList<Card>()
    private var titleGravity = Gravity.LEFT
    private var title: String? = null
    private var titleExpanded: String? = null
    private var text: String? = null
    private var rightText: String? = null
    private var titleColor = 0
    private var rightTextColor = 0
    private var textColor = 0
    private var originalSeted = false
    private var dividerVisible = true
    private var titleColorOriginal = 0
    private var rightTextColorOriginal = 0
    private var textColorOriginal = 0
    private var iconColor = 0
    private var useExpandedArrow = true
    private var useExpandedTitleArrow = false

    internal var expanded = false
    internal var enabled = true

    @Suppress("DEPRECATION")
    override fun bindView(view: View) {
        super.bindView(view)
        val vIcon:ImageView = view.findViewById(R.id.vIcon)
        val vTitle:TextView = view.findViewById(R.id.vTitle)
        val vText:TextView = view.findViewById(R.id.vText)
        val vRightText:TextView = view.findViewById(R.id.vRightText)
        val vTouch:View = view.findViewById(R.id.vTouch)
        val vDivider:View = view.findViewById(R.id.vDivider)

        val iconDown = ToolsResources.getDrawableAttrId(R.attr.ic_keyboard_arrow_down_24dp)
        val iconUp = ToolsResources.getDrawableAttrId(R.attr.ic_keyboard_arrow_up_24dp)

        if (!originalSeted) {
            originalSeted = true
            titleColorOriginal = vTitle.currentTextColor
            textColorOriginal = vText.currentTextColor
            rightTextColorOriginal = vRightText.currentTextColor
        }

        vText.text = if (text == null) null else Html.fromHtml(text)
        vRightText.text = if (rightText == null) null else Html.fromHtml(rightText)

        if (expanded && titleExpanded != null)
            vTitle.text = Html.fromHtml(titleExpanded)
        else
            vTitle.text = if (title == null) null else Html.fromHtml(title)

        vText.visibility = if (text == null) View.GONE else View.VISIBLE
        vRightText.visibility = if (rightText == null) View.GONE else View.VISIBLE
        vTitle.visibility = if (title == null) View.GONE else View.VISIBLE
        vIcon.visibility = if (useExpandedArrow) View.VISIBLE else View.GONE



       if(useExpandedTitleArrow) vTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, if (expanded) iconUp else iconDown, 0)
       else vTitle.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0)


        vText.isEnabled = enabled
        vRightText.isEnabled = enabled
        vTitle.isEnabled = enabled

        (vTitle.layoutParams as LinearLayout.LayoutParams).gravity = titleGravity

        vDivider.visibility = if (dividerVisible) View.VISIBLE else View.GONE
        vText.setTextColor(if (textColor != 0) textColor else textColorOriginal)
        vRightText.setTextColor(if (rightTextColor != 0) rightTextColor else rightTextColorOriginal)
        vTitle.setTextColor(if (titleColor != 0) titleColor else titleColorOriginal)

        vIcon.setImageResource(if (expanded) iconUp else iconDown)
        vIcon.setAlpha(if (enabled) 255 else 106)
        if (iconColor != 0) vIcon.setColorFilter(iconColor, PorterDuff.Mode.SRC_ATOP)
        if (enabled) vTouch.setOnClickListener { setExpanded(!expanded) }
        else vTouch.setOnClickListener(null)
        vTouch.isClickable = enabled
    }

    //
    //  Setters
    //

    fun setUseExpandedArrow(useExpandedArrow:Boolean): CardSpoiler {
        this.useExpandedArrow = useExpandedArrow
        update()
        return this
    }

    fun setUseExpandedTitleArrow(useExpandedArrow:Boolean): CardSpoiler {
        this.useExpandedTitleArrow = useExpandedArrow
        update()
        return this
    }

    fun add(card: Card): CardSpoiler {
        cards.add(card)
        setExpanded(expanded)
        return this
    }

    fun remove(card: Card): CardSpoiler {
        cards.remove(card)
        adapter?.remove(card)
        return this
    }

    open fun setTitle(@StringRes title: Int): CardSpoiler {
        return setTitle(ToolsResources.s(title))
    }

    fun setTitle(title: String?): CardSpoiler {
        this.title = title
        update()
        return this
    }

    open fun setTitleExpanded(@StringRes title: Int): CardSpoiler {
        return setTitleExpanded(ToolsResources.s(title))
    }

    fun setTitleExpanded(title: String?): CardSpoiler {
        this.titleExpanded = title
        update()
        return this
    }

    fun setTitleGravity(gravity: Int): CardSpoiler {
        this.titleGravity = gravity
        update()
        return this
    }

    override fun setCardAdapter(adapter: CardAdapter?) {
        super.adapter = adapter
        setExpanded(expanded)
    }

    open fun onExpandedClicked(expanded: Boolean){

    }

    fun setExpanded(expanded: Boolean): CardSpoiler {
        this.expanded = expanded
        onExpandedClicked(expanded)
        update()

        if (adapter != null) {
            if (expanded) {

                var myIndex = adapter!!.indexOf(this)
                for (c in cards)
                    if (myIndex != -1) {
                        ++myIndex
                        if (!adapter!!.contains(c)) adapter!!.add(myIndex, c)
                    }

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
