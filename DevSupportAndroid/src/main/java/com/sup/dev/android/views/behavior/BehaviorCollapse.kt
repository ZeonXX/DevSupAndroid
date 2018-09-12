package com.sup.dev.android.views.behavior

import android.content.Context
import android.support.design.widget.AppBarLayout
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View

open class BehaviorCollapse<V : View>(context: Context, attrs: AttributeSet) : CoordinatorLayout.Behavior<V>(context, attrs) {

    override fun layoutDependsOn(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        return dependency is AppBarLayout
    }

    override fun onDependentViewChanged(parent: CoordinatorLayout, child: V, dependency: View): Boolean {
        child.scaleX = Math.max(1 - -dependency.y / 255, 0f)
        child.scaleY = Math.max(1 - -dependency.y / 255, 0f)
        child.visibility = if (child.scaleX == 0f) View.GONE else View.VISIBLE
        return super.onDependentViewChanged(parent, child, dependency)
    }
}