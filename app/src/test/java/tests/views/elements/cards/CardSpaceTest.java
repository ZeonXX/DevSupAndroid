package tests.views.elements.cards;


import android.view.View;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.cards.CardSpace;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class CardSpaceTest {

    private CardSpace card;
    private View view;
    private View vSpace;

    @Before
    public void before() {
        TestAndroid.init();
        card = new CardSpace();
    }

    @Test
    public void init() {
        instanceView();
        assertEquals(4, vSpace.getLayoutParams().height);
    }

    @Test
    public void setSpaceDp() {
        card.setSpace(8);
        instanceView();
        assertEquals(16, vSpace.getLayoutParams().height);
    }

    private void instanceView() {
        view = card.instanceView(RuntimeEnvironment.systemContext);
        vSpace = view.findViewById(R.id.space);
        card.bindView(view);
    }

}
