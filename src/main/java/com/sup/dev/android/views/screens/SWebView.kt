package com.sup.dev.android.views.screens

import android.webkit.WebView
import com.sup.dev.android.R
import com.sup.dev.android.libs.screens.Screen

class SWebView() : Screen(R.layout.screen_web_view) {

    protected val vWebView: WebView = findViewById(R.id.vWebView)

    constructor(data:String):this(){
        vWebView.settings.javaScriptEnabled = true
        vWebView.loadData(data, "text/html", "UTF-8")
    }

    init {
        isNavigationVisible = false
        isNavigationAllowed = false
        isNavigationAnimation = false
        isBackStackAllowed = false


    }

}
