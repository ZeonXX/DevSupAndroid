package com.sup.dev.android.views.watchers;

import android.text.Editable;

public class BaseTextWatcher implements android.text.TextWatcher{


    public BaseTextWatcher() {
    }

    @Override
    public final void afterTextChanged(Editable s) {
        onTextChanged(s.toString());
    }

    @Override
    public final void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public final void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    protected void onTextChanged(String s){

    }


}