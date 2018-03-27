package tests.views.elements.dialogs;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.dialogs.DialogProgressTransparent;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class DialogProgressTransparentTest {

    private DialogProgressTransparent dialog;

    @Before
    public void before() {
        TestAndroid.init();
        dialog = new DialogProgressTransparent(RuntimeEnvironment.application);
    }

    @Test
    public void init() {
        dialog.show();
    }


}
