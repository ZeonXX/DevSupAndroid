package tests.views.animations;

import android.view.View;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.animations.AnimationViewHideVertical;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class AnimationHideVerticalTest {

    private View view;
    private AnimationViewHideVertical animation;

    @Before
    public void before() {
        TestAndroid.init();

        view = new View(RuntimeEnvironment.systemContext);
        animation = new AnimationViewHideVertical(view);

        TestAndroid.measure(view, 10, 10);
    }

    @Test
    public void init() {
        assertEquals(0f, view.getY());
        assertEquals(10, view.getMeasuredHeight());
        assertTrue(animation.isShoved());
    }

    @Test
    public void show_hide_Animation() {
        animation.setAnimationTime(100);
        animation.hide();
        TestAndroid.di.utilsThreadsStub.stepTimerMain(40);
        assertEquals(4f, view.getY());
        TestAndroid.di.utilsThreadsStub.stepTimerMain(70);
        assertEquals(10f, view.getY());

        animation.show();
        TestAndroid.di.utilsThreadsStub.stepTimerMain(40);
        assertEquals(6f, view.getY());
        TestAndroid.di.utilsThreadsStub.stepTimerMain(70);
        assertEquals(0f, view.getY());
    }

    @Test
    public void show_hide_noAnimation() {
        animation.hide(false);
        TestAndroid.di.utilsThreadsStub.stepTimerMain(1);
        assertEquals(10f, view.getY());
        animation.show(false);
        TestAndroid.di.utilsThreadsStub.stepTimerMain(1);
        assertEquals(0f, view.getY());
    }

    @Test
    public void setAutoHide() {
        animation.setAutoHide(1000);

        assertEquals(1000, TestAndroid.di.utilsThreadsStub.main_time);
        TestAndroid.di.utilsThreadsStub.stepTimerMain(1000);
        assertEquals(10f, view.getY());
    }

    @Test
    public void switchShow() {
        animation.switchShow(false);
        TestAndroid.di.utilsThreadsStub.stepTimerMain(1);
        assertEquals(10f, view.getY());
        animation.switchShow(false);
        TestAndroid.di.utilsThreadsStub.stepTimerMain(1);
        assertEquals(0f, view.getY());
        animation.switchShow();
        assertEquals(0f, view.getY());
        TestAndroid.di.utilsThreadsStub.stepTimerMain(1000);
        assertEquals(10f, view.getY());
    }

}
