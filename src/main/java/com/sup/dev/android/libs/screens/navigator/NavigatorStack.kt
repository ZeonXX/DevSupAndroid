package com.sup.dev.android.libs.screens.navigator

import com.sup.dev.android.libs.screens.Screen
import java.util.ArrayList

class NavigatorStack {

    internal var backStack = ArrayList<Screen>()

    fun isEmpty() = backStack.isEmpty()

    fun isNotEmpty() = backStack.isNotEmpty()

    fun size() = backStack.size

}