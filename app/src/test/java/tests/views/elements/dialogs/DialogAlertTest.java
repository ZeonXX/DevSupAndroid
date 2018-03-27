package tests.views.elements.dialogs;

import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.dialogs.DialogAlert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class DialogAlertTest {

    private DialogAlert dialog;
    private TextView vText;

    @Before
    public void before() {
        TestAndroid.init();
        dialog = new DialogAlert(RuntimeEnvironment.application);

        vText = TestAndroid.field(dialog, "vText");
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



}
