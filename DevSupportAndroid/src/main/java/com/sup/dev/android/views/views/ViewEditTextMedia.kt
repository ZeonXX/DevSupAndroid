package com.sup.dev.android.views.views

import android.content.Context
import android.os.Bundle
import android.support.v13.view.inputmethod.EditorInfoCompat
import android.support.v13.view.inputmethod.InputConnectionCompat
import android.support.v13.view.inputmethod.InputContentInfoCompat
import android.support.v4.os.BuildCompat
import android.util.AttributeSet
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import com.sup.dev.java.libs.debug.Debug


class ViewEditTextMedia @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) : android.support.v7.widget.AppCompatEditText(context, attrs) {

    private var callback: ((String) -> Unit)? = null

    override fun onCreateInputConnection(editorInfo: EditorInfo): InputConnection {
        if (callback == null) return super.onCreateInputConnection(editorInfo)
        try {
            var ic: InputConnection = super.onCreateInputConnection(editorInfo);
            EditorInfoCompat.setContentMimeTypes(editorInfo, arrayOf("image/*"))

            var funct: (InputContentInfoCompat, Int, Bundle) -> Boolean = { inputContentInfo, flags, opts ->
                if (BuildCompat.isAtLeastNMR1() &&
                        (flags and InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION) != 0) {
                    try {
                        inputContentInfo.requestPermission();
                    } catch (e: Exception) {
                        Debug.log(e)
                        false
                    }
                }

                if (BuildCompat.isAtLeastNMR1() && (flags and InputConnectionCompat.INPUT_CONTENT_GRANT_READ_URI_PERMISSION) != 0) {
                    try {
                        inputContentInfo.requestPermission();
                    } catch (e: Exception) {
                        Debug.log(e)
                        false
                    }
                }
                try {
                    if (callback != null) callback!!.invoke(inputContentInfo.getLinkUri().toString())
                    true
                } catch (e: Exception) {
                    Debug.log(e)
                }
                false
            }

            return InputConnectionCompat.createWrapper(ic, editorInfo, InputConnectionCompat.OnCommitContentListener(
                    function = funct))
        } catch (e: Exception) {
            Debug.log(e)
            return super.onCreateInputConnection(editorInfo);
        }
    }

    fun setCallback(callback: ((String) -> Unit)?) {
        this.callback = callback
    }

}
