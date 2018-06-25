package com.sup.dev.android.views.screens;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.View;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.libs.screens.SNavigator;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.android.views.widgets.WidgetProgressTransparent;
import com.sup.dev.android.views.dialogs.DialogWidget;
import com.sup.dev.android.views.views.cropper.ViewCropImage;
import com.sup.dev.java.classes.callbacks.simple.Callback2;

public class SCrop extends Screen {


    private final ViewCropImage vCropImageView;
    private final View vFinish;
    private final View vAll;
    private final Callback2<SCrop, Bitmap> onCrop;

    private boolean autoBackOnCrop = true;
    private boolean locked;

    private DialogWidget dialogProgress;

    public SCrop(Bitmap bitmap, Callback2<SCrop, Bitmap> onCrop) {
        this(bitmap, 0, 0, onCrop);
    }

    public SCrop(Bitmap bitmap, int aw, int ah, Callback2<SCrop, Bitmap> onCrop) {
        super(R.layout.screen_image_crop);

        this.onCrop = onCrop;

        vCropImageView = findViewById(R.id.crop);
        vFinish = findViewById(R.id.fab);
        vAll = findViewById(R.id.all);

        if (aw > 0 && ah > 0) vCropImageView.setAspectRatio(aw, ah);
        vCropImageView.setImageBitmap(bitmap);

        vAll.setOnClickListener(v -> vCropImageView.setCropRect(new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight())));

        vFinish.setOnClickListener(v -> {
            if (onCrop != null) {
                if (autoBackOnCrop) SNavigator.back();
                else setLock(false);

                onCrop.callback(this, vCropImageView.getCroppedImage());
            }
        });
    }

    public SCrop setLock(boolean b) {
        locked = b;
        if (b) {
            if (locked) dialogProgress = new WidgetProgressTransparent().asDialogShow();
        } else {
            if (dialogProgress != null) {
                dialogProgress.hide();
                dialogProgress = null;
            }
        }
        return this;
    }

    public SCrop setAutoBackOnCrop(boolean autoBackOnCrop) {
        this.autoBackOnCrop = autoBackOnCrop;
        return this;
    }

    @Override
    public boolean onBackPressed() {
        return locked;
    }

    public void back() {
        SNavigator.back();
    }

}
