package com.sup.dev.android.views.screens

import android.support.annotation.DrawableRes
import android.support.design.widget.AppBarLayout
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.support.adapters.recycler_view.RecyclerCardAdapterLoading
import com.sup.dev.android.views.cards.Card
import com.sup.dev.android.views.views.ViewIcon
import com.sup.dev.java.libs.api_simple.client.Request
import com.sup.dev.java.tools.ToolsThreads
import kotlinx.android.synthetic.main.screen_loading_recycler.view.*

abstract class SLoadingRecycler<C : Card, V> constructor(res: Int = R.layout.screen_loading_recycler) : SLoading(res) {

    protected var textErrorRetry = SupAndroid.TEXT_APP_RETRY

    protected val vRecycler: RecyclerView
    protected val vRefresh: SwipeRefreshLayout?

    protected var adapter: RecyclerCardAdapterLoading<C, V>? = null
    protected var subscription: Request<*>? = null

    protected val notifyCount: Int
        get() = 5

    init {
        textErrorNetwork = SupAndroid.TEXT_ERROR_NETWORK

        vRecycler = findViewById(R.id.vRecycler)
        vRefresh = findViewById(R.id.vRefresh)

        vRecycler.layoutManager = LinearLayoutManager(context)
        if (vRefresh != null)
            vRefresh.setOnRefreshListener {
                vRefresh.isRefreshing = false
                onReloadClicked()
            }

        ToolsThreads.main(true) {
            adapter = instanceAdapter()
                    .setOnEmpty { setState(SLoading.State.EMPTY) }
                    .setOnErrorAndEmpty { setState(SLoading.State.ERROR) }
                    .setOnStartLoadingAndEmpty { setState(SLoading.State.PROGRESS) }
                    .setOnLoadingAndNotEmpty { setState(SLoading.State.NONE) }
                    .setOnLoadedNotEmpty { setState(SLoading.State.NONE) }
                    .setRetryMessage(textErrorNetwork, textErrorRetry)
                    .setNotifyCount(5)

            setAdapter(adapter as RecyclerView.Adapter<RecyclerView.ViewHolder>)
            ToolsThreads.main(true) { reload() }
        }
    }

    override fun onReloadClicked() {
        reload()
    }

    private fun reload() {
        if (subscription != null) subscription!!.unsubscribe()
        adapter!!.reloadBottom()
    }

    protected fun enableToolbarHide(){
      (vToolbar.layoutParams as AppBarLayout.LayoutParams).scrollFlags = AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL or AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS
    }

    protected abstract fun instanceAdapter(): RecyclerCardAdapterLoading<C, V>

    protected fun addToolbarIcon(@DrawableRes res: Int, onClick: (View) -> Unit): ViewIcon {
        val viewIcon: ViewIcon = ToolsView.inflate(context, R.layout.view_icon)
        viewIcon.setImageResource(res)
        viewIcon.setOnClickListener { onClick.invoke(viewIcon) }
        vToolbarIconsContainer.addView(viewIcon)
        return viewIcon
    }

    protected fun addToolbarView(v: View) {
        vToolbar.addView(v)
    }

    //
    //  Setters
    //

    fun setAdapter(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>) {
        vRecycler.adapter = adapter
    }


}
