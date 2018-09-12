package com.sup.dev.android.tools

import android.support.annotation.StringRes
import android.support.design.widget.Snackbar
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.java.tools.ToolsThreads


object ToolsToast {

    fun show(@StringRes textRes: Int) {
        show(SupAndroid.appContext!!.getString(textRes))
    }

    fun show(@StringRes textRes: Int, vararg args: Any) {
        show(ToolsResources.getString(textRes, *args))
    }

    fun show(text: String?) {
        ToolsThreads.main { showNow(text) }
    }

    private fun showNow(text: String?) {
        if (text == null || text.isEmpty()) return
        Toast.makeText(SupAndroid.appContext!!, text, Toast.LENGTH_SHORT).show()
    }

    fun showSnack(@StringRes textRes: Int) {
        showSnack(SupAndroid.appContext!!.getString(textRes))
    }

    fun showSnack(text: String) {
        ToolsThreads.main { showSnackNow(SupAndroid.activity!!.getViewContainer()!!, text) }
    }

    fun showSnack(v: View, @StringRes textRes: Int) {
        showSnack(v, SupAndroid.appContext!!.getString(textRes))
    }

    fun showSnack(v: View, text: String) {
        ToolsThreads.main { showSnackNow(v, text) }
    }

    private fun showSnackNow(v: View, text: String?) {
        if (text == null || text.isEmpty()) return
        val snack = Snackbar.make(v, text, Snackbar.LENGTH_SHORT)
        val snackbarView = snack.view
        val textView = snackbarView.findViewById<TextView>(android.support.design.R.id.snackbar_text)
        textView.maxLines = 5
        snack.show()
    }

    fun showSnackIfLocked(v: View, @StringRes textRes: Int) {
        showSnackIfLocked(v, ToolsResources.getString(textRes))
    }

    fun showSnackIfLocked(v: View, text: String?) {
        ToolsThreads.main {
            if (ToolsAndroid.isScreenKeyLocked())
                showSnackNow(v, text)
            else
                showNow(text)
        }
    }


}
