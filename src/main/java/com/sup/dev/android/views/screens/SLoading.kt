package com.sup.dev.android.views.screens

import androidx.annotation.DrawableRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.image_loader.ImageLoader
import com.sup.dev.android.libs.image_loader.ImageLink
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
    private val vProgressLine: ProgressBar = findViewById(R.id.vProgressLine)
    private val vEmptyImage: ImageView = findViewById(R.id.vEmptyImage)
    private val vContainer: ViewGroup = findViewById(R.id.vContainer)
    private val vMessageContainer: ViewGroup = findViewById(R.id.vMessageContainer)
    private val vEmptyContainer: ViewGroup = findViewById(R.id.vEmptyContainer)

    protected var textErrorNetwork = SupAndroid.TEXT_ERROR_NETWORK
    protected var textRetry = SupAndroid.TEXT_APP_RETRY
    protected var textEmptyS: String? = null
    protected var textProgressS: String? = null
    protected var image: ImageLink? = null
    protected var imageError: ImageLink? = SupAndroid.imgErrorNetwork
    protected var textProgressAction: String? = null
    protected var onProgressAction: (() -> Unit)? = null
    protected var textAction: String? = null
    protected var onAction: (() -> Unit)? = null
    protected var stateS: State = State.NONE
    private var lastStateRequestKey = 0L

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

    protected fun setContent(@LayoutRes res: Int) {
        vContainer.addView(ToolsView.inflate(context, res), 0)
        vAppBar = vContainer.findViewById(R.id.vAppBar)
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

    fun setBackgroundImage(@DrawableRes res: Int) {
        this.image = ImageLoader.load(res).noHolder()
    }

    fun setBackgroundImage(image: Any) {
        setBackgroundImage(ImageLoader.loadByAny(image)?.noHolder())
    }

    fun setBackgroundImage(image: ImageLink?) {
        this.image = image
    }

    override fun setTitle(@StringRes title: Int) {
        setTitle(ToolsResources.s(title))
    }

    override fun setTitle(title: String?) {
        (findViewById<View>(R.id.vToolbar) as Toolbar).title = title
    }

    fun setState(state: State) {
        val key = System.nanoTime()
        lastStateRequestKey = key
        if (state == State.EMPTY)
            ToolsThreads.main(200) { if (key == lastStateRequestKey) setStateNow(state) }//  Защита от мерцаний при частых вызовах
        else
            setStateNow(state)

    }

    private fun setStateNow(state: State) {
        this.stateS = state

        if (vAppBar != null && vEmptySubContainer == null) {
            (vMessageContainer.layoutParams as LayoutParams).topMargin = (vAppBar!!.height / 1.8f).toInt()
        }

        if (state == State.PROGRESS) {
            ToolsThreads.main(600) {
                vProgress.visibility = if (this.stateS != State.PROGRESS || textProgressS != null) View.GONE else View.VISIBLE
                vProgressLine.visibility = if (this.stateS != State.PROGRESS || textProgressS == null) View.GONE else View.VISIBLE
                if (this.stateS == State.PROGRESS && vMessage.text.isNotEmpty()) {
                    vMessage.visibility = View.VISIBLE
                }
            }
        } else {
            vProgress.visibility = View.GONE
            vProgressLine.visibility = View.GONE
        }

        if (state == State.EMPTY && image != null) {
            image?.into(vEmptyImage)
            vEmptyImage.visibility = View.VISIBLE
        } else if (state == State.ERROR && imageError != null) {
            imageError?.into(vEmptyImage)
            vEmptyImage.visibility = View.VISIBLE
        } else {
            vEmptyImage.setImageBitmap(null)
            vEmptyImage.visibility = View.GONE
        }

        if (state == State.ERROR) {
            vMessage.text = textErrorNetwork
            vAction.text = textRetry
            vMessage.visibility = if (vMessage.text.isEmpty()) View.GONE else View.VISIBLE
            vAction.visibility = if (vAction.text.isEmpty()) View.GONE else View.VISIBLE

            vAction.setOnClickListener { onReloadClicked() }
        }

        if (state == State.EMPTY) {
            vMessage.text = textEmptyS
            vAction.text = textAction
            vMessage.visibility = if (vMessage.text.isEmpty()) View.GONE else View.VISIBLE
            vAction.visibility = if (vAction.text.isEmpty()) View.GONE else View.VISIBLE

            vAction.setOnClickListener { if (onAction != null) onAction!!.invoke() }
        }

        if (state == State.PROGRESS) {
            vMessage.visibility = View.GONE
            vMessage.text = textProgressS
            vAction.text = textProgressAction
            vAction.setOnClickListener { if (onProgressAction != null) onProgressAction!!.invoke() }
        }

        if (state == State.NONE) {
            vMessage.visibility = View.GONE
            vAction.visibility = View.GONE
        }

    }


}
