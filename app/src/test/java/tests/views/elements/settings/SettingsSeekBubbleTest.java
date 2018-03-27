package tests.views.elements.settings;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class SettingsSeekBubbleTest {

    private SettingsSeekBubble settings;

    @Before
    public void before() {
        TestAndroid.init();
        settings = new SettingsSeekBubble(RuntimeEnvironment.application, null);
    }

    @Test
    public void init() {
        assertTrue(settings.vSeekBar.isEnabled());
        assertEquals(View.GONE, settings.vIconRight.getVisibility());
        assertEquals(View.GONE, settings.vIconLeft.getVisibility());
    }

    @Test
    public void setProgress() {
        settings.setProgress(1);
        assertEquals(1f, settings.getProgress());
    }

    @Test
    public void setRightIconRes() {
        settings.setRightIcon(R.drawable.ic_arrow_back_white_24dp);
        assertEquals(View.VISIBLE, settings.vIconRight.getVisibility());
    }

    @Test
    public void setRightIcon() {
        Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        settings.setRightIcon(bitmap);
        assertEquals(View.VISIBLE, settings.vIconRight.getVisibility());
        assertEquals(bitmap, ((BitmapDrawable)settings.vIconRight.getDrawable()).getBitmap());
    }

    @Test
    public void setLeftIconRes() {
        settings.setLeftIcon(R.drawable.ic_arrow_back_white_24dp);
        assertEquals(View.VISIBLE, settings.vIconLeft.getVisibility());
    }

    @Test
    public void setLeftIcon() {
        Bitmap bitmap = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        settings.setLeftIcon(bitmap);
        assertEquals(View.VISIBLE, settings.vIconLeft.getVisibility());
        assertEquals(bitmap, ((BitmapDrawable)settings.vIconLeft.getDrawable()).getBitmap());
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
    public void setScrollUpdater() {
        settings.setScrollUpdater(new View(RuntimeEnvironment.systemContext));
    }

    @Test
    public void setEnabled() {
        settings.setEnabled(false);
        assertFalse(settings.vSeekBar.isEnabled());
        settings.setEnabled(true);
        assertTrue(settings.vSeekBar.isEnabled());
    }

    @Test
    public void getProgressOnActionUp() {
        settings.getProgressOnActionUp(null, 0, 0);
    }

    @Test
    public void getProgressOnFinally() {
        settings.getProgressOnFinally(null, 0, 0);
    }

    @Test
    public void getConfigBuilder() {
        assertNotNull(settings.getConfigBuilder());
    }

}
