package com.sup.dev.android.libs.api_simple

import android.support.annotation.StringRes
import com.sup.dev.android.R
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
import com.sup.dev.java.libs.api_simple.client.ApiClient
import com.sup.dev.java.libs.api_simple.client.Request
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.tools.ToolsDate

object ApiRequestsSupporter {


    private var api: ApiClient? = null

    fun init(api: ApiClient) {
        ApiRequestsSupporter.api = api
    }

    fun <K : Request.Response> execute(request: Request<K>, onComplete: (K) -> Unit): Request<K> {
        request.onComplete { r -> onComplete.invoke(r) }
                .onNetworkError { ToolsToast.show(R.string.error_network) }
                .onApiError(ApiClient.ERROR_ACCOUNT_IS_BANED) { ex -> ToolsToast.show(String.format(ToolsResources.s(R.string.error_account_baned), ToolsDate.dateToStringFull(java.lang.Long.parseLong(ex.params!![0])))) }
                .onApiError(ApiClient.ERROR_GONE) { ex -> ToolsToast.show(String.format(ToolsResources.s(R.string.error_gone))) }
                .send(api!!)
        return request
    }

    fun <K : Request.Response> executeInterstitial(action: NavigationAction, request: Request<K>, onComplete: (K) -> Screen): Request<K> {
        val sInterstitialProgress = SInterstitialProgress()
        Navigator.action(action, sInterstitialProgress)

        return execute(request) { r ->
            if (Navigator.getCurrent() === sInterstitialProgress) {
                Navigator.replace(onComplete.invoke(r))
            }
        }
                .onApiError(ApiClient.ERROR_GONE) {
                    if (Navigator.getCurrent() === sInterstitialProgress) SAlert.showGone(Navigator.REPLACE)
                }
                .onNetworkError {
                    if (Navigator.getCurrent() === sInterstitialProgress)
                        SAlert.showNetwork(Navigator.REPLACE) {
                            Navigator.replace(sInterstitialProgress)
                            request.send(api!!)
                        }
                }
    }

    fun <K : Request.Response> executeProgressDialog(request: Request<K>, onComplete: (K) -> Unit): Request<K> {
        return executeProgressDialog(null as String?, request, onComplete)
    }

    fun <K : Request.Response> executeProgressDialog(@StringRes title: Int, request: Request<K>, onComplete: (K) -> Unit): Request<K> {
        return executeProgressDialog(ToolsResources.s(title), request, onComplete)
    }

    fun <K : Request.Response> executeProgressDialog(title: String?, request: Request<K>, onComplete: (K) -> Unit): Request<K> {
        val dialog = if (title == null) ToolsView.showProgressDialog() else ToolsView.showProgressDialog(title)
        return executeProgressDialog(dialog, request, onComplete)
    }

    fun <K : Request.Response> executeProgressDialog(dialog: Widget, request: Request<K>, onComplete: (K) -> Unit): Request<K> {
        return execute(request, onComplete)
                .onFinish { dialog.hide() }
    }

    fun <K : Request.Response> executeProgressDialog(request: Request<K>, onComplete: (Widget, K) -> Unit): Request<K> {
        return executeProgressDialog<K>(null, request, onComplete)
    }

    fun <K : Request.Response> executeProgressDialog(@StringRes title: Int, request: Request<K>, onComplete: (Widget, K) -> Unit): Request<K> {
        return executeProgressDialog(ToolsResources.s(title), request, onComplete)
    }

    fun <K : Request.Response> executeProgressDialog(title: String?, request: Request<K>, onComplete: (Widget, K) -> Unit): Request<K> {
        val w = if (title == null) ToolsView.showProgressDialog() else ToolsView.showProgressDialog(title)
        return execute(request) { r -> onComplete.invoke(w, r) }
                .onNetworkError {
                    ToolsToast.show(R.string.error_network)
                    w.hide()
                }
    }

    fun <K : Request.Response> executeEnabledCallback(request: Request<K>, onComplete: (K) -> Unit, enabled: (Boolean) -> Unit): Request<K> {
        enabled.invoke(false)
        return execute(request, onComplete)
                .onFinish { enabled.invoke(true) }
    }


    fun <K : Request.Response> executeEnabled(widget: Widget?, request: Request<K>, onComplete: (K) -> Unit): Request<K> {
        widget?.setEnabled(false)
        widget?.hideCancel()
        return execute(request) { r ->
            onComplete.invoke(r)
            widget?.hideForce()
        }.onFinish {
            widget?.setEnabled(true)
            if (widget != null && widget is WidgetProgressTransparent) widget.hideForce()
            if (widget != null && widget is WidgetProgressWithTitle) widget.hideForce()
        }
    }

    fun <K : Request.Response> executeEnabledConfirm(@StringRes text: Int, @StringRes enter: Int, request: Request<K>, onComplete: (K) -> Unit): Request<K> {
        return executeEnabledConfirm(ToolsResources.s(text), ToolsResources.s(enter), request, onComplete)
    }

    fun <K : Request.Response> executeEnabledConfirm(text: String, enter: String, request: Request<K>, onComplete: (K) -> Unit): Request<K> {
        WidgetAlert()
                .setText(text)
                .setOnCancel(R.string.app_cancel)
                .setAutoHideOnEnter(false)
                .setOnEnter(enter) { w -> executeEnabled(w, request, onComplete) }
                .asSheetShow()
        return request
    }
}