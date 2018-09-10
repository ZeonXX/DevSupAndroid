package com.sup.dev.android.tools

import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.text.Html
import android.text.InputFilter
import android.widget.EditText
import com.sup.dev.java.tools.ToolsText


object ToolsTextAndroid {

    fun getFilterSpecChars(): InputFilter {
        return InputFilter{ source, start, end, dest, dstart, dend -> if (source != null && ToolsText.SPEC.contains(source)) "" else null }
    }

    fun getFilterLetterOrDigit(): InputFilter {
        return InputFilter{ source, start, end, dest, dstart, dend ->
            for (i in start until end)
                if (!Character.isLetterOrDigit(source.get(i)) && source.toString() != ".")
                     ""
            null
        }
    }

    //
    //  Fonts
    //

    private val SUPPORT_PAINT = Paint()

    fun empty(s: CharSequence?): Boolean {
        return s == null || s.length == 0
    }

    fun equalsNoCase(vararg s: String): Boolean {
        if (s == null) return true
        val s1 = s[0]
        for (i in 1 until s.size) {
            if (s1 == null && s[i] != null) return false
            if (s1 != null && s[i] == null) return false
            if (s1 == null && s[i] == null) continue
            if (s1.toLowerCase() != s[i].toLowerCase()) return false
        }
        return true
    }

    fun equals(vararg s: String): Boolean {
        if (s == null) return true
        val s1 = s[0]
        for (i in 1 until s.size) {
            if (s1 == null && s[i] != null) return false
            if (s1 != null && s[i] == null) return false
            if (s1 == null && s[i] == null) continue
            if (s1 != s[i]) return false
        }
        return true
    }

    fun setFilters(editText: EditText, vararg filters: InputFilter) {
        editText.filters = filters
    }

    fun setFilters(editText: EditText, allowed: String) {
        setFilters(editText, InputFilter{ source, start, end, dest, dstart, dend -> if (source != null && allowed.contains(source)) null else "" })
    }

    fun htmlFromEditText(editText: EditText): String {
        val s = Html.toHtml(editText.text)
        return if (s.length == 0) s else s.substring(13, s.length - 5)
    }

    @Synchronized
    fun getStringWidth(font: Typeface, textSize: Float, string: String?): Float {
        if (string == null) return 0f
        SUPPORT_PAINT.typeface = font
        SUPPORT_PAINT.textSize = textSize
        val bounds = Rect()
        SUPPORT_PAINT.getTextBounds(string, 0, string.length, bounds)
        return bounds.width().toFloat()
    }

    fun getStringHeight(font: Typeface, textSize: Float): Float {
        return getFontAscent(font, textSize) + getFontDescent(font, textSize)
    }

    @Synchronized
    fun getFontAscent(font: Typeface, textSize: Float): Float {
        SUPPORT_PAINT.typeface = font
        SUPPORT_PAINT.textSize = textSize
        return -SUPPORT_PAINT.fontMetrics.ascent
    }

    @Synchronized
    fun getFontDescent(font: Typeface, textSize: Float): Float {
        SUPPORT_PAINT.typeface = font
        SUPPORT_PAINT.textSize = textSize
        return SUPPORT_PAINT.fontMetrics.descent
    }


}
