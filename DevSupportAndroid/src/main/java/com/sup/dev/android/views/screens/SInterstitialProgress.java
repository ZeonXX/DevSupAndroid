package com.sup.dev.android.views.screens;

import android.widget.ProgressBar;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.java.tools.ToolsThreads;

public class SInterstitialProgress extends Screen {

    private final ProgressBar vProgress;

    public SInterstitialProgress() {
        this(false);
    }

    public SInterstitialProgress(boolean startNow) {
        super(R.layout.screen_interstitial_progress);

        vProgress = findViewById(R.id.progress);

        if (!startNow)
            vProgress.setVisibility(INVISIBLE);
        ToolsThreads.main(1000, () -> vProgress.setVisibility(VISIBLE));
    }
}



