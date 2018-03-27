package tests._sup_android.stubs.mvp;

import android.app.Fragment;
import android.os.Bundle;

import com.sup.dev.android.libs.mvp.navigator.MvpNavigator;
import com.sup.dev.android.libs.mvp.fragments.MvpPresenterInterface;

import java.util.HashMap;

import tests._sup_android.TestAndroid;

import static junit.framework.Assert.assertEquals;

public class MvpNavigatorStub implements MvpNavigator {

    public HashMap<Integer, MvpPresenterInterface> presenters = new HashMap<>();
    public int backCounter;
    public int onBackCounter;
    public MvpPresenterInterface lastPresenterSet;
    public MvpPresenterInterface lastPresenterTo;
    public MvpPresenterInterface lastPresenterReplace;
    public MvpPresenterInterface current;


    @Override
    public boolean hasBackStack() {
        return current != null;
    }

    @Override
    public MvpPresenterInterface getPresenter(int key) {
        return presenters.get(key);
    }

    @Override
    public MvpPresenterInterface getCurrent() {
        return current;
    }

    @Override
    public void to(MvpPresenterInterface presenter) {
        current = presenter;
        lastPresenterTo = presenter;

    }

    @Override
    public void replace(MvpPresenterInterface presenter) {
        current = presenter;
        lastPresenterReplace = presenter;
    }

    @Override
    public void set(MvpPresenterInterface presenter) {
        current = presenter;
        lastPresenterSet = presenter;
    }

    @Override
    public void back() {
        backCounter++;
    }

    @Override
    public boolean onBackPressed() {
        onBackCounter++;
        boolean b = current != null;
        current = null;
        return b;
    }

    //
    //  Test support
    //

    public void assertLastSet(Class<? extends MvpPresenterInterface> c) {
        assertEquals(lastPresenterSet.getClass(), c);
    }

    public void assertLastTo(Class<? extends MvpPresenterInterface> c) {
        assertEquals(lastPresenterTo.getClass(), c);
    }

    public void assertLastReplace(Class<? extends MvpPresenterInterface> c) {
        assertEquals(lastPresenterReplace.getClass(), c);
    }

    public void setPresenter(Fragment fragment, MvpPresenterInterface presenter) {
        presenters.put(1, presenter);
        Bundle bundle = new Bundle();
        bundle.putInt("KEY", 1);
        TestAndroid.setBundle(fragment, bundle);
    }

}
