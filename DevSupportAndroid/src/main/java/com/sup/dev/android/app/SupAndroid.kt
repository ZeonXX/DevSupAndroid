package com.sup.dev.android.app

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.sup.dev.android.libs.eventbus_multi_process.EventBusMultiProcess
import com.sup.dev.android.libs.screens.activity.SActivity
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.java.libs.debug.Debug
import com.sup.dev.java.libs.debug.err
import com.sup.dev.java.tools.ToolsThreads


@SuppressLint("StaticFieldLeak")
object SupAndroid {

    var SERVICE_FOREGROUND = 4000
    var SERVICE_NETWORK_CHECK = 4001

    var TEXT_APP_NAME: String? = null

    var IMG_ERROR_NETWORK = 0
    var IMG_ERROR_GONE = 0

    var editMode = false
    var appContext: Context? = null
    var activity: SActivity? = null
    var activityIsVisible = false

    fun initEditMode(view: View) {
        if (!view.isInEditMode) return
        editMode = true
        init(view.context)
    }

    fun init(appContext: Context) {
        SupAndroid.appContext = appContext

        ToolsThreads.onMain = { onNextTime, runnable ->
            if ((!onNextTime) && ToolsAndroid.isMainThread()) runnable.invoke()
            else Handler(Looper.getMainLooper()).post { runnable.invoke() }
        }

        Debug.printer = { s -> Log.e("Debug", s) }
        Debug.printerInfo = { s -> Log.i("Debug", s) }
        Debug.exceptionPrinter = { th -> Log.e("Debug", "", th) }

        TEXT_APP_NAME = loadText("app_name")

        IMG_ERROR_NETWORK = loadImage("error_network")?:0
        IMG_ERROR_GONE = loadImage("error_gone")?:0


        EventBusMultiProcess.init()
    }

    private fun loadText(id: String): String? {
        try {
            return ToolsResources.s(id)
        } catch (e: Resources.NotFoundException) {
            err("Init warning: can't find vText with id [$id]")
            return null
        }
    }

    private fun loadImage(id: String): Int? {
        try {
            return ToolsResources.getDrawableId(id)
        } catch (e: Resources.NotFoundException) {
            err("Init warning: can't find image with id [$id]")
            return null
        }
    }

}