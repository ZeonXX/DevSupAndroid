package com.sup.dev.android.views.screens

import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.widgets.Widget
import com.sup.dev.android.views.widgets.WidgetViewWrapper

class SWidget(private val widget: Widget) : Screen(R.layout.screen_widget), WidgetViewWrapper {
    private val vContainer: ViewGroup = findViewById(R.id.container)

    init {
        vContainer.addView(ToolsView.removeFromParent(widget.view))
    }

    override fun setTitle(title: String?) {
        (findViewById<View>(R.id.toolbar) as Toolbar).title = title
    }

    override fun onResume() {
        super.onResume()
        widget.onShow()
    }

    override fun onDestroy() {
        super.onDestroy()
        widget.onHide()
    }

    override fun <K : WidgetViewWrapper> hideWidget(): K {
        Navigator.remove(this)
        return this as K
    }

    override fun <K : WidgetViewWrapper> setWidgetCancelable(cancelable: Boolean): K {
        return this as K
    }

    override fun <K : WidgetViewWrapper> setWidgetEnabled(enabled: Boolean): K {
        return this as K
    }
}
