package com.sup.dev.android.libs.screens.activity

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.ViewChipMini
import com.sup.dev.android.views.views.ViewIcon


abstract class SActivityType(
        val activity: SActivity
) {

    abstract fun onCreate()

    abstract fun onSetScreen(screen: Screen?)

    abstract fun getLayout(): Int

    open fun onViewBackPressed(){
        activity.onBackPressed()
    }

    open fun getNavigationDrawable(screen: Screen): Drawable? {
        if (Navigator.hasBackStack()) {
            val drawableAttr = ToolsResources.getDrawableAttr(screen.toolbarNavigationIcon)
            if (drawableAttr != null) return drawableAttr else return ToolsResources.getDrawable(screen.toolbarNavigationIcon)
        } else {
            return null
        }
    }

    fun addNavigationItem(icon: Int, text: Int, hided: Boolean, onClick: (View) -> Unit): NavigationItem? {
       return addNavigationItem(icon, ToolsResources.s(text), hided, onClick)
    }


    open fun addNavigationDivider(){

    }

    open fun addNavigationItem(icon: Int, text: String, hided: Boolean, onClick: (View) -> Unit): NavigationItem? {
        if(getNavigationItemLayout() == 0) return null
        val view = NavigationItem()
        view.vIcon.setImageResource(icon)
        view.view.setOnClickListener(onClick)
        view.vChip.visibility = View.GONE
        view.vText?.setText(text)
        addNavigationView(view.view)
        return view
    }

    open fun getNavigationItemLayout() = 0

    open fun addNavigationView(view:View){

    }

    //
    //  Navigation Item
    //

    inner class NavigationItem {

        val view: View = ToolsView.inflate(activity, getNavigationItemLayout())
        val vIcon: ViewIcon = view.findViewById(R.id.vNavigationItemIcon)
        val vChip: ViewChipMini = view.findViewById(R.id.vNavigationItemChip)
        val vText: TextView? = view.findViewById(R.id.vNavigationItemText)

    }
}