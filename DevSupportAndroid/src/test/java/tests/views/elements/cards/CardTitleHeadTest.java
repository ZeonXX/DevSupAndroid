package tests.views.elements.cards;

import android.view.View;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.cards.CardTitle;

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
public class CardTitleHeadTest {

    private CardTitle card;
    private View view;
    private TextView vText;

    @Before
    public void before() {
        TestAndroid.init();
        card = new CardTitle();
    }

    @Test
    public void init() {
        instanceView();
        assertEquals(0, vText.getText().length());
    }

    @Test
    public void setText() {
        card.setText(R.string.test);

        instanceView();
        assertEquals("test", vText.getText().toString());
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

        card.setEnabled(true);
        instanceView();
        assertTrue(vText.isEnabled());
    }


    private void instanceView() {
        view = card.instanceView(RuntimeEnvironment.systemContext);
        vText = view.findViewById(R.id.text);
        card.bindView(view);
    }

}
