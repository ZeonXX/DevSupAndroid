package tests.views.elements.settings;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.settings.SettingsActionCheckBox;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class SettingsActionCheckBoxTest {

    private SettingsActionCheckBox settings;

    @Before
    public void before() {
        TestAndroid.init();
        settings = new SettingsActionCheckBox(RuntimeEnvironment.application);
        assertFalse(settings.vCheckBox.isFocusable());
    }


    @Test
    public void init() {
        assertFalse(settings.isChecked());
        assertTrue(settings.vCheckBox.isEnabled());
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
        assertFalse(settings.vCheckBox.isEnabled());
        settings.setEnabled(true);
        assertTrue(settings.vCheckBox.isEnabled());
    }


}
