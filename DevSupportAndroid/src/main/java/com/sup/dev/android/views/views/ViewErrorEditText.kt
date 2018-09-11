package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet


class ViewErrorEditText : android.support.design.widget.TextInputEditText {

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    fun setError(b: Boolean) {
        error = if (b) "" else null
    }

    override fun setError(pError: CharSequence, pIcon: Drawable) {
        setCompoundDrawables(null, null, pIcon, null)
    }

}
