package com.sup.dev.android.views.views.layouts;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.java.classes.Subscription;
import com.sup.dev.java.classes.callbacks.simple.Callback;
import com.sup.dev.java.classes.geometry.Line;
import com.sup.dev.java.classes.geometry.Point;
import com.sup.dev.java.classes.items.RangeF;
import com.sup.dev.java.tools.ToolsThreads;

public class LayoutZoom extends FrameLayout {

    private final RangeF range = new RangeF(1, 4);

    private Callback onZoom;
    private int animateTimeMs = 300;
    private float doubleTouchRadius;
    private View vBoundsView;

    private float zoom = 1.0f;
    private float translateX = 0f;
    private float translateY = 0f;
    private Subscription subscriptionAnimateZoom;

    public LayoutZoom(Context context) {
        this(context, null);
    }

    @SuppressLint("ClickableViewAccessibility")
    public LayoutZoom(Context context, AttributeSet attrs) {
        super(context, attrs);

        SupAndroid.initEditMode(this);
        doubleTouchRadius = ToolsView.dpToPx(16);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.LayoutZoom, 0, 0);
        range.min = a.getFloat(R.styleable.LayoutZoom_LayoutZoom_minZoom, range.min);
        range.max = a.getFloat(R.styleable.LayoutZoom_LayoutZoom_maxZoom, range.max);
        doubleTouchRadius = a.getDimension(R.styleable.LayoutZoom_LayoutZoom_doubleTouchRadius, doubleTouchRadius);
        animateTimeMs = a.getInteger(R.styleable.LayoutZoom_LayoutZoom_animateTimeMs, animateTimeMs);
        a.recycle();

        setOnTouchListener((view, motionEvent) -> {
            onTouch(motionEvent);
            return true;
        });

        zoom = range.min;
        updateParams();
    }

    //
    //  Events
    //

    private void onTouch(MotionEvent motionEvent) {

        if (motionEvent.getPointerCount() == 1 && motionEvent.getAction() == MotionEvent.ACTION_DOWN)
            doubleTouch(motionEvent.getX(), motionEvent.getY());
        if(motionEvent.getAction() == MotionEvent.ACTION_MOVE)
            clearDoubleTouch();

        if (motionEvent.getPointerCount() == 1 && (motionEvent.getAction() == MotionEvent.ACTION_MOVE || motionEvent.getAction() == MotionEvent.ACTION_DOWN))
            move(motionEvent.getX(), motionEvent.getY());
        else
            clearMove();

        if (motionEvent.getPointerCount() == 2 && (motionEvent.getAction() == MotionEvent.ACTION_MOVE || motionEvent.getAction() == MotionEvent.ACTION_DOWN || motionEvent.getAction() == MotionEvent.ACTION_POINTER_DOWN))
            zoom(new Line(motionEvent.getX(), motionEvent.getY(), motionEvent.getX(1), motionEvent.getY(1)));
        else
            clearZoomParams();
    }

    //
    //  Double Touch
    //

    private Point doubleTouch = new Point();
    private long doubleTouchTime;

    private void doubleTouch(float x, float y) {

        if (doubleTouchTime > System.currentTimeMillis() - 500 && doubleTouch.inRadius(x, y, doubleTouchRadius)) {
            animateZoom(zoom == 1 ? (range.max - range.min) / 2 + range.min : 1, x, y);
            doubleTouch.clear();
            doubleTouchTime = 0;
        } else {
            doubleTouch.set(x, y);
            doubleTouchTime = System.currentTimeMillis();
        }
    }

    private void clearDoubleTouch(){
        doubleTouch.clear();
    }

    //
    //  Move
    //

    private Point move = new Point();

    public void move(float x, float y) {

        if (move.isEmpty()) move.set(x, y);

        translateX += (x - move.x);
        translateY += (y - move.y);

        updateParams();

        move.set(x, y);
    }

    private void clearMove() {
        move.clear();
    }

    //
    //  Zoom
    //

    private Line zoomLine = new Line();
    private Point mid = new Point();

    private void zoom(Line touchLine) {

        if (zoomLine.isEmpty()) {
            zoomLine.set(touchLine);
            mid = touchLine.middle();
        }

        float zoomChange = ((touchLine.length() / zoomLine.length()) - 1) * zoom;

        zoom(zoomChange, mid.x, mid.y);
        zoomLine.set(touchLine);
    }

    private void clearZoomParams() {
        zoomLine.clear();
        mid.clear();
    }

    public void zoom(float zoomChange, float midX, float midY) {
        zoom += zoomChange;

        if (zoom < range.min) {
            zoomChange += range.min - zoom;
            zoom = range.min;
        }
        if (zoom > range.max) {
            zoomChange -= zoom - range.max;
            zoom = range.max;
        }

        translateX += (getWidth() / 2 - midX) * zoomChange;
        translateY += (getHeight() / 2 - midY) * zoomChange;

        updateParams();
    }


    public void animateZoom(float targetZoom, float midX, float midY) {

        if(subscriptionAnimateZoom != null)subscriptionAnimateZoom.unsubscribe();

        int fameCount = animateTimeMs * 60 / 1000;
        float step = (targetZoom - zoom) / fameCount;

        subscriptionAnimateZoom = ToolsThreads.timerMain(animateTimeMs/fameCount, animateTimeMs+100,
                subscription -> zoom(step, midX, midY));

    }

    public void clearZoom() {
        zoom = 1;
        translateX = 0;
        translateY = 0;
        updateParams();
    }

    //
    //  Params
    //

    private void updateParams() {

        View vBound = getBoundsView();

        if (getWidth() > vBound.getWidth() * zoom) translateX = 0;
        else
           translateX = new RangeF((getWidth() - vBound.getWidth() * zoom) / 2).toRange(translateX);

        if (getHeight() > vBound.getHeight() * zoom) translateY = 0;
        else
            translateY = new RangeF((getHeight() - vBound.getHeight() * zoom) / 2).toRange(translateY);

        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.setScaleX(zoom);
            v.setScaleY(zoom);
            v.setTranslationX(translateX);
            v.setTranslationY(translateY);
        }

        if (onZoom != null) onZoom.callback();
    }

    //
    //  State
    //

    @Override
    public Parcelable onSaveInstanceState() {

        Bundle bundle = new Bundle();
        bundle.putParcelable("SUPER_STATE", super.onSaveInstanceState());
        bundle.putFloat("range_min", range.min);
        bundle.putFloat("range_max", range.max);
        bundle.putInt("animateTimeMs", animateTimeMs);
        bundle.putFloat("doubleTouchRadius", doubleTouchRadius);
        bundle.putFloat("zoom", zoom);
        bundle.putFloat("translateX", translateX);
        bundle.putFloat("translateY", translateY);
        bundle.putFloat("w", getWidth());
        bundle.putFloat("h", getHeight());

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle bundle = (Bundle) state;
            range.min = bundle.getFloat("range_min");
            range.max = bundle.getFloat("range_max");
            animateTimeMs = bundle.getInt("animateTimeMs");
            doubleTouchRadius = bundle.getFloat("doubleTouchRadius");
            zoom = bundle.getFloat("zoom");
            translateX = bundle.getFloat("translateX");
            translateY = bundle.getFloat("translateY");
            float w = bundle.getFloat("w");
            float h = bundle.getFloat("h");

            state = bundle.getParcelable("SUPER_STATE");

            ToolsThreads.main(true, () -> {
                if (w == 0 || h == 0) return;
                translateX *= getWidth() > w ? getWidth() / w : w / getWidth();
                translateY *= getHeight() > h ? getHeight() / h : h / getHeight();
                updateParams();
            });
        }
        super.onRestoreInstanceState(state);
    }

    //
    //  Setters
    //

    public void setOnZoom(Callback onZoom) {
        this.onZoom = onZoom;
    }

    public void setMaxZoom(float maxZoom) {
        range.max = maxZoom;
        if (zoom > maxZoom) {
            zoom = maxZoom;
            updateParams();
        }
    }

    public void setMinZoom(float minZoom) {
        range.min = minZoom;
        if (zoom < minZoom) {
            zoom = minZoom;
            updateParams();
        }
    }

    public void setAnimateTimeMs(int animateTimeMs) {
        this.animateTimeMs = animateTimeMs;
    }

    public void setDoubleTouchRadius(float doubleTouchRadiusDp) {
        this.doubleTouchRadius = ToolsView.dpToPx(doubleTouchRadiusDp);
    }

    public void setBoundsView(View vBoundsView) {
        this.vBoundsView = vBoundsView;
    }

    //
    //  Getters
    //

    public float getZoom() {
        return zoom;
    }

    public float getTranslateX() {
        return translateX;
    }

    public float getTranslateY() {
        return translateY;
    }

    public View getBoundsView() {
        return vBoundsView == null ? (getChildCount() == 0 ? this : getChildAt(0)) : vBoundsView;
    }
}