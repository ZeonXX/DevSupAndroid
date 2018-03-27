package com.sup.dev.android.views.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.views.animations.AnimationFocus;
import com.sup.dev.android.utils.interfaces.UtilsBitmap;
import com.sup.dev.android.utils.interfaces.UtilsResources;
import com.sup.dev.java.classes.animation.AnimationSpringColor;
import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsColor;

public class ViewIcon extends android.support.v7.widget.AppCompatImageView {

    private final Paint paint;
    private final AnimationFocus animationFocus;
    private final AnimationSpringColor animationSelectedBackground;

    private int src = 0;
    private int filter = 0x01FF0000;
    private int srcSelect = 0;
    private int filterSelect = 0x01FF0000;
    private int accentColor;
    private float padding;
    private int background = 0x01FF0000;
    private boolean useActiveBackground = true;
    private int circleColor = 0xFFFFFFFF;
    private float circleSize = 0;
    private boolean transparentOnDisabled = true;

    private boolean selected;

    public ViewIcon(Context context) {
        this(context, null);
    }

    public ViewIcon(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        SupAndroid.initEditMode(this);
        UtilsResources utilsResources = SupAndroid.di.utilsResources();

        accentColor = utilsResources.getAccentColor(context);
        int focusColor = utilsResources.getColor(R.color.focus);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ViewIcon, 0, 0);
        src = a.getResourceId(R.styleable.ViewIcon_android_src, src);
        boolean enabled = a.getBoolean(R.styleable.ViewIcon_android_enabled, true);
        srcSelect = a.getResourceId(R.styleable.ViewIcon_ViewIcon_srcSelect, srcSelect);
        filter = a.getColor(R.styleable.ViewIcon_ViewIcon_filter, filter);
        filterSelect = a.getColor(R.styleable.ViewIcon_ViewIcon_filterSelect, filterSelect);
        accentColor = a.getColor(R.styleable.ViewIcon_ViewIcon_accentColor, accentColor);
        focusColor = a.getColor(R.styleable.ViewIcon_ViewIcon_focusColor, focusColor);
        padding = a.getDimension(R.styleable.ViewIcon_ViewIcon_padding, padding);
        background = a.getColor(R.styleable.ViewIcon_ViewIcon_background, background);
        useActiveBackground = a.getBoolean(R.styleable.ViewIcon_ViewIcon_useActiveBackground, useActiveBackground);
        circleColor = a.getColor(R.styleable.ViewIcon_ViewIcon_circleColor, circleColor);
        circleSize = a.getDimension(R.styleable.ViewIcon_ViewIcon_circleSize, circleSize);
        transparentOnDisabled = a.getBoolean(R.styleable.ViewIcon_ViewIcon_transparent_on_disabled, transparentOnDisabled);
        a.recycle();

        animationFocus = new AnimationFocus(this, focusColor);
        animationSelectedBackground = new AnimationSpringColor(0x0000000, 200);

        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        setEnabled(enabled);
        updateIcon();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        updateIcon();
    }

    //
    //  Draw
    //

    @Override
    protected void onDraw(Canvas canvas) {

        animationSelectedBackground.update();

        float p = padding;
        float x = getWidth() / 2;
        float y = getHeight() / 2;
        float r = getWidth() / 2 - p;

        if (background != 0x01FF0000) {
            paint.setColor(isEnabled() ? background : ToolsColor.setAlpha(106, background));
            canvas.drawCircle(x, y, r, paint);
        }

        if (useActiveBackground) {
            paint.setColor(animationSelectedBackground.getColor());
            canvas.drawCircle(x, y, r, paint);
        }


        if (circleSize != 0) {
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor((transparentOnDisabled && !isEnabled()) ? ToolsColor.setAlpha(106, circleColor) : circleColor);
            paint.setStrokeWidth(circleSize);
            canvas.drawCircle(x, y, r - (circleSize / 2), paint);
            paint.setStyle(Paint.Style.FILL);
        }

        super.onDraw(canvas);

        paint.setColor(animationFocus.update());
        canvas.drawCircle(x, y, r, paint);

        if (animationSelectedBackground.isNeedUpdate())
            invalidate();

    }

    private void updateIcon() {

        animationSelectedBackground.to(selected ? (isEnabled() ? accentColor : ToolsColor.setAlpha(106, accentColor)) : 0x00000000);

        super.setImageResource((selected && srcSelect != 0) ? srcSelect : src);

        int myFilter = (selected && filterSelect != 0x01FF0000) ? filterSelect : filter;
        if (myFilter != 0x01FF0000) setColorFilter(myFilter, PorterDuff.Mode.SRC_ATOP);

        setAlpha((!isEnabled() && transparentOnDisabled)?106:255);
    }


    //
    //  Setters
    //


    public void setTransparentOnDisabled(boolean transparentOnDisabled) {
        this.transparentOnDisabled = transparentOnDisabled;
        updateIcon();
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        super.setOnTouchListener(l);
        if (l == null) animationFocus.resetTouchListener();
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        super.setOnFocusChangeListener(l);
        if (l == null) animationFocus.resetOnFocusChangedListener();
    }

    public void setIconSelected(boolean selected) {
        this.selected = selected;
        updateIcon();
        animationFocus.setClickAnimationEnabled(!selected);
    }

    public void setIconBackgroundColor(int color){
        this.background = color;
        invalidate();
    }

    @Override
    public void setImageResource(int resId) {
        this.src = resId;
        updateIcon();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        setClickable(enabled);
        setFocusable(enabled);
        animationFocus.updateFocusColor();
        updateIcon();
    }

    public void setPadding(float padding) {
        this.padding = padding;
        invalidate();
    }

    //
    //  Getters
    //

    public boolean isIconSelected() {
        return selected;
    }
}

