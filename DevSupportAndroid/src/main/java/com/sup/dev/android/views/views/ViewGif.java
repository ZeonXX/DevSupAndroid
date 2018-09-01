package com.sup.dev.android.views.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Movie;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;

import com.sup.dev.android.R;
import com.sup.dev.android.app.SupAndroid;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;

public class ViewGif extends View {

    private static final int DEFAULT_MOVIE_VIEW_DURATION = 1000;

    private Movie movie;
    private long movieStart;
    private int currentAnimationTime;
    private float left;
    private float top;
    private float scale;
    private int measuredMovieWidth;
    private int measuredMovieHeight;
    private volatile boolean paused;
    private byte[] gifBytes;


    public ViewGif(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        final TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewGif, 0, R.style.Widget_GifView);
        int movieResourceId = array.getResourceId(R.styleable.ViewGif_android_src, 0);
        array.recycle();

        if (movieResourceId != 0)
            movie = Movie.decodeStream(getResources().openRawResource(movieResourceId));
    }


    //
    //  Methods
    //

    public void setGif(byte[] gifBytes) {
        this.gifBytes = gifBytes;
        movie = Movie.decodeStream(new ByteArrayInputStream(gifBytes));
        requestLayout();
    }


    public void setGif(Uri gifUri) {
        try {
            movie = Movie.decodeStream(SupAndroid.appContext.getContentResolver().openInputStream(gifUri));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        requestLayout();
    }

    public void setGif(int movieResourceId) {
        movie = Movie.decodeStream(getResources().openRawResource(movieResourceId));
        requestLayout();
    }

    public void clear() {
        movie = null;
        requestLayout();
    }


    public void play() {
        if (this.paused) {
            this.paused = false;

            movieStart = android.os.SystemClock.uptimeMillis() - currentAnimationTime;

            invalidate();
        }
    }

    public void pause() {
        if (!this.paused) {
            this.paused = true;

            invalidate();
        }

    }

    private void updateAnimationTime() {
        long now = android.os.SystemClock.uptimeMillis();

        if (movieStart == 0) {
            movieStart = now;
        }

        int dur = movie.duration();

        if (dur == 0) {
            dur = DEFAULT_MOVIE_VIEW_DURATION;
        }

        currentAnimationTime = (int) ((now - movieStart) % dur);
    }

    private void drawMovieFrame(Canvas canvas) {

        movie.setTime(currentAnimationTime);

        canvas.save();
        canvas.scale(scale, scale);
        movie.draw(canvas, left / scale, top / scale);
        canvas.restore();
    }

    //
    //  Events
    //


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (movie != null) {
            int movieWidth = movie.width();
            int movieHeight = movie.height();

            float scW = 1;
            if (widthMeasureSpec > 0) {
                int maximumWidth = MeasureSpec.getSize(widthMeasureSpec);
                if (movieWidth > maximumWidth)
                    scW = (float) movieWidth / (float) maximumWidth;
                else
                    scW = (float) maximumWidth / (float) movieWidth;
            }

            float scH = 1;
            if (heightMeasureSpec > 0) {
                int maximumHeight = MeasureSpec.getSize(heightMeasureSpec);
                if (movieHeight > maximumHeight)
                    scH = (float) movieHeight / (float) maximumHeight;
                else
                    scH = (float) maximumHeight / (float) movieHeight;
            }

            scale = Math.max(scW, scH);
            measuredMovieWidth = (int) (movieWidth * scale);
            measuredMovieHeight = (int) (movieHeight * scale);

            setMeasuredDimension(measuredMovieWidth, measuredMovieHeight);

        } else {
            setMeasuredDimension(getSuggestedMinimumWidth(), getSuggestedMinimumHeight());
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        left = (getWidth() - measuredMovieWidth) / 2f;
        top = (getHeight() - measuredMovieHeight) / 2f;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (movie != null) {
            if (!paused) {
                updateAnimationTime();
                drawMovieFrame(canvas);
                invalidate();
            } else {
                drawMovieFrame(canvas);
            }
        }
    }

    @Override
    public void onScreenStateChanged(int screenState) {
        super.onScreenStateChanged(screenState);
        invalidate();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        invalidate();
    }

    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        invalidate();
    }

    //
    //  Getters
    //


    public byte[] getGifBytes() {
        return gifBytes;
    }

    public boolean isPaused() {
        return this.paused;
    }

    public boolean isPlaying() {
        return !this.paused;
    }

}