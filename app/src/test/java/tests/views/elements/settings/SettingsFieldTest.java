package tests.views.elements.settings;

import android.text.InputType;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.settings.SettingsField;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class SettingsFieldTest {

    private SettingsField settings;

    @Before
    public void before() {
        TestAndroid.init();
        settings = new SettingsField(RuntimeEnvironment.application);
    }

    @Test
    public void init() {
        assertEquals(0, settings.vField.getText().length());
    }

    @Test
    public void setText() {
        settings.setText("test");
        assertEquals("test", settings.getText());
        assertEquals("test".length(), settings.vField.getSelectionEnd());
    }

    @Test
    public void setEnabled() {
        settings.setEnabled(false);
        assertFalse(settings.vField.isEnabled());
        settings.setEnabled(true);
        assertTrue(settings.vField.isEnabled());
    }

    @Test
    public void setHintRes() {
        settings.setHint(R.string.test);
        assertEquals("test", settings.vInputLayout.getHint());
    }

    @Test
    public void setHint() {
        settings.setHint("test");
        assertEquals("test", settings.vInputLayout.getHint());
    }

    @Test
    public void setInputType() {
        settings.setInputType(InputType.TYPE_CLASS_DATETIME);
        assertEquals(InputType.TYPE_CLASS_DATETIME, settings.vField.getInputType());
    }

    @Test
    public void setMaxLength() {
        settings.setMaxLength(5);
    }

}
