package tests.views.elements.cards;


import android.view.View;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.views.elements.cards.CardAvatar;
import com.sup.dev.android.views.widgets.ViewAvatarTitle;

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
public class CardAvatarTest {

    private CardAvatar card;
    private View view;
    private ViewAvatarTitle vAvatar;

    @Before
    public void before() {
        TestAndroid.init();
        card = new CardAvatar();
    }

    @Test
    public void init() {
        instanceView();
        assertEquals(0, vAvatar.getTitle().length());
    }

    @Test
    public void setTitle() {
        card.setTitle("test");
        instanceView();
        assertEquals("test", vAvatar.getTitle());
    }

    @Test
    public void setSubtitle() {
        card.setSubtitle("test");
        instanceView();
        assertEquals("test", vAvatar.getSubTitle());
    }

    @Test
    public void setAvatarText() {
        card.setChipText("test");
        instanceView();
        assertEquals("test", vAvatar.getViewAvatar().getText());
    }

    @Test
    public void setEnabled() {

        card.setEnabled(false);
        instanceView();
        assertFalse(vAvatar.isEnabled());

        card.setEnabled(true);
        instanceView();
        assertTrue(vAvatar.isEnabled());
    }
    
    private void instanceView() {
        view = card.instanceView(RuntimeEnvironment.systemContext);
        vAvatar = view.findViewById(R.id.avatar);
        card.bindView(view);
    }
    
}
