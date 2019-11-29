package com.sup.dev.android.tools

import com.sup.dev.java.libs.api.ApiRequest
import com.sup.dev.java.libs.api.ApiResponse

object ToolsApi {

    fun <K:ApiResponse> sendProgressDialog(request: ApiRequest<K>){
        val dLoading = ToolsView.showProgressDialog()
        request
                .onFinish { dLoading.hide() }
                .send()
    }

}