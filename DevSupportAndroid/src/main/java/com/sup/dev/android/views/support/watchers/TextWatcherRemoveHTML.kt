package com.sup.dev.android.views.support.watchers

import android.text.SpannableStringBuilder
import android.widget.EditText

class TextWatcherRemoveHTML(val vText:EditText) : BaseTextWatcher() {

    private var ignoreNext: Boolean = false

    override fun onTextChanged(charSequence: CharSequence, start: Int, before: Int, count: Int) {
        if (!ignoreNext
                && count - before > 1 // Чтоб не тригеррилось при обычном вводе
                && charSequence is SpannableStringBuilder) {
            val selectionStart = vText.selectionStart
            val selectionEnd = vText.selectionEnd
            val text = charSequence.toString()
            charSequence.clear()
            ignoreNext = true
            charSequence.append(text)
            ignoreNext = false
            vText.setSelection(selectionStart, selectionEnd)
        }
    }
}
