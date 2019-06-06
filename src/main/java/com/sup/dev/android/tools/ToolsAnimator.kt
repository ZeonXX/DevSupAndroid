package com.sup.dev.android.tools

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.PorterDuff
import android.view.View
import android.view.animation.Animation
import android.widget.ImageView

object ToolsAnimator {

    fun flicker(view: View, color1: Int, color2: Int, duration: Int): ValueAnimator {

        val anim = ValueAnimator.ofObject(ArgbEvaluator(), color1, color2)
        anim.addUpdateListener { animator -> view.setBackgroundColor(animator.animatedValue as Int) }
        anim.repeatCount = Animation.INFINITE
        anim.repeatMode = ValueAnimator.REVERSE
        anim.duration = duration.toLong()
        anim.start()

        return anim
    }

    fun flickerFilter(view: ImageView, color1: Int, color2: Int, duration: Int): ValueAnimator {
        val anim = ValueAnimator.ofObject(ArgbEvaluator(), color1, color2)
        anim.addUpdateListener { animator -> view.setColorFilter(animator.animatedValue as Int, PorterDuff.Mode.SRC_IN) }
        anim.repeatCount = Animation.INFINITE
        anim.repeatMode = ValueAnimator.REVERSE
        anim.duration = duration.toLong()
        anim.start()

        return anim
    }
}
