package com.sup.dev.android.views.watchers;

import com.sup.dev.java.classes.callbacks.simple.Callback4;

public class TextWatcherBeforeChanged extends BaseTextWatcher{

    private final Callback4<CharSequence, Integer, Integer, Integer> callback;

    public TextWatcherBeforeChanged(Callback4<CharSequence, Integer, Integer, Integer> callback) {
        this.callback = callback;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        if(callback != null)callback.callback(s, start, count, after);
    }
}