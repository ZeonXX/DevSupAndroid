package tests.views.elements.dialogs;

import android.support.v7.app.AppCompatDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.stubs.support.CallbackSourceStub;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.dialogs.BaseDialog;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class BaseDialogTest {

    private BaseDialog dialog;
    protected TextView vTitle;
    protected Button vCancel;
    protected Button vEnter;

    @Before
    public void before() {
        TestAndroid.init();
        dialog = new BaseDialog(RuntimeEnvironment.application, R.layout.w_unit_test_dialog);
        vTitle = TestAndroid.field(dialog, "vTitle");
        vCancel = TestAndroid.field(dialog, "vCancel");
        vEnter = TestAndroid.field(dialog, "vEnter");
    }

    @Test
    public void init() {
        assertEquals(0, vTitle.getText().length());
        assertEquals(0, vCancel.getText().length());
        assertEquals(0, vEnter.getText().length());

        assertEquals(View.GONE, vTitle.getVisibility());
        assertEquals(View.GONE, vCancel.getVisibility());
        assertEquals(View.GONE, vEnter.getVisibility());
    }

    @Test
    public void setAutoHideOnEnter() {
        dialog.setAutoHideOnEnter(false);

        dialog.show();
        vEnter.performClick();

        assertFalse(dialog.isDismissCalled());
    }

    @Test
    public void setTitle() {
        dialog.setTitle(R.string.test);

        assertEquals("test", vTitle.getText().toString());
        assertEquals(View.VISIBLE, vTitle.getVisibility());
    }

    @Test
    public void setOnCancel() {
        CallbackSourceStub<BaseDialog> callbackStub = new CallbackSourceStub<>();

        dialog.setOnCancel(R.string.test, callbackStub);

        assertEquals("test", vCancel.getText().toString());

        vCancel.performClick();
        assertTrue(callbackStub.called);
        assertFalse(dialog.isShoved());
        assertEquals(dialog, callbackStub.value);
    }

    @Test
    public void setOnCancel_AutoHideFalse() {
        dialog.setOnCancel(R.string.test);
        dialog.setAutoHideOnCancel(false);
        dialog.show();

        assertEquals("test", vCancel.getText().toString());

        vCancel.performClick();
        assertFalse(dialog.isAutoHideOnCancel());
        assertTrue(dialog.isShoved());
    }

    @Test
    public void setOnCancel_DialogCancel() {
        CallbackSourceStub<BaseDialog> callbackStub = new CallbackSourceStub<>();

        dialog.setOnCancel(callbackStub);
        dialog.setAutoHideOnCancel(false);
        dialog.show();


        assertEquals(View.GONE, vCancel.getVisibility());
        assertEquals(0, vCancel.getText().length());

        ((AppCompatDialog)TestAndroid.field(dialog, "dialog")).cancel();

        assertTrue(callbackStub.called);
        assertFalse(dialog.isShoved());
        assertEquals(dialog, callbackStub.value);
    }

    @Test
    public void setOnCancel_text() {
        dialog.setOnCancel("test");
        assertEquals("test", vCancel.getText().toString());
    }


}
