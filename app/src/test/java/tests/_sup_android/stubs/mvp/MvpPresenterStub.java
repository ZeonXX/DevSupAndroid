package tests._sup_android.stubs.mvp;

import android.app.Fragment;

import com.sup.dev.android.libs.mvp.fragments.MvpFragmentInterface;
import com.sup.dev.android.libs.mvp.fragments.MvpPresenterInterface;

public class MvpPresenterStub implements MvpPresenterInterface {

    public int key = 1;
    public boolean isDestroy;
    public MvpFragmentInterface view;

    public Fragment retFragment;

    public boolean retOnBackPressed = false;

    @Override
    public int getKey() {
        return key;
    }

    @Override
    public void setKey(int key) {
        this.key = key;
    }

    @Override
    public void onDestroy() {
        isDestroy = true;
    }

    @Override
    public Fragment instanceFragment() {
        return retFragment;
    }

    @Override
    public boolean onBackPressed() {
        return retOnBackPressed;
    }

    @Override
    public void onAttachView(MvpFragmentInterface view) {
        this.view = view;
    }

    @Override
    public void onDetachView() {
        this.view = null;
    }

    @Override
    public void setProgressVisible(Visibility type) {

    }


}
