package tests.libs.mvp.activity;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sup.dev.android.androiddevsup.R;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;
import tests._sup_android.stubs.support.CallbackStub;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static junit.framework.Assert.*;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class DrawerMenuItemTest {

    public DrawerMenuItem item;
    private TextView vText;
    private TextView vCounter;

    @Before
    public void before() {
        TestAndroid.init();

        item = new DrawerMenuItem(RuntimeEnvironment.systemContext);
        vText = TestAndroid.field(item, "vText");
        vCounter = TestAndroid.field(item, "vCounter");
    }

    @Test
    public void init(){
        assertEquals(0, vText.getText().length());
        assertNull(vText.getCompoundDrawables()[0]);
        assertEquals(View.INVISIBLE, vCounter.getVisibility());
    }

    @Test
    public void setName(){
        item.setText(R.string.test);
        assertEquals("test", vText.getText().toString());
    }

    @Test
    public void setIcon(){
        item.setIcon(R.drawable.ic_arrow_back_white_24dp);
        assertNotNull(vText.getCompoundDrawables()[0]);
    }

    @Test
    public void setOnClick(){
        CallbackStub callbackStub = new CallbackStub();
        item.setOnClick(callbackStub);

        vText.performClick();
        assertTrue(callbackStub.called);
    }

    @Test
    public void setCounter(){
        item.setCounter(5);
        assertEquals("5", vCounter.getText().toString());
        assertEquals(View.VISIBLE, vCounter.getVisibility());

        item.setCounter(0);
        assertEquals(View.INVISIBLE, vCounter.getVisibility());
    }

    @Test
    public void marginTop(){
        item.marginTop(5);
        assertEquals(10, ((ViewGroup.MarginLayoutParams)vText.getLayoutParams()).topMargin);
    }





}
