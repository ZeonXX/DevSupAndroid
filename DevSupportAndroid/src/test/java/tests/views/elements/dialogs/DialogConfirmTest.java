package tests.views.elements.dialogs;

import android.widget.Button;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.stubs.support.CallbackSourceStub;
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
public class DialogConfirmTest {

    private DialogConfirm dialog;
    private TextView vText;
    private Button vEnter;

    @Before
    public void before() {
        TestAndroid.init();
        dialog = new DialogConfirm(RuntimeEnvironment.application);

        vText = TestAndroid.field(dialog, "vText");
        vEnter = TestAndroid.field(dialog, "vEnter");
    }

    @Test
    public void init() {
        assertEquals(0, vText.getText().length());
    }

    @Test
    public void setText() {
        dialog.setText(R.string.test);
        assertEquals("test", vText.getText().toString());
    }

    @Test
    public void setOnYes() {
        CallbackSourceStub<DialogConfirm> callbackStub = new CallbackSourceStub<>();
        dialog.setOnYes(R.string.test, callbackStub);

        dialog.show();
        vEnter.performClick();

        assertTrue(callbackStub.called);
        assertTrue(dialog.isDismissCalled());
    }

}
