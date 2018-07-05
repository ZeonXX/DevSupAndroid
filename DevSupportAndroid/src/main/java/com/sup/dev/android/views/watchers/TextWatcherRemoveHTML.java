package com.sup.dev.android.views.watchers;

import android.text.SpannableStringBuilder;

public class TextWatcherRemoveHTML extends BaseTextWatcher {

    private boolean ignoreNext;

    @Override
    public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
        if (!ignoreNext && charSequence instanceof SpannableStringBuilder) {
            String text = charSequence.toString();
            SpannableStringBuilder s = (SpannableStringBuilder) charSequence;
            s.clear();
            ignoreNext = true;
            s.append(text);
            ignoreNext = false;
        }
    }
}
