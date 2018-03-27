package tests.views.elements.settings;
import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.settings.SettingsActionSwitcher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class SettingsActionSwitcherTest {

    private SettingsActionSwitcher settings;

    @Before
    public void before() {
        TestAndroid.init();
        settings = new SettingsActionSwitcher(RuntimeEnvironment.application);
    }

    @Test
    public void init() {
        assertTrue(settings.vSwitcher.isEnabled());
        assertFalse(settings.isChecked());
        assertFalse(settings.vSwitcher.isFocusable());
    }

    @Test
    public void onClick() {
        settings.vTouch.performClick();
        assertTrue(settings.isChecked());
        settings.vTouch.performClick();
        assertFalse(settings.isChecked());
    }

    @Test
    public void setChecked() {
        settings.setChecked(true);
        assertTrue(settings.isChecked());
        settings.setChecked(false);
        assertFalse(settings.isChecked());
    }

    @Test
    public void setEnabled() {
        settings.setEnabled(false);
        assertFalse(settings.vSwitcher.isEnabled());
        settings.setEnabled(true);
        assertTrue(settings.vSwitcher.isEnabled());
    }


}
