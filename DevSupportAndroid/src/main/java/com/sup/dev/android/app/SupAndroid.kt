package com.sup.dev.android.app

import android.content.Context
import android.content.res.Resources
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.sup.dev.android.libs.eventbus_multi_process.EventBusMultiProcess
import com.sup.dev.android.libs.screens.activity.SActivity
import com.sup.dev.android.tools.ToolsAndroid
import com.sup.dev.android.tools.ToolsImagesLoader
import com.sup.dev.android.tools.ToolsResources
import com.sup.dev.java.libs.debug.Debug
import com.sup.dev.java.tools.ToolsThreads


object SupAndroid {

    var SERVICE_FOREGROUND = 4000
    var SERVICE_NETWORK_CHECK = 4001

    var TEXT_APP_NAME: String? = null
    var TEXT_APP_WHOOPS: String? = null
    var TEXT_APP_RETRY: String? = null
    var TEXT_APP_BACK: String? = null
    var TEXT_APP_CANCEL: String? = null
    var TEXT_APP_DONT_SHOW_AGAIN: String? = null
    var TEXT_APP_ATTENTION: String? = null
    var TEXT_APP_DOWNLOADING: String? = null
    var TEXT_APP_DOWNLOADED: String? = null
    var TEXT_APP_DONE: String? = null
    var TEXT_APP_CHOOSE: String? = null
    var TEXT_APP_LINK: String? = null

    var TEXT_ERROR_PERMISSION_READ_FILES: String? = null
    var TEXT_ERROR_CANT_LOAD_IMAGE: String? = null
    var TEXT_ERROR_NETWORK: String? = null
    var TEXT_ERROR_GONE: String? = null
    var TEXT_ERROR_ACCOUNT_BANED: String? = null

    var IMG_ERROR_NETWORK: Int? = null
    var IMG_ERROR_GONE: Int? = null


    var editMode: Boolean = false
    var appContext: Context? = null
    var activity: SActivity? = null
    var activityIsVisible: Boolean = false

    fun initEditMode(view: View) {
        if (!view.isInEditMode) return
        editMode = true
        init(view.context)
    }

    fun init(appContext: Context) {
        SupAndroid.appContext = appContext

        ToolsThreads.setOnMain { onNextTime, runnable ->
            if ((!onNextTime) && ToolsAndroid.isMainThread()) runnable.invoke()
            else Handler(Looper.getMainLooper()).post { runnable.invoke() }

        }
        Debug.printer = { s ->
            Log.e("Debug", s)
            Unit
        }
        Debug.printerInfo = { tag, s ->
            Log.i(tag, s)
            Unit
        }
        Debug.exceptionPrinter = { th ->
            Log.e("Debug", "", th)
            Unit
        }

        ToolsImagesLoader.init()
        EventBusMultiProcess.init()

        TEXT_APP_NAME = loadText("app_name")
        TEXT_APP_WHOOPS = loadText("app_whoops")
        TEXT_APP_RETRY = loadText("app_retry")
        TEXT_APP_BACK = loadText("app_back")
        TEXT_APP_CANCEL = loadText("app_cancel")
        TEXT_APP_DONT_SHOW_AGAIN = loadText("app_dont_show_again")
        TEXT_APP_ATTENTION = loadText("app_attention")
        TEXT_ERROR_PERMISSION_READ_FILES = loadText("error_permission_files")
        TEXT_ERROR_CANT_LOAD_IMAGE = loadText("error_cant_load_image")
        TEXT_ERROR_NETWORK = loadText("error_network")
        TEXT_ERROR_GONE = loadText("error_gone")
        TEXT_ERROR_ACCOUNT_BANED = loadText("error_account_baned")
        TEXT_APP_DOWNLOADING = loadText("app_downloading")
        TEXT_APP_DOWNLOADED = loadText("app_downloaded")
        TEXT_APP_DONE = loadText("app_done")
        TEXT_APP_CHOOSE = loadText("app_choose")
        TEXT_APP_LINK = loadText("app_link")

        IMG_ERROR_NETWORK = loadImage("error_network")
        IMG_ERROR_GONE = loadImage("error_gone")


    }

    private fun loadText(id: String): String? {
        try {
            return ToolsResources.getString(id)
        } catch (e: Resources.NotFoundException) {
            Debug.error("Init warning: can't find vText with id [$id]")
            return null
        }
    }

    private fun loadImage(id: String): Int? {
        try {
            return ToolsResources.getDrawableId(id)
        } catch (e: Resources.NotFoundException) {
            Debug.error("Init warning: can't find image with id [$id]")
            return null
        }
    }

}