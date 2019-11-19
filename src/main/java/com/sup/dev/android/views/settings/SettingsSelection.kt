package com.sup.dev.android.views.settings

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.widgets.WidgetMenu

class SettingsSelection constructor(context: Context, attrs: AttributeSet? = null) : Settings(context, attrs) {

    private val menu: WidgetMenu = WidgetMenu()
    private var currentIndex = 0
    private val list = ArrayList<String>()
    private var onSelected: (Int) -> Unit = {}
    private val vArrow: ImageView = ImageView(context)

    init {
        view.setOnClickListener { menu.asSheetShow() }
        vArrow.setImageDrawable(ToolsResources.getDrawableAttr(R.attr.ic_keyboard_arrow_down_24dp))

        setSubView(vArrow)
    }

    fun add(v: Int, onItemSelected: (Int) -> Unit = {}) {
        add(ToolsResources.s(v), onItemSelected)
    }

    fun add(v: String, onItemSelected: (Int) -> Unit = {}) {
        list.add(v)
        menu.add(v) { _, _ ->
            currentIndex = list.indexOf(v)
            setSubtitle(v)
            onItemSelected.invoke(currentIndex)
            onSelected.invoke(currentIndex)
        }
    }

    fun getCurrentIndex() = currentIndex

    fun setCurrentIndex(index: Int) {
        currentIndex = index
        setSubtitle(list[index])
    }

    fun getTitles() = list

    fun onSelected(onSelected: (Int) -> Unit) {
        this.onSelected = onSelected
    }

}
