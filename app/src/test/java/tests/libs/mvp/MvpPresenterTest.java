package tests.libs.mvp;

import android.app.Fragment;

import com.sup.dev.android.libs.mvp.fragments.MvpPresenter;
import com.sup.dev.android.libs.mvp.fragments.MvpFragmentInterface;
import com.sup.dev.android.libs.mvp.fragments.MvpPresenterInterface;

import tests._sup_android.TestAndroid;
import tests._sup_android.stubs.mvp.MvpFragmentStub;
import com.sup.dev.java.tools.ToolsClass;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class MvpPresenterTest{

    private MvpPresenter<MvpFragmentStub> mvpPresenter;

    @Before
    public void before(){
        TestAndroid.init();
        mvpPresenter = new MvpPresenter(MvpFragmentStub.class);
    }

    @Test
    public void startParams() {
        assertEquals(0, mvpPresenter.getKey());
        mvpPresenter.setKey(1);
        assertEquals(1,  mvpPresenter.getKey());
    }

    @Test
    public void onDestroy() {
        mvpPresenter.onDestroy();
    }

    @Test
    public void instanceFragment() {
        MvpPresenter<MyFragment> presenter = new MvpPresenter<>(MyFragment.class);

        assertTrue(ToolsClass.instanceOf(presenter.instanceFragment(), MyFragment.class));
    }

    @Test
    public void onBackPressed() {
        assertFalse( mvpPresenter.onBackPressed());
    }

    @Test
    public void onAttachView() {
        assertEquals(attachFragment(),  mvpPresenter.getMvpFragment());
    }

    @Test
    public void onDetachView() {
        attachFragment();
        mvpPresenter.onDetachView();
        assertNull( mvpPresenter.getMvpFragment());
    }

    @Test
    public void setProgressDialogVisible() {
        MvpFragmentStub mvpFragmentStub = attachFragment();

        mvpPresenter.setProgressVisible(MvpPresenterInterface.Visibility.VISIBLE);
        assertEquals(MvpPresenterInterface.Visibility.VISIBLE, mvpFragmentStub.progressType);
    }

    @Test
    public void clearTag() {
        assertFalse( mvpPresenter.containsTag("tag"));
        mvpPresenter.actionSingle("tag", v -> v.setProgressVisible(MvpPresenterInterface.Visibility.VISIBLE));
        assertTrue(mvpPresenter.containsTag("tag"));
        mvpPresenter.clearTag("tag");
        assertFalse( mvpPresenter.containsTag("tag"));
    }

    @Test
    public void actionOnDetach() {
        MvpFragmentStub mvpFragmentStub = new MvpFragmentStub();
        mvpPresenter.setProgressVisible(MvpPresenterInterface.Visibility.VISIBLE);
        mvpPresenter.onAttachView(mvpFragmentStub);
        assertEquals(MvpPresenterInterface.Visibility.VISIBLE, mvpFragmentStub.progressType);
    }

    @Test
    public void actionAdd() {
        MvpFragmentStub mvpFragmentStub = attachFragment();
        mvpPresenter.actionAdd(v -> v.incrementCounter());
        mvpPresenter.actionAdd(v -> v.incrementCounter());
        assertEquals(2, mvpFragmentStub.counter);
        mvpPresenter.onDetachView();
        mvpFragmentStub = attachFragment();
        assertEquals(2, mvpFragmentStub.counter);
    }

    @Test
    public void actionAddTag() {
        assertFalse(mvpPresenter.containsTag("tag"));
        mvpPresenter.actionAdd("tag", v -> v.setProgressVisible(MvpPresenterInterface.Visibility.VISIBLE));
        assertTrue(mvpPresenter.containsTag("tag"));
        assertEquals(MvpPresenterInterface.Visibility.VISIBLE, attachFragment().progressType);
    }

    @Test
    public void actionSingle() {
        mvpPresenter.actionSingle("tag1", v -> v.incrementCounter());
        mvpPresenter.actionSingle("tag2", v -> v.incrementCounter());
        mvpPresenter.actionSingle(v -> v.incrementCounter());
        MvpFragmentStub mvpFragmentStub = attachFragment();
        assertEquals(1, mvpFragmentStub.counter);
    }

    @Test
    public void actionSingleTag() {
        attachFragment();
        mvpPresenter.actionSingle("tag", v -> v.incrementCounter());
        mvpPresenter.actionSingle("tag", v -> v.incrementCounter());
        mvpPresenter.actionSingle("tag2", v -> v.incrementCounter());
        MvpFragmentStub mvpFragmentStub = attachFragment();
        assertEquals(2, mvpFragmentStub.counter);
    }

    @Test
    public void actionSingleExecute() {
        mvpPresenter.actionSingleExecute(v -> v.incrementCounter());
        MvpFragmentStub mvpFragmentStub = attachFragment();
        assertEquals(1, mvpFragmentStub.counter);
        mvpPresenter.onDetachView();
        mvpFragmentStub = attachFragment();
        assertEquals(0, mvpFragmentStub.counter);
    }

    @Test
    public void actionSkip() {
        mvpPresenter.actionSkip(v -> v.incrementCounter());
        MvpFragmentStub mvpFragmentStub = attachFragment();
        assertEquals(0, mvpFragmentStub.counter);
        mvpPresenter.actionSkip(v -> v.incrementCounter());
        assertEquals(1, mvpFragmentStub.counter);
    }

    //
    //  Methods
    //

    private MvpFragmentStub attachFragment() {
        MvpFragmentStub mvpFragmentStub = new MvpFragmentStub();
        mvpPresenter.onAttachView(mvpFragmentStub);
        return mvpFragmentStub;
    }

    //
    //  ExceptionTest
    //

    @Test(expected = RuntimeException.class)
    public void badFragment() {
        BadPresenter badPresenter = new BadPresenter();
        badPresenter.instanceFragment();
    }


    private class BadPresenter extends MvpPresenter {
        public BadPresenter() {
            super(BadFragment.class);
        }
    }

    private class BadFragment extends MvpFragmentStub {
        public BadFragment(int x) {

        }
    }

    public static class MyFragment extends Fragment implements MvpFragmentInterface {

        @Override
        public void setPresenter(MvpPresenterInterface presenter) {

        }

        @Override
        public void setProgressVisible(MvpPresenterInterface.Visibility type) {

        }
    }


}
