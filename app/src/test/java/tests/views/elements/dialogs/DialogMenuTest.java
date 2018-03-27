package tests.views.elements.dialogs;

import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;
import com.sup.dev.android.views.elements.cards.CardDivider;
import com.sup.dev.android.views.elements.cards.CardMenu;

import tests._sup_android.TestAndroid;
import tests._sup_android.stubs.support.CallbackSourceStub;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.dialogs.DialogMenu;
import com.sup.dev.java.tools.ToolsClass;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class DialogMenuTest {

    private DialogMenu dialog;

    private RecyclerCardAdapter adapter;

    @Before
    public void before() {
        TestAndroid.init();
        dialog = new DialogMenu(RuntimeEnvironment.application);

        adapter = TestAndroid.field(dialog, "adapter");
    }

    @Test
    public void init() {
    }



    @Test
    public void addPreferred() {
        dialog.add("1");
        dialog.add("2");
        dialog.add("3");
        dialog.addPreferred("4");
        dialog.setPreferred("2");
        dialog.addPreferred("5");

        assertEquals("4", ((CardMenu)adapter.get(0)).getText());
        assertEquals("2", ((CardMenu)adapter.get(1)).getText());
        assertEquals("5", ((CardMenu)adapter.get(2)).getText());
        assertTrue(ToolsClass.instanceOf(adapter.get(3), CardDivider.class));
        assertEquals("1", ((CardMenu)adapter.get(4)).getText());
        assertEquals("3", ((CardMenu)adapter.get(5)).getText());

    }

    @Test
    public void setOnSelected() {
        CallbackSourceStub<String> callbackSourceStub = new CallbackSourceStub();
        dialog.setOnSelected(callbackSourceStub);

        dialog.show();
        dialog.onSelected("1");

        assertTrue(callbackSourceStub.called);
        assertEquals("1", callbackSourceStub.value);
    }

    @Test
    public void setAutoHideOnSelect() {
        dialog.setOnSelected(null);
        dialog.setAutoHideOnEnter(false);

        dialog.show();
        dialog.onSelected("1");

        assertFalse(dialog.isDismissCalled());
    }

    @Test
    public void setEnabled() {
        dialog.add("1");
        dialog.setEnabled(false);
        assertFalse(adapter.get(0).isEnabled());

        dialog.setEnabled(true);
        assertTrue(adapter.get(0).isEnabled());
    }



}
