package com.sup.dev.android.tools

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.java.tools.ToolsThreads


object ToolsToast {

    fun showToast(@StringRes textRes: Int) {
        showToast(SupAndroid.appContext!!.getString(textRes))
    }

    fun showToast(@StringRes textRes: Int, vararg args: Any) {
        showToast(ToolsResources.getString(textRes, *args))
    }

    fun showToast(text: String?) {
        ToolsThreads.main { showToastNow(text) }
    }

    private fun showToastNow(text: String?) {
        if (text == null || text.isEmpty()) return
        Toast.makeText(SupAndroid.appContext!!, text, Toast.LENGTH_SHORT).show()
    }

    fun show(@StringRes textRes: Int) {
        show(SupAndroid.appContext!!.getString(textRes))
    }

    fun show(text: String) {
        ToolsThreads.main { showNow(SupAndroid.activity!!.getViewContainer()!!, text) }
    }

    fun show(v: View, @StringRes textRes: Int) {
        show(v, SupAndroid.appContext!!.getString(textRes))
    }

    fun show(v: View, text: String) {
        ToolsThreads.main { showNow(v, text) }
    }

    private fun showNow(v: View, text: String?) {
        if (text == null || text.isEmpty()) return
        val snack = Snackbar.make(v, text, Snackbar.LENGTH_SHORT)
        val snackbarView = snack.view
        val textView = snackbarView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView.maxLines = 5
        snack.show()
    }

    fun showIfLocked(v: View, @StringRes textRes: Int) {
        showIfLocked(v, ToolsResources.getString(textRes))
    }

    fun showIfLocked(v: View, text: String?) {
        ToolsThreads.main {
            if (ToolsAndroid.isScreenKeyLocked())
                showNow(v, text)
            else
                showToastNow(text)
        }
    }


}
