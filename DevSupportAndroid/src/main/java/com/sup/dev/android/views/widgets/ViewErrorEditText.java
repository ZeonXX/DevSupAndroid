package com.sup.dev.android.views.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

public class ViewErrorEditText extends android.support.design.widget.TextInputEditText {

    public ViewErrorEditText(Context context) {
        super(context);
    }

    public ViewErrorEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewErrorEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setError(boolean b){
        setError(b?"":null);
    }

    @Override
    public void setError(CharSequence pError, Drawable pIcon) {
        setCompoundDrawables(null, null, pIcon, null);
    }

}
