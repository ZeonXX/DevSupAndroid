package com.sup.dev.android.magic_box;

import android.support.design.widget.CollapsingToolbarLayout;

import com.sup.dev.java.libs.debug.Debug;
import com.sup.dev.java.tools.ToolsThreads;

import java.lang.reflect.Field;

public class WorkaroundCollapsingToolbarSkrim {

    public static void suppressSkrim(CollapsingToolbarLayout bar){
        if (bar != null) {
            try {
                Field field = CollapsingToolbarLayout.class.getDeclaredField("mScrimAnimator");
                field.setAccessible(true);

                long scrimAnimationDuration = bar.getScrimAnimationDuration();
                bar.setScrimAnimationDuration(0);
                ToolsThreads.INSTANCE.main(scrimAnimationDuration, () -> {
                    try {
                        field.set(bar, null);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    bar.setScrimAnimationDuration(scrimAnimationDuration);
                });
            } catch (Exception e) {
                Debug.INSTANCE.log(e);
            }

        }
    }

}
