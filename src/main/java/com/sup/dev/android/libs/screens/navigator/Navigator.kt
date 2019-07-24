package com.sup.dev.android.libs.screens.navigator

import android.os.Build
import android.view.View
import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.android.libs.screens.ScreenProtected
import com.sup.dev.java.classes.callbacks.CallbacksList2
import java.util.ArrayList

object Navigator {

    var currentStack = NavigatorStack()
        private set

    val TO = NavigationAction.to().immutable()
    val SET = NavigationAction.set().immutable()
    val REPLACE = NavigationAction.replace().immutable()
    val REORDER = NavigationAction.reorder().immutable()

    //
    //  Listeners
    //

    private val onBack = CallbacksList2<Screen, Screen>()
    private val onBackCallbacks = ArrayList<() -> Boolean>()
    private val onScreenChangedCallbacks = ArrayList<() -> Boolean>()

    enum class Animation {
        IN, OUT, ALPHA, NONE
    }

    //
    //  Views
    //

    fun removeScreen(screen: Screen) {
        screen.onDestroy()
        currentStack.stack.remove(screen)
    }

    //
    //  Navigation
    //

    fun action(action: NavigationAction, screen: Screen) {
        action.doAction(screen)
    }

    @JvmOverloads
    fun to(screen: Screen, animation: Animation = Animation.IN) {
        if (!currentStack.stack.isEmpty()) {
            if (!getCurrent()!!.isBackStackAllowed) {
                removeScreen(getCurrent()!!)
            } else {
                getCurrent()!!.onPause()
            }
            if (screen.isSingleInstanceInBackStack) {
                removeAll(screen.javaClass)
            }
        }
        currentStack.stack.add(screen)
        setCurrentViewNew(animation)
    }

    fun replace(screen: Screen, newScreen: Screen) {
        if (currentStack.stack.isEmpty()) return
        if (getCurrent() == screen) {
            replace(newScreen)
            return
        }
        for (i in currentStack.stack.indices) if (currentStack.stack[i] == screen) currentStack.stack[i] = newScreen
    }

    fun replace(screen: Screen) {
        if (!currentStack.stack.isEmpty()) removeScreen(getCurrent()!!)
        to(screen, Animation.ALPHA)
    }

    fun set(screen: Screen, animation: Animation = Animation.ALPHA) {
        while (currentStack.stack.size != 0) removeScreen(currentStack.stack[0])
        to(screen, animation)
    }

    fun reorder(screen: Screen) {
        currentStack.stack.remove(screen)
        to(screen)
    }

    fun reorderOrCreate(viewClass: Class<out Screen>, provider: () -> Screen) {

        if (getCurrent() != null && getCurrent()!!.javaClass == viewClass)
            return

        for (i in currentStack.stack.size - 1 downTo -1 + 1)
            if (currentStack.stack[i].javaClass == viewClass) {
                reorder(currentStack.stack[i])
                return
            }

        to(provider.invoke())
    }

    fun removeAllEqualsAndTo(view: Screen) {

        var i = 0
        while (i < currentStack.stack.size) {
            if (currentStack.stack[i].equalsNView(view))
                remove(currentStack.stack[i--])
            i++
        }

        to(view)
    }

    fun removeAll(viewClass: Class<out Screen>) {
        val current = getCurrent()
        val needUpdate = current != null && current.javaClass == viewClass

        var i = 0
        while (i < currentStack.stack.size) {
            if (currentStack.stack[i].javaClass == viewClass) {
                remove(currentStack.stack[i--])
            }
            i++
        }

        if (needUpdate) setCurrentViewNew(Animation.OUT)
    }


    fun back(): Boolean {
        if (!hasBackStack()) return false

        val current = getCurrent()
        removeScreen(current!!)
        setCurrentViewNew(Animation.OUT)

        onBack.invoke(current, current)

        return true
    }

    fun remove(view: Screen) {
        if (hasBackStack() && getCurrent() == view)
            back()
        else
            removeScreen(view)
    }

    fun setStack(stack: NavigatorStack) {
        if (currentStack == stack) return
        val oldStack = currentStack
        currentStack = stack
        for (screen in oldStack.stack) screen.onStackChanged()
        setCurrentViewNew(Animation.ALPHA)
    }

    fun closeProtected(onClose: () -> Unit) {
        val current = getCurrent()
        if (current is ScreenProtected) current.onProtectedClose(onClose)
        else onClose.invoke()
    }

    //
    //  Activity Callbacks
    //

    private fun setCurrentViewNew(animation: Animation) {
        setCurrentView(animation)

        val array = Array(onScreenChangedCallbacks.size) { onScreenChangedCallbacks[it] }
        for (i in array) if (i.invoke()) onScreenChangedCallbacks.remove(i)
    }

    fun resetCurrentView() {
        setCurrentView(Animation.NONE)
    }

    private fun setCurrentView(animation: Animation) {
        val screen = getCurrent() ?: return

        SupAndroid.activity!!.setScreen(screen, animation)

        if (SupAndroid.activity != null) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                val statusBarColor = screen.statusBarColor
                if (SupAndroid.activity?.window?.statusBarColor != statusBarColor) SupAndroid.activity?.window?.statusBarColor = statusBarColor
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val statusBarIsLight = if (screen.statusBarIsLight) View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR else View.SYSTEM_UI_FLAG_VISIBLE
                if (SupAndroid.activity!!.window.decorView.systemUiVisibility != statusBarIsLight) SupAndroid.activity!!.window.decorView.systemUiVisibility = statusBarIsLight
            }
        }

        if (getCurrent() != null) screen.onResume()
    }

    fun onActivityStop() {
        if (getCurrent() != null) getCurrent()!!.onPause()
    }

    fun onActivityConfigChanged() {
        if (getCurrent() != null) getCurrent()!!.onConfigChanged()
    }

    fun onBackPressed(): Boolean {

        for (i in onBackCallbacks.size - 1 downTo -1 + 1) {
            val c = onBackCallbacks[i]
            if (c.invoke()) {
                onBackCallbacks.remove(c)
                return true
            }
        }

        return getCurrent() != null && getCurrent()!!.onBackPressed() || back()
    }

    //
    //  Getters
    //


    fun getStackSize(): Int {
        return currentStack.stack.size
    }

    fun getPrevious(): Screen? {
        return if (hasPrevious()) currentStack.stack[currentStack.stack.size - 2] else null
    }

    fun getCurrent(): Screen? {
        return if (currentStack.stack.isEmpty()) null else currentStack.stack[currentStack.stack.size - 1]
    }

    fun isEmpty(): Boolean {
        return currentStack.stack.isEmpty()
    }


    fun hasBackStack(): Boolean {
        return currentStack.stack.size > 1
    }

    fun hasPrevious(): Boolean {
        return currentStack.stack.size > 1
    }

    fun addOnBackScreenListener(onBack: (Screen?, Screen?) -> Unit) {
        Navigator.onBack.remove(onBack)
        Navigator.onBack.add(onBack)
    }

    fun removeOnBackScreenListener(onBack: (Screen?, Screen?) -> Unit) {
        Navigator.onBack.remove(onBack)
    }

    fun addOnBack(onBack: () -> Boolean) {
        if (onBackCallbacks.contains(onBack)) onBackCallbacks.remove(onBack)
        onBackCallbacks.add(onBack)
    }

    fun removeOnBack(onBack: () -> Boolean) {
        onBackCallbacks.remove(onBack)
    }

    fun addOnScreenChanged(onScreenChanged: () -> Boolean) {
        if (onScreenChangedCallbacks.contains(onScreenChanged)) onScreenChangedCallbacks.remove(onScreenChanged)
        onScreenChangedCallbacks.add(onScreenChanged)
    }

    fun removeOnScreenChanged(onScreenChanged: () -> Boolean) {
        onScreenChangedCallbacks.remove(onScreenChanged)
    }

}
