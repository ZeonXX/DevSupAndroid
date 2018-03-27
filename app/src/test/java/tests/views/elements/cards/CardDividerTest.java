package tests.views.elements.cards;

import android.view.View;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;

import com.sup.dev.android.views.elements.cards.CardDivider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class CardDividerTest {

    private CardDivider card;

    @Before
    public void before() {
        TestAndroid.init();
        card = new CardDivider();
    }

    @Test
    public void init() {
        instanceView();
    }

    private View instanceView() {
        View view = card.instanceView(RuntimeEnvironment.systemContext);
        card.bindView(view);
        return view;
    }



}
