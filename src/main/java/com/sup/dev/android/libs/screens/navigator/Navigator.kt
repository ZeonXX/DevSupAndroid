package com.sup.dev.android.libs.screens.navigator

import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.java.classes.callbacks.CallbacksList2
import java.util.ArrayList

object Navigator {

    var currentStack = NavigatorStack()
        private set

    val TO = NavigationAction.to().immutable()
    val SET = NavigationAction.set().immutable()
    val REPLACE = NavigationAction.replace().immutable()
    val REORDER = NavigationAction.reorder().immutable()
    val TO_BACK_STACK_OR_NEW = NavigationAction.toBackStackOrNew().immutable()

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

    fun to(screen: Screen, animation: Animation = Animation.IN, checkCanHideCurrent: Boolean=true) {
        checkHideCurrent(checkCanHideCurrent) {
            if (currentStack.stack.isNotEmpty()) {
                if (!getCurrent()!!.isBackStackAllowed) {
                    removeScreen(getCurrent()!!)
                } else {
                    getCurrent()!!.onPause()
                }
                if (screen.isSingleInstanceInBackStack) {
                    removeAll(screen.javaClass, false)
                }
            }
            currentStack.stack.add(screen)
            setCurrentViewNew(animation)
        }
    }

    fun replace(screen: Screen, checkCanHideCurrent: Boolean=true) {
        checkHideCurrent(checkCanHideCurrent) {
            if (currentStack.stack.isNotEmpty()) removeScreen(getCurrent()!!)
            to(screen, Animation.ALPHA, false)
        }
    }

    fun replace(screen: Screen, newScreen: Screen, checkCanHideCurrent: Boolean=true) {
        checkHideCurrent(checkCanHideCurrent) {
            if (currentStack.stack.isNotEmpty()) {
                if (getCurrent() == screen) {
                    replace(newScreen, false)
                } else {
                    for (i in currentStack.stack.indices) if (currentStack.stack[i] == screen) currentStack.stack[i] = newScreen
                }
            }
        }
    }

    fun set(screen: Screen, animation: Animation = Animation.ALPHA, checkCanHideCurrent: Boolean=true) {
        checkHideCurrent(checkCanHideCurrent) {
            while (currentStack.stack.size != 0) removeScreen(currentStack.stack[0])
            to(screen, animation, false)
        }
    }

    fun reorder(screen: Screen, checkCanHideCurrent: Boolean=true) {
        checkHideCurrent(checkCanHideCurrent) {
            currentStack.stack.remove(screen)
            to(screen, Animation.ALPHA, false)
        }
    }

    fun toBackStackOrNew(screen: Screen, checkCanHideCurrent: Boolean=true) {
        checkHideCurrent(checkCanHideCurrent) {
            reorderOrCreate(screen::class.java, false) { screen }
        }
    }

    fun reorderOrCreate(viewClass: Class<out Screen>, checkCanHideCurrent: Boolean=true, provider: () -> Screen) {
        checkHideCurrent(checkCanHideCurrent) {
            if (getCurrent() != null && getCurrent()!!.javaClass == viewClass)
                return@checkHideCurrent

            for (i in currentStack.stack.size - 1 downTo -1 + 1)
                if (currentStack.stack[i].javaClass == viewClass) {
                    reorder(currentStack.stack[i], false)
                    return@checkHideCurrent
                }

            to(provider.invoke(), Animation.ALPHA, false)
        }
    }

    fun removeAllEqualsAndTo(view: Screen, checkCanHideCurrent: Boolean=true) {
        checkHideCurrent(checkCanHideCurrent) {
            var i = 0
            while (i < currentStack.stack.size) {
                if (currentStack.stack[i].equalsNView(view))
                    remove(currentStack.stack[i--], false)
                i++
            }

            to(view, Animation.ALPHA, false)
        }
    }

    fun removeAll(viewClass: Class<out Screen>, checkCanHideCurrent: Boolean=true) {
        checkHideCurrent(checkCanHideCurrent) {
            val current = getCurrent()
            val needUpdate = current != null && current.javaClass == viewClass

            var i = 0
            while (i < currentStack.stack.size) {
                if (currentStack.stack[i].javaClass == viewClass) {
                    remove(currentStack.stack[i--], false)
                }
                i++
            }

            if (needUpdate) setCurrentViewNew(Animation.OUT)
        }
    }

    fun back(checkCanHideCurrent: Boolean=true): Boolean {
        if (!hasBackStack()) return false

        checkHideCurrent(checkCanHideCurrent) {
            val current = getCurrent()
            removeScreen(current!!)
            setCurrentViewNew(Animation.OUT)

            onBack.invoke(current, current)
        }

        return true
    }

    fun remove(view: Screen, checkCanHideCurrent: Boolean=true) {
        checkHideCurrent(checkCanHideCurrent) {
            if (hasBackStack() && getCurrent() == view)
                back(false)
            else
                removeScreen(view)
        }
    }

    //
    //  Stack
    //

    fun setStack(stack: NavigatorStack) {
        if (currentStack == stack) return
        val oldStack = currentStack
        currentStack = stack
        for (screen in oldStack.stack) screen.onStackChanged()
        setCurrentViewNew(Animation.ALPHA)
    }

    //
    //  Support
    //

    private fun checkHideCurrent(checkCanHideCurrent: Boolean, callback: () -> Unit) {
        if (!checkCanHideCurrent) callback.invoke()
        val current = getCurrent()
        if (current == null) callback.invoke()
        else {
            current.onTryToHideOrClose {
                if(it)callback.invoke()
            }
        }
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

        return getCurrent() != null && getCurrent()!!.onBackPressed() || back(false)
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
