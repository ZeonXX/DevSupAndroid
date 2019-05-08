package com.sup.dev.android.views.screens

import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.annotation.StringRes
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.java.tools.ToolsThreads


abstract class SLoading(@LayoutRes layoutRes: Int) : Screen(R.layout.screen_loading) {

    enum class State {
        NONE, EMPTY, PROGRESS, ERROR
    }

    protected var vFab: FloatingActionButton = findViewById(R.id.vFab)
    private val vAction: Button = findViewById(R.id.vAction)
    private var vAppBar: View? = null
    private var vEmptySubContainer: ViewGroup? = null
    private val vMessage: TextView = findViewById(R.id.vMessage)
    private val vProgress: ProgressBar = findViewById(R.id.vProgress)
    private val vEmptyImage: ImageView = findViewById(R.id.vEmptyImage)
    private val vContainer: ViewGroup = findViewById(R.id.vContainer)
    private val vMessageContainer: ViewGroup = findViewById(R.id.vMessageContainer)
    private val vEmptyContainer: ViewGroup = findViewById(R.id.vEmptyContainer)

    protected var textErrorNetwork = SupAndroid.TEXT_ERROR_NETWORK
    protected var textRetry = SupAndroid.TEXT_APP_RETRY
    protected var textEmptyS: String? = null
    protected var textProgressS: String? = null
    protected var image: Int? = null
    protected var textProgressAction: String? = null
    protected var onProgressAction: (() -> Unit)? = null
    protected var textAction: String? = null
    protected var onAction: (() -> Unit)? = null
    protected var stateS: State = State.NONE

    init {

        (vFab as View).visibility = View.GONE
        vAction.visibility = View.INVISIBLE
        vMessage.visibility = View.INVISIBLE
        vProgress.visibility = View.INVISIBLE
        vEmptyImage.visibility = View.GONE

        setState(State.PROGRESS)
        setContent(layoutRes)
    }

    abstract fun onReloadClicked()

    protected fun setContent(@LayoutRes res: Int) {
        vContainer.addView(ToolsView.inflate(context, res), 0)
        vAppBar = vContainer.findViewById(R.id.vAppBar)
        vEmptySubContainer = vContainer.findViewById(R.id.vEmptySubContainer)

        if(vEmptySubContainer != null) {
            (vEmptyContainer.parent as ViewGroup).removeView(vEmptyContainer)
            vEmptySubContainer!!.addView(vEmptyContainer)
        }
    }

    protected fun setTextErrorNetwork(@StringRes t: Int) {
        textErrorNetwork = ToolsResources.s(t)
    }

    protected fun setTextRetry(@StringRes t: Int) {
        textRetry = ToolsResources.s(t)
    }

    protected fun setTextEmpty(@StringRes t: Int) {
        setTextEmpty(ToolsResources.s(t))
    }

    protected fun setAction(@StringRes textAction: Int, onAction: () -> Unit) {
        setAction(ToolsResources.s(textAction), onAction)
    }

    protected fun clearAction() {
        this.textAction = null
        this.onAction = null
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
        this.image = res
    }

    override fun setTitle(@StringRes title: Int) {
        setTitle(ToolsResources.s(title))
    }

    override fun setTitle(title: String?) {
        (findViewById<View>(R.id.vToolbar) as Toolbar).title = title
    }

    fun setState(state: State) {
        this.stateS = state

        if (vAppBar != null && vEmptySubContainer == null) {
            (vMessageContainer.layoutParams as FrameLayout.LayoutParams).topMargin = (vAppBar!!.height / 1.8f).toInt()
        }

        if (state == State.PROGRESS) {
            ToolsThreads.main(600) {
                ToolsView.alpha(vProgress, this.stateS != State.PROGRESS)
                if (this.stateS == State.PROGRESS && vMessage.text.isNotEmpty()) ToolsView.fromAlpha(vMessage)
            }
        } else
            ToolsView.toAlpha(vProgress)

        if (image == null || state != State.EMPTY) {
            vEmptyImage.setImageBitmap(null)
            vEmptyImage.visibility = View.GONE
        } else {
            vEmptyImage.setImageResource(image!!)
            ToolsView.alpha(vEmptyImage, false)
        }

        if (state == State.ERROR) {

            ToolsView.setTextOrGone(vMessage, textErrorNetwork)
            ToolsView.setTextOrGone(vAction, textRetry)
            vAction.setOnClickListener { v -> onReloadClicked() }
        }

        if (state == State.EMPTY) {

            ToolsView.setTextOrGone(vMessage, textEmptyS)
            ToolsView.setTextOrGone(vAction, textAction)
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
