package com.sup.dev.android.views.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sup.dev.android.R;
import com.sup.dev.android.tools.ToolsBitmap;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.views.layouts.LayoutAspectRatio;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;

public class ViewGifImage extends FrameLayout {

    private final LayoutAspectRatio view;
    private final ViewGif vGif;
    private final ImageView vImage;
    private final ProgressBar vProgress;
    private final View vFade;
    private final View vTouch;
    private final ViewIcon vIcon;

    private boolean customImageControl;
    private byte[] image;
    private byte[] gif;
    private Callback2<ImageView, Callback1<byte[]>> callbackImage;
    private Callback1<Callback1<byte[]>> callbackGif;

    public ViewGifImage(@NonNull Context context) {
        this(context, null);
    }

    public ViewGifImage(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        view = ToolsView.inflate(context, R.layout.view_gif);
        vGif = view.findViewById(R.id.view_gif_gif);
        vImage = view.findViewById(R.id.view_gif_image);
        vProgress = view.findViewById(R.id.view_gif_progress);
        vFade = view.findViewById(R.id.view_gif_fade);
        vIcon = view.findViewById(R.id.view_gif_icon);
        vTouch = view.findViewById(R.id.view_gif_touch);

        vTouch.setOnClickListener(v -> onClick());

        addView(view);
    }

    private void onClick() {
        if (callbackImage == null) return;
        if (image == null) {
            loadImage();
            return;
        }

        if (callbackGif == null) return;

        if (gif == null) {
            loadGif();
            return;
        }

        if (vGif.isPaused()) play();
        else pause();
    }

    public void pause() {
        vFade.setVisibility(VISIBLE);
        vIcon.setVisibility(VISIBLE);
        vIcon.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        vGif.pause();
    }

    public void play() {
        vFade.setVisibility(GONE);
        vIcon.setVisibility(GONE);
        vGif.play();
    }

    public void init(Callback2<ImageView, Callback1<byte[]>> callbackImage) {
        this.init(callbackImage, null);
    }

    public void init(Callback2<ImageView, Callback1<byte[]>> callbackImage, Callback1<Callback1<byte[]>> callbackGif) {
        this.callbackImage = callbackImage;
        this.callbackGif = callbackGif;
        image = null;
        gif = null;
        vGif.clear();
        vImage.setImageBitmap(null);
        vGif.setVisibility(GONE);
        vFade.setVisibility(VISIBLE);
        vIcon.setVisibility(GONE);

        loadImage();
    }

    public void loadImage() {
        Callback2<ImageView, Callback1<byte[]>> callback = this.callbackImage;
        vImage.setImageBitmap(null);
        vImage.setVisibility(VISIBLE);
        callback.callback(vImage, image -> {
            if (callback != this.callbackImage) return;
            this.image = image;
            if (image == null) {
                vFade.setVisibility(VISIBLE);
                vIcon.setVisibility(VISIBLE);
                vIcon.setImageResource(R.drawable.ic_refresh_white_24dp);
            } else {
                vFade.setVisibility(GONE);
                vProgress.setVisibility(GONE);
                if (!customImageControl) vImage.setImageBitmap(ToolsBitmap.decode(image));
                loadGif();
            }


        });
    }

    private void loadGif() {
        if (callbackGif == null) return;
        Callback1<Callback1<byte[]>> callback = this.callbackGif;
        vProgress.setVisibility(VISIBLE);
        callback.callback(gif -> {
            if (callback != this.callbackGif) return;
            this.gif = gif;
            if (gif == null) {
                vFade.setVisibility(VISIBLE);
                vIcon.setVisibility(VISIBLE);
                vIcon.setImageResource(R.drawable.ic_refresh_white_24dp);
            } else {
                vImage.setVisibility(GONE);
                vProgress.setVisibility(GONE);
                vGif.setVisibility(VISIBLE);
                vGif.setGif(gif);
                pause();
            }
        });
    }

    public void setRatio(int w, int h) {
        view.setRatio(w, h);
    }

    public void setCustomImageControl(boolean customImageControl) {
        this.customImageControl = customImageControl;
    }
}
