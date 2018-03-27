package com.sup.dev.android.utils.implementations;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Html;
import android.text.InputFilter;
import android.widget.EditText;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.utils.interfaces.UtilsText;

public class UtilsTextImpl extends com.sup.dev.java.tools.ToolsText implements UtilsText {

    public InputFilter getFilterSpecChars() {
        return (source, start, end, dest, dstart, dend) -> source != null && SPEC.contains(source) ? "" : null;
    }

    public InputFilter getFilterLetterOrDigit() {
        return (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++)
                if (!Character.isLetterOrDigit(source.charAt(i)) && !source.toString().equals("."))
                    return "";
            return null;
        };
    }

    // Key, Word, Key, Word...
    public String replaceKeys(int textRes, Object... values) {
        return replaceKeys(SupAndroid.di.utilsResources().getString(textRes), values);
    }

    // Key, Word, Key, Word...
    public String replaceKeys(String text, Object... values) {
        String key;
        String value;
        for (int i = 0; i < values.length; i += 2) {
            if (values[i] instanceof String)
                key = (String) values[i];
            else
                key = SupAndroid.di.utilsResources().getString((int) values[i]);
            if (values[i + 1] instanceof String)
                value = (String) values[i + 1];
            else
                value = SupAndroid.di.utilsResources().getString((int) values[i + 1]);
            text = text.replaceAll(key, value);
        }
        return text;
    }

    public void setFilters(EditText editText, InputFilter... filters) {
        editText.setFilters(filters);
    }

    public String htmlFromEditText(EditText editText) {
        String s = Html.toHtml(editText.getText());
        if (s.length() == 0)
            return s;
        return s.substring(13, s.length() - 5);
    }

    //
    //  Fonts
    //

    private final Paint SUPPORT_PAINT = new Paint();

    public synchronized float getStringWidth(Typeface font, float textSize, String string) {
        if (string == null) return 0;
        SUPPORT_PAINT.setTypeface(font);
        SUPPORT_PAINT.setTextSize(textSize);
        Rect bounds = new Rect();
        SUPPORT_PAINT.getTextBounds(string, 0, string.length(), bounds);
        return bounds.width();
    }

    public float getStringHeight(Typeface font, float textSize) {
        return getFontAscent(font, textSize) + getFontDescent(font, textSize);
    }

    public synchronized float getFontAscent(Typeface font, float textSize) {
        SUPPORT_PAINT.setTypeface(font);
        SUPPORT_PAINT.setTextSize(textSize);
        return -SUPPORT_PAINT.getFontMetrics().ascent;
    }

    public synchronized float getFontDescent(Typeface font, float textSize) {
        SUPPORT_PAINT.setTypeface(font);
        SUPPORT_PAINT.setTextSize(textSize);
        return SUPPORT_PAINT.getFontMetrics().descent;
    }


}
