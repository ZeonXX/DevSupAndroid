package com.sup.dev.android.tools;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.Html;
import android.text.InputFilter;
import android.widget.EditText;

public class ToolsText extends com.sup.dev.java.tools.ToolsText {

    public static boolean empty(CharSequence s) {
        return s == null || s.length() == 0;
    }

    public static boolean equalsNoCase(String... s) {
        if (s == null) return true;
        String s1 = s[0];
        for (int i = 1; i < s.length; i++) {
            if (s1 == null && s[i] != null) return false;
            if (s1 != null && s[i] == null) return false;
            if (s1 == null && s[i] == null) continue;
            if (!s1.toLowerCase().equals(s[i].toLowerCase())) return false;
        }
        return true;
    }

    public static boolean equals(String... s) {
        if (s == null) return true;
        String s1 = s[0];
        for (int i = 1; i < s.length; i++) {
            if (s1 == null && s[i] != null) return false;
            if (s1 != null && s[i] == null) return false;
            if (s1 == null && s[i] == null) continue;
            if (!s1.equals(s[i])) return false;
        }
        return true;
    }

    public static InputFilter getFilterSpecChars() {
        return (source, start, end, dest, dstart, dend) -> source != null && SPEC.contains(source) ? "" : null;
    }

    public static InputFilter getFilterLetterOrDigit() {
        return (source, start, end, dest, dstart, dend) -> {
            for (int i = start; i < end; i++)
                if (!Character.isLetterOrDigit(source.charAt(i)) && !source.toString().equals("."))
                    return "";

            return null;
        };
    }

    public static void setFilters(EditText editText, InputFilter... filters) {
        editText.setFilters(filters);
    }

    public static String htmlFromEditText(EditText editText) {
        String s = Html.toHtml(editText.getText());
        if (s.length() == 0) return s;
        return s.substring(13, s.length() - 5);
    }

    //
    //  Fonts
    //

    private static final Paint SUPPORT_PAINT = new Paint();

    public static synchronized float getStringWidth(Typeface font, float textSize, String string) {
        if (string == null) return 0;
        SUPPORT_PAINT.setTypeface(font);
        SUPPORT_PAINT.setTextSize(textSize);
        Rect bounds = new Rect();
        SUPPORT_PAINT.getTextBounds(string, 0, string.length(), bounds);
        return bounds.width();
    }

    public static float getStringHeight(Typeface font, float textSize) {
        return getFontAscent(font, textSize) + getFontDescent(font, textSize);
    }

    public static synchronized float getFontAscent(Typeface font, float textSize) {
        SUPPORT_PAINT.setTypeface(font);
        SUPPORT_PAINT.setTextSize(textSize);
        return -SUPPORT_PAINT.getFontMetrics().ascent;
    }

    public static synchronized float getFontDescent(Typeface font, float textSize) {
        SUPPORT_PAINT.setTypeface(font);
        SUPPORT_PAINT.setTextSize(textSize);
        return SUPPORT_PAINT.getFontMetrics().descent;
    }


}
