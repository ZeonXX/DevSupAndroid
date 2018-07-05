package com.sup.dev.android.views.watchers;

import android.text.Editable;

public class BaseTextWatcher implements android.text.TextWatcher{


    public BaseTextWatcher() {
    }

    @Override
    public void afterTextChanged(Editable s) {
        onTextChanged(s.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    protected void onTextChanged(String s){

    }


}