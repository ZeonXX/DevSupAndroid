package com.sup.dev.android.libs.screens;

import com.sup.dev.android.app.SupAndroid;
import com.sup.dev.java.classes.callbacks.list.CallbacksList2;
import com.sup.dev.java.classes.callbacks.simple.Callback2;
import com.sup.dev.java.classes.providers.Provider;
import com.sup.dev.java.libs.debug.Debug;

import java.util.ArrayList;

public class Navigator {

    public final static NavigationAction TO = NavigationAction.to().immutable();
    public final static NavigationAction SET = NavigationAction.set().immutable();
    public final static NavigationAction REPLACE = NavigationAction.replace().immutable();

    private static ArrayList<Screen> backStack = new ArrayList<>();

    //
    //  Views
    //

    public static void removeView(Screen view) {
        view.onDestroy();
        backStack.remove(view);
    }

    //
    //  Navigation
    //

    public static void action(NavigationAction action, Screen screen) {
        action.doAction(screen);
    }

    public static void to(Screen view) {
        if (!backStack.isEmpty()) {
            if (!getCurrent().isBackStackAllowed()) removeView(getCurrent());
            else {
                getCurrent().onPause();
            }
            if(view.singleInstanceInBackstack)removeAll(view.getClass());
        }
        backStack.add(view);
        setCurrentView();
    }

    public static void replace(Screen screen, Screen newScreen) {
        if (backStack.isEmpty()) return;
        if (getCurrent() == screen) {
            replace(newScreen);
            return;
        }
        for (int i = 0; i < backStack.size(); i++) if (backStack.get(i) == screen) backStack.set(i, newScreen);
    }

    public static void replace(Screen screen) {
        if (!backStack.isEmpty()) removeView(getCurrent());
        to(screen);
    }

    public static void set(Screen screen) {
        while (backStack.size() != 0) removeView(backStack.get(0));
        to(screen);
    }

    public static void reorder(Screen screen) {
        backStack.remove(screen);
        to(screen);
    }

    public static void reorderOrCreate(Class<? extends Screen> viewClass, Provider<Screen> provider) {

        if (getCurrent() != null && getCurrent().getClass() == viewClass)
            return;

        for (int i = backStack.size() - 1; i > -1; i--)
            if (backStack.get(i).getClass() == viewClass) {
                reorder(backStack.get(i));
                return;
            }

        to(provider.provide());
    }

    public static void removeAllEqualsAndTo(Screen view) {

        for (int i = 0; i < backStack.size(); i++)
            if (backStack.get(i).equalsNView(view))
                remove(backStack.get(i--));

        to(view);
    }

    public static void removeAll(Class<? extends Screen> viewClass) {
        Screen current = getCurrent();
        boolean needUpdate = current != null && current.getClass() == viewClass;

        for (int i = 0; i < backStack.size(); i++)
            if (backStack.get(i).getClass() == viewClass)
                remove(backStack.get(i--));

        if (needUpdate) setCurrentView();
    }


    public static boolean back() {
        if (!hasBackStack()) return false;

        Screen current = getCurrent();
        removeView(current);
        setCurrentView();

        onBack.callback(current, getCurrent());

        return true;
    }

    public static void remove(Screen view) {
        if (getCurrent() == view) back();
        else removeView(view);
    }


    //
    //  Activity Callbacks
    //

    public static void setCurrentView() {
        if(getCurrent() == null)return;
        SupAndroid.activity.setView(getCurrent());
        if (getCurrent() != null) getCurrent().onResume();
    }

    public static void onActivityStop() {
        if (getCurrent() != null) getCurrent().onPause();
    }

    public static void onActivityResume() {
        setCurrentView();
    }

    public static void onActivityDestroy() {
        backStack.clear();
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
        return backStack.size() > 1;
    }

    public static Screen getCurrent() {
        if (backStack.isEmpty()) return null;
        return backStack.get(backStack.size() - 1);
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
