package com.sup.dev.android.libs.screens.navigator;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.java.classes.callbacks.list.CallbacksList2;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.providers.Provider;
import com.sup.dev.java.libs.debug.Debug;

import java.util.ArrayList;

public class Navigator {

    public enum Animation {IN, OUT, ALPHA, NONE}

    private static NavigatorStack currentStack = new NavigatorStack();

    public final static NavigationAction TO = NavigationAction.to().immutable();
    public final static NavigationAction SET = NavigationAction.set().immutable();
    public final static NavigationAction REPLACE = NavigationAction.replace().immutable();
    public final static NavigationAction REORDER = NavigationAction.reorder().immutable();

    //
    //  Views
    //

    public static void removeScreen(Screen screen) {
        screen.onDestroy();
        currentStack.backStack.remove(screen);
    }

    //
    //  Navigation
    //

    public static void action(NavigationAction action, Screen screen) {
        action.doAction(screen);
    }

    public static void to(Screen screen) {
        to(screen, Animation.IN);
    }

    public static void to(Screen screen, Animation animation) {
        if (!currentStack.backStack.isEmpty()) {
            if (!getCurrent().isBackStackAllowed()) {
                removeScreen(getCurrent());
            } else {
                getCurrent().onPause();
            }
            if (screen.isSingleInstanceInBackstack()) {
                removeAll(screen.getClass());
            }
        }
        currentStack.backStack.add(screen);
        setCurrentView(animation);
    }

    public static void replace(Screen screen, Screen newScreen) {
        if (currentStack.backStack.isEmpty()) return;
        if (getCurrent() == screen) {
            replace(newScreen);
            return;
        }
        for (int i = 0; i < currentStack.backStack.size(); i++) if (currentStack.backStack.get(i) == screen) currentStack.backStack.set(i, newScreen);
    }

    public static void replace(Screen screen) {
        if (!currentStack.backStack.isEmpty()) removeScreen(getCurrent());
        to(screen, Animation.ALPHA);
    }

    public static void set(Screen screen) {
        set(screen, Animation.ALPHA);
    }

    public static void set(Screen screen, Animation animation) {
        while (currentStack.backStack.size() != 0) removeScreen(currentStack.backStack.get(0));
        to(screen, animation);
    }

    public static void reorder(Screen screen) {
        currentStack.backStack.remove(screen);
        to(screen);
    }

    public static void reorderOrCreate(Class<? extends Screen> viewClass, Provider<Screen> provider) {

        if (getCurrent() != null && getCurrent().getClass() == viewClass)
            return;

        for (int i = currentStack.backStack.size() - 1; i > -1; i--)
            if (currentStack.backStack.get(i).getClass() == viewClass) {
                reorder(currentStack.backStack.get(i));
                return;
            }

        to(provider.provide());
    }

    public static void removeAllEqualsAndTo(Screen view) {

        for (int i = 0; i < currentStack.backStack.size(); i++)
            if (currentStack.backStack.get(i).equalsNView(view))
                remove(currentStack.backStack.get(i--));

        to(view);
    }

    public static void removeAll(Class<? extends Screen> viewClass) {
        Screen current = getCurrent();
        boolean needUpdate = current != null && current.getClass() == viewClass;

        for (int i = 0; i < currentStack.backStack.size(); i++) {
            if (currentStack.backStack.get(i).getClass() == viewClass) {
                remove(currentStack.backStack.get(i--));
            }
        }

        if (needUpdate) setCurrentView(Animation.OUT);
    }


    public static boolean back() {
        if (!hasBackStack()) return false;

        Screen current = getCurrent();
        removeScreen(current);
        setCurrentView(Animation.OUT);

        onBack.callback(current, getCurrent());

        return true;
    }

    public static void remove(Screen view) {
        if (hasBackStack() && getCurrent() == view) back();
        else removeScreen(view);
    }

    public static void setStack(NavigatorStack stack) {
        if (currentStack == stack) return;
        currentStack = stack;
        if (!currentStack.backStack.isEmpty()) setCurrentView(Animation.ALPHA);
    }

    //
    //  Activity Callbacks
    //

    public static void resetCurrentView() {
        setCurrentView(Animation.NONE);
    }

    private static void setCurrentView(Animation animation) {
        if (getCurrent() == null) return;
        SupAndroid.activity.setScreen(getCurrent(), animation);
        if (getCurrent() != null) getCurrent().onResume();
    }

    public static void onActivityStop() {
        if (getCurrent() != null) getCurrent().onPause();
    }

    public static void onActivityConfigChanged() {
        if (getCurrent() != null) getCurrent().onConfigChanged();
    }

    public static boolean onBackPressed() {

        for (int i = onBackCallbacks.size() - 1; i > -1; i--) {
            Provider<Boolean> onBack = onBackCallbacks.remove(i);
            if (onBack.provide()) return true;
        }

        return (getCurrent() != null && getCurrent().onBackPressed()) || back();
    }

    //
    //  Getters
    //

    public static boolean hasBackStack() {
        return currentStack.backStack.size() > 1;
    }

    public static int getStackSize() {
        return currentStack.backStack.size();
    }

    public static boolean hasPrevious() {
        return currentStack.backStack.size() > 1;
    }

    public static Screen getPrevious() {
        return hasPrevious() ? currentStack.backStack.get(currentStack.backStack.size() - 2) : null;
    }

    public static Screen getCurrent() {
        if (currentStack.backStack.isEmpty()) return null;
        return currentStack.backStack.get(currentStack.backStack.size() - 1);
    }

    public static NavigatorStack getCurrentStack() {
        return currentStack;
    }

    public static boolean isEmpty() {
        return currentStack.backStack.isEmpty();
    }

    //
    //  Listeners
    //

    private static final CallbacksList2<Screen, Screen> onBack = new CallbacksList2<>();
    public static final ArrayList<Provider<Boolean>> onBackCallbacks = new ArrayList<>();

    public static void addOnBackScreenListener(Callback2<Screen, Screen> onBack) {
        Navigator.onBack.remove(onBack);
        Navigator.onBack.add(onBack);
    }

    public static void removeOnBackScreenListener(Callback2<Screen, Screen> onBack) {
        Navigator.onBack.remove(onBack);
    }


    public static void addOnBack(Provider<Boolean> onBack) {
        if (onBackCallbacks.contains(onBack)) onBackCallbacks.remove(onBack);
        onBackCallbacks.add(onBack);
    }

    public static void removeOnBack(Provider<Boolean> onBack) {
        onBackCallbacks.remove(onBack);
    }

}
