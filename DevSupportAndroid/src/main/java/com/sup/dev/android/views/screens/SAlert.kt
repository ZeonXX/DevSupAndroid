package com.sup.dev.android.views.screens

import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.NavigationAction
import com.sup.dev.android.libs.screens.navigator.Navigator
import kotlinx.android.synthetic.main.screen_alert.view.*


class SAlert(title: String?, text: String?, action: String?, image: Int? = null, imageFul: Int? = null, onAction: (() -> Unit)?) : Screen(R.layout.screen_alert) {

    constructor(title: String?, text: String?, action: String?, onAction:  (() -> Unit)?) : this(title, text, action, 0, onAction) {}

    constructor(title: String?, text: String?, action: String?, image: Int?, onAction:  (() -> Unit)?) : this(title, text, action, image, 0, onAction) {}

    init {

        vTitle.text = title
        vText.text = text
        vAction.setText(action)

        if (image != null) {
            vImage.setImageResource(image)
            vImage.visibility = View.VISIBLE
        } else {
            vImage.setImageBitmap(null)
            vImage.visibility = View.GONE
        }

        if (imageFul != null) {
            vImageFull.setImageResource(imageFul)
            vImageFull.visibility = View.VISIBLE
        } else {
            vImageFull.setImageBitmap(null)
            vImageFull.visibility = View.GONE
        }

        vAction.setOnClickListener { v -> if (onAction != null) onAction.invoke() }

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
                    SupAndroid.IMG_ERROR_GONE,
                    { Navigator.back() })
        }
    }

}