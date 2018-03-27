package tests.libs.mvp;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;

import com.sup.dev.android.androiddevsup.R;
import com.sup.dev.android.libs.mvp.fragments.MvpFragment;
import com.sup.dev.android.libs.mvp.fragments.MvpPresenterInterface;

import tests._sup_android.TestAndroid;
import tests._sup_android.TestApplication;
import tests._sup_android.stubs.mvp.MvpPresenterStub;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.android.controller.FragmentController;
import org.robolectric.annotation.Config;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestApplication.class)
public class MvpFragmentTest {

    private MvpPresenterStub presenter;
    private FragmentController<Fragment> fragmentFragmentController;
    private Fragment fragment;
    private View vProgressContainer;

    @Before
    public void before(){
        TestAndroid.init();

        presenter = new MvpPresenterStub();
        TestAndroid.di.navigatorStub.presenters.put(1, presenter);

        Bundle bundle = new Bundle();
        bundle.putInt("KEY", 1);

        fragmentFragmentController = Robolectric.buildFragment(Fragment.class);
        fragment = fragmentFragmentController.get();

        TestAndroid.setBundle(fragment, bundle);

        fragmentFragmentController.create();

        vProgressContainer = TestAndroid.field(fragment, "vProgressContainer");


    }

    @Test
    public void init(){
        assertFalse(fragment.isAttachedToPresenter());
        assertEquals(View.GONE, vProgressContainer.getVisibility());
        assertEquals(presenter, fragment.getPresenter());
    }

    @Test
    public void onStart_onStop() {
        fragment.onStart();
        assertTrue(fragment.isAttachedToPresenter());
        fragment.onStop();
        assertFalse(fragment.isAttachedToPresenter());
    }

    @Test
    public void onResume() {
        fragment.onResume();
        assertTrue(fragment.isAttachedToPresenter());
    }


    @Test
    public void setPresenter() {
        MvpPresenterStub mvpPresenterStub = new MvpPresenterStub();
        fragment.setPresenter(mvpPresenterStub);
        assertEquals(mvpPresenterStub, fragment.getPresenter());
    }

    @Test
    public void onSaveInstanceState() {
        Bundle bundle = new Bundle();

        fragment.onSaveInstanceState(bundle);
        assertFalse(fragment.isAttachedToPresenter());
        assertTrue(bundle.containsKey("KEY"));
        assertEquals(1, bundle.getInt("KEY"));
    }

    @Test
    public void onDestroyView() {
        fragment.onDestroyView();
        assertFalse(fragment.isAttachedToPresenter());
    }

    @Test
    public void setProgressVisible() {
        fragment.setProgressVisible(MvpPresenterInterface.Visibility.TRANSPARENT);
        assertEquals(View.VISIBLE, vProgressContainer.getVisibility());
        assertEquals(0xF0F0F0F0, ((ColorDrawable)vProgressContainer.getBackground()).getColor());

        fragment.setProgressVisible(MvpPresenterInterface.Visibility.VISIBLE);
        assertEquals(0xF0F0F0FF, ((ColorDrawable)vProgressContainer.getBackground()).getColor());

        fragment.setProgressVisible(MvpPresenterInterface.Visibility.GONE);
        assertEquals(View.GONE, vProgressContainer.getVisibility());
    }

    //
    //  Class
    //

    public static class Fragment extends MvpFragment {

        boolean viewCreated;

        @Override
        protected int getLayout() {
            return R.layout.w_unit_test_fragment;
        }

        @Override
        protected void onViewCreated() {
            viewCreated = true;
        }


    }

}
