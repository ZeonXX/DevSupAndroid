package tests.views.elements.settings;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.settings.SettingsTitle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class SettingsTitleTest {

    private SettingsTitle settings;

    @Before
    public void before() {
        TestAndroid.init();
        settings = new SettingsTitle(RuntimeEnvironment.application);
    }

    @Test
    public void init() {
        assertEquals(0, settings.vTitle.getText().length());
    }

    @Test
    public void setTitleRes() {
        settings.setTitle(R.string.test);
        assertEquals("test", settings.vTitle.getText());
    }

    @Test
    public void setTitle() {
        settings.setTitle("test");
        assertEquals("test", settings.vTitle.getText());
    }


}
