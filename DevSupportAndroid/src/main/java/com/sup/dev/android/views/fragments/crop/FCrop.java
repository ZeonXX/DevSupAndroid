package com.sup.dev.android.views.fragments.crop;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.libs.mvp.fragments.MvpFragment;
import com.sup.dev.android.views.widgets.cropper.ViewCropImage;

public class FCrop extends MvpFragment<PCrop> {

    private final ViewCropImage vCropImageView;
    private final View vButtonFinish;

    public FCrop(Context context, PCrop presenter) {
        super(context, presenter, R.layout.fragment_image_crop);

        vCropImageView = findViewById(R.id.crop);
        vButtonFinish = findViewById(R.id.fab);

        vButtonFinish.setOnClickListener(v -> presenter.onFinishClicked(vCropImageView.getCroppedImage()));
    }

    //
    //  Presenter
    //

    public void setBitmap(Bitmap bitmap){
        vCropImageView.setImageBitmap(bitmap);
    }

    public void setRation(int aw, int ah){
        vCropImageView.setAspectRatio(aw, ah);
    }



}
