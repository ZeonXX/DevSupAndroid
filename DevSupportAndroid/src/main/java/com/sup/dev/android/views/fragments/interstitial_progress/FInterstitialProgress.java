package com.sup.dev.android.views.fragments.interstitial_progress;

import android.content.Context;
import android.widget.ProgressBar;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.libs.mvp.fragments.MvpFragment;

public class FInterstitialProgress extends MvpFragment<PInterstitialProgress> {

    private final ProgressBar vProgress;

    public FInterstitialProgress(Context context, PInterstitialProgress presenter) {
        super(context, presenter, R.layout.fragment_interstitial_progress);

        vProgress = findViewById(R.id.progress);
        vProgress.setVisibility(INVISIBLE);
    }

    //
    //  Presenter
    //

    public void show(){
        vProgress.setVisibility(VISIBLE);
    }

}
