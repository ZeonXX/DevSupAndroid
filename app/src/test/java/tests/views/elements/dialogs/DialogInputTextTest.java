package tests.views.elements.dialogs;

import android.support.design.widget.TextInputLayout;
import android.widget.Button;
import android.widget.EditText;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.stubs.support.CallbackPairStub;
import tests._sup_android.stubs.support.ProviderArgStub;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.dialogs.DialogInputText;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class DialogInputTextTest {

    private DialogInputText dialog;
    private EditText vField;
    private TextInputLayout vFieldLayout;
    private Button vEnter;

    @Before
    public void before() {
        TestAndroid.init();
        dialog = new DialogInputText(RuntimeEnvironment.application);

        vField = TestAndroid.field(dialog, "vField");
        vFieldLayout = TestAndroid.field(dialog, "vFieldLayout");
        vEnter = TestAndroid.field(dialog, "vEnter");
    }

    @Test
    public void init() {
        assertEquals(0, vField.getText().length());
        assertNull(vFieldLayout.getHint());
        assertNull(vField.getHint());
    }

    @Test
    public void setHint() {
        dialog.setHint(R.string.test);
        assertEquals("test", vFieldLayout.getHint().toString());

    }

    @Test
    public void setOnEnter() {
        CallbackPairStub<DialogInputText, String> callbackSourceStub = new CallbackPairStub<>();

        dialog.setText("test");
        dialog.setOnEnter(R.string.test, callbackSourceStub);
        vEnter.performClick();

        assertTrue(callbackSourceStub.called);
        assertEquals(dialog, callbackSourceStub.first);
        assertEquals("test", callbackSourceStub.second);
    }

    @Test
    public void setInputText() {
        dialog.setText("test");
        assertEquals("test", vField.getText().toString());
    }

    @Test
    public void setChecker() {
        ProviderArgStub<String, Boolean> providerArgStub = new ProviderArgStub<>(false);
        dialog.setChecker(R.string.test, providerArgStub);
        dialog.setText("my text");

        assertTrue(providerArgStub.called);
        assertEquals("my text", providerArgStub.lastArgument);
        assertEquals("test", vFieldLayout.getError().toString());
        assertFalse(vEnter.isEnabled());
    }

    @Test
    public void setAutoHideOnEnter() {
        dialog.setAutoHideOnEnter(false);
        dialog.setOnEnter(R.string.test, null);

        dialog.show();
        vEnter.performClick();

        assertFalse(dialog.isDismissCalled());
    }

    @Test
    public void setEnabled() {
        dialog.setEnabled(false);
        assertFalse(vField.isEnabled());
        assertFalse(vFieldLayout.isEnabled());
        dialog.setEnabled(true);
        assertTrue(vField.isEnabled());
        assertTrue(vFieldLayout.isEnabled());
    }

}
