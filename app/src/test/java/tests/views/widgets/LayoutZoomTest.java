package tests.views.widgets;
import android.view.View;
import android.view.ViewGroup;

import com.sup.dev.android.views.widgets.layouts.LayoutZoom;
import com.sup.dev.java.libs.debug.Debug;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class LayoutZoomTest {


    private View view;
    private LayoutZoom layout;

    @Before
    public void before() {
        TestAndroid.init();

        view = new View(RuntimeEnvironment.systemContext);
        layout = new LayoutZoom(RuntimeEnvironment.systemContext);
        layout.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        TestAndroid.measure(layout, 100, 100);
        TestAndroid.measure(view, 100, 100);
    }

    @Test
    public void init() {
        assertEquals(0f, view.getY());
        assertEquals(0f, view.getX());
        assertEquals(100, view.getMeasuredHeight());
        assertEquals(100, view.getMeasuredWidth());
    }

    @Test
    public void zoom(){
        layout.zoom(1, 50, 50);
        assertEquals(2f, view.getScaleX());
        assertEquals(2f, view.getScaleY());
        assertEquals(-50f, view.getX());
        assertEquals(-50f, view.getY());

        layout.zoom(1, 50, 50);
        assertEquals(3f, view.getScaleX());
        assertEquals(3f, view.getScaleY());
        assertEquals(-100f, view.getX());
        assertEquals(-100f, view.getY());

        layout.zoom(-1, 50, 50);
        assertEquals(2f, view.getScaleX());
        assertEquals(2f, view.getScaleY());
        assertEquals(-50f, view.getX());
        assertEquals(-50f, view.getY());

        layout.zoom(-1, 50, 50);
        assertEquals(1f, view.getScaleX());
        assertEquals(1f, view.getScaleY());
        assertEquals(0f, view.getX());
        assertEquals(0f, view.getY());
    }



}
