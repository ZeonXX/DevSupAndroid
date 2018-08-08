package com.sup.dev.android.libs.api_simple;

import android.support.annotation.StringRes;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.screens.NavigationAction;
import com.sup.dev.android.libs.screens.Navigator;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.tools.ToolsResources;
import com.sup.dev.android.tools.ToolsToast;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.screens.SAlert;
import com.sup.dev.android.views.screens.SInterstitialProgress;
import com.sup.dev.android.views.widgets.Widget;
import com.sup.dev.android.views.widgets.WidgetAlert;
import com.sup.dev.android.views.widgets.WidgetProgressTransparent;
import com.sup.dev.android.views.widgets.WidgetProgressWithTitle;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.providers.Provider1;
import com.sup.dev.java.libs.api_simple.client.ApiClient;
import com.sup.dev.java.libs.api_simple.client.Request;
import com.sup.dev.java.tools.ToolsDate;
import com.sup.dev.java.tools.ToolsThreads;

public class ApiRequestsSupporter {
    
    private static ApiClient api;
    
    public static void init(ApiClient api){
        ApiRequestsSupporter.api = api;
    }


    public static <K extends Request.Response> Request<K> executeInterstitial(NavigationAction action, Request<K> request, Provider1<K, Screen> onComplete) {

        SInterstitialProgress sInterstitialProgress = new SInterstitialProgress();
        Navigator.action(action, sInterstitialProgress);

        long startTime = System.currentTimeMillis();
        request.onComplete(
                r -> {
                    if (Navigator.getCurrent() == sInterstitialProgress) {
                        long timeLeft = System.currentTimeMillis() - startTime;
                        if (timeLeft >= 300) Navigator.replace(onComplete.provide(r));
                        else ToolsThreads.main(300 - timeLeft, () -> {
                            if (Navigator.getCurrent() == sInterstitialProgress) Navigator.replace(onComplete.provide(r));
                        });
                    }

                })
                .onNetworkError(() -> {
                    if (Navigator.getCurrent() == sInterstitialProgress) {
                        SAlert.showNetwork(Navigator.REPLACE, () -> {
                            Navigator.replace(sInterstitialProgress);
                            request.send(api);
                        });
                    }
                })
                .onApiError(ApiClient.ERROR_GONE, e -> {
                    if (Navigator.getCurrent() == sInterstitialProgress) SAlert.showGone(Navigator.REPLACE);
                })
                .onApiError(ApiClient.ERROR_ACCOUNT_IS_BANED, ex -> ToolsToast.show(String.format(ToolsResources.getString(SupAndroid.TEXT_ERROR_ACCOUNT_BANED), ToolsDate.dateToStringFull(Long.parseLong(ex.params[0])))))
                .send(api);
        return request;
    }

    public static <K extends Request.Response> Request<K> executeProgressDialog(Request<K> request, Callback1<K> onComplete) {
        return executeProgressDialog((String) null, request, onComplete);
    }

    public static <K extends Request.Response> Request<K> executeProgressDialog(@StringRes int title, Request<K> request, Callback1<K> onComplete) {
        return executeProgressDialog(ToolsResources.getString(title), request, onComplete);
    }

    public static <K extends Request.Response> Request<K> executeProgressDialog(String title, Request<K> request, Callback1<K> onComplete) {
        Widget dialog = title == null ? ToolsView.showProgressDialog() : ToolsView.showProgressDialog(title);
        return executeProgressDialog(dialog, request, onComplete);
    }

    public static <K extends Request.Response> Request<K> executeProgressDialog(Widget dialog, Request<K> request, Callback1<K> onComplete) {
        request.onComplete(r -> onComplete.callback(r))
                .onNetworkError(() -> ToolsToast.show(SupAndroid.TEXT_ERROR_NETWORK))
                .onFinish(() -> dialog.hide())
                .onApiError(ApiClient.ERROR_ACCOUNT_IS_BANED, ex -> ToolsToast.show(String.format(ToolsResources.getString(SupAndroid.TEXT_ERROR_ACCOUNT_BANED), ToolsDate.dateToStringFull(Long.parseLong(ex.params[0])))))
                .send(api);
        return request;
    }


    public static <K extends Request.Response> Request<K> executeProgressDialog(Request<K> request, Callback2<Widget, K> onComplete) {
        return executeProgressDialog(null, request, onComplete);
    }

    public static <K extends Request.Response> Request<K> executeProgressDialog(@StringRes int title, Request<K> request, Callback2<Widget, K> onComplete) {
        return executeProgressDialog(ToolsResources.getString(title), request, onComplete);
    }

    public static <K extends Request.Response> Request<K> executeProgressDialog(String title, Request<K> request, Callback2<Widget, K> onComplete) {
        Widget w = title == null ? ToolsView.showProgressDialog() : ToolsView.showProgressDialog(title);
        request.onComplete(r -> onComplete.callback(w, r))
                .onNetworkError(() -> {
                    ToolsToast.show(SupAndroid.TEXT_ERROR_NETWORK);
                    w.hide();
                })
                .onApiError(ApiClient.ERROR_ACCOUNT_IS_BANED, ex -> ToolsToast.show(String.format(ToolsResources.getString(SupAndroid.TEXT_ERROR_ACCOUNT_BANED), ToolsDate.dateToStringFull(Long.parseLong(ex.params[0])))))
                .send(api);
        return request;
    }

    public static <K extends Request.Response> Request<K> executeEnabledCallback(Request<K> request, Callback1<K> onComplete, Callback1<Boolean> enabled) {
        enabled.callback(false);
        request
                .onComplete(r -> onComplete.callback(r))
                .onNetworkError(() -> ToolsToast.show(SupAndroid.TEXT_ERROR_NETWORK))
                .onFinish(() -> enabled.callback(true))
                .onApiError(ApiClient.ERROR_ACCOUNT_IS_BANED, ex -> ToolsToast.show(String.format(ToolsResources.getString(SupAndroid.TEXT_ERROR_ACCOUNT_BANED), ToolsDate.dateToStringFull(Long.parseLong(ex.params[0])))))
                .send(api);

        return request;
    }


    public static <K extends Request.Response> Request<K> executeEnabled(Widget widget, Request<K> request, Callback1<K> onComplete) {
        if (widget != null) widget.setEnabled(false);
        request
                .onComplete(r -> {
                    onComplete.callback(r);
                    if (widget != null) widget.hide();
                })
                .onNetworkError(() -> ToolsToast.show(SupAndroid.TEXT_ERROR_NETWORK))
                .onFinish(() -> {
                    if (widget != null) widget.setEnabled(true);
                    if (widget != null && widget instanceof WidgetProgressTransparent) widget.hide();
                    if (widget != null && widget instanceof WidgetProgressWithTitle) widget.hide();
                })
                .send(api);

        return request;
    }

    public static <K extends Request.Response> Request<K> executeEnabledConfirm(@StringRes int text, @StringRes int enter, Request<K> request, Callback1<K> onComplete) {
        new WidgetAlert()
                .setText(text)
                .setOnCancel(SupAndroid.TEXT_APP_CANCEL)
                .setAutoHideOnEnter(false)
                .setOnEnter(enter, w -> executeEnabled(w, request, onComplete))
                .asDialogShow();
        return request;
    }
    
}
