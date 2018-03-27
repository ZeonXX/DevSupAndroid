package tests.views.elements.dialogs;
import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class DialogProgressTest {

    private DialogProgress dialog;

    @Before
    public void before() {
        TestAndroid.init();
        dialog = new DialogProgress(RuntimeEnvironment.application);
    }

    @Test
    public void init() {
        dialog.show();
    }


}
