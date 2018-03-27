package tests.libs.mvp;

import com.sup.dev.android.libs.mvp.navigator.MvpNavigatorImpl;

import tests._sup_android.TestAndroid;
import tests._sup_android.stubs.mvp.MvpPresenterStub;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class MvpNavigatorImplTest{

    private MvpNavigatorImpl navigator;

    @Before
    public void before() {
        TestAndroid.init();
        navigator = new MvpNavigatorImpl();
    }

    @Test
    public void startParams() {
        assertFalse(navigator.hasBackStack());
        assertEquals(0, navigator.getBackStackSize());
        assertNull(navigator.getCurrent());
    }


    @Test
    public void to() {
        MvpPresenterStub mvpPresenterStub = new MvpPresenterStub();
        navigator.to(mvpPresenterStub);

        assertEquals(mvpPresenterStub, navigator.getCurrent());
        assertEquals(1, navigator.getBackStackSize());
        assertFalse(navigator.hasBackStack());
    }

    @Test
    public void replace() {
        MvpPresenterStub mvpPresenterStub = new MvpPresenterStub();
        navigator.to(mvpPresenterStub);
        mvpPresenterStub = new MvpPresenterStub();
        navigator.to(mvpPresenterStub);
        mvpPresenterStub = new MvpPresenterStub();
        navigator.replace(mvpPresenterStub);

        assertEquals(mvpPresenterStub, navigator.getCurrent());
        assertEquals(2, navigator.getBackStackSize());
    }

    @Test
    public void set() {
        MvpPresenterStub mvpPresenterStub = new MvpPresenterStub();
        navigator.to(mvpPresenterStub);
        mvpPresenterStub = new MvpPresenterStub();
        navigator.to(mvpPresenterStub);
        mvpPresenterStub = new MvpPresenterStub();
        navigator.set(mvpPresenterStub);

        assertEquals(mvpPresenterStub, navigator.getCurrent());
        assertEquals(1, navigator.getBackStackSize());
    }

    @Test
    public void back() {
        assertEquals(0, navigator.getBackStackSize());
        navigator.back();
        assertEquals(0, navigator.getBackStackSize());
        MvpPresenterStub mvpPresenterStub = new MvpPresenterStub();
        navigator.to(mvpPresenterStub);
        assertEquals(1, navigator.getBackStackSize());
        navigator.back();
        assertEquals(0, navigator.getBackStackSize());
    }

    @Test
    public void onBackPressed() {
        assertFalse(navigator.onBackPressed());
        MvpPresenterStub mvpPresenterStub = new MvpPresenterStub();
        navigator.to(mvpPresenterStub);
        assertEquals(1, navigator.getBackStackSize());
        assertTrue(navigator.onBackPressed());
        assertEquals(0, navigator.getBackStackSize());
    }


}
