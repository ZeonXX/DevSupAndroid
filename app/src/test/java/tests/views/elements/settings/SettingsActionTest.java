package tests.views.elements.settings;

import android.view.View;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.settings.SettingsAction;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)

public class SettingsActionTest {

    private SettingsAction settings;

    @Before
    public void before() {
        TestAndroid.init();
        settings = new SettingsAction(RuntimeEnvironment.application);
    }

    @Test
    public void init() {
        assertEquals(0, settings.vSubViewContainer.getChildCount());
        assertEquals(View.GONE, settings.vIcon.getVisibility());
    }

    @Test
    public void setSubView() {
        View view = new View(RuntimeEnvironment.systemContext);
        settings.setSubView(view);
        assertEquals(1, settings.vSubViewContainer.getChildCount());
        assertEquals(view, settings.vSubViewContainer.getChildAt(0));
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

    @Test
    public void setSubtitleRes() {
        settings.setSubtitle(R.string.test);
        assertEquals("test", settings.vSubtitle.getText());
    }

    @Test
    public void setSubtitle() {
        settings.setSubtitle("test");
        assertEquals("test", settings.vSubtitle.getText());
    }

    @Test
    public void setIcon() {
        settings.setIcon(R.drawable.ic_arrow_back_white_24dp);
        assertEquals(View.VISIBLE, settings.vIcon.getVisibility());
    }

    @Test
    public void setEnabled() {
        settings.setEnabled(false);
        assertEquals(0xFF757575, settings.vTitle.getCurrentTextColor());
        assertEquals(0xFF757575, settings.vSubtitle.getCurrentTextColor());
    }

    @Test
    public void onClick() {
        settings.vTouch.performClick();
    }

}
