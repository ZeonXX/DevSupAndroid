package com.sup.dev.android.libs.screens.activity

import android.graphics.drawable.Drawable
import android.view.View
import com.sup.dev.android.R

class SActivityTypeSimple(
        activity:SActivity
) : SActivityType(activity) {

    override fun addNavigationItem(icon: Drawable, text: String, hided: Boolean, onClick: (View) -> Unit): NavigationItem {
        return NavigationItem()
    }

    override fun getLayout() = R.layout.screen_activity

    override fun onCreate() {
    }

    inner class NavigationItem : SActivityType.NavigationItem() {


        override fun setVisible(visible: Boolean) {

        }

    }

}