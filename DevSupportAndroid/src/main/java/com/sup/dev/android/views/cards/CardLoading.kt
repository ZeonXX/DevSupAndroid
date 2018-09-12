package com.sup.dev.android.views.cards

import android.support.annotation.StringRes
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.java.tools.ToolsThreads


class CardLoading : Card() {

    private var dividerVisible = false
    private var enabled = true
    private var background = 0

    private var actionMessage: String? = null
    private var actionButton: String? = null
    private var onAction: ((CardLoading)->Unit)? = null
    private var retryMessage: String? = null
    private var retryButton: String? = null
    private var onRetry: ((CardLoading)->Unit)? = null
    private var state = State.LOADING
    private var type = Type.CIRCLE

    enum class State {
        LOADING, ACTION, RETRY
    }

    enum class Type {
        CIRCLE, HORIZONTAL, NONE
    }

    override fun getLayout(): Int {
        return R.layout.card_loading
    }

    override fun bindView(view: View) {
        val vDivider = view.findViewById<View>(R.id.divider)
        val vLoadingCircle = view.findViewById<View>(R.id.loading)
        val vLoadingHorizontal = view.findViewById<View>(R.id.loading_horizontal)
        val vContainer = view.findViewById<View>(R.id.vContainer)
        val vAction = view.findViewById<Button>(R.id.action)
        val vText = view.findViewById<TextView>(R.id.text)

        vDivider.visibility = if (dividerVisible) View.VISIBLE else View.GONE
        if (background != 0) view.setBackgroundColor(background)
        vAction.isEnabled = isEnabled()
        vText.isEnabled = isEnabled()

        if (state == State.LOADING) {
            vContainer.visibility = View.GONE

            if (type == Type.CIRCLE)
                ToolsThreads.main(1000) {
                    if (state == State.LOADING) ToolsView.alpha(vLoadingCircle, type != Type.CIRCLE)
                    Unit
                }
            else
                vLoadingCircle.visibility = View.GONE
            if (type == Type.HORIZONTAL)
                ToolsThreads.main(1000) {
                    if (state == State.LOADING) ToolsView.alpha(vLoadingHorizontal, type != Type.HORIZONTAL)
                    Unit
                }
            else
                vLoadingHorizontal.visibility = View.GONE

            vText.visibility = View.GONE
            vAction.visibility = View.GONE
            vText.text = ""
            vAction.text = ""
        }
        if (state == State.RETRY) {
            vContainer.visibility = View.VISIBLE
            ToolsView.toAlpha(vLoadingCircle)
            ToolsView.toAlpha(vLoadingHorizontal)
            vText.visibility = View.VISIBLE
            vAction.visibility = if (retryButton == null || retryButton!!.isEmpty()) View.GONE else View.VISIBLE
            vText.text = retryMessage
            vAction.text = retryButton
            vAction.setOnClickListener { v ->
                setState(State.LOADING)
                if (onRetry != null) onRetry!!.invoke(this)
            }
        }
        if (state == State.ACTION) {
            vContainer.visibility = View.VISIBLE
            ToolsView.toAlpha(vLoadingCircle)
            ToolsView.toAlpha(vLoadingHorizontal)
            vText.visibility = View.VISIBLE
            vAction.visibility = if (actionButton == null || actionButton!!.isEmpty()) View.GONE else View.VISIBLE
            vText.text = actionMessage
            vAction.text = actionButton
            vAction.setOnClickListener { v -> if (onAction != null) onAction!!.invoke(this) }
        }
    }

    //
    //  Setters
    //


    fun setDividerVisible(dividerVisible: Boolean): CardLoading {
        this.dividerVisible = dividerVisible
        update()
        return this
    }

    fun setEnabled(enabled: Boolean): CardLoading {
        this.enabled = enabled
        update()
        return this
    }

    fun setBackground(background: Int): CardLoading {
        this.background = background
        update()
        return this
    }

    fun setState(state: State): CardLoading {
        this.state = state
        update()
        return this
    }

    fun setActionMessage(@StringRes text: Int): CardLoading {
        return setActionMessage(ToolsResources.getString(text))
    }

    fun setActionMessage(text: String?): CardLoading {
        this.actionMessage = text
        update()
        return this
    }

    fun setActionButton(@StringRes text: Int, onAction: (CardLoading)->Unit): CardLoading {
        return setActionButton(ToolsResources.getString(text), onAction)
    }

    fun setActionButton(text: String?, onAction: (CardLoading)->Unit): CardLoading {
        actionButton = text
        this.onAction = onAction
        update()
        return this
    }

    fun setRetryMessage(@StringRes text: Int): CardLoading {
        return setRetryMessage(ToolsResources.getString(text))
    }

    fun setRetryMessage(text: String?): CardLoading {
        this.retryMessage = text
        update()
        return this
    }

    fun setRetryButton(@StringRes text: Int, onRetry: (CardLoading)->Unit): CardLoading {
        return setRetryButton(ToolsResources.getString(text), onRetry)
    }

    fun setRetryButton(text: String?, onRetry: (CardLoading)->Unit): CardLoading {
        retryButton = text
        this.onRetry = onRetry
        update()
        return this
    }

    fun setOnRetry(onRetry: (CardLoading)->Unit): CardLoading {
        this.onRetry = onRetry
        update()
        return this
    }

    fun setType(type: Type): CardLoading {
        this.type = type
        update()
        return this
    }

    //
    //  Getters
    //

    fun isEnabled(): Boolean {
        return enabled
    }
}
