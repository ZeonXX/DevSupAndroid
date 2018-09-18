package com.sup.dev.android.views.adapters.recycler_view

import android.support.annotation.StringRes
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.views.adapters.CardAdapter
import com.sup.dev.android.views.cards.Card
import com.sup.dev.android.views.cards.CardLoading
import com.sup.dev.java.tools.ToolsThreads
import java.util.ArrayList
import kotlin.reflect.KClass


class RecyclerCardAdapterLoading<K : Card, V>(private val cardClass: KClass<K>, private var mapper: ((V) -> K)?) : RecyclerCardAdapter(), CardAdapter {

    private var bottomLoader: (((Array<V?>) -> Unit, ArrayList<K>) -> Unit)? = null
    private var topLoader: (((Array<V?>) -> Unit, ArrayList<K>) -> Unit)? = null
    private val cardLoading: CardLoading = CardLoading()

    private var addBottomPositionOffset = 0
    private var addTopPositionOffset = 0
    private var startBottomLoadOffset = 0
    private var startTopLoadOffset = 0
    var isLockTop: Boolean = false
        private set
    //
    //  Getters
    //

    var isLockBottom: Boolean = false
        private set
    var isInProgress: Boolean = false
        private set
    private var retryEnabled: Boolean = false
    private var actionEnabled: Boolean = false
    private var onErrorAndEmpty: (() -> Unit)? = null
    private var onEmpty: (() -> Unit)? = null
    private var onStartLoadingAndEmpty: (() -> Unit)? = null
    private var onLoadingAndNotEmpty: (() -> Unit)? = null
    private var onLoadedNotEmpty: (() -> Unit)? = null

    override fun onBindViewHolder(holder: RecyclerCardAdapter.Holder, position: Int, payloads: List<Any>) {
        super.onBindViewHolder(holder, position, payloads)

        if (isInProgress) return

        if (!isLockBottom && position >= getItemCount() - 1 - startBottomLoadOffset) {
            isInProgress = true
            ToolsThreads.main(true) {
                loadNow(true)
                Unit
            }
        } else if (!isLockTop && topLoader != null && position <= startTopLoadOffset) {
            isInProgress = true
            ToolsThreads.main(true) {
                loadNow(false)
                Unit
            }
        }
    }

    fun loadTop() {
        load(false)
    }

    fun loadBottom() {
        load(true)
    }

    fun load(bottom: Boolean) {
        if (isInProgress) return
        isInProgress = true
        loadNow(bottom)
    }

    private fun loadNow(bottom: Boolean) {
        if (bottom)
            isLockBottom = false
        else
            isLockTop = false
        cardLoading.setOnRetry({ source -> load(bottom) })
        cardLoading.setState(CardLoading.State.LOADING)

        val cards = get(cardClass)

        if (!contains(cardClass)) {
            if (onStartLoadingAndEmpty != null)
                onStartLoadingAndEmpty!!.invoke()
            else if (!contains(cardLoading)) add(if (bottom) size() - addBottomPositionOffset else addTopPositionOffset, cardLoading)
        } else {
            if (onLoadingAndNotEmpty != null) onLoadingAndNotEmpty!!.invoke()
            if (!contains(cardLoading)) add(if (bottom) size() - addBottomPositionOffset else addTopPositionOffset, cardLoading)
        }

        if (bottom)
            bottomLoader!!.invoke({ result -> onLoaded(result, bottom) }, cards)
        else
            topLoader!!.invoke({ result -> onLoaded(result, bottom) }, cards)
    }

    private fun onLoaded(result: Array<V?>?, bottom: Boolean) {

        isInProgress = false

        if (result == null) {
            if (retryEnabled && (contains(cardClass) || onErrorAndEmpty == null))
                cardLoading.setState(CardLoading.State.RETRY)
            else
                remove(cardLoading)
            if (!contains(cardClass) && onErrorAndEmpty != null) onErrorAndEmpty!!.invoke()
            return
        }


        if (!contains(cardClass) && result.size == 0) {
            if (bottom)
                lockBottom()
            else
                lockTop()
            if (actionEnabled)
                cardLoading.setState(CardLoading.State.ACTION)
            else {
                remove(cardLoading)
                if (onEmpty != null) onEmpty!!.invoke()
            }
        } else {
            if (result.size == 0) {
                if (bottom)
                    lockBottom()
                else
                    lockTop()
            }
            remove(cardLoading)
        }

        for (i in result.indices)
            if (bottom)
                add(size() - addBottomPositionOffset, mapper!!.invoke(result[i]!!))
            else
                add(addTopPositionOffset + i, mapper!!.invoke(result[i]!!))

        if (contains(cardClass) || result.size != 0)
            if (onLoadedNotEmpty != null) onLoadedNotEmpty!!.invoke()

    }

    fun reloadTop() {
        reload(false)
    }

    fun reloadBottom() {
        reload(true)
    }

    fun reload(bottom: Boolean) {
        remove(cardClass)
        load(bottom)
    }


    fun lockBottom() {
        isLockBottom = true
    }

    fun lockTop() {
        isLockTop = true
    }

    fun unlockBottom() {
        isLockBottom = false
    }

    fun unlockTop() {
        isLockTop = false
    }

    override fun remove(position: Int) {
        super.remove(position)
        if (onEmpty != null && !contains(cardClass)) onEmpty!!.invoke()
    }

    //
    //  Setters
    //

    fun setAddBottomPositionOffset(i: Int): RecyclerCardAdapterLoading<K, V> {
        this.addBottomPositionOffset = i
        return this
    }

    fun setAddTopPositionOffset(i: Int): RecyclerCardAdapterLoading<K, V> {
        this.addTopPositionOffset = i
        return this
    }

    fun setOnLoadedNotEmpty(onLoadedNotEmpty: () -> Unit): RecyclerCardAdapterLoading<K, V> {
        this.onLoadedNotEmpty = onLoadedNotEmpty
        return this
    }

    fun setOnLoadingAndNotEmpty(onLoadingAndNotEmpty: () -> Unit): RecyclerCardAdapterLoading<K, V> {
        this.onLoadingAndNotEmpty = onLoadingAndNotEmpty
        return this
    }

    fun setOnStartLoadingAndEmpty(onStartLoadingAndEmpty: () -> Unit): RecyclerCardAdapterLoading<K, V> {
        this.onStartLoadingAndEmpty = onStartLoadingAndEmpty
        return this
    }

    fun setActionEnabled(actionEnabled: Boolean): RecyclerCardAdapterLoading<K, V> {
        this.actionEnabled = actionEnabled
        return this
    }

    fun setOnEmpty(onEmpty: () -> Unit): RecyclerCardAdapterLoading<K, V> {
        this.onEmpty = onEmpty
        return this
    }

    fun setOnErrorAndEmpty(onErrorAndEmpty: () -> Unit): RecyclerCardAdapterLoading<K, V> {
        this.onErrorAndEmpty = onErrorAndEmpty
        return this
    }

    fun setRetryMessage(@StringRes message: Int, @StringRes button: Int): RecyclerCardAdapterLoading<K, V> {
        return setRetryMessage(ToolsResources.getString(message), ToolsResources.getString(button))
    }

    fun setRetryMessage(message: String?, button: String?): RecyclerCardAdapterLoading<K, V> {
        retryEnabled = true
        cardLoading.setRetryMessage(message)
        cardLoading.setRetryButton(button, {})
        return this
    }


    fun setEmptyMessage(@StringRes message: Int): RecyclerCardAdapterLoading<K, V> {
        return setEmptyMessage(ToolsResources.getString(message))
    }

    fun setEmptyMessage(@StringRes message: Int, @StringRes button: Int, onAction: () -> Unit): RecyclerCardAdapterLoading<K, V> {
        return setEmptyMessage(ToolsResources.getString(message), ToolsResources.getString(button), onAction)
    }

    @JvmOverloads
    fun setEmptyMessage(message: String?, button: String? = null, onAction: () -> Unit = {}): RecyclerCardAdapterLoading<K, V> {
        actionEnabled = true
        cardLoading.setActionMessage(message)
        cardLoading.setActionButton(button) { card -> onAction.invoke() }
        return this
    }

    fun setTopLoader(topLoader: ((Array<V?>) -> Unit, ArrayList<K>) -> Unit): RecyclerCardAdapterLoading<K, V> {
        this.topLoader = topLoader
        return this
    }

    fun setBottomLoader(bottomLoader: ((Array<V?>) -> Unit, ArrayList<K>) -> Unit): RecyclerCardAdapterLoading<K, V> {
        this.bottomLoader = bottomLoader
        return this
    }

    fun setStartBottomLoadOffset(i: Int): RecyclerCardAdapterLoading<K, V> {
        this.startBottomLoadOffset = i
        return this
    }

    fun setTopBottomLoadOffset(i: Int): RecyclerCardAdapterLoading<K, V> {
        this.startTopLoadOffset = i
        return this
    }

    fun setMapper(mapper: (V) -> K): RecyclerCardAdapterLoading<K, V> {
        this.mapper = mapper
        return this
    }

    fun setCardLoadingType(type: CardLoading.Type): RecyclerCardAdapterLoading<K, V> {
        cardLoading.setType(type)
        return this
    }

    override fun setNotifyCount(notifyCount: Int): RecyclerCardAdapterLoading<K, V> {
        return super.setNotifyCount(notifyCount) as RecyclerCardAdapterLoading<K, V>
    }

}