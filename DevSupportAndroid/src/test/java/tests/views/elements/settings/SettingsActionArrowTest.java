package tests.views.elements.settings;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.settings.SettingsActionArrow;
import com.sup.dev.java.classes.items.Pair;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class SettingsActionArrowTest {

    private SettingsActionArrow settings;

    @Before
    public void before() {
        TestAndroid.init();
        settings = new SettingsActionArrow(RuntimeEnvironment.application);
    }

    @Test
    public void setEnabled() {
        assertEquals(0, TestAndroid.di.utilsBitmapStub.filterResCalls.size());
        settings.setEnabled(false);
        assertEquals(1, TestAndroid.di.utilsBitmapStub.filterResCalls.size());
        Pair<Integer, Integer> pair = TestAndroid.di.utilsBitmapStub.filterResCalls.get(0);
        assertEquals((int)pair.left, R.drawable.ic_keyboard_arrow_right_white_24dp);
        assertEquals((int)pair.right, 0x80000000);
        assertEquals(1, TestAndroid.di.utilsBitmapStub.filterResCalls.size());
        settings.setEnabled(true);
    }



}
