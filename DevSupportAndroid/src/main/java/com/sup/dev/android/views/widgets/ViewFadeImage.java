package com.sup.dev.android.views.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.views.widgets._support.FadeView;
import com.sup.dev.java.classes.animation.AnimationSpring;
import com.sup.dev.java.tools.ToolsColor;

public class ViewFadeImage extends android.support.v7.widget.AppCompatImageView  implements FadeView {

    private final Paint paint = new Paint();
    private final AnimationSpring animationFade;

    private int fadeColor = 0x01FF0000;

    public ViewFadeImage(Context context) {
        this(context, null);
    }

    public ViewFadeImage(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewFadeImage(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        SupAndroid.initEditMode(this);
        animationFade = new AnimationSpring(0, AnimationSpring.SpeedType.TIME_MS, 400);
        paint.setAntiAlias(true);
        if(getBackground() == null) setBackgroundColor(context.getResources().getColor(R.color.focus));
    }

    @Override
    public void makeFade() {
        animationFade.set(ToolsColor.alpha(fadeColor));
        animationFade.to(0);
        invalidate();
    }

    @Override
    public void stopFade() {
        animationFade.set(0);
        invalidate();
    }

    @Override
    public void setBackground(Drawable background) {
        super.setBackground(background);
        if(background instanceof ColorDrawable) fadeColor = ((ColorDrawable)background).getColor();
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        if(fadeColor != 0x01FF0000 && animationFade.getValue() != 0) {
            paint.setColor(ToolsColor.setAlpha((int) animationFade.getValue(), fadeColor));
            canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
        }

        if (animationFade.isNeedUpdate()) {
            animationFade.update();
            invalidate();
        }
    }


    public void setFadeColor(int color) {
        this.fadeColor = color;
    }

}
