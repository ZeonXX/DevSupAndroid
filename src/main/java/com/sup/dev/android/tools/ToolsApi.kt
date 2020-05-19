package com.sup.dev.android.tools

import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.navigator.NavigationAction
import com.sup.dev.android.libs.screens.navigator.Navigator
import com.sup.dev.android.views.screens.SAlert
import com.sup.dev.android.views.splash.Splash
import com.sup.dev.android.views.splash.SplashProgressTransparent
import com.sup.dev.java.libs.api.ApiRequest
import com.sup.dev.java.libs.api.ApiResponse

object ToolsApi {

    fun <K:ApiResponse> send(request: ApiRequest<K>){
        request.send()
    }

    fun <K:ApiResponse> sendProgressDialog(request: ApiRequest<K>){
        sendProgressDialog(ToolsView.showProgressDialog(), request)
    }

    fun <K:ApiResponse> sendProgressDialog(dLoading: Splash, request: ApiRequest<K>){
        request
                .onFinish { dLoading.hide() }
                .send()
    }

    fun <K:ApiResponse> sendSplash(action: NavigationAction, request: ApiRequest<K>, onComplete: (K) -> Screen){
        val vProgress = SplashProgressTransparent()
        vProgress.asSplashShow()

        send(request.onComplete {
            if (vProgress.isHided()) return@onComplete
            Navigator.action(action, onComplete.invoke(it))
        }.onError {
            if(vProgress.isHided()) return@onError
            SAlert.showNetwork(action) {
                Navigator.remove(it)
                sendSplash(action, request, onComplete)
            }
        }.onFinish {
            vProgress.hide()
        })



    }

}