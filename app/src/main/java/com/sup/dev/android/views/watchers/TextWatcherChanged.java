package com.sup.dev.android.views.watchers;

import com.sup.dev.java.classes.callbacks.simple.CallbackSource;

public class TextWatcherChanged extends BaseTextWatcher{

    private final CallbackSource<String> callback;

    public TextWatcherChanged(CallbackSource<String> callback) {
        this.callback = callback;
    }

    @Override
    protected void onTextChanged(String s) {
        callback.callback(s);
    }
}