package com.sup.dev.android.views.views

import android.content.Context
import android.text.Spannable
import android.text.Spanned
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.MotionEvent
import com.sup.dev.java.libs.debug.err
import java.lang.IndexOutOfBoundsException

class ViewTextLinkable constructor(context: Context, attrs: AttributeSet) : ViewTextRounded(context, attrs) {

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if (e == null) return false

        if (text is Spanned && text is Spannable && layout != null && !isTextSelectable && (e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_DOWN)) {

            val x = e.x - totalPaddingLeft + scrollX
            val y = e.y - totalPaddingTop + scrollY
            val off = layout.getOffsetForHorizontal(layout.getLineForVertical(y.toInt()), x)
            val link = (text as Spannable).getSpans(off, off, ClickableSpan::class.java)

            if (link.isEmpty()) return false

        }

        return try {
            super.onTouchEvent(e)
        }catch (e:IndexOutOfBoundsException){
            err(e)
            true
        }
    }

}