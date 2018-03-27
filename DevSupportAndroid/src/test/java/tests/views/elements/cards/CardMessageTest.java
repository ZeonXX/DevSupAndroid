package tests.views.elements.cards;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.stubs.support.CallbackStub;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.cards.CardMessage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class CardMessageTest {

    private CardMessage card;
    private View view;
    private TextView vText;
    private Button vAction;

    @Before
    public void before() {
        TestAndroid.init();
        card = new CardMessage();
    }

    @Test
    public void init() {
       instanceView();
        assertEquals(0, vText.length());
        assertEquals(0, vAction.length());
        assertEquals(View.GONE, vAction.getVisibility());
        assertEquals(View.GONE, vText.getVisibility());
    }

    @Test
    public void setText() {
        card.setText(R.string.test);
       instanceView();
        assertEquals("test", vText.getText().toString());
        assertEquals(View.VISIBLE, vText.getVisibility());
    }

    @Test
    public void setAction() {
        CallbackStub callbackStub = new CallbackStub();
        card.setAction(R.string.test, callbackStub);

       instanceView();
        assertEquals("test", vAction.getText().toString());
        vAction.performClick();

        assertTrue(callbackStub.called);
    }

    @Test
    public void setTextColor() {
        card.setTextColor(0xF0F0F0F0);

        instanceView();
        assertEquals(0xF0F0F0F0, vText.getCurrentTextColor());
    }

    @Test
    public void setEnabled() {
        card.setEnabled(false);
        instanceView();
        assertFalse(vText.isEnabled());
        assertFalse(vAction.isEnabled());

        card.setEnabled(true);
        instanceView();
        assertTrue(vText.isEnabled());
        assertTrue(vAction.isEnabled());
    }


    private void instanceView() {
        view = card.instanceView(RuntimeEnvironment.systemContext);
        vText = view.findViewById(R.id.text);
        vAction = view.findViewById(R.id.action);
        card.bindView(view);
    }
    

}
