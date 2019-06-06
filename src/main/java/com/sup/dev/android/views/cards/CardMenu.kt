package com.sup.dev.android.views.cards

import android.support.annotation.ColorRes
import android.support.annotation.StringRes
import android.view.View
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.ViewIcon


open class CardMenu : Card() {

    var onClick: ((View, Int, Int)->Unit)? = null
    var dividerVisible = false
    var enabled = true
    var background: Int = 0

    var text: String = ""
    var description: String = ""
    var customColor: Boolean = false
    var textColor: Int = 0
    var icon: Int = 0

    override fun getLayout(): Int {
        return R.layout.card_menu
    }

    override fun bindView(view: View) {
        super.bindView(view)
        val vTouch = view.findViewById<View>(R.id.vTouch)
        val vDivider = view.findViewById<View>(R.id.vDivider)
        val vText = view.findViewById<TextView>(R.id.vText)
        val vDescription = view.findViewById<TextView>(R.id.vDesc)
        val vIcon = view.findViewById<ViewIcon>(R.id.vIcon)

        if (icon == 0)
            vIcon.visibility = View.GONE
        else
            vIcon.setImageResource(icon)

        vDivider.visibility = if (dividerVisible) View.VISIBLE else View.GONE
        vTouch.isFocusable = onClick != null && enabled
        vTouch.isClickable = onClick != null && enabled
        vTouch.isEnabled = onClick != null && enabled
        ToolsView.setOnClickCoordinates(vTouch) { v, x, y -> if (enabled && onClick != null) onClick!!.invoke(v, x, y) }
        view.setBackgroundColor(background)

        vDescription.text = description
        vDescription.isEnabled = enabled
        vDescription.visibility = if (description.isNotEmpty()) View.VISIBLE else View.GONE
        vText.text = text
        vText.isEnabled = enabled
        if (customColor) vText.setTextColor(textColor)
    }

    //
    //  Setters
    //

    fun setOnClick(onClick: (View, Int, Int) -> Unit): CardMenu {
        this.onClick = onClick
        update()
        return this
    }

    fun setDividerVisible(dividerVisible: Boolean): CardMenu {
        this.dividerVisible = dividerVisible
        update()
        return this
    }

    fun setEnabled(enabled: Boolean): CardMenu {
        this.enabled = enabled
        update()
        return this
    }

    fun setIcon(icon: Int): CardMenu {
        this.icon = icon
        update()
        return this
    }

    fun setBackgroundRes(@ColorRes background: Int): CardMenu {
        return setBackground(ToolsResources.getColor(background))
    }

    fun setBackground(background: Int): CardMenu {
        this.background = background
        update()
        return this
    }

    fun setText(@StringRes text: Int): CardMenu {
        return setText(ToolsResources.s(text))
    }

    fun setText(text: String?): CardMenu {
        this.text = text?:""
        update()
        return this
    }

    fun setDescription(@StringRes desc: Int): CardMenu {
        return setDescription(ToolsResources.s(text))
    }

    fun setDescription(desc: String?): CardMenu {
        this.description = desc?:""
        update()
        return this
    }

    fun setTextColor(color: Int): CardMenu {
        customColor = true
        textColor = color
        update()
        return this
    }

}