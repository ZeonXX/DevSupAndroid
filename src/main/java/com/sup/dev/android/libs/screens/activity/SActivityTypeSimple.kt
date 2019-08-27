package com.sup.dev.android.libs.screens.activity

import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen

class SActivityTypeSimple(
        activity:SActivity
) : SActivityType(activity) {

    override fun getLayout() = R.layout.screen_activity

    override fun onCreate() {
    }

    override fun onSetScreen(screen: Screen?) {
    }

}