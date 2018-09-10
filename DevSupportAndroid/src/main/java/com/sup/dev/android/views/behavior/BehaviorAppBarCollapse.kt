package com.sup.dev.android.views.behavior

import android.content.Context
import android.support.design.widget.CoordinatorLayout
import android.util.AttributeSet
import android.view.View


class BehaviorAppBarCollapse<V : View>(context: Context, attrs: AttributeSet) : BehaviorCollapse<V>(context, attrs) {

    override fun onDependentViewChanged(parent: CoordinatorLayout?, child: V?, dependency: View?): Boolean {
        child!!.y = dependency!!.y + dependency.height - child.height / 2
        return super.onDependentViewChanged(parent, child, dependency)
    }
}