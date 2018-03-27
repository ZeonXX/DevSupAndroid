package tests.views.elements.dialogs;

import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.stubs.support.CallbackSourceStub;
import tests._sup_android.stubs.support.ProviderArgStub;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.dialogs.DialogSeekDiscrete;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class DialogSeekDiscreteTest {

    private DialogSeekDiscrete dialog;
    private SeekBar vSeekBar;
    private TextView vMin;
    private TextView vCurrent;
    private TextView vMax;
    private Button vEnter;

    @Before
    public void before() {
        TestAndroid.init();
        dialog = new DialogSeekDiscrete(RuntimeEnvironment.application);
        vSeekBar = TestAndroid.field(dialog, "vSeekBar");
        vMin = TestAndroid.field(dialog, "vMin");
        vCurrent = TestAndroid.field(dialog, "vCurrent");
        vMax = TestAndroid.field(dialog, "vMax");
        vEnter = TestAndroid.field(dialog, "vEnter");
    }

    @Test
    public void init() {
        assertEquals(0, vMin.getText().length());
        assertEquals(0, vMax.getText().length());
        assertEquals(0, vCurrent.getText().length());
    }

    @Test
    public void setCurrentTextMask() {
        ProviderArgStub<Integer, String> providerArgStub = new ProviderArgStub<>("1");
        dialog.setCurrentTextMask(providerArgStub);

        vSeekBar.setProgress(2);

        assertTrue(providerArgStub.called);
        assertEquals(2, (Object)providerArgStub.lastArgument);
        assertEquals("1", vCurrent.getText().toString());
    }

    @Test
    public void setMax( ) {
        dialog.setMax(1);
        assertEquals(1, vSeekBar.getMax());
        assertEquals("1", vMax.getText().toString());
    }

    @Test
    public void setMax2( ) {
        dialog.setMaxMask("2");
        dialog.setMax(1);
        assertEquals(1, vSeekBar.getMax());
        assertEquals("2", vMax.getText().toString());
    }

    @Test
    public void setMaxMask() {
        dialog.setMaxMask("1");
        assertEquals("1", vMax.getText().toString());
    }

    @Test
    public void setMinMask() {
        dialog.setMinMask("1");
        assertEquals("1", vMin.getText().toString());
    }

    @Test
    public void setProgress() {
        dialog.setProgress(1);
        assertEquals(1, vSeekBar.getProgress());
    }

    @Test
    public void setOnEnter() {
        CallbackSourceStub<Integer> callbackSourceStub = new CallbackSourceStub<>();
        dialog.setOnEnter(R.string.test, callbackSourceStub);
        dialog.setProgress(4);


        vEnter.performClick();

        assertTrue(callbackSourceStub.called);
        assertTrue(dialog.isDismissCalled());
        assertEquals(4, (int)callbackSourceStub.value);
    }

    @Test
    public void setEnabled() {
        dialog.setEnabled(false);
        assertFalse(vSeekBar.isEnabled());

        dialog.setEnabled(true);
        assertTrue(vSeekBar.isEnabled());
    }




}
