package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.Color
import android.support.annotation.AttrRes
import android.support.annotation.StyleRes
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import com.sup.dev.android.R
import com.sup.dev.android.tools.ToolsView
import com.sup.dev.android.views.views.layouts.LayoutCorned

class ViewChipMini @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        @AttrRes defStyleAttr: Int = 0,
        @StyleRes defStyleRes: Int = 0
) : LayoutCorned(context, attrs, defStyleAttr, defStyleRes) {

    private val vText: TextView = ToolsView.inflate(context, R.layout.z_text_caption)

    init {
        val a = context.obtainStyledAttributes(attrs, R.styleable.ViewChipMini)
        val text = a.getString(R.styleable.ViewChipMini_android_text) ?: ""
        val textColor = a.getColor(R.styleable.ViewChipMini_android_textColor, 0)
        a.recycle()

        if (textColor != 0) vText.setTextColor(textColor)
        setChipMode(true)
        setCircleMode(true)
        setText(text)

        addView(vText)

        vText.layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
        vText.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        (vText.layoutParams as LayoutParams).marginStart = ToolsView.dpToPx(4).toInt()
        (vText.layoutParams as LayoutParams).marginEnd = ToolsView.dpToPx(4).toInt()
        (vText.layoutParams as LayoutParams).gravity = Gravity.CENTER
        vText.gravity = Gravity.CENTER
        vText.textSize = 9f
    }

    //
    //  Setters
    //

    fun setText(text: String?) {
        vText.text = text
        visibility = if (text == null || text.isEmpty()) View.GONE else View.VISIBLE
     }

    //
    //  Getters
    //

    fun getText() = vText.text

}