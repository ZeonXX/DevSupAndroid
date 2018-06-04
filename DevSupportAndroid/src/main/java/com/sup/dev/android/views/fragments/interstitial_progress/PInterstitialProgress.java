package com.sup.dev.android.views.fragments.interstitial_progress;

import com.sup.dev.android.libs.mvp.fragments.MvpPresenter;
import com.sup.dev.java.tools.ToolsThreads;

public class PInterstitialProgress extends MvpPresenter<FInterstitialProgress> {

    public PInterstitialProgress() {
        this(false);
    }

    public PInterstitialProgress(boolean startNow) {
        super(FInterstitialProgress.class);

        if (startNow)
            show();
        else
            ToolsThreads.main(1000, () -> show());
    }

    private void show() {
        actionAdd(v -> v.show());
    }
}
