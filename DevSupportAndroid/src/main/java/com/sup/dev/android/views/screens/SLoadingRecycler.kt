package com.sup.dev.android.views.screens

import android.support.annotation.DrawableRes
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.View
import android.view.ViewGroup
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.adapters.recycler_view.RecyclerCardAdapterLoading
import com.sup.dev.android.views.cards.Card
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.java.libs.api_simple.client.Request
import com.sup.dev.java.tools.ToolsThreads

abstract class SLoadingRecycler<C : Card, V>(res: Int = R.layout.screen_loading_recycler) : SLoading(res) {

    protected var textErrorRetry = SupAndroid.TEXT_APP_RETRY

    protected val vToolbar: Toolbar? = findViewById(R.id.vToolbar)
    protected val vToolbarIconsContainer: ViewGroup? = findViewById(R.id.vToolbarIconsContainer)
    protected val vRecycler: RecyclerView = findViewById(R.id.vRecycler)
    protected val vRefresh: SwipeRefreshLayout? = findViewById(R.id.vRefresh)
    protected val vScreenRoot: ViewGroup? = findViewById(R.id.vScreenRoot)

    protected var adapter: RecyclerCardAdapterLoading<C, V>? = null
    protected var subscription: Request<*>? = null

    protected val notifyCount: Int
        get() = 5

    init {
        textErrorNetwork = SupAndroid.TEXT_ERROR_NETWORK

        vRecycler.layoutManager = LinearLayoutManager(context)
        if (vRefresh != null)
            vRefresh.setOnRefreshListener {
                vRefresh.isRefreshing = false
                onReloadClicked()
            }

        val vFabX:FloatingActionButton? = findViewById(R.id.vFabX)
        if (vFabX != null) {
            if (vFab.parent is ViewGroup) (vFab.parent as ViewGroup).removeView(vFab)
            vFabX.id = R.id.vFab
            (vFabX as View).visibility = vFab.visibility
            vFab = vFabX
        }

        ToolsThreads.main(true) {
            adapter = instanceAdapter()
                    .addOnEmpty { setState(State.EMPTY) }
                    .addOnErrorAndEmpty { setState(State.ERROR) }
                    .addOnStartLoadingAndEmpty { setState(State.PROGRESS) }
                    .addOnLoadingAndNotEmpty { setState(State.NONE) }
                    .addOnLoadedNotEmpty { setState(State.NONE) }
                    .setRetryMessage(textErrorNetwork, textErrorRetry)
                    .setShowLoadingCardIfEmpty(false)
                    .setNotifyCount(5)


            prepareAdapter(adapter!!)
            setAdapter(adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)

            ToolsThreads.main(true) {
                reload()
            }
        }
    }

    open fun prepareAdapter(adapter: RecyclerCardAdapterLoading<C, V>) {

    }

    override fun onReloadClicked() {
        reload()
    }

    open fun reload() {
        if (subscription != null) subscription!!.unsubscribe()
        adapter!!.reloadBottom()
    }

    protected abstract fun instanceAdapter(): RecyclerCardAdapterLoading<C, V>

    protected fun addToolbarIcon(@DrawableRes res: Int, onClick: (View) -> Unit): ViewIcon {
        val viewIcon: ViewIcon = ToolsView.inflate(context, R.layout.z_icon)
        viewIcon.setImageResource(res)
        viewIcon.setOnClickListener { onClick.invoke(viewIcon) }
        if (vToolbarIconsContainer != null) vToolbarIconsContainer.addView(viewIcon)
        return viewIcon
    }

    protected fun addToolbarView(v: View) {
        vToolbar?.addView(v)
    }

    //
    //  Getters
    //

    fun getAdapterCards() = adapter

    //
    //  Setters
    //

    fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        vRecycler.adapter = adapter
    }


}
