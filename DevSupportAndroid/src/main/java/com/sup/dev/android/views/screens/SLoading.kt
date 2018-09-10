package com.sup.dev.android.views.screens

import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.java.tools.ToolsThreads


abstract class SLoading(@LayoutRes layoutRes: Int) : Screen(R.layout.screen_loading) {

    enum class State {
        NONE, EMPTY, PROGRESS, ERROR
    }

    protected val vContainer: ViewGroup
    protected val vToolbarContainer: ViewGroup
    protected val vToolbar: Toolbar
    protected val vMessage: TextView
    protected val vAction: TextView
    protected val vProgress: View
    protected val vEmptyImage: ImageView
    protected val vFab: FloatingActionButton

    protected var textErrorNetwork = SupAndroid.TEXT_ERROR_NETWORK
    protected var textRetry = SupAndroid.TEXT_APP_RETRY
    protected var textEmptyS: String? = null
    protected var textProgressS: String? = null
    protected var textProgressAction: String? = null
    protected var onProgressAction: (() -> Unit)? = null
    protected var textAction: String? = null
    protected var onAction: (() -> Unit)? = null
    protected var stateS: State = State.NONE



    init {

        vContainer = findViewById(R.id.container)
        vToolbarContainer = findViewById(R.id.toolbar_icons_container)
        vToolbar = findViewById(R.id.toolbar)
        vMessage = findViewById(R.id.message)
        vAction = findViewById(R.id.action)
        vProgress = findViewById(R.id.progress)
        vEmptyImage = findViewById(R.id.empty_image)
        vFab = findViewById(R.id.fab)

        vAction.visibility = View.INVISIBLE
        vMessage.visibility = View.INVISIBLE
        vProgress.visibility = View.INVISIBLE
        vFab.visibility = View.GONE
        vEmptyImage.setImageDrawable(null)

        setState(State.PROGRESS)
        setContent(layoutRes)
    }

    abstract fun onReloadClicked()

    protected fun setContent(@LayoutRes res: Int) {
        while (vContainer.childCount != 2) vContainer.removeViewAt(0)
        vContainer.addView(ToolsView.inflate(context, res), 0)
    }

    protected fun addToolbarIcon(@DrawableRes res: Int, onClick: (View) -> Unit): ViewIcon {
        val viewIcon = ToolsView.inflate(context, R.layout.view_icon)
        viewIcon.setImageResource(res)
        viewIcon.setOnClickListener(OnClickListener { onClick.invoke(viewIcon) })
        vToolbarContainer.addView(viewIcon)
        return viewIcon
    }

    protected fun addToolbarView(v: View) {
        vToolbar.addView(v)
    }

    protected fun setTextErrorNetwork(@StringRes t: Int) {
        textErrorNetwork = ToolsResources.getString(t)
    }

    protected fun setTextRetry(@StringRes t: Int) {
        textRetry = ToolsResources.getString(t)
    }

    protected fun setTextEmpty(@StringRes t: Int) {
        setTextEmpty(ToolsResources.getString(t))
    }

    protected fun setAction(@StringRes textAction: Int, onAction: () -> Unit) {
        setAction(ToolsResources.getString(textAction), onAction)
    }

    fun setTextProgress(textProgress: String) {
        this.textProgressS = textProgress
    }

    fun setProgressAction(textProgressAction: String, onAction: () -> Unit) {
        this.textProgressAction = textProgressAction
        this.onProgressAction = onAction
    }

    fun setProgressAction(textProgressAction: String) {
        this.textProgressAction = textProgressAction
    }

    protected fun setAction(textAction: String?, onAction: () -> Unit) {
        this.textAction = textAction
        this.onAction = onAction
    }

    protected fun setTextEmpty(t: String?) {
        textEmptyS = t
    }

    fun setBackgroundImage(@DrawableRes res: Int) {
        vEmptyImage.setImageResource(res)
    }

    override fun setTitle(@StringRes title: Int) {
        setTitle(ToolsResources.getString(title))
    }

    override fun setTitle(title: String?) {
        (findViewById<View>(R.id.toolbar) as Toolbar).title = title
    }

    fun setState(state: State) {
        this.stateS = state

        if (state == State.PROGRESS) {
            ToolsThreads.main(600) {
                ToolsView.alpha(vProgress, this.stateS != State.PROGRESS)
                if (this.stateS == State.PROGRESS && vMessage.text.length > 0) ToolsView.fromAlpha(vMessage)
            }
        } else
            ToolsView.toAlpha(vProgress)

        if (vEmptyImage.drawable == null)
            vEmptyImage.visibility = View.GONE
        else
            ToolsView.alpha(vEmptyImage, state == State.NONE)

        if (state == State.ERROR) {

            ToolsView.setTextOrGone(vMessage, textErrorNetwork!!)
            ToolsView.setTextOrGone(vAction, textRetry!!)
            vAction.setOnClickListener { v -> onReloadClicked() }
        }

        if (state == State.EMPTY) {

            ToolsView.setTextOrGone(vMessage, textEmptyS!!)
            ToolsView.setTextOrGone(vAction, textAction!!)
            vAction.setOnClickListener { v -> if (onAction != null) onAction!!.invoke() }
        }

        if (state == State.PROGRESS) {

            vMessage.visibility = View.GONE
            vMessage.text = textProgressS
            ToolsView.setTextOrGone(vAction, textProgressAction)
            vAction.setOnClickListener { v -> if (onProgressAction != null) onProgressAction!!.invoke() }
        }

        if (state == State.NONE) {

            ToolsView.toAlpha(vMessage)
            ToolsView.toAlpha(vAction)
        }

    }


}
