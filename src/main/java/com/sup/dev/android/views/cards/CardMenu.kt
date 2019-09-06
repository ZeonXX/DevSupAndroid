package com.sup.dev.android.views.cards

import android.graphics.drawable.Drawable
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import android.view.View
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.ViewIcon


open class CardMenu(
    layout : Int = 0
) : Card(if(layout > 0) layout else R.layout.card_menu) {

    var onClick: ((View, Int, Int)->Unit)? = null
    var dividerVisible = false
    var enabled = true
    var background = 0
    var visible = true

    var text = ""
    var description = ""
    var customColor = false
    var textColor = 0
    var icon = 0
    var iconDrawable:Drawable? = null

    override fun bindView(view: View) {
        super.bindView(view)
        val vTouch = view.findViewById<View>(R.id.vTouch)
        val vDivider = view.findViewById<View>(R.id.vDivider)
        val vText = view.findViewById<TextView>(R.id.vText)
        val vDescription = view.findViewById<TextView>(R.id.vDesc)
        val vIcon = view.findViewById<ViewIcon>(R.id.vIcon)

        vTouch.visibility = if(visible) View.VISIBLE else View.GONE

        if(iconDrawable != null) vIcon.setImageDrawable(iconDrawable)
        else if (icon == 0) vIcon.visibility = View.GONE
        else vIcon.setImageResource(icon)

        vDivider.visibility = if (visible && dividerVisible) View.VISIBLE else View.GONE
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

    fun setVisible(visible:Boolean):CardMenu{
        this.visible = visible
        update()
        return this
    }

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

    fun setIcon(icon: Drawable?): CardMenu {
        this.iconDrawable = icon
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
        return setDescription(ToolsResources.s(desc))
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