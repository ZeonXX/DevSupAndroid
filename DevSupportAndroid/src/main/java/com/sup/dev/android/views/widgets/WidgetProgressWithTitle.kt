package com.sup.dev.android.views.widgets

import android.support.annotation.StringRes
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView

class WidgetProgressWithTitle : Widget(R.layout.widget_progress_with_title) {

    private val vLoadTitle: TextView

    init {
        vLoadTitle = view.findViewById(R.id.load_title)
    }

    //
    //  Setters
    //


    override fun <K : Widget> setTitle(@StringRes title: Int): K {
        return setTitle(ToolsResources.getString(title))
    }

    override fun <K : Widget> setTitle(title: String?): K {
        ToolsView.setTextOrGone(vLoadTitle, title)
        return this as K
    }

}
