package tests.views.elements.cards;

import android.graphics.drawable.ColorDrawable;
import android.view.View;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.views.adapters.recycler_view.RecyclerCardAdapter;

import tests._sup_android.TestAndroid;
import tests._sup_android.stubs.support.CallbackStub;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.cards.Card;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class CardTest {

    private Card card;
    private View view;
    private View vTouch;
    private View vDivider;

    @Before
    public void before() {
        TestAndroid.init();
        card = new Card() {
            @Override
            public int getLayout() {
                return 0;
            }
        };
    }

    @Test
    public void init() {
        RecyclerCardAdapter recyclerCardAdapter = new RecyclerCardAdapter();
        card.setAdapter(recyclerCardAdapter);
        card.update();

        instanceView();
        assertEquals(View.GONE, vDivider.getVisibility());
    }

    @Test
    public void setBackground() {
        card.setBackground(0xF0F0F0F0);

        instanceView();
        assertEquals(0xF0F0F0F0, ((ColorDrawable) view.getBackground()).getColor());
    }

    @Test
    public void setAction() {
        CallbackStub callbackStub = new CallbackStub();
        card.setOnClick(callbackStub);

        instanceView();
        vTouch.performClick();

        assertTrue(callbackStub.called);
    }

    @Test
    public void setDividerVisible() {
        card.setDividerVisible(false);
        instanceView();
        assertEquals(View.GONE, vDivider.getVisibility());
    }

    @Test
    public void setEnabled() {
        card.setOnClick(new CallbackStub());

        card.setEnabled(false);
        instanceView();
        assertFalse(vTouch.hasOnClickListeners());

        card.setEnabled(true);
        instanceView();
        assertTrue(vTouch.hasOnClickListeners());
    }



    private void instanceView() {
        view = card.instanceView(RuntimeEnvironment.systemContext);
        vTouch = view.findViewById(R.id.touch);
        vDivider = view.findViewById(R.id.divider);
        card.bindView(view);
    }


}
