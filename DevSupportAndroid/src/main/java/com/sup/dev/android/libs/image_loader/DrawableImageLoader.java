package com.sup.dev.android.libs.image_loader;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;

final class DrawableImageLoader extends BitmapDrawable {

    private static final float FADE_DURATION = 200f;

    private boolean animating;
    private long startTimeMillis;
    private int alpha = 0xFF;

    public DrawableImageLoader(Context context, Bitmap bitmap, boolean animate){
        super(context.getResources(), bitmap);
        if (animate) {
            animating = true;
            startTimeMillis = SystemClock.uptimeMillis();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        if (!animating) {
            super.draw(canvas);
        } else {
            float normalized = (SystemClock.uptimeMillis() - startTimeMillis) / FADE_DURATION;
            if (normalized >= 1f) {
                animating = false;
                super.draw(canvas);
            } else {
                int partialAlpha = (int) (alpha * normalized);
                super.setAlpha(partialAlpha);
                super.draw(canvas);
                super.setAlpha(alpha);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {
        this.alpha = alpha;
        super.setAlpha(alpha);
    }

}