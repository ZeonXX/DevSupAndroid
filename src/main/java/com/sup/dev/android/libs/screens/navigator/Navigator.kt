package com.sup.dev.android.libs.screens.navigator

import com.sup.dev.android.app.SupAndroid
import com.sup.dev.android.libs.screens.Screen
import com.sup.dev.java.classes.callbacks.CallbacksList2
import com.sup.dev.java.libs.debug.log
import com.sup.dev.java.tools.ToolsThreads
import java.util.ArrayList
import kotlin.reflect.KClass

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
        log("Navigator[$currentStack] removeScreen [$screen]")
        screen.onPause()
        screen.onDestroy()
        currentStack.stack.remove(screen)
    }

    //
    //  Navigation
    //

    fun action(action: NavigationAction, screen: Screen) {
        log("Navigator[$currentStack] action [$action] screen [$screen]")
        action.doAction(screen)
    }

    @JvmOverloads
    fun to(screen: Screen, animation: Animation = Animation.IN) {
        log("Navigator[$currentStack] TO [$screen]")
        if (!currentStack.stack.isEmpty()) {
            if (!getCurrent()!!.isBackStackAllowed) {
                removeScreen(getCurrent()!!)
            } else {
                getCurrent()!!.onPause()
            }
            if (screen.isSingleInstanceInBackStack) {
                removeAll(screen::class)
            }
        }
        currentStack.stack.add(screen)
        setCurrentViewNew(animation)
    }

    fun replace(screen: Screen, newScreen: Screen) {
        log("Navigator[$currentStack] REPLACE [$screen]")
        if (currentStack.stack.isEmpty()) return
        if (getCurrent() == screen) {
            replace(newScreen)
            return
        }
        for (i in currentStack.stack.indices) if (currentStack.stack[i] == screen) currentStack.stack[i] = newScreen
    }

    fun replace(screen: Screen) {
        log("Navigator[$currentStack] replace [$screen]")
        if (!currentStack.stack.isEmpty()) removeScreen(getCurrent()!!)
        to(screen, Animation.ALPHA)
    }

    fun set(screen: Screen, animation: Animation = Animation.ALPHA) {
        log("Navigator[$currentStack] SET [$screen]")
        while (currentStack.stack.size != 0) removeScreen(currentStack.stack[0])
        to(screen, animation)
    }

    fun reorder(screen: Screen) {
        log("Navigator[$currentStack] reorder [$screen]")
        currentStack.stack.remove(screen)
        to(screen)
    }

    fun toBackStackOrNew(screen: Screen) {
        log("Navigator[$currentStack] toBackStackOrNew [$screen]")
        reorderOrCreate(screen::class) { screen }
    }

    fun reorderOrCreate(viewClass: KClass<out Screen>, provider: () -> Screen) {
        log("Navigator[$currentStack] reorderOrCreate [$viewClass]")

        if (getCurrent() != null && getCurrent()!!::class == viewClass)
            return

        for (i in currentStack.stack.size - 1 downTo -1 + 1)
            if (currentStack.stack[i]::class == viewClass) {
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

    fun removeAll(screenClass: KClass<out Screen>) {
        log("Navigator[$currentStack] removeAll [$screenClass]")
        val current = getCurrent()
        val needUpdate = current != null && current::class == screenClass

        var i = 0
        while (i < currentStack.stack.size) {
            if (currentStack.stack[i]::class == screenClass) {
                remove(currentStack.stack[i--])
            }
            i++
        }

        if (needUpdate) setCurrentViewNew(Animation.OUT)
    }


    fun back(): Boolean {
        log("Navigator[$currentStack] BACK")
        if (!hasBackStack()) return false

        val current = getCurrent()
        removeScreen(current!!)
        setCurrentViewNew(Animation.OUT)

        onBack.invoke(current, current)

        return true
    }

    fun remove(screen: Screen) {
        log("Navigator[$currentStack] REMOVE [$screen]")
        if (hasBackStack() && getCurrent() == screen) {
            back()
        } else {
            removeScreen(screen)
            if(currentStack.isEmpty()) SupAndroid.activity?.onLastBackPressed()
        }
    }

    fun setStack(stack: NavigatorStack) {
        log("Navigator[$currentStack] SET_STACK [$stack]")
        if (currentStack == stack) return
        val oldStack = currentStack
        currentStack = stack
        for (screen in oldStack.stack) screen.onStackChanged()
        setCurrentViewNew(Animation.ALPHA)
    }

    //
    //  Activity Callbacks
    //

    private fun setCurrentViewNew(animation: Animation) {
        setCurrentView(animation, true)

        val array = Array(onScreenChangedCallbacks.size) { onScreenChangedCallbacks[it] }
        for (i in array) if (i.invoke()) onScreenChangedCallbacks.remove(i)
    }

    fun resetCurrentView() {
        setCurrentView(Animation.NONE, false)
    }

    private fun setCurrentView(animation: Animation, hideDialogs:Boolean) {
        val screen = getCurrent() ?: return

        SupAndroid.activity!!.setScreen(screen, animation, hideDialogs)

        if (getCurrent() != null) ToolsThreads.main(true){
            if(!screen.wasShowed) {
                screen.onFirstShow()
                screen.wasShowed = true
            }
            screen.onResume()
        }   //  В следующем проходе, чтоб все успело инициализироваться
    }

    fun onActivityStop() {
        getCurrent()?.onPause()
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
        log("Navigator[$currentStack] getCurrent [${currentStack.stack.size}]")
        return if (currentStack.stack.isEmpty()) null else currentStack.stack[currentStack.stack.size - 1]
    }

    fun getLast(screenClass: KClass<out Screen>): Screen? {
        for (i in currentStack.stack.indices.reversed()) if (currentStack.stack[i]::class == screenClass) return currentStack.stack[i]
        return null
    }

    fun isEmpty(): Boolean {
        return currentStack.stack.isEmpty()
    }

    fun hasInBackStack(screenClass: KClass<out Screen>): Boolean {
        for (i in currentStack.stack) if (i::class == screenClass) return true
        return false
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