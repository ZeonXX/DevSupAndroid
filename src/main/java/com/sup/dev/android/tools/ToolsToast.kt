package com.sup.dev.android.tools

import android.graphics.PorterDuff
import androidx.annotation.StringRes
import com.google.android.material.snackbar.Snackbar
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorInt
import com.sup.dev.android.R
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.java.tools.ToolsThreads

object ToolsToast {

    fun show(@StringRes textRes: Int) {
        showNow(SupAndroid.appContext!!.getString(textRes))
    }

    fun show(text: String?) {
        if(text != null) showNow(text)
    }

    fun show(@StringRes textRes: Int, @ColorInt textColor: Int?) {
        show(SupAndroid.appContext!!.getString(textRes), null, textColor)
    }

    fun show(@StringRes textRes: Int, @ColorInt textColor: Int?, vararg args: Any) {
        show(ToolsResources.s(textRes, *args), null, textColor)
    }

    fun show(text: String?, @ColorInt textColor: Int?) {
        ToolsThreads.main { showNow(text, null, textColor) }
    }

    fun show(@StringRes textRes: Int, @ColorInt backColor: Int?, @ColorInt textColor: Int?) {
        show(SupAndroid.appContext!!.getString(textRes), backColor, textColor)
    }

    fun show(@StringRes textRes: Int, @ColorInt backColor: Int?, @ColorInt textColor: Int?, vararg args: Any) {
        show(ToolsResources.s(textRes, *args), backColor, textColor)
    }

    fun show(text: String?, @ColorInt backColor: Int?, @ColorInt textColor: Int?) {
        ToolsThreads.main { showNow(text, backColor, textColor) }
    }

    private fun showNow(text: String?, @ColorInt backColor: Int? = null, @ColorInt textColor: Int? = null) {
        if (text == null || text.isEmpty()) return
        ToolsThreads.main {
            val toast = Toast.makeText(SupAndroid.appContext!!, text, Toast.LENGTH_SHORT)
            if (backColor != null) toast.view.background.setColorFilter(backColor, PorterDuff.Mode.SRC_IN)
            if (textColor != null) {
                val v: TextView = toast.view.findViewById(android.R.id.message)
                v.setTextColor(textColor)
            }
            if (ToolsView.pxToDp(ToolsAndroid.getScreenW()) <= 320) toast.view.scaleX = 0.95f
            toast.show()
        }
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
        val textView = snackbarView.findViewById<TextView>(R.id.snackbar_text)
        textView.maxLines = 5
        snack.show()
    }

    fun showIfLocked(v: View, @StringRes textRes: Int) {
        showIfLocked(v, ToolsResources.s(textRes))
    }

    fun showIfLocked(v: View, text: String?) {
        ToolsThreads.main {
            if (ToolsAndroid.isScreenKeyLocked())
                showSnackNow(v, text)
            else
                showNow(text)
        }
    }


}
