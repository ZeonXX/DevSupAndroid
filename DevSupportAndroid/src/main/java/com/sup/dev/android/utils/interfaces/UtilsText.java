package com.sup.dev.android.utils.interfaces;

import android.graphics.Typeface;
import android.text.InputFilter;
import android.widget.EditText;

public interface UtilsText {

    InputFilter getFilterSpecChars() ;

    InputFilter getFilterLetterOrDigit();

    // Key, Word, Key, Word...
    String replaceKeys(int textRes, Object... values) ;

    // Key, Word, Key, Word...
    String replaceKeys(String text, Object... values);

    void setFilters(EditText editText, InputFilter... filters);

    String htmlFromEditText(EditText editText);

    //
    //  Fonts
    //

    float getStringWidth(Typeface font, float textSize, String string);

    float getStringHeight(Typeface font, float textSize);

    float getFontAscent(Typeface font, float textSize);

    float getFontDescent(Typeface font, float textSize);




}
