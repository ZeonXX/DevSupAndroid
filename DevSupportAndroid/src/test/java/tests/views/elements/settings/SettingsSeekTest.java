package tests.views.elements.settings;
import android.view.View;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.settings.SettingsSeek;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class SettingsSeekTest {

    private SettingsSeek settings;

    @Before
    public void before() {
        TestAndroid.init();
        settings = new SettingsSeek(RuntimeEnvironment.application);
    }


    @Test
    public void init() {
        assertEquals(View.GONE, settings.vIcon.getVisibility());
        assertEquals(View.GONE, settings.vTitle.getVisibility());
        assertEquals(View.GONE, settings.vSubtitle.getVisibility());
    }

    @Test
    public void setTitleRes() {
        settings.setTitle(R.string.test);
        assertEquals("test", settings.vTitle.getText());
        assertEquals(View.VISIBLE, settings.vTitle.getVisibility());
    }

    @Test
    public void setTitle() {
        settings.setTitle("test");
        assertEquals("test", settings.vTitle.getText());
        assertEquals(View.VISIBLE, settings.vTitle.getVisibility());
    }

    @Test
    public void setSubtitleRes() {
        settings.setSubtitle(R.string.test);
        assertEquals("test", settings.vSubtitle.getText());
        assertEquals(View.VISIBLE, settings.vSubtitle.getVisibility());
    }

    @Test
    public void setSubtitle() {
        settings.setSubtitle("test");
        assertEquals("test", settings.vSubtitle.getText());
        assertEquals(View.VISIBLE, settings.vSubtitle.getVisibility());
    }

    @Test
    public void setIcon() {
        settings.setIcon(R.drawable.ic_arrow_back_white_24dp);
        assertEquals(View.VISIBLE, settings.vIcon.getVisibility());
    }


    @Test
    public void setEnabled() {
        settings.setEnabled(false);
        assertFalse(settings.vSeekBar.isEnabled());
        assertEquals(0xFF757575, settings.vTitle.getCurrentTextColor());
        settings.setEnabled(true);
        assertTrue(settings.vSeekBar.isEnabled());
    }


    @Test
    public void setMaxProgress() {
        settings.setMaxProgress(10);
        assertEquals(10, settings.vSeekBar.getMax());
    }

    @Test
    public void setProgress() {
        settings.setProgress(10);
        assertEquals(10, settings.getProgress());
    }

    @Test
    public void onStartTrackingTouch( ) {
        settings.onStartTrackingTouch(settings.vSeekBar);
    }


    @Test
    public void onStopTrackingTouch() {
        settings.onStopTrackingTouch(settings.vSeekBar);
    }
    
}
