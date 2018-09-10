package com.sup.dev.android.libs.screens.activity

import android.content.Context
import android.support.annotation.DrawableRes
import android.support.annotation.StringRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.ViewChip

class SNavigationRow(context: Context, @DrawableRes icon: Int, text: String?, onClickListener: View.OnClickListener) {

    val view: View?
    private val vChip: ViewChip

    constructor(context: Context, @DrawableRes icon: Int, @StringRes text: Int, onClickListener: View.OnClickListener) : this(context, icon, ToolsResources.getString(text), onClickListener) {}

    init {
        view = ToolsView.inflate(context, R.layout.screen_activity_navigation_row)
        vChip = view.findViewById(R.id.navigation_row_chip)

        (view.findViewById(R.id.navigation_row_icon) as ImageView).setImageResource(icon)
        view.setOnClickListener(onClickListener)

        setChipVisible(false)
        (view.findViewById(R.id.navigation_row_text) as TextView).setText(text)
    }

    fun setChip(text: String) {
        vChip.text = text
    }

    fun setChipVisible(b: Boolean) {
        view!!.findViewById<View>(R.id.navigation_row_chip).visibility = if (b) View.VISIBLE else View.GONE
    }
}
