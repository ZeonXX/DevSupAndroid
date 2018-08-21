package com.sup.dev.android.libs.screens.navigator;

import com.sup.dev.android.libs.screens.Screen;
import com.sup.dev.java.classes.callbacks.simple.Callback1;

public class NavigationAction {

    private Callback1<Screen> action;
    private Callback1<Screen> before;
    private Callback1<Screen> after;
    boolean immutable;

    public static NavigationAction to() {
        return new NavigationAction(Navigator::to);
    }
    public static NavigationAction set() {
        return new NavigationAction(Navigator::set);
    }
    public static NavigationAction replace() {
        return new NavigationAction(Navigator::replace);
    }
    public static NavigationAction reorder() {
        return new NavigationAction(Navigator::reorder);
    }

    private NavigationAction(Callback1<Screen> action) {
        this.action = action;
    }

    public void doAction(Screen screen) {
        if(before != null)before.callback(screen);
        if(action != null)action.callback(screen);
        if(after != null)after.callback(screen);
    }

    public NavigationAction action(Callback1<Screen> action) {
        if(immutable)throw new RuntimeException("Can't change immutable NavigationAction");
        this.action = action;
        return this;
    }

    public NavigationAction after(Callback1<Screen> after) {
        if(immutable)throw new RuntimeException("Can't change immutable NavigationAction");
        this.after = after;
        return this;
    }

    public NavigationAction before(Callback1<Screen> before) {
        if(immutable)throw new RuntimeException("Can't change immutable NavigationAction");
        this.before = before;
        return this;
    }

    public NavigationAction immutable(){
        immutable = true;
        return this;
    }

}
