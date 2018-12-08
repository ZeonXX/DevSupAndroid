package com.sup.dev.android.views.views

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet


open class ViewErrorEditText : android.support.v7.widget.AppCompatEditText {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle)

    override fun setError(pError: CharSequence?, pIcon: Drawable?) {
        when {
            pError == null -> super.setError(null, null)
            pError.isEmpty() -> setCompoundDrawables(null, null, pIcon, null)
            else -> super.setError(pError, pIcon)
        }
    }

}
