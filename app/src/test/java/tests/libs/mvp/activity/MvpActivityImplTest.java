package tests.libs.mvp.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.view.ViewGroup;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;
import tests._sup_android.stubs.mvp.MvpPresenterStub;
import com.sup.dev.android.libs.mvp.activity_navigation.DrawerMenuItem;
import com.sup.dev.android.libs.mvp.activity_navigation.MvpActivityNavigationImpl;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class MvpActivityImplTest {

    private ActivityController<MvpActivityNavigationImpl> controller;
    private MvpActivityNavigationImpl activity;
    private DrawerLayout drawerLayout;
    private ViewGroup drawerContainer;
    private ViewGroup bottomDrawerContainer;

    @Before
    public void before() {
        TestAndroid.init();

        controller = Robolectric.buildActivity(MvpActivityNavigationImpl.class).create();
        activity = controller.get();

        drawerLayout = TestAndroid.field(activity, "drawerLayout");
        drawerContainer = TestAndroid.field(activity, "drawerContainer");
        bottomDrawerContainer = TestAndroid.field(activity, "bottomDrawerContainer");
    }

    @Test
    public void init() {
        assertEquals(0, drawerContainer.getChildCount());
        assertEquals(0, bottomDrawerContainer.getChildCount());
        assertNull(TestAndroid.di.setedMvpActivity);
    }

    @Test
    public void onStart_Stop() {
        controller.start();
        assertEquals(activity, TestAndroid.di.setedMvpActivity);
        controller.stop();
        assertNull(TestAndroid.di.setedMvpActivity);
    }

    @Test
    public void onActivityResult() {

        Intent intent = new Intent();
        TestAndroid.method(activity, "onActivityResult", 1, 2, intent);

        assertEquals(1, TestAndroid.di.utilsIntentStub.onActivityResult_requestCode);
        assertEquals(2, TestAndroid.di.utilsIntentStub.onActivityResult_resultCode);
        assertEquals(intent, TestAndroid.di.utilsIntentStub.onActivityResult_intent);
    }

    @Test
    public void onFragmentBackPressed() {
        assertEquals(0, TestAndroid.di.navigatorStub.onBackCounter);

        TestAndroid.di.navigatorStub.current = new MvpPresenterStub();
        activity.onFragmentBackPressed();
        assertEquals(1, TestAndroid.di.navigatorStub.onBackCounter);

        activity.onFragmentBackPressed();
        assertEquals(1, TestAndroid.di.navigatorStub.onBackCounter);
        assertTrue(drawerLayout.isDrawerOpen(GravityCompat.START));
    }

    @Test
    public void onBackPressed() {
        assertFalse(activity.isFinishing());
        TestAndroid.di.navigatorStub.current = new MvpPresenterStub();
        activity.onBackPressed();
        assertFalse(activity.isFinishing());
        activity.onBackPressed();
        assertTrue(activity.isFinishing());
    }


    @Test
    public void fragmentsCalls() {

        assertEquals(0, activity.getBackStackSize());

        Fragment fragment = new Fragment();
        activity.addFragment(fragment, "1", true);
        activity.addFragment(new Fragment(), "2", false);

        assertTrue(TestAndroid.di.utilsViewStub.hideKeyboardCalled);

        //  Просто вызов. Менеджер фрагментов не раотает в тестах
        activity.getBackStackSize();
        // activity.getBackStackTagAt(0);
        activity.backFragment();

    }

    @Test
    public void hideDrawer() {
        activity.showDrawer();
        assertTrue(drawerLayout.isDrawerOpen(GravityCompat.START));
        activity.hideDrawer();
        assertFalse(drawerLayout.isDrawerOpen(GravityCompat.START));
    }

    @Test
    public void addDrawerView() {
        View view = new View(RuntimeEnvironment.systemContext);

        activity.addDrawerView(view);

        assertEquals(view, drawerContainer.getChildAt(0));
    }

    @Test
    public void addDrawerMenu() {
        DrawerMenuItem menuItem = new DrawerMenuItem(RuntimeEnvironment.systemContext);

        activity.addDrawerMenu(menuItem);

        assertEquals(menuItem.getView(), drawerContainer.getChildAt(0));
    }

    @Test
    public void addDrawerViewBottom() {
        View view = new View(RuntimeEnvironment.systemContext);

        activity.addDrawerViewBottom(view);

        assertEquals(view, bottomDrawerContainer.getChildAt(0));
    }

    @Test
    public void addDrawerMenuBottom() {
        DrawerMenuItem menuItem = new DrawerMenuItem(RuntimeEnvironment.systemContext);

        activity.addDrawerMenuBottom(menuItem);

        assertEquals(menuItem.getView(), bottomDrawerContainer.getChildAt(0));
    }

    @Test
    public void setNavigationLock() {
        activity.setNavigationLock(true);
        assertEquals(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, drawerLayout.getDrawerLockMode(GravityCompat.START));
        activity.setNavigationLock(false);
        assertEquals(DrawerLayout.LOCK_MODE_UNLOCKED, drawerLayout.getDrawerLockMode(GravityCompat.START));

        activity.setNavigationLock(true);
        Activity activity =  Robolectric.buildActivity(MvpActivityNavigationImpl.class).create().get();
        DrawerLayout drawerLayout = TestAndroid.field(activity, "drawerLayout");
        assertEquals(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, drawerLayout.getDrawerLockMode(GravityCompat.START));
    }

    @Test
    public void onDrawerCalls() {
        activity.onDrawerSlide(null, 0);
        activity.onDrawerOpened(null);
        activity.onDrawerClosed(null);
    }

    @Test
    public void onDrawerStateChanged() {
        activity.onDrawerStateChanged(DrawerLayout.STATE_DRAGGING);
        assertTrue(TestAndroid.di.utilsViewStub.hideKeyboardCalled);
    }

}
