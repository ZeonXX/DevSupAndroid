package com.sup.dev.android.views.watchers

import android.text.SpannableStringBuilder

class TextWatcherRemoveHTML : BaseTextWatcher() {

    private var ignoreNext: Boolean = false

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
        if (!ignoreNext
                && count - before > 1 // Чтоб не тригеррилось при обычном вводе
                && charSequence is SpannableStringBuilder) {
            val text = charSequence.toString()
            charSequence.clear()
            ignoreNext = true
            charSequence.append(text)
            ignoreNext = false
        }
    }
}
