package com.sup.dev.android.views.screens

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.NavigationAction
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsResources


class SAlert(title: String?, text: String?, action: String?, image: Int = 0, imageFul: Int = 0, onAction: (() -> Unit)?) : Screen(R.layout.screen_alert) {

    constructor(title: String?, text: String?, action: String?, onAction:  (() -> Unit)?) : this(title, text, action, 0, onAction) {}

    constructor(title: String?, text: String?, action: String?, image: Int, onAction:  (() -> Unit)?) : this(title, text, action, image, 0, onAction) {}

    private val vTitle:TextView = findViewById(R.id.vTitle)
    private val vText:TextView = findViewById(R.id.vText)
    private val vAction:TextView = findViewById(R.id.vAction)
    private val vImage:ImageView = findViewById(R.id.vImage)
    private val vImageFull:ImageView = findViewById(R.id.vImageFull)

    init {

        vTitle.text = title
        vText.text = text
        vAction.text = action

        if (image != 0) {
            vImage.setImageResource(image)
            vImage.visibility = View.VISIBLE
        } else {
            vImage.setImageBitmap(null)
            vImage.visibility = View.GONE
        }

        if (imageFul != 0) {
            vImageFull.setImageResource(imageFul)
            vImageFull.visibility = View.VISIBLE
        } else {
            vImageFull.setImageBitmap(null)
            vImageFull.visibility = View.GONE
        }

        vAction.setOnClickListener { v -> onAction?.invoke() }

    }

    companion object {

        fun showNetwork(action: NavigationAction, onRetry: () -> Unit) {
            Navigator.action(action, instanceNetwork(onRetry))
        }

        fun instanceNetwork(onRetry: () -> Unit): SAlert {
            return SAlert(
                    SupAndroid.TEXT_APP_WHOOPS,
                    SupAndroid.TEXT_ERROR_NETWORK,
                    SupAndroid.TEXT_APP_RETRY,
                    SupAndroid.IMG_ERROR_NETWORK,
                    onRetry)
        }

        fun showGone(action: NavigationAction) {
            Navigator.action(action, instanceGone())
        }

        fun instanceGone(): SAlert {
            return SAlert(
                    SupAndroid.TEXT_APP_WHOOPS,
                    SupAndroid.TEXT_ERROR_GONE,
                    SupAndroid.TEXT_APP_BACK,
                    SupAndroid.IMG_ERROR_GONE
            ) { Navigator.back() }
        }
    }

}