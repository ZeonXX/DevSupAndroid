package tests.views.elements.dialogs;

import android.widget.TextView;

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
public class DialogLongTextTest {

    private DialogLongText dialog;
    private TextView vText;

    @Before
    public void before() {
        TestAndroid.init();
        dialog = new DialogLongText(RuntimeEnvironment.application);

        vText = TestAndroid.field(dialog, "vText");
    }

    @Test
    public void init() {
        assertEquals(0, vText.getText().length());
    }

    @Test
    public void addLine() {
        dialog.addLine("test");
        dialog.addLine("test");
        assertEquals("test\ntest", vText.getText());
    }

}
