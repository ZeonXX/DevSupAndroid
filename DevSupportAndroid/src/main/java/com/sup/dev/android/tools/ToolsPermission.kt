package com.sup.dev.android.tools

import android.Manifest.permission.*
import android.app.Activity
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.java.tools.ToolsThreads


object ToolsPermission {

    private val MAX_WAIT_TIME = (1000 * 10).toLong()
    private val REQUEST_CODE = 102

    //
    //  Requests Simple
    //

    fun requestReadPermission(onGranted: () -> Unit) {
        requestPermission(READ_EXTERNAL_STORAGE, onGranted)
    }

    fun requestWritePermission(onGranted: () -> Unit) {
        requestPermission(WRITE_EXTERNAL_STORAGE, onGranted)
    }
    //
    //  Requests
    //

    fun requestReadPermission(onGranted: () -> Unit, onPermissionRestriction: () -> Unit) {
        requestPermission(READ_EXTERNAL_STORAGE, onGranted, onPermissionRestriction)
    }

    fun requestWritePermission(onGranted: () -> Unit, onPermissionRestriction: () -> Unit) {
        requestPermission(WRITE_EXTERNAL_STORAGE, onGranted, onPermissionRestriction)
    }

    fun requestCallPhonePermission(onGranted: () -> Unit, onPermissionRestriction: () -> Unit) {
        requestPermission(CALL_PHONE, onGranted, onPermissionRestriction)
    }

    fun requestOverlayPermission(onGranted: () -> Unit, onPermissionRestriction: () -> Unit) {
        requestPermission(SYSTEM_ALERT_WINDOW, onGranted, onPermissionRestriction)
    }

    fun requestMicrophonePermission(onGranted: () -> Unit, onPermissionRestriction: () -> Unit) {
        requestPermission(RECORD_AUDIO, onGranted, onPermissionRestriction)
    }

    //
    //  Checks
    //

    fun hasReadPermission(): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN || hasPermission(READ_EXTERNAL_STORAGE)
    }

    fun hasWritePermission(): Boolean {
        return hasPermission(WRITE_EXTERNAL_STORAGE)
    }

    fun hasCallPhonePermission(): Boolean {
        return hasPermission(CALL_PHONE)
    }

    fun hasOverlayPermission(): Boolean {
        return hasPermission(SYSTEM_ALERT_WINDOW)
    }

    fun hasMicrophonePermission(): Boolean {
        return hasPermission(RECORD_AUDIO)
    }


    //
    //  Methods
    //

    fun hasPermission(permission: String): Boolean {
        return ContextCompat.checkSelfPermission(SupAndroid.appContext, permission) == PERMISSION_GRANTED
    }

    @JvmOverloads
    fun requestPermission(permission: String, onGranted: () -> Unit, onPermissionRestriction: () -> Unit = { ToolsToast.show(SupAndroid.TEXT_ERROR_PERMISSION_READ_FILES) }) {
        if (hasPermission(permission)) {
            onGranted.invoke()
            return
        }
        ToolsThreads.thread {
            if (requestPermission(SupAndroid.activity, permission)) {
                ToolsThreads.main(2000) { onGranted.invoke() }    //  Без задержки приложение ведет себя так, будто разрешение еще не получено
            } else {
                ToolsThreads.main { onPermissionRestriction.invoke() }
            }
        }
    }

    private fun requestPermission(activity: Activity, permission: String): Boolean {

        if (hasPermission(permission)) return true

        ActivityCompat.requestPermissions(activity, arrayOf(permission), REQUEST_CODE)
        return waitForPermission(permission)
    }

    private fun waitForPermission(permission: String): Boolean {

        val t = System.currentTimeMillis() + MAX_WAIT_TIME

        while (t > System.currentTimeMillis() && !hasPermission(permission))
            ToolsThreads.sleep(50)

        return hasPermission(permission)
    }


}
