package com.sup.dev.android.libs.http

import com.sup.dev.android.tools.ToolsView
import com.sup.dev.java.libs.http_api.HttpApiRequest


object ToolsHttpApi{

    fun executeProgressDialog(request: HttpApiRequest){
        val dialog = ToolsView.showProgressDialog()
        request.addOnFinish { dialog.hide() }
        request.execute()
    }

    fun execute(request: HttpApiRequest){
        request.execute()
    }

}