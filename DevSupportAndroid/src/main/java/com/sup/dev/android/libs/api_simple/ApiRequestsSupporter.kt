package com.sup.dev.android.libs.api_simple

import android.support.annotation.StringRes
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.NavigationAction
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.android.tools.ToolsToast
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.screens.SAlert
import com.sup.dev.android.views.screens.SInterstitialProgress
import com.sup.dev.android.views.widgets.Widget
import com.sup.dev.android.views.widgets.WidgetAlert
import com.sup.dev.android.views.widgets.WidgetProgressTransparent
import com.sup.dev.android.views.widgets.WidgetProgressWithTitle
import com.sup.dev.java.classes.callbacks.simple.Callback1
import com.sup.dev.java.classes.callbacks.simple.Callback1Stub
import com.sup.dev.java.classes.callbacks.simple.CallbackStub
import com.sup.dev.java.classes.providers.Provider1
import com.sup.dev.java.libs.api_simple.client.ApiClient
import com.sup.dev.java.libs.api_simple.client.Request
import com.sup.dev.java.tools.ToolsDate

object ApiRequestsSupporter {


    private var api: ApiClient? = null

    fun init(api: ApiClient) {
        ApiRequestsSupporter.api = api
    }


    fun <K : Request.Response> executeInterstitial(action: NavigationAction, request: Request<K>, onComplete: Provider1<K, Screen>): Request<K> {
        val sInterstitialProgress = SInterstitialProgress()
        Navigator.action(action, sInterstitialProgress)

        request.onComplete { r -> if (Navigator.getCurrent() === sInterstitialProgress) Navigator.replace(onComplete.provide(r)) }
                .onNetworkError {
                    if (Navigator.getCurrent() === sInterstitialProgress) {
                        SAlert.showNetwork(Navigator.REPLACE, CallbackStub {
                            Navigator.replace(sInterstitialProgress)
                            request.send(api!!)
                        })
                    }
                }
                .onApiError(ApiClient.ERROR_GONE) { e -> if (Navigator.getCurrent() === sInterstitialProgress) SAlert.showGone(Navigator.REPLACE) }
                .onApiError(ApiClient.ERROR_ACCOUNT_IS_BANED) { ex -> ToolsToast.show(String.format(ToolsResources.getString(SupAndroid.TEXT_ERROR_ACCOUNT_BANED!!)!!, ToolsDate.dateToStringFull(java.lang.Long.parseLong(ex.params!![0])))) }
                .send(api!!)
        return request
    }

    fun <K : Request.Response> executeProgressDialog(request: Request<K>, onComplete: (K) -> Unit): Request<K> {
        return executeProgressDialog(null as String?, request, onComplete)
    }

    fun <K : Request.Response> executeProgressDialog(@StringRes title: Int, request: Request<K>, onComplete: (K) -> Unit): Request<K> {
        return executeProgressDialog(ToolsResources.getString(title), request, onComplete)
    }

    fun <K : Request.Response> executeProgressDialog(title: String?, request: Request<K>, onComplete: (K) -> Unit): Request<K> {
        val dialog = if (title == null) ToolsView.showProgressDialog() else ToolsView.showProgressDialog(title)
        return executeProgressDialog(dialog, request, onComplete)
    }

    fun <K : Request.Response> executeProgressDialog(dialog: Widget, request: Request<K>, onComplete: (K) -> Unit): Request<K> {
        request.onComplete { r -> onComplete.invoke(r) }
                .onNetworkError { ToolsToast.show(SupAndroid.TEXT_ERROR_NETWORK) }
                .onFinish { dialog.hide() }
                .onApiError(ApiClient.ERROR_ACCOUNT_IS_BANED) { ex -> ToolsToast.show(String.format(ToolsResources.getString(SupAndroid.TEXT_ERROR_ACCOUNT_BANED!!)!!, ToolsDate.dateToStringFull(java.lang.Long.parseLong(ex.params!![0])))) }
                .send(api!!)
        return request
    }


    fun <K : Request.Response> executeProgressDialog(request: Request<K>, onComplete: (Widget, K) -> Unit): Request<K> {
        return executeProgressDialog<K>(null, request, onComplete)
    }

    fun <K : Request.Response> executeProgressDialog(@StringRes title: Int, request: Request<K>, onComplete: (Widget, K) -> Unit): Request<K> {
        return executeProgressDialog(ToolsResources.getString(title), request, onComplete)
    }

    fun <K : Request.Response> executeProgressDialog(title: String?, request: Request<K>, onComplete: (Widget, K) -> Unit): Request<K> {
        val w = if (title == null) ToolsView.showProgressDialog() else ToolsView.showProgressDialog(title)
        request.onComplete { r -> onComplete.invoke(w, r) }
                .onNetworkError {
                    ToolsToast.show(SupAndroid.TEXT_ERROR_NETWORK)
                    w.hide()
                }
                .onApiError(ApiClient.ERROR_ACCOUNT_IS_BANED) { ex -> ToolsToast.show(String.format(ToolsResources.getString(SupAndroid.TEXT_ERROR_ACCOUNT_BANED!!)!!, ToolsDate.dateToStringFull(java.lang.Long.parseLong(ex.params!![0])))) }
                .send(api!!)
        return request
    }

    fun <K : Request.Response> executeEnabledCallback(request: Request<K>, onComplete: (K) -> Unit, enabled: (Boolean) -> Unit): Request<K> {
        enabled.invoke(false)
        request
                .onComplete { r -> onComplete.invoke(r) }
                .onNetworkError { ToolsToast.show(SupAndroid.TEXT_ERROR_NETWORK) }
                .onFinish { enabled.invoke(true) }
                .onApiError(ApiClient.ERROR_ACCOUNT_IS_BANED) { ex -> ToolsToast.show(String.format(ToolsResources.getString(SupAndroid.TEXT_ERROR_ACCOUNT_BANED!!)!!, ToolsDate.dateToStringFull(java.lang.Long.parseLong(ex.params!![0])))) }
                .send(api!!)

        return request
    }


    fun <K : Request.Response> executeEnabled(widget: Widget?, request: Request<K>, onComplete: (K) -> Unit): Request<K> {
        widget?.setEnabled<Widget>(false)
        request
                .onComplete { r ->
                    onComplete.invoke(r)
                    widget?.hide()
                }
                .onNetworkError { ToolsToast.show(SupAndroid.TEXT_ERROR_NETWORK) }
                .onFinish {
                    widget?.setEnabled<Widget>(true)
                    if (widget != null && widget is WidgetProgressTransparent) widget.hide()
                    if (widget != null && widget is WidgetProgressWithTitle) widget.hide()
                }
                .send(api!!)

        return request
    }

    fun <K : Request.Response> executeEnabledConfirm(@StringRes text: Int, @StringRes enter: Int, request: Request<K>, onComplete: (K)->Unit): Request<K> {
        WidgetAlert()
                .setText(text)
                .setOnCancel(SupAndroid.TEXT_APP_CANCEL)
                .setAutoHideOnEnter(false)
                .setOnEnter(enter, Callback1Stub{ w -> executeEnabled(w, request, onComplete) })
                .asSheetShow()
        return request
    }
}