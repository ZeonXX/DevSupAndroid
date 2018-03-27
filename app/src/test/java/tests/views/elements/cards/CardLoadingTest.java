package tests.views.elements.cards;


import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.stubs.support.CallbackStub;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.cards.CardLoading;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class CardLoadingTest {

    private CardLoading card;
    private View view;
    private View vLoading;
    private Button vAction;
    private TextView vText;

    @Before
    public void before() {
        TestAndroid.init();
        card = new CardLoading();
    }

    @Test
    public void init() {
        instanceView();

        assertEquals(View.VISIBLE, vLoading.getVisibility());
        assertEquals(View.INVISIBLE, vText.getVisibility());
        assertEquals(View.INVISIBLE, vAction.getVisibility());
        assertEquals(0, vText.getText().length());
        assertEquals(0, vAction.getText().length());
    }

    @Test
    public void setState() {

        card.setRetryButton("retry", null);
        card.setActionButton("action", null);
        card.setRetryMessage("retry_mes");
        card.setActionMessage("action_MES");

        card.setState(CardLoading.State.RETRY);
        instanceView();
        assertEquals(View.INVISIBLE, vLoading.getVisibility());
        assertEquals(View.VISIBLE, vText.getVisibility());
        assertEquals(View.VISIBLE, vAction.getVisibility());
        assertEquals("retry", vAction.getText().toString());
        assertEquals("retry_mes", vText.getText().toString());

        card.setState(CardLoading.State.ACTION);
        instanceView();
        assertEquals(View.INVISIBLE, vLoading.getVisibility());
        assertEquals(View.VISIBLE, vText.getVisibility());
        assertEquals(View.VISIBLE, vAction.getVisibility());
        assertEquals("action", vAction.getText().toString());
        assertEquals("action_MES", vText.getText().toString());
    }

    @Test
    public void setActionMessage() {
        card.setActionMessage(R.string.test);

        card.setState(CardLoading.State.ACTION);
        instanceView();
        assertEquals("test", vText.getText().toString());
    }


    @Test
    public void setActionButton() {
        CallbackStub callbackStub = new CallbackStub();
        card.setActionButton(R.string.test, callbackStub);
        card.setState(CardLoading.State.ACTION);

        instanceView();
        assertEquals("test", vAction.getText().toString());
        vAction.performClick();

        assertTrue(callbackStub.called);
    }

    @Test
    public void setRetryMessage() {
        card.setRetryMessage(R.string.test);
        card.setState(CardLoading.State.RETRY);
        instanceView();
        assertEquals("test", vText.getText().toString());
    }

    @Test
    public void setRetryButton() {
        CallbackStub callbackStub = new CallbackStub();
        card.setRetryButton(R.string.test, callbackStub);
        card.setState(CardLoading.State.RETRY);

        instanceView();
        assertEquals("test", vAction.getText().toString());
        vAction.performClick();

        assertTrue(callbackStub.called);
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
        vLoading = view.findViewById(R.id.loading);
        vAction = view.findViewById(R.id.action);
        vText = view.findViewById(R.id.text);
        card.bindView(view);
    }


}
