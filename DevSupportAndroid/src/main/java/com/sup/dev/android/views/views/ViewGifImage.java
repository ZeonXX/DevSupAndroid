package com.sup.dev.android.views.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.sup.dev.android.R;
import com.sup.dev.android.libs.image_loader.ImageLoader;
import com.sup.dev.android.libs.image_loader.ImageLoaderId;
import com.sup.dev.android.libs.screens.navigator.Navigator;
import com.sup.dev.android.tools.ToolsBitmap;
import com.sup.dev.android.tools.ToolsView;
import com.sup.dev.android.views.screens.SImageView;
import com.sup.dev.android.views.views.layouts.LayoutAspectRatio;
import com.sup.dev.java.classes.callbacks.simple.Callback1;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.libs.debug.Debug;

public class ViewGifImage extends FrameLayout {

    private final LayoutAspectRatio view;
    private final ViewGif vGif;
    private final ImageView vImage;
    private final ProgressBar vProgress;
    private final View vFade;
    private final View vTouch;
    private final ViewIcon vIcon;

    private long imageId;
    private long gifId;

    private boolean customImageControl;
    private byte[] image;
    private byte[] gif;
    private Callback2<ImageView, Callback1<byte[]>> callbackImage;
    private Callback1<Callback1<byte[]>> callbackGif;
    private OnClickListener onClickListener;

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
        vTouch.setOnLongClickListener(v -> {
            if (onClickListener != null) onClickListener.onClick(v);
            else if (gifId != 0 || imageId != 0) Navigator.to(new SImageView(gifId == 0 ? imageId : gifId, gifId != 0));
            else return true;
            return true;
        });

        addView(view);
    }

    @Override
    public void setClickable(boolean clickable) {
        vTouch.setVisibility(clickable ? VISIBLE : GONE);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        vTouch.setOnTouchListener(l);
    }

    private void onClick() {

        if (callbackImage != null && image == null) {
            loadImage();
            return;
        }

        if (callbackGif != null) {
            if (gif == null) loadGif();
            else if (vGif.isPaused()) play();
            else pause();
        } else {
            if(imageId != 0) Navigator.to(new SImageView(imageId));
            else if (onClickListener != null) onClickListener.onClick(this);
        }

    }

    public void pause() {
        vFade.setVisibility(VISIBLE);
        vIcon.setVisibility(VISIBLE);
        vIcon.setImageResource(R.drawable.ic_play_arrow_white_24dp);
        vGif.pause();
    }

    public void play() {
        vProgress.setVisibility(GONE);
        vImage.setVisibility(GONE);
        vFade.setVisibility(GONE);
        vIcon.setVisibility(GONE);
        vGif.setVisibility(VISIBLE);
        vGif.play();
    }

    public void setImageBitmap(Bitmap bitmap) {
        callbackImage = null;
        callbackGif = null;
        vProgress.setVisibility(GONE);
        vFade.setVisibility(GONE);
        vIcon.setVisibility(GONE);
        vGif.setVisibility(GONE);
        vImage.setVisibility(VISIBLE);
        vImage.setImageBitmap(bitmap);
    }

    public void clear() {
        onClickListener = null;
        customImageControl = false;
        callbackImage = null;
        callbackGif = null;
        image = null;
        gif = null;
        vGif.clear();
        vImage.setImageBitmap(null);
        vGif.setVisibility(GONE);
        vFade.setVisibility(VISIBLE);
        vIcon.setVisibility(GONE);
    }

    public void setImageLoader(Callback2<ImageView, Callback1<byte[]>> callbackImage) {
        this.callbackImage = callbackImage;
    }

    public void setGifLoader(Callback1<Callback1<byte[]>> callbackGif) {
        this.callbackGif = callbackGif;
    }

    public void loadImage() {
        Callback2<ImageView, Callback1<byte[]>> callback = this.callbackImage;
        vImage.setImageBitmap(null);
        vImage.setVisibility(VISIBLE);
        vFade.setVisibility(GONE);
        vProgress.setVisibility(GONE);
        vIcon.setVisibility(GONE);
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

    public void loadGif() {
        if (callbackGif == null) return;
        Callback1<Callback1<byte[]>> callback = this.callbackGif;
        vImage.setVisibility(VISIBLE);
        vProgress.setVisibility(VISIBLE);
        vFade.setVisibility(VISIBLE);
        vIcon.setVisibility(GONE);
        callback.callback(gif -> {
            if (callback != this.callbackGif) return;
            this.gif = gif;
            if (gif == null) {
                vFade.setVisibility(VISIBLE);
                vIcon.setVisibility(VISIBLE);
                vIcon.setImageResource(R.drawable.ic_refresh_white_24dp);
            } else {
                vGif.setOnGifLoaded(this::play);
                vGif.setGif(gif);
            }
        });
    }

    public void setRatio(int w, int h) {
        view.setRatio(w, h);
    }

    public void setCustomImageControl(boolean customImageControl) {
        this.customImageControl = customImageControl;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public void setGifId(long gifId) {
        this.gifId = gifId;
    }

    //
    //  Inits
    //

    public void init(long imageId, long gifId) {
        init(imageId, gifId, 0, 0);
    }

    public void init(long imageId, long gifId, int w, int h) {
        init(imageId, gifId, w, h, false);
    }

    public void init(long imageId, long gifId, int w, int h, boolean ignoreRation) {
        clear();
        setCustomImageControl(true);
        if (!ignoreRation) setRatio(w, h);
        setImageLoader(
                (vImageGif, callbackImage) -> ImageLoader.load(new ImageLoaderId(imageId).sizes(w, h).setImage(vImageGif).onLoaded(callbackImage)));
        if (gifId != 0) setGifLoader(
                callbackGif -> ImageLoader.load(new ImageLoaderId(gifId).onLoaded(callbackGif)));

        if (imageId != 0) loadImage();
        else if (gifId != 0) loadGif();

        setImageId(imageId);
        setGifId(gifId);
    }

}
