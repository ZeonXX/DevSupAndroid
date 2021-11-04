package com.sup.dev.android.magic_box

import android.support.design.widget.CollapsingToolbarLayout
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.tools.ToolsThreads

object WorkaroundCollapsingToolbarSkrim {

    fun suppressSkrim(bar: CollapsingToolbarLayout?) {
        if (bar != null) {
            try {
                val field = CollapsingToolbarLayout::class.java.getDeclaredField("mScrimAnimator")
                field.isAccessible = true

                val scrimAnimationDuration = bar.scrimAnimationDuration
                bar.scrimAnimationDuration = 0
                ToolsThreads.main(scrimAnimationDuration) {
                    try {
                        field.set(bar, null)
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    }

                    bar.scrimAnimationDuration = scrimAnimationDuration
                }
            } catch (e: Exception) {
                log(e)
            }

        }
    }

}