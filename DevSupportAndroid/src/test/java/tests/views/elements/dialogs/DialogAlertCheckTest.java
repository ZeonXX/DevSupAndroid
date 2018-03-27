package tests.views.elements.dialogs;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.stubs.support.CallbackSourceStub;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.dialogs.DialogAlertCheck;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class DialogAlertCheckTest {

    private DialogAlertCheck dialog;
    private TextView vText;
    private CheckBox vCheck;
    private Button vEnter;

    @Before
    public void before() {
        TestAndroid.init();
        dialog = new DialogAlertCheck(RuntimeEnvironment.application, "KEY");

        vText = TestAndroid.field(dialog, "vText");
        vCheck = TestAndroid.field(dialog, "vCheck");
        vEnter = TestAndroid.field(dialog, "vEnter");

    }

    @Test
    public void init() {
        assertEquals(0, vText.getText().length());
        assertEquals(0, vCheck.getText().length());
    }

    @Test
    public void check() {
        TestAndroid.di.utilsStorageStub.put("KEY", true);
        assertTrue(DialogAlertCheck.check("KEY"));
    }

    @Test
    public void setText() {
        dialog.setText(R.string.test);

        assertEquals("test", vText.getText().toString());
    }

    @Test
    public void setCheckText() {
        dialog.setCheckText(R.string.test);

        assertEquals("test", vCheck.getText().toString());
    }

    @Test
    public void setOnEnter_unchecked() {
        CallbackSourceStub<DialogAlertCheck> callbackStub = new CallbackSourceStub<>();
        dialog.setOnEnter(R.string.test, callbackStub);

        assertEquals("test", vEnter.getText().toString());
        assertFalse(TestAndroid.di.utilsStorageStub.values.containsKey("KEY"));

        dialog.show();
        vEnter.performClick();

        assertTrue(callbackStub.called);
        assertTrue(dialog.isDismissCalled());
        assertEquals(false, TestAndroid.di.utilsStorageStub.values.get("KEY"));
    }

    @Test
    public void setOnEnter_checked() {
        CallbackSourceStub<DialogAlertCheck> callbackStub = new CallbackSourceStub<>();
        dialog.setOnEnter(R.string.test, callbackStub);
        vCheck.setChecked(true);

        assertFalse(TestAndroid.di.utilsStorageStub.values.containsKey("KEY"));

        dialog.show();
        vEnter.performClick();

        assertTrue(dialog.isDismissCalled());
        assertEquals(true, TestAndroid.di.utilsStorageStub.values.get("KEY"));
    }

    @Test
    public void setEnabled() {
        dialog.setEnabled(false);
        assertFalse(vCheck.isEnabled());
        dialog.setEnabled(true);
        assertTrue(vCheck.isEnabled());
    }

}
