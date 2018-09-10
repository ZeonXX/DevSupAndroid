package com.sup.dev.android.views.watchers

class TextWatcherChanged(private val callback: (String) -> Unit) : BaseTextWatcher() {

    override fun onTextChanged(s: String) {
        callback.invoke(s)
    }
}