package com.sup.dev.android.views.views

import android.content.Context
import android.text.Selection
import android.text.Spannable
import android.text.Spanned
import android.text.style.ClickableSpan
import android.util.AttributeSet
import android.view.MotionEvent

class ViewTextLinkable constructor(context: Context, attrs: AttributeSet) : ViewTextRounded(context, attrs) {

    override fun onTouchEvent(e: MotionEvent?): Boolean {
        if(e == null) return false
        if (text is Spanned && text is Spannable) {
            val buffer = text as Spannable

            if (e.action == MotionEvent.ACTION_UP || e.action == MotionEvent.ACTION_DOWN) {
                var x = e.x
                var y = e.y

                x -= totalPaddingLeft
                y -= totalPaddingTop

                x += scrollX
                y += scrollY

                val off = layout.getOffsetForHorizontal( layout.getLineForVertical(y.toInt()), x)

                val link = buffer.getSpans(off, off, ClickableSpan::class.java)


                if (link.isNotEmpty()) {
                    if (e.action  == MotionEvent.ACTION_UP) {
                        link[0].onClick(this)
                    } else if (e.action  == MotionEvent.ACTION_DOWN) {
                        Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]))
                    }
                    return true
                }else{
                    return false
                }
            }

        }

        return super.onTouchEvent(e)
    }

}