package com.sup.dev.android.views.screens

import android.view.View
import android.widget.ProgressBar
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.java.tools.ToolsThreads

class SInterstitialProgress constructor(startNow: Boolean = false) : Screen(R.layout.screen_interstitial_progress) {

    private val vProgress: ProgressBar

    init {

        vProgress = findViewById(R.id.progress)
        isBackStackAllowed = false

        if (!startNow)
            vProgress.visibility = View.INVISIBLE
        ToolsThreads.main(1000) { vProgress.visibility = View.VISIBLE }
    }
}
