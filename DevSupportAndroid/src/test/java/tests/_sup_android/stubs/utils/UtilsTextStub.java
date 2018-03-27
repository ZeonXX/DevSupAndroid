package tests._sup_android.stubs.utils;

import android.graphics.Typeface;
import android.text.InputFilter;
import android.widget.EditText;

import com.sup.dev.android.utils.interfaces.UtilsText;

public class UtilsTextStub implements UtilsText {
    @Override
    public InputFilter getFilterSpecChars() {
        return null;
    }

    @Override
    public InputFilter getFilterLetterOrDigit() {
        return null;
    }

    @Override
    public String replaceKeys(int textRes, Object... values) {
        return null;
    }

    @Override
    public String replaceKeys(String text, Object... values) {
        return null;
    }

    @Override
    public void setFilters(EditText editText, InputFilter... filters) {

    }

    @Override
    public String htmlFromEditText(EditText editText) {
        return null;
    }

    @Override
    public float getStringWidth(Typeface font, float textSize, String string) {
        return 0;
    }

    @Override
    public float getFontAscent(Typeface font, float textSize) {
        return 0;
    }

    @Override
    public float getFontDescent(Typeface font, float textSize) {
        return 0;
    }
}
