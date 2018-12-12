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

class SNavigationRow(context: Context, @DrawableRes icon: Int, text: String?, onClickListener: (View) -> Unit) {

    val view: View?
    private val vChip: ViewChip

    constructor(context: Context, @DrawableRes icon: Int, @StringRes text: Int, onClickListener: (View) -> Unit) : this(context, icon, ToolsResources.getString(text), onClickListener) {}

    init {
        view = ToolsView.inflate(context, R.layout.screen_activity_navigation_row)
        vChip = view.findViewById(R.id.vNavigationRowChip)

        (view.findViewById(R.id.vNavigationRowIcon) as ImageView).setImageResource(icon)
        view.setOnClickListener(onClickListener)

        setChipVisible(false)
        (view.findViewById(R.id.vNavigationRowText) as TextView).setText(text)
    }

    fun setChipVisible(b: Boolean) {
        view!!.findViewById<View>(R.id.vNavigationRowChip).visibility = if (b) View.VISIBLE else View.GONE
    }
}
