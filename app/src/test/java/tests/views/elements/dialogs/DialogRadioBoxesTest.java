package tests.views.elements.dialogs;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.stubs.support.CallbackSourceStub;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.dialogs.DialogRadioBoxes;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class DialogRadioBoxesTest {

    private DialogRadioBoxes dialog;
    private LinearLayout vOptionsContainer;
    private Button vEnter;

    @Before
    public void before() {
        TestAndroid.init();
        dialog = new DialogRadioBoxes(RuntimeEnvironment.application);
        vOptionsContainer = TestAndroid.field(dialog, "vOptionsContainer");
        vEnter = TestAndroid.field(dialog, "vEnter");
    }

    @Test
    public void init() {

    }

    @Test
    public void addItem() {
        dialog.addItem(R.string.test);
        dialog.addItem("test 1");
        dialog.addItem("test 2", R.string.test);
        dialog.addItem("test 3", "test 2");

        assertEquals("test", ((CheckBox)vOptionsContainer.getChildAt(0)).getText().toString());
        assertEquals("test 1", ((CheckBox)vOptionsContainer.getChildAt(1)).getText().toString());
        assertEquals(4, vOptionsContainer.getChildCount());
    }

    @Test
    public void setSelectedKey() {
        dialog.addItem("test 1");
        dialog.addItem("test 2");
        dialog.addItem("test 3");

        dialog.setSelectedKey("test 2");

        assertTrue(((CheckBox)vOptionsContainer.getChildAt(1)).isChecked());
    }

    @Test
    public void setOnEnter() {
        dialog.addItem("test 1");
        dialog.addItem("test 2");
        dialog.addItem("test 3");
        dialog.setSelectedKey("test 2");

        CallbackSourceStub<Object> callbackSourceStub = new CallbackSourceStub<>();
        dialog.setOnEnter(R.string.test, callbackSourceStub);

        vEnter.performClick();

        assertTrue(callbackSourceStub.called);
        assertEquals("test 2", callbackSourceStub.value);

    }

    @Test
    public void setEnabled() {
        dialog.addItem("test 1");

        dialog.setEnabled(false);
        assertFalse(vOptionsContainer.getChildAt(0).isEnabled());

        dialog.setEnabled(true);
        assertTrue(vOptionsContainer.getChildAt(0).isEnabled());
    }



}
