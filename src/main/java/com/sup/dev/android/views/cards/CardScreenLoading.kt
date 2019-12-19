package com.sup.dev.android.views.cards

import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.image_loader.ImageLoader
import com.sup.dev.android.libs.image_loader.ImageLoaderA
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.java.tools.ToolsThreads

abstract class CardScreenLoading(@LayoutRes layoutRes: Int) : Card(0) {

    enum class State {
        NONE, EMPTY, PROGRESS, ERROR
    }

    protected val vRoot: View = ToolsView.inflate(R.layout.screen_loading)
    protected var vFab: FloatingActionButton = vRoot.findViewById(R.id.vFab)
    private val vAction: Button = vRoot.findViewById(R.id.vAction)
    private var vEmptySubContainer: ViewGroup? = null
    private val vMessage: TextView = vRoot.findViewById(R.id.vMessage)
    private val vProgress: ProgressBar = vRoot.findViewById(R.id.vProgress)
    private val vProgressLine: ProgressBar = vRoot.findViewById(R.id.vProgressLine)
    private val vEmptyImage: ImageView = vRoot.findViewById(R.id.vEmptyImage)
    private val vContainer: ViewGroup = vRoot.findViewById(R.id.vContainer)
    private val vMessageContainer: ViewGroup = vRoot.findViewById(R.id.vMessageContainer)
    private val vEmptyContainer: ViewGroup = vRoot.findViewById(R.id.vEmptyContainer)

    protected var textErrorNetwork = SupAndroid.TEXT_ERROR_NETWORK
    protected var textRetry = SupAndroid.TEXT_APP_RETRY
    protected var textEmptyS: String? = null
    protected var textProgressS: String? = null
    protected var image: ImageLoaderA? = null
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
        vProgressLine.visibility = View.INVISIBLE
        vEmptyImage.visibility = View.GONE

        setState(State.PROGRESS)
        setContent(layoutRes)
    }

    abstract fun onReloadClicked()

    override fun instanceView() = vRoot

    protected fun setContent(@LayoutRes res: Int) {
        vContainer.addView(ToolsView.inflate(vRoot.context, res), 0)
        vEmptySubContainer = vContainer.findViewById(R.id.vEmptySubContainer)

        if (vEmptySubContainer != null) {
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

    protected fun setTextEmpty(t: String?) {
        textEmptyS = t
    }

    protected fun setAction(@StringRes textAction: Int, onAction: () -> Unit) {
        setAction(ToolsResources.s(textAction), onAction)
    }

    protected fun clearAction() {
        this.textAction = null
        this.onAction = null
    }

    fun setTextProgress(@StringRes textProgress: Int) {
        setTextProgress(ToolsResources.s(textProgress))
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

    fun setBackgroundImage(image: Any) {
        setBackgroundImage(ImageLoader.load(image))
    }

    fun setBackgroundImage(image: ImageLoaderA?) {
        this.image = image
    }

    fun setState(state: State) {
        this.stateS = state

        if (state == State.PROGRESS) {
            ToolsThreads.main(600) {
                ToolsView.alpha(vProgress, this.stateS != State.PROGRESS || textProgressS != null)
                ToolsView.alpha(vProgressLine, this.stateS != State.PROGRESS || textProgressS == null)
                if (this.stateS == State.PROGRESS && vMessage.text.isNotEmpty()) {
                    ToolsView.fromAlpha(vMessage)
                }
            }
        } else {
            ToolsView.toAlpha(vProgress)
            ToolsView.toAlpha(vProgressLine)
        }

        if (image == null || state != State.EMPTY) {
            vEmptyImage.setImageBitmap(null)
            vEmptyImage.visibility = View.GONE
        } else {
            image?.noHolder()?.into(vEmptyImage)
            vEmptyImage.visibility = View.VISIBLE
        }

        if (state == State.ERROR) {
            vMessage.text = textErrorNetwork
            vAction.text = textRetry
            ToolsView.alpha(vMessage, vMessage.text.isEmpty())
            ToolsView.alpha(vAction, vAction.text.isEmpty())

            vAction.setOnClickListener { onReloadClicked() }
        }

        if (state == State.EMPTY) {
            vMessage.text = textEmptyS
            vAction.text = textAction
            ToolsView.alpha(vMessage, vMessage.text.isEmpty())
            ToolsView.alpha(vAction, vAction.text.isEmpty())

            vAction.setOnClickListener { if (onAction != null) onAction!!.invoke() }
        }

        if (state == State.PROGRESS) {
            vMessage.visibility = View.INVISIBLE
            vMessage.text = textProgressS
            vAction.text = textProgressAction
            vAction.setOnClickListener { if (onProgressAction != null) onProgressAction!!.invoke() }
        }

        if (state == State.NONE) {

            ToolsView.toAlpha(vMessage)
            ToolsView.toAlpha(vAction)
        }

    }

}
