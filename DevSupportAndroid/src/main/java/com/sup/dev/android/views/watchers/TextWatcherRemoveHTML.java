package com.sup.dev.android.views.watchers;

import android.text.SpannableStringBuilder;

import com.sup.dev.java.libs.debug.Debug;

public class TextWatcherRemoveHTML extends BaseTextWatcher {

    private boolean ignoreNext;

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (!ignoreNext
                && count - before > 1 // Чтоб не тригеррилось при обычном вводе
                && charSequence instanceof SpannableStringBuilder) {
            String text = charSequence.toString();
            SpannableStringBuilder s = (SpannableStringBuilder) charSequence;
            s.clear();
            ignoreNext = true;
            s.append(text);
            ignoreNext = false;
        }
    }
}
