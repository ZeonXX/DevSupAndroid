package com.sup.dev.android.libs.mvp.presets.interstitial_progress;

import com.sup.dev.android.libs.mvp.fragments.MvpPresenter;
import com.sup.dev.java.tools.ToolsThreads;

public class PInterstitialProgress extends MvpPresenter<FInterstitialProgress> {

    public PInterstitialProgress() {
        super(FInterstitialProgress.class);

        ToolsThreads.main(1000, () -> actionAdd(v -> v.show()));
    }
}
