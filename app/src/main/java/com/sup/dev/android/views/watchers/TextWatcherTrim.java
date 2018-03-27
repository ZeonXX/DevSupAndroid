package com.sup.dev.android.views.watchers;

import android.widget.EditText;

public class TextWatcherTrim extends BaseTextWatcher{

    private final EditText vField;

    public TextWatcherTrim(EditText vField){
        this.vField = vField;
    }

    @Override
    protected void onTextChanged(String s) {
        String ss = s.trim();
        if (!s.equals(ss)) {
            int selectionStart = vField.getSelectionStart();
            vField.setText(ss);
            vField.setSelection(selectionStart < 2 ? 0 : ss.length());
            return;
        }
    }
}
