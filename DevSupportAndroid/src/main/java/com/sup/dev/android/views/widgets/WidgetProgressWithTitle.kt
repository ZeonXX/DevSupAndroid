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

}
