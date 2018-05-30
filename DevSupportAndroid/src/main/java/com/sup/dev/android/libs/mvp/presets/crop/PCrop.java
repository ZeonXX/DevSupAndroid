package com.sup.dev.android.libs.mvp.presets.crop;

import android.graphics.Bitmap;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.mvp.fragments.MvpPresenter;
import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.views.dialogs.BaseDialog;
import com.sup.dev.android.views.dialogs.DialogProgressTransparent;
import com.sup.dev.java.classes.callbacks.simple.Callback2;

public class PCrop extends MvpPresenter<FCrop> {

    private final Callback2<PCrop, Bitmap> onCrop;
    private boolean autoBackOnCrop = true;
    private boolean locked;

    private BaseDialog dialogProgress;

    public PCrop(Bitmap bitmap, Callback2<PCrop, Bitmap> onCrop) {
        this(bitmap, 0, 0, onCrop);
    }

    public PCrop(Bitmap bitmap, int aw, int ah, Callback2<PCrop, Bitmap> onCrop) {
        super(FCrop.class);

        this.onCrop = onCrop;

        if (aw > 0 && ah > 0) actionAdd(v -> v.setRation(aw, ah));
        actionAdd(v -> v.setBitmap(bitmap));
    }

    public PCrop setLock(boolean b) {
        locked = b;
        if (b) {
            SupAndroid.mvpActivity(activity -> {
                if (locked) dialogProgress = new DialogProgressTransparent(activity).show();
            });
        } else {
            if (dialogProgress != null) {
                dialogProgress.hide();
                dialogProgress = null;
            }
        }
        return this;
    }

    public PCrop setAutoBackOnCrop(boolean autoBackOnCrop) {
        this.autoBackOnCrop = autoBackOnCrop;
        return this;
    }

    @Override
    public boolean onBackPressed() {
        return locked;
    }

    public void back() {
        MvpNavigator.back();
    }

    //
    //  Presenter
    //

    public void onFinishClicked(Bitmap bitmap) {
        if (onCrop != null) {
            if (autoBackOnCrop) MvpNavigator.back();
            else setLock(false);

            onCrop.callback(this, bitmap);
        }
    }

}
