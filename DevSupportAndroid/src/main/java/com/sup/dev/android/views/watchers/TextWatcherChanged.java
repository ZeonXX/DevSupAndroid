package com.sup.dev.android.views.watchers;

import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class TextWatcherChanged extends BaseTextWatcher{

    private final Callback1<String> callback;

    public TextWatcherChanged(Callback1<String> callback) {
        this.callback = callback;
    }

    @Override
    protected void onTextChanged(String s) {
        callback.callback(s);
    }
}