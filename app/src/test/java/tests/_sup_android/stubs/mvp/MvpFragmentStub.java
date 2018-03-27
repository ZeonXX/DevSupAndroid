package tests._sup_android.stubs.mvp;

import com.sup.dev.android.libs.mvp.fragments.MvpFragmentInterface;
import com.sup.dev.android.libs.mvp.fragments.MvpPresenterInterface;

public class MvpFragmentStub implements MvpFragmentInterface {

    public MvpPresenterInterface presenter;
    public MvpPresenterInterface.Visibility progressType;
    public int counter;

    @Override
    public void setPresenter(MvpPresenterInterface presenter) {
        this.presenter = presenter;
    }

    @Override
    public void setProgressVisible(MvpPresenterInterface.Visibility type) {
        this.progressType = type;
    }


    public void incrementCounter(){
        counter++;
    }
}
