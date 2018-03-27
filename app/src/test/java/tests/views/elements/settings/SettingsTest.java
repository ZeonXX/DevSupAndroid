package tests.views.elements.settings;

import android.view.View;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.settings.Settings;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class SettingsTest {

    private Settings settings;

    @Before
    public void before() {
        TestAndroid.init();
        settings = new Settings(RuntimeEnvironment.application, R.layout.settings_action);
    }

    @Test
    public void init() {
        assertEquals(settings.line.getVisibility(), View.VISIBLE);
        assertTrue(settings.vTouch.isEnabled());
        assertTrue(settings.vTouch.isFocusable());
    }

    @Test
    public void getView() {
        assertNotNull(settings.getView());
    }

    @Test
    public void subSettings() {
        Settings subSettings = new Settings(RuntimeEnvironment.application, R.layout.settings_action);
        settings.addSubSettings(subSettings);

        assertTrue(subSettings.vTouch.isEnabled());
        settings.setEnabledSubSettings(false);
        assertFalse(subSettings.vTouch.isEnabled());
    }

    @Test
    public void setEnabled() {
        settings.setEnabled(false);
        assertFalse(settings.vTouch.isEnabled());
        settings.setEnabled(true);
        assertTrue(settings.vTouch.isEnabled());
    }

    @Test
    public void setBodyUntouchable() {
        settings.setBodyUntouchable();
        assertFalse(settings.vTouch.isFocusable());
    }

    @Test
    public void removeLine() {
        settings.removeLine();
        assertEquals(settings.line.getVisibility(), View.GONE);
    }


}
